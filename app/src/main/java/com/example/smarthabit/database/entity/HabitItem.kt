package com.example.smarthabit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


/**
 * Entity representing a habit in the database.
 * Each habit stores a basic information like  name, category, target frequency.
 */
@Serializable
@Entity(tableName = "habits")
data class HabitItem(

    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,
    val habitName: String,
    val habitCategory: String,

    // Determines if the habit is Daily or Weekly
    val targetType: String,

    // Date the habit was created
    val startDate: Long = System.currentTimeMillis(),

    // Number of times the habit should be completed e.g. daily 1 / weekly multiple
    val targetTimesPerWeek: Int
)