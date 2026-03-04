package com.example.smarthabit.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.viewmodel.LogViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    // Status dropdown (All/Active/Completed)
    var statusFilter by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    // Swipe pages: 0=All, 1=Daily, 2=Weekly
    val pages = listOf("All", "Daily", "Weekly")
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        // Page labels (highlight current page) + tap to jump
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pages.forEachIndexed { index, label ->
                val isSelected = pagerState.currentPage == index

                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                )
            }
        }

        // Status dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = statusFilter,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status Filter") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            // Use DropdownMenu (compatible)
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

        // Swipeable pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->

            val typeFilteredHabits = habits.filter { habit ->
                when (pageIndex) {
                    0 -> true // All
                    1 -> habit.targetType == "Daily"
                    2 -> habit.targetType == "Weekly"
                    else -> true
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ){

                items(typeFilteredHabits, key = { it.habitId }) { habit ->

                    val logs by if (habit.targetType == "Daily") {
                        logVm.getTodayLogs(habit.habitId).collectAsState(initial = emptyList())
                    } else {
                        logVm.getThisWeekLogs(habit.habitId).collectAsState(initial = emptyList())
                    }

                    val status =
                        if (logs.size >= habit.targetTimesPerWeek) "Completed" else "Active"

                    val passesStatusFilter =
                        statusFilter == "All" ||
                                (statusFilter == "Active" && status == "Active") ||
                                (statusFilter == "Completed" && status == "Completed")

                    if (passesStatusFilter) {

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
                                    Text(text = habit.habitName, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Category: ${habit.habitCategory}")
                                    Text(text = "Type: ${habit.targetType}")
                                    Text(text = "Status: $status")
                                }

                                Row {
                                    IconButton(onClick = { onViewHabit(habit) }) { Text("View") }

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
            }
        }

        // Add button (unchanged)
        Button(
            onClick = onOpenAddHabitScreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(bottom = 25.dp),
            shape = ButtonDefaults.elevatedShape
        ) {
            Text(text = "+ Add New Habit", fontSize = 22.sp)
        }
    }
}