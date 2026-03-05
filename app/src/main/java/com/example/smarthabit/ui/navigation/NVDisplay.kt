package com.example.smarthabit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.smarthabit.database.DatabaseInstance
import com.example.smarthabit.ui.screens.LoginScreen
import com.example.smarthabit.ui.screens.ListHabitScreen
import com.example.smarthabit.ui.screens.AddHabitScreen
import com.example.smarthabit.ui.screens.ViewHabitScreen
import com.example.smarthabit.viewmodel.HabitViewModel
import com.example.smarthabit.viewmodel.LogViewModel


/**
 * Main navigation display for the app.
 * Handles screen routing and initializes required ViewModels.
 */
@Composable
fun NVDisplay(modifier: Modifier = Modifier) {

    // Navigation back stack starting with the login screen
    val backStack = remember {
        mutableStateListOf<Any>(NavObjects.LoginScreen)
    }

    val context = LocalContext.current.applicationContext

    // Initialize database instance
    val database = remember(context) {
        DatabaseInstance.getDatabase(context)
    }

    // ViewModel for managing habits
    val habitVm: HabitViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HabitViewModel(database.habitDao())
            }
        }
    )

    // ViewModel for managing habit logs
    val logVm: LogViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                LogViewModel(database.logDao())
            }
        }
    )

    // Observe habits from ViewModel
    val habits by habitVm.habits.collectAsStateWithLifecycle()


    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },

        // Defines how each navigation route maps to a screen
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
                            habits = habits,
                            logVm = logVm,
                            onOpenAddHabitScreen = {
                                backStack.add(NavObjects.AddHabitScreen)
                            },
                            onViewHabit = { habit ->
                                backStack.add(NavObjects.ViewHabitScreen(habit))
                            },
                            onEditHabit = { habit ->
                                backStack.add(NavObjects.EditHabitScreen(habit))
                            },
                            onDeleteHabit = { habit ->
                                habitVm.deleteHabit(habit)
                            }
                        )
                    }

                is NavObjects.AddHabitScreen ->
                    NavEntry(route) {
                        AddHabitScreen(
                            modifier = modifier,
                            onUp = { backStack.removeLastOrNull() },
                            onSaveHabit = { habit ->
                                habitVm.upsertHabit(habit)
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                is NavObjects.EditHabitScreen ->
                    NavEntry(route) {
                        AddHabitScreen(
                            modifier = modifier,
                            existingHabit = route.habit,
                            onUp = { backStack.removeLastOrNull() },
                            onSaveHabit = { habit ->
                                habitVm.upsertHabit(habit)
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                is NavObjects.ViewHabitScreen -> NavEntry(route) {
                    ViewHabitScreen(
                        modifier = modifier,
                        habit = route.habit,
                        onUp = { backStack.removeLastOrNull() },
                        logVm = logVm
                    )
                }

                else -> error("Unknown route: $route")
            }
        }
    )
}