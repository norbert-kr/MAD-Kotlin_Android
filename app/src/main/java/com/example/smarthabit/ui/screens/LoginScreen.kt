package com.example.smarthabit.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * Simple login screen used as the entry point of the app.
 * Currently just navigates to the habit list.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOpenListHabitScreen: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "SmartHabit",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.size(30.dp))

        Button(
            onClick = onOpenListHabitScreen,
            shape = ButtonDefaults.elevatedShape,
            modifier = Modifier.height(50.dp)
        ) {
            Text(
                "Login Now",
                fontSize = 22.sp
            )
        }

    }
}