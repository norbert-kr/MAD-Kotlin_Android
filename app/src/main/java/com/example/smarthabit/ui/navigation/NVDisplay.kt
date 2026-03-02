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
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.ui.screens.LoginScreen
import com.example.smarthabit.ui.screens.ListHabitScreen
import com.example.smarthabit.ui.screens.AddHabitScreen
import com.example.smarthabit.viewmodel.HabitViewModel

@Composable
fun NVDisplay(modifier: Modifier = Modifier) {

    val backStack = remember {
        mutableStateListOf<Any>(NavObjects.LoginScreen)
    }

    val context = LocalContext.current.applicationContext

    val dao = remember(context) {
        DatabaseInstance.getDatabase(context).habitDao()
    }

    val vm: HabitViewModel = viewModel(
        factory = viewModelFactory {
            initializer { HabitViewModel(dao) }
        }
    )

    val habits by vm.habits.collectAsStateWithLifecycle()

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
                            habits = habits,
                            onOpenAddHabitScreen = {
                                backStack.add(NavObjects.AddHabitScreen)
                            },
                            onEditHabit = { habit: HabitItem ->
                                backStack.add(NavObjects.EditHabitScreen(habit))
                            },
                            onDeleteHabit = { habit ->
                                vm.deleteHabit(habit)
                            }
                        )
                    }

                is NavObjects.AddHabitScreen ->
                    NavEntry(route) {
                        AddHabitScreen(
                            modifier = modifier,
                            onUp = { backStack.removeLastOrNull() },
                            onSaveHabit = { habit ->
                                vm.upsertHabit(habit)
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
                                vm.upsertHabit(habit)
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                else -> error("Unknown route: $route")
            }
        }
    )
}