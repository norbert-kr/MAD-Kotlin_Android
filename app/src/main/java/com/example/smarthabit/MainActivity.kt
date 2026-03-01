package com.example.smarthabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.smarthabit.ui.theme.SmartHabitTheme
import com.example.smarthabit.ui.navigation.NVDisplay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SmartHabitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NVDisplay(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


