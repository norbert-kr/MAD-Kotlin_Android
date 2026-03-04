package com.example.smarthabit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.smarthabit.database.entity.LogItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Upsert
    suspend fun upsertLog(log: LogItem)

    @Query("SELECT * FROM habitlogs WHERE habitId = :habitId")
    fun getLogsForHabit(habitId: Int): Flow<List<LogItem>>

    @Query("""
        SELECT * FROM habitlogs
        WHERE habitId = :habitId
        AND logDate BETWEEN :start AND :end
        ORDER BY logDate ASC
    """)
    fun getLogsForHabitBetween(
        habitId: Int,
        start: Long,
        end: Long
    ): Flow<List<LogItem>>

    @Delete
    suspend fun deleteLog(log: LogItem)
}