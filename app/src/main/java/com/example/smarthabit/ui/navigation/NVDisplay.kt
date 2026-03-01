package com.example.smarthabit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.smarthabit.ui.screens.LoginScreen
import com.example.smarthabit.ui.screens.ListHabitScreen
import com.example.smarthabit.ui.screens.AddHabitScreen


@Composable
fun NVDisplay(modifier: Modifier = Modifier) {

    val backStack = remember {
        mutableStateListOf<Any>(NavObjects.LoginScreen)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { route ->

            when (route) {

                is NavObjects.LoginScreen ->
                    NavEntry(route) {
                        LoginScreen(
                            onOpenListHabitScreen = {
                                backStack.add(NavObjects.ListHabitScreen)
                            }
                        )
                    }

                is NavObjects.ListHabitScreen ->
                    NavEntry(route) {
                        ListHabitScreen(
                            modifier = modifier,
                            onOpenAddHabitScreen = {
                                backStack.add(NavObjects.AddHabitScreen)
                            }
                        )
                    }

                is NavObjects.AddHabitScreen ->
                    NavEntry(route) {
                        AddHabitScreen(
                            modifier = modifier,
                            onUp = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                else -> error("Unknown route: $route")
            }
        }
    )
}