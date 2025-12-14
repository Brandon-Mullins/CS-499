package com.example.weighttracker_brandonmullins

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighttracker_brandonmullins.data.AppDatabase
import com.example.weighttracker_brandonmullins.data.Goal
import com.example.weighttracker_brandonmullins.data.User
import com.example.weighttracker_brandonmullins.data.WeightEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

// Simple screen switcher so we don't need a Nav library
enum class Screen { Login, Tracker, Sms }

data class TrackerUiState(
    val currentUser: String? = null,
    val weights: List<WeightEntry> = emptyList(),
    val goalWeight: Double? = null,
    val error: String? = null,
    val screen: Screen = Screen.Login,
    val smsGranted: Boolean = false
)

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.get(app).dao()

    private val _ui = MutableStateFlow(TrackerUiState())
    val ui: StateFlow<TrackerUiState> = _ui

    // --------------------------
    // Login / Signup
    // --------------------------
    fun login(username: String, password: String) = viewModelScope.launch {
        val user = dao.getUser(username)
        if (user == null) {
            // not found -> show error
            _ui.value = _ui.value.copy(error = "User not found. Create an account first.")
        } else if (user.password != password) {
            _ui.value = _ui.value.copy(error = "Incorrect password.")
        } else {
            _ui.value = _ui.value.copy(currentUser = username, error = null, screen = Screen.Tracker)
            loadFor(username)
        }
    }

    fun createAccount(username: String, password: String) = viewModelScope.launch {
        if (username.isBlank() || password.isBlank()) {
            _ui.value = _ui.value.copy(error = "Username and password are required.")
            return@launch
        }
        val existing = dao.getUser(username)
        if (existing != null) {
            _ui.value = _ui.value.copy(error = "Username already exists.")
            return@launch
        }
        dao.insertUser(User(username = username, password = password))
        // Start them at tracker
        _ui.value = _ui.value.copy(currentUser = username, error = null, screen = Screen.Tracker)
        loadFor(username)
    }

    private fun loadFor(username: String) = viewModelScope.launch {
        val weights = dao.listWeights(username)
        val goal = dao.getGoal(username)?.goalWeightLbs
        _ui.value = _ui.value.copy(weights = weights, goalWeight = goal)
    }

    fun logout() {
        _ui.value = TrackerUiState() // back to login fresh
    }

    // --------------------------
    // CRUD for weights
    // --------------------------
    fun addWeight(weightLbs: Double, date: String = LocalDate.now().toString()) =
        withUser { user ->
            viewModelScope.launch {
                dao.insertWeight(WeightEntry(username = user, date = date, weightLbs = weightLbs))
                loadFor(user)
                checkGoalAndMaybeAlert(user)
            }
        }

    fun deleteWeight(entry: WeightEntry) = withUser { user ->
        viewModelScope.launch {
            dao.deleteWeight(entry)
            loadFor(user)
        }
    }

    fun updateWeight(updated: WeightEntry) = withUser { user ->
        viewModelScope.launch {
            dao.updateWeight(updated)
            loadFor(user)
            checkGoalAndMaybeAlert(user)
        }
    }

    // --------------------------
    // Goal handling
    // --------------------------
    fun setGoal(goal: Double) = withUser { user ->
        viewModelScope.launch {
            dao.upsertGoal(Goal(username = user, goalWeightLbs = goal))
            loadFor(user)
            checkGoalAndMaybeAlert(user)
        }
    }

    private fun withUser(block: (String) -> Unit) {
        val u = _ui.value.currentUser ?: run {
            _ui.value = _ui.value.copy(error = "Not logged in.")
            return
        }
        block(u)
    }

    // --------------------------
    // SMS permission + alert stub
    // --------------------------
    fun setSmsGranted(granted: Boolean) {
        _ui.value = _ui.value.copy(smsGranted = granted)
    }

    private fun checkGoalAndMaybeAlert(user: String) {
        val goal = _ui.value.goalWeight ?: return
        val latest = _ui.value.weights.maxByOrNull { it.date }
        if (latest != null && latest.weightLbs <= goal) {
            // If SMS is granted we'll send later (hook in MainActivity)
            // For now we just clear any prior error and rely on MainActivity to call sendSms(...)
            _ui.value = _ui.value.copy(error = null)
        }
    }

    // Simple navigation helpers
    fun goToSms() {
        _ui.value = _ui.value.copy(screen = Screen.Sms)
    }
    fun goToTracker() {
        _ui.value = _ui.value.copy(screen = Screen.Tracker)
    }
    fun clearError() {
        _ui.value = _ui.value.copy(error = null)
    }
}
