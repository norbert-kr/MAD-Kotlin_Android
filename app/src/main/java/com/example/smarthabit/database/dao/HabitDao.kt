package com.example.smarthabit.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.smarthabit.database.entity.HabitItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsertHabit(habit: HabitItem)

    @Query("SELECT * FROM habits ORDER BY habitId DESC")
    fun getAllHabits(): Flow<List<HabitItem>>

    @Delete
    suspend fun deleteHabit(habit: HabitItem)
}