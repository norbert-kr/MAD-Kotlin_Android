package com.example.smarthabit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.LogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LogViewModel(
    private val logDao: LogDao
) : ViewModel() {

    fun getLogsForHabit(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabit(habitId)
    }

    fun createLog(habitId: Int) {
        viewModelScope.launch {
            logDao.upsertLog(
                LogItem(
                    habitId = habitId,
                    logDate = System.currentTimeMillis(),
                    isCompleted = true
                )
            )
        }
    }

    // Delete a log
    fun deleteLog(log: LogItem) {
        viewModelScope.launch {
            logDao.deleteLog(log)
        }
    }
}