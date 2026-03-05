package com.example.smarthabit.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.viewmodel.LogViewModel
import com.example.smarthabit.ui.components.ConfirmDialog
import com.example.smarthabit.ui.components.AlertDialogMessage
import java.text.SimpleDateFormat
import java.util.*


/**
 * Screen displaying detailed information about a habit.
 * Shows progress, streak and allows logging activity.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewHabitScreen(
    modifier: Modifier = Modifier,
    habit: HabitItem,
    onUp: () -> Unit,
    logVm: LogViewModel
) {

    // Format the date the habit was created
    val createdDate = SimpleDateFormat(
        "dd MMM yyyy • HH:mm",
        Locale.getDefault()
    ).format(Date(habit.startDate))

    // Retrieve logs depending on habit type (daily or weekly)
    val logs by if (habit.targetType == "Daily") {
        logVm.getTodayLogs(habit.habitId)
            .collectAsState(initial = emptyList())
    } else {
        logVm.getThisWeekLogs(habit.habitId)
            .collectAsState(initial = emptyList())
    }

    // Current streak calculated in the ViewModel
    val streak by logVm
        .getDailyStreak(habit.habitId)
        .collectAsState(initial = 0)

    val target = habit.targetTimesPerWeek

    // Progress based on completed logs vs target
    val progress = logs.size.toFloat() / target.toFloat()

    val completed = logVm.isHabitCompleted(logs, target)

    val status =
        if (completed) "Completed"
        else "Active"

    var showLogDialog by remember { mutableStateOf(false) }
    var showDailyLimitDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Habit Details") },
                navigationIcon = {
                    IconButton(onClick = onUp) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Text(
                            text = habit.habitName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text("Category: ${habit.habitCategory}")
                        Text("Type: ${habit.targetType}")
                        Text("Status: $status")
                        Text("Streak: $streak days")
                        Text("Created: $createdDate")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Text(
                            "Progress",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )

                        Text("${logs.size} / $target completed")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Activity Logs",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn {

                            if (logs.isEmpty()) {

                                item {
                                    Text(
                                        text = "No activity logged yet.",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }

                            } else {

                                items(logs) { log ->

                                    val formatted = SimpleDateFormat(
                                        "dd MMM yyyy • HH:mm",
                                        Locale.getDefault()
                                    ).format(Date(log.logDate))

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Text(
                                            text = formatted,
                                            fontSize = 16.sp,
                                            color = Color(0xFF0D47A1)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {

                    // Prevent logging more than once per day
                    if (logVm.hasLoggedToday(logs)) {
                        showDailyLimitDialog = true
                    } else {
                        showLogDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("+ Log Activity", fontSize = 22.sp)
            }
        }
    }

    // Confirmation dialog before creating a log
    if (showLogDialog) {
        ConfirmDialog(
            title = "Log Activity",
            message = "Do you want to log an activity?",
            onConfirm = {
                logVm.createLog(habit.habitId)
                showLogDialog = false
            },
            onDismiss = { showLogDialog = false }
        )
    }

    // Alert shown if user already logged today
    if (showDailyLimitDialog) {
        AlertDialogMessage(
            title = "Daily Limit",
            message = "You can only log this habit once per day.",
            onDismiss = { showDailyLimitDialog = false }
        )
    }
}