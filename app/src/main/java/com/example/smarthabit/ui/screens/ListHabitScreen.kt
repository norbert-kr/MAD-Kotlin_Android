package com.example.smarthabit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.smarthabit.getSampleHabits

@Composable
fun ListHabitScreen(
    modifier: Modifier = Modifier,
    onOpenAddHabitScreen: () -> Unit,
) {


    //val habits = getSampleHabits()

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


        /*
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            items(habits) { habit ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = habit.name, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Category: ${habit.category}")
                        Text(text = "Type: ${habit.type}")
                    }
                }
            }
        }
        */


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