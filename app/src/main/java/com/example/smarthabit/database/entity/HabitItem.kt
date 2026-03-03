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
    val status: String = "Ongoing",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val targetTimesPerWeek: Int? = null
)