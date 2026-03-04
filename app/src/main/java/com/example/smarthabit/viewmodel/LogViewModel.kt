package com.example.smarthabit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.LogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class LogViewModel(
    private val logDao: LogDao
) : ViewModel() {

    fun getLogsForHabit(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabit(habitId)
    }


    private fun startOfToday(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    private fun endOfToday(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.timeInMillis
    }


    private fun startOfWeek(): Long {
        val c = Calendar.getInstance()

        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        c.firstDayOfWeek = Calendar.MONDAY

        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        val diff = if (dayOfWeek == Calendar.SUNDAY) -6 else Calendar.MONDAY - dayOfWeek

        c.add(Calendar.DAY_OF_MONTH, diff)

        return c.timeInMillis
    }

    private fun endOfWeek(): Long {
        val c = Calendar.getInstance()

        c.firstDayOfWeek = Calendar.MONDAY

        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        val diff = if (dayOfWeek == Calendar.SUNDAY) 0 else Calendar.SUNDAY - dayOfWeek

        c.add(Calendar.DAY_OF_MONTH, diff)

        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)

        return c.timeInMillis
    }



    fun getTodayLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(
            habitId,
            startOfToday(),
            endOfToday()
        )
    }



    fun getThisWeekLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(
            habitId,
            startOfWeek(),
            endOfWeek()
        )
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

    fun deleteLog(log: LogItem) {
        viewModelScope.launch {
            logDao.deleteLog(log)
        }
    }
}