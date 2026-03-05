package com.example.smarthabit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.LogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class LogViewModel(
    private val logDao: LogDao
) : ViewModel() {


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

    fun hasLoggedToday(logs: List<LogItem>): Boolean {
        val todayStart = startOfDayMillis(System.currentTimeMillis())
        return logs.any {
            startOfDayMillis(it.logDate) == todayStart
        }
    }

    fun isHabitCompleted(
        logs: List<LogItem>,
        target: Int
    ): Boolean {
        return logs.size >= target
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
        c.firstDayOfWeek = Calendar.MONDAY

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        return c.timeInMillis
    }

    private fun endOfWeek(): Long {
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY

        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)

        return c.timeInMillis
    }

    fun getTodayLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(habitId, startOfToday(), endOfToday())
    }

    fun getThisWeekLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(habitId, startOfWeek(), endOfWeek())
    }

    fun getDailyStreak(habitId: Int): Flow<Int> {
        return logDao.getLogsForHabit(habitId).map { logs ->

            val completedDays = HashSet<Long>()

            for (log in logs) {
                completedDays.add(startOfDayMillis(log.logDate))
            }

            val todayStart = startOfDayMillis(System.currentTimeMillis())

            val startCursor =
                if (completedDays.contains(todayStart)) {
                    todayStart
                } else {
                    previousDayStart(todayStart)
                }

            var streak = 0
            var cursor = startCursor

            while (completedDays.contains(cursor)) {
                streak++
                cursor = previousDayStart(cursor)
            }

            streak
        }
    }

    private fun startOfDayMillis(timeMillis: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = timeMillis
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    private fun previousDayStart(dayStartMillis: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = dayStartMillis
        c.add(Calendar.DAY_OF_YEAR, -1)
        return c.timeInMillis
    }
}