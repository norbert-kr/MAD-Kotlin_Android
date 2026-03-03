package com.example.smarthabit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "habits")
data class HabitItem(

    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,

    val habitName: String,
    val habitCategory: String,

    val targetType: String,

    val startDate: Long = System.currentTimeMillis(),

    val targetTimesPerWeek: Int
)