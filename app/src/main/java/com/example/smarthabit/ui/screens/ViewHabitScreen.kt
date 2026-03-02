package com.example.smarthabit.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthabit.database.entity.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewHabitScreen(
    modifier: Modifier = Modifier,
    habit: HabitItem,
    onUp: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Habit Details") },
                navigationIcon = {
                    IconButton(onClick = onUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
            Text(text = "Start Date: ${habit.startDate}", fontSize = 16.sp)
        }
    }
}