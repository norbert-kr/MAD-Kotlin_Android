package com.example.smarthabit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthabit.database.dao.HabitDao
import com.example.smarthabit.database.entity.HabitItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(
    private val dao: HabitDao
) : ViewModel() {

    val habits: StateFlow<List<HabitItem>> =
        dao.getAllHabits().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun upsertHabit(habit: HabitItem) {
        viewModelScope.launch {
            dao.upsertHabit(habit)
        }
    }

    fun deleteHabit(habit: HabitItem) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
        }
    }
}