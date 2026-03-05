package com.example.smarthabit.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.viewmodel.LogViewModel
import com.example.smarthabit.ui.components.ConfirmDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListHabitScreen(
    modifier: Modifier = Modifier,
    habits: List<HabitItem>,
    logVm: LogViewModel,
    onOpenAddHabitScreen: () -> Unit,
    onViewHabit: (HabitItem) -> Unit,
    onEditHabit: (HabitItem) -> Unit,
    onDeleteHabit: (HabitItem) -> Unit
) {

    var statusFilter by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<HabitItem?>(null) }

    val pages = listOf("All", "Daily", "Weekly")
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Habits",
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            pages.forEachIndexed { index, label ->

                val selected = pagerState.currentPage == index

                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = statusFilter,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status Filter") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                listOf("All", "Active", "Completed").forEach { option ->

                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            statusFilter = option
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(15.dp))


        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->

            val filteredHabits = habits.filter {

                when (pageIndex) {
                    1 -> it.targetType == "Daily"
                    2 -> it.targetType == "Weekly"
                    else -> true
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {

                items(filteredHabits, key = { it.habitId }) { habit ->

                    val logs by if (habit.targetType == "Daily") {
                        logVm.getTodayLogs(habit.habitId)
                            .collectAsState(initial = emptyList())
                    } else {
                        logVm.getThisWeekLogs(habit.habitId)
                            .collectAsState(initial = emptyList())
                    }

                    val status =
                        if (logs.size >= habit.targetTimesPerWeek)
                            "Completed"
                        else
                            "Active"

                    val passesFilter =
                        statusFilter == "All" ||
                                (statusFilter == "Active" && status == "Active") ||
                                (statusFilter == "Completed" && status == "Completed")

                    if (passesFilter) {

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

                                    Text(habit.habitName, fontSize = 18.sp)

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text("Category: ${habit.habitCategory}")
                                    Text("Type: ${habit.targetType}")
                                    Text("Status: $status")
                                }

                                Row {

                                    IconButton(
                                        onClick = { onViewHabit(habit) }
                                    ) {
                                        Text("View")
                                    }

                                    IconButton(
                                        onClick = { onEditHabit(habit) }
                                    ) {
                                        Icon(Icons.Default.Edit, "Edit")
                                    }

                                    IconButton(
                                        onClick = {
                                            selectedHabit = habit
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, "Delete")
                                    }
                                }
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
                .padding(bottom = 25.dp)
        ) {
            Text("+ Add New Habit", fontSize = 22.sp)
        }
    }

    if (showDeleteDialog && selectedHabit != null) {

        ConfirmDialog(
            title = "Delete Habit",
            message = "Do you want to delete this habit?",
            onConfirm = {
                onDeleteHabit(selectedHabit!!)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

