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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.database.entity.LogItem
import com.example.smarthabit.viewmodel.LogViewModel
import com.example.smarthabit.ui.components.ConfirmDialog
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewHabitScreen(
    modifier: Modifier = Modifier,
    habit: HabitItem,
    onUp: () -> Unit,
    logVm: LogViewModel
) {

    val createdDate = SimpleDateFormat(
        "dd MMM yyyy • HH:mm",
        Locale.getDefault()
    ).format(Date(habit.startDate))

    val logs by if (habit.targetType == "Daily") {
        logVm.getTodayLogs(habit.habitId)
            .collectAsState(initial = emptyList())
    } else {
        logVm.getThisWeekLogs(habit.habitId)
            .collectAsState(initial = emptyList())
    }

    val streak by logVm
        .getDailyStreak(habit.habitId)
        .collectAsState(initial = 0)

    val target = habit.targetTimesPerWeek

    val progress = logs.size.toFloat() / target.toFloat()

    val status =
        if (logs.size >= target) "Completed"
        else "Active"

    var showLogDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedLog by remember { mutableStateOf<LogItem?>(null) }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Text("Name: ${habit.habitName}", fontSize = 20.sp)

                Text("Category: ${habit.habitCategory}", fontSize = 18.sp)

                Text("Type: ${habit.targetType}", fontSize = 18.sp)

                Text("Status: $status", fontSize = 18.sp)

                Text("Streak: $streak days", fontSize = 18.sp)

                Text("Created On: $createdDate", fontSize = 16.sp)

                Text("Target: $target", fontSize = 20.sp)

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Text("${logs.size} / $target completed")

                Text("Logs", fontSize = 18.sp)

                LazyColumn {

                    items(logs) { log ->

                        val formatted = SimpleDateFormat(
                            "dd MMM yyyy • HH:mm",
                            Locale.getDefault()
                        ).format(Date(log.logDate))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text("Completed At: $formatted")

                            TextButton(
                                onClick = {
                                    selectedLog = log
                                    showDeleteDialog = true
                                }
                            ) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { showLogDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)

            ) {
                Text("+ Log Activity", fontSize = 22.sp)
            }
        }
    }

    if (showLogDialog) {

        ConfirmDialog(
            title = "Log Activity",
            message = "Do you want to log an activity?",
            onConfirm = {
                logVm.createLog(habit.habitId)
                showLogDialog = false
            },
            onDismiss = {
                showLogDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedLog != null) {

        ConfirmDialog(
            title = "Remove Activity",
            message = "Do you want to remove this activity?",
            onConfirm = {
                logVm.deleteLog(selectedLog!!)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}