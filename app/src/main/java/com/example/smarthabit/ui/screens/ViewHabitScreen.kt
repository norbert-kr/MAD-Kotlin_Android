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
import com.example.smarthabit.viewmodel.LogViewModel
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
    val formattedDate = SimpleDateFormat(
        "dd MMM yyyy • HH:mm",
        Locale.getDefault()
    ).format(Date(habit.startDate))


    val streak by logVm.getDailyStreak(habit.habitId)
        .collectAsState(initial = 0)


    val logs by if (habit.targetType == "Daily") {
        logVm.getTodayLogs(habit.habitId).collectAsState(initial = emptyList())
    } else {
        logVm.getThisWeekLogs(habit.habitId).collectAsState(initial = emptyList())
    }

    val target = habit.targetTimesPerWeek
    val progress = logs.size.toFloat() / target.toFloat()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Habit Details") },
                navigationIcon = {
                    IconButton(onClick = onUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Name: ${habit.habitName}", fontSize = 20.sp)
            Text(text = "Category: ${habit.habitCategory}", fontSize = 18.sp)
            Text(text = "Type: ${habit.targetType}", fontSize = 18.sp)
            Text(text = "Created On: $formattedDate", fontSize = 16.sp)

            Text(text = "Streak: $streak day(s)", fontSize = 18.sp)

            Text(text = "Target: $target", fontSize = 20.sp)

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Text("${logs.size} / $target completed")

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(target) { index ->
                    val log = logs.getOrNull(index)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (log != null) {
                            val formatted = SimpleDateFormat(
                                "dd MMM yyyy • HH:mm",
                                Locale.getDefault()
                            ).format(Date(log.logDate))

                            Text("Log ${index + 1}: Completed on $formatted", fontSize = 16.sp)

                            Checkbox(
                                checked = true,
                                onCheckedChange = { checked ->
                                    if (!checked) logVm.deleteLog(log)
                                }
                            )
                        } else {
                            Text("Log ${index + 1}: Not completed", fontSize = 16.sp)

                            Checkbox(
                                checked = false,
                                onCheckedChange = { checked ->
                                    if (checked) logVm.createLog(habit.habitId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}