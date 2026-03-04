package com.example.smarthabit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.viewmodel.LogViewModel

@Composable
fun ListHabitScreen(
    modifier: Modifier = Modifier,
    habits: List<HabitItem>,
    logVm: LogViewModel,
    onOpenAddHabitScreen: () -> Unit,
    onViewHabit: (HabitItem) -> Unit,
    onEditHabit: (HabitItem) -> Unit,
    onDeleteHabit: (HabitItem) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = "My Habits",
                fontSize = 30.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            items(habits, key = { it.habitId }) { habit ->

                val logs by if (habit.targetType == "Daily") {
                    logVm.getTodayLogs(habit.habitId).collectAsState(initial = emptyList())
                } else {
                    logVm.getThisWeekLogs(habit.habitId).collectAsState(initial = emptyList())
                }

                val status =
                    if (logs.size >= habit.targetTimesPerWeek)
                        "Completed"
                    else
                        "Active"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {
                            Text(
                                text = habit.habitName,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(text = "Category: ${habit.habitCategory}")
                            Text(text = "Type: ${habit.targetType}")
                            Text(text = "Status: $status")
                        }

                        Row {
                            IconButton(onClick = { onViewHabit(habit) }) {
                                Text("View")
                            }

                            IconButton(onClick = { onEditHabit(habit) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit"
                                )
                            }

                            IconButton(onClick = { onDeleteHabit(habit) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onOpenAddHabitScreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(bottom = 25.dp),
            shape = ButtonDefaults.elevatedShape
        ) {
            Text(
                text = "+ Add New Habit",
                fontSize = 22.sp
            )
        }
    }
}