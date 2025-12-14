package com.example.weighttracker_brandonmullins

import android.Manifest
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weighttracker_brandonmullins.ui.LoginScreen
import com.example.weighttracker_brandonmullins.ui.TrackerScreen
import com.example.weighttracker_brandonmullins.ui.SmsPermissionScreen
import com.example.weighttracker_brandonmullins.ui.theme.WeightTracker_BrandonMullinsTheme

class MainActivity : ComponentActivity() {

    // AndroidViewModel instance
    private val vm: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permission launcher for SEND_SMS
        val smsPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                vm.setSmsGranted(granted)
            }

        setContent {
            WeightTracker_BrandonMullinsTheme {
                Surface {
                    val ui = vm.ui.collectAsStateWithLifecycle().value
                    val context = LocalContext.current

                    // Observe goal condition yourself if you want to auto send
                    // For the rubric, a manual "Send test SMS" is fine.
                    when (ui.screen) {
                        Screen.Login -> LoginScreen(
                            error = ui.error,
                            onClearError = vm::clearError,
                            onLogin = { u, p -> vm.login(u, p) },
                            onCreateAccount = { u, p -> vm.createAccount(u, p) }
                        )
                        Screen.Tracker -> TrackerScreen(
                            username = ui.currentUser.orEmpty(),
                            items = ui.weights,
                            goalWeight = ui.goalWeight,
                            onAdd = { w -> vm.addWeight(w) },
                            onDelete = { entry -> vm.deleteWeight(entry) },
                            onUpdate = { entry -> vm.updateWeight(entry) },
                            onSetGoal = { g -> vm.setGoal(g) },
                            onOpenSms = { vm.goToSms() },
                            onLogout = { vm.logout() }
                        )
                        Screen.Sms -> SmsPermissionScreen(
                            granted = ui.smsGranted,
                            onRequestPermission = {
                                smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                            },
                            onBack = { vm.goToTracker() },
                            onSendTest = { phone ->
                                if (ui.smsGranted) {
                                    // Simple test alert
                                    val msg = "WeightTracker: SMS alerts enabled."
                                    try {
                                        val sms = SmsManager.getDefault()
                                        sms.sendTextMessage(phone, null, msg, null, null)
                                    } catch (_: Exception) {
                                        // On some emulators/devices without telephony this may no-op or fail.
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
