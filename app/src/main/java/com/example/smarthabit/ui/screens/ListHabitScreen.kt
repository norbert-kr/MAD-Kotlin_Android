package com.example.smarthabit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListHabitScreen(
    modifier: Modifier = Modifier,
    onOpenAddHabitScreen: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Top content
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


        Button(
            onClick = onOpenAddHabitScreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(bottom = 45.dp),
            shape = ButtonDefaults.elevatedShape
        ) {
            Text(
                text = "+ Add New Habit",
                fontSize = 22.sp
            )
        }
    }
}