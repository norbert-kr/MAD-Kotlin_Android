package com.example.smarthabit.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.LogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar


/**
 * ViewModel responsible for managing habit logs.
 * Handles creation of logs, date filtering and streak calculation.
 */
class LogViewModel(
    private val logDao: LogDao
) : ViewModel() {


    // Create a new log entry for the habit
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

    // Checks if a habit was already logged today
    fun hasLoggedToday(logs: List<LogItem>): Boolean {
        val todayStart = startOfDayMillis(System.currentTimeMillis())
        return logs.any {
            startOfDayMillis(it.logDate) == todayStart
        }
    }

    // Determines if the habit reached its required target
    fun isHabitCompleted(
        logs: List<LogItem>,
        target: Int
    ): Boolean {
        return logs.size >= target
    }


    // Start of the current day (00:00)
    private fun startOfToday(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    // End of the current day (23:59)
    private fun endOfToday(): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.timeInMillis
    }


    // Start of the current week (Monday)
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


    // End of the current week (Sunday)
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


    // Retrieve logs for today
    fun getTodayLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(habitId, startOfToday(), endOfToday())
    }

    // Retrieve logs for the current week
    fun getThisWeekLogs(habitId: Int): Flow<List<LogItem>> {
        return logDao.getLogsForHabitBetween(habitId, startOfWeek(), endOfWeek())
    }


    // Calculates daily streak based on consecutive logged days
    fun getDailyStreak(habitId: Int): Flow<Int> {
        return logDao.getLogsForHabit(habitId).map { logs ->

            val completedDays = HashSet<Long>()

            // Store unique completed days
            for (log in logs) {
                completedDays.add(startOfDayMillis(log.logDate))
            }

            val todayStart = startOfDayMillis(System.currentTimeMillis())

            // If today is logged start from today, otherwise start from yesterday
            val startCursor =
                if (completedDays.contains(todayStart)) {
                    todayStart
                } else {
                    previousDayStart(todayStart)
                }

            var streak = 0
            var cursor = startCursor

            // Walk backwards through days until a gap is found
            while (completedDays.contains(cursor)) {
                streak++
                cursor = previousDayStart(cursor)
            }

            streak
        }
    }


    // Normalize a timestamp to start of the day
    private fun startOfDayMillis(timeMillis: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = timeMillis
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    // Move cursor one day backwards
    private fun previousDayStart(dayStartMillis: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = dayStartMillis
        c.add(Calendar.DAY_OF_YEAR, -1)
        return c.timeInMillis
    }
}