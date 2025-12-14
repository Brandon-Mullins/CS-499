package com.example.weighttracker_brandonmullins.ui

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.weighttracker_brandonmullins.data.WeightEntry
import java.time.LocalDate

@Composable
fun TrackerScreen(
    username: String,
    items: List<WeightEntry>,
    goalWeight: Double?,
    onAdd: (Double) -> Unit,
    onDelete: (WeightEntry) -> Unit,
    onUpdate: (WeightEntry) -> Unit,
    onSetGoal: (Double) -> Unit,
    onOpenSms: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    var newWeight by remember { mutableStateOf(TextFieldValue("")) }
    var newGoal by remember { mutableStateOf(TextFieldValue(goalWeight?.toString() ?: "")) }

    // Backup status message
    var backupMessage by remember { mutableStateOf<String?>(null) }

    // Weekly trend summary (Enhancement #2)
    val trendSummary = remember(items) { calculateWeeklyTrend(items) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Logged in as $username")
            Row {
                TextButton(onClick = {
                    backupHistoryToCsv(context, items) { msg ->
                        backupMessage = msg
                    }
                }) {
                    Text("Backup")
                }
                TextButton(onClick = onOpenSms) { Text("SMS") }
                TextButton(onClick = onLogout) { Text("Logout") }
            }
        }

        // Show backup status
        if (!backupMessage.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(backupMessage!!, style = MaterialTheme.typography.bodySmall)
        }

        // Weekly productivity summary card
        if (trendSummary != null) {
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Weekly progress")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "This week average: ${
                            "%.1f".format(
                                trendSummary.thisWeekAvg
                            )
                        } lbs"
                    )
                    trendSummary.lastWeekAvg?.let {
                        Text("Last week average: ${"%.1f".format(it)} lbs")
                    }
                    Text(trendSummary.trendMessage)
                }
            }
        }

        // Add weight row
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newWeight,
                onValueChange = { newWeight = it },
                label = { Text("Add weight (lbs)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                newWeight.text.toDoubleOrNull()?.let {
                    onAdd(it)
                    newWeight = TextFieldValue("")
                }
            }) { Text("Add") }
        }

        // Goal row
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newGoal,
                onValueChange = { newGoal = it },
                label = { Text("Goal (lbs)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                newGoal.text.toDoubleOrNull()?.let(onSetGoal)
            }) { Text("Save Goal") }
        }

        // Weight trend graph (Enhancement #1)
        Spacer(Modifier.height(12.dp))
        if (items.isNotEmpty()) {
            Text("Weight trend")
            Spacer(Modifier.height(4.dp))

            val sorted = remember(items) { items.sortedBy { it.date } }
            val minWeight = sorted.minOf { it.weightLbs }.toFloat()
            val maxWeight = sorted.maxOf { it.weightLbs }.toFloat()
            val weightRange = (maxWeight - minWeight).takeIf { it > 0f } ?: 1f

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            ) {
                val bottomPadding = 16.dp.toPx()
                val topPadding = 16.dp.toPx()
                val usableHeight = size.height - topPadding - bottomPadding

                val stepX = if (sorted.size == 1) 0f else size.width / (sorted.size - 1)

                fun weightToY(w: Float): Float {
                    val normalized = (w - minWeight) / weightRange
                    return size.height - bottomPadding - normalized * usableHeight
                }

                // X axis
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height - bottomPadding),
                    end = Offset(size.width, size.height - bottomPadding),
                    strokeWidth = 2.dp.toPx()
                )

                var prevPoint: Offset? = null
                sorted.forEachIndexed { index, entry ->
                    val x = stepX * index
                    val y = weightToY(entry.weightLbs.toFloat())
                    val point = Offset(x, y)

                    if (prevPoint != null) {
                        drawLine(
                            color = Color(0xFF1976D2),
                            start = prevPoint!!,
                            end = point,
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    prevPoint = point
                }
            }
        }

        // Entries list
        Spacer(Modifier.height(12.dp))
        Text("Entries")
        LazyColumn {
            items(items) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${entry.date}  •  ${entry.weightLbs} lbs")
                    Row {
                        TextButton(onClick = {
                            onUpdate(entry.copy(weightLbs = entry.weightLbs + 1.0))
                        }) { Text("+1") }
                        TextButton(onClick = {
                            onUpdate(entry.copy(weightLbs = entry.weightLbs - 1.0))
                        }) { Text("-1") }
                        TextButton(onClick = { onDelete(entry) }) { Text("Delete") }
                    }
                }
            }
        }

        // Goal reached message
        if (goalWeight != null) {
            Spacer(Modifier.height(8.dp))
            val latest = items.maxByOrNull { it.date }
            if (latest != null && latest.weightLbs <= goalWeight) {
                Text(
                    "Goal reached on ${latest.date}!",
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}

// ---- Weekly trend logic (Enhancement #2) ----

data class WeeklyTrendSummary(
    val thisWeekAvg: Double,
    val lastWeekAvg: Double?,
    val trendMessage: String
)

fun calculateWeeklyTrend(items: List<WeightEntry>): WeeklyTrendSummary? {
    if (items.isEmpty()) return null

    val today = LocalDate.now()

    val thisWeekStart = today.minusDays((today.dayOfWeek.value - 1).toLong())
    val lastWeekStart = thisWeekStart.minusWeeks(1)

    // ✅ Convert String -> LocalDate safely
    fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    val thisWeekWeights = items
        .filter { entry ->
            val d = parseDate(entry.date)
            d != null && d >= thisWeekStart && d <= today
        }
        .map { it.weightLbs }

    if (thisWeekWeights.isEmpty()) return null

    val thisWeekAvg = thisWeekWeights.average()

    val lastWeekWeights = items
        .filter { entry ->
            val d = parseDate(entry.date)
            d != null && d >= lastWeekStart && d < thisWeekStart
        }
        .map { it.weightLbs }

    val lastWeekAvg = if (lastWeekWeights.isNotEmpty()) lastWeekWeights.average() else null

    val trendMessage = if (lastWeekAvg == null) {
        "Not enough data from last week to compare yet."
    } else {
        val diff = thisWeekAvg - lastWeekAvg
        when {
            diff < -0.5 -> "You are losing weight compared to last week."
            diff > 0.5 -> "You are gaining weight compared to last week."
            else -> "Your weight is holding steady compared to last week."
        }
    }

    return WeeklyTrendSummary(
        thisWeekAvg = thisWeekAvg,
        lastWeekAvg = lastWeekAvg,
        trendMessage = trendMessage
    )
}

// ---- Database-related history backup (Enhancement #3) ----

fun backupHistoryToCsv(
    context: Context,
    items: List<WeightEntry>,
    onResult: (String) -> Unit
) {
    if (items.isEmpty()) {
        onResult("No entries to back up yet.")
        return
    }

    val filename = "weight_history_backup.csv"

    val csv = buildString {
        appendLine("date,weight_lbs")
        items.forEach { entry ->
            appendLine("${entry.date},${entry.weightLbs}")
        }
    }

    try {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
            stream.write(csv.toByteArray())
        }
        onResult("Backup saved as $filename in app storage.")
    } catch (e: Exception) {
        onResult("Backup failed: ${e.message ?: "unknown error"}")
    }
}
