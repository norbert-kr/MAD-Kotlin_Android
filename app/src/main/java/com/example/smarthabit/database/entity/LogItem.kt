package com.example.smarthabit.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Entity representing an activity log for a habit.
 * Each log records when a habit was complete.
 */
@Entity(
    tableName = "habitlogs",
    foreignKeys = [
        // Links each log to a habit, deleting the habit will also delete its logs
        ForeignKey(
            entity = HabitItem::class,
            parentColumns = ["habitId"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habitId"])]
)
data class LogItem(

    @PrimaryKey(autoGenerate = true)
    val logId: Int = 0,
    val habitId: Int,

    // Timestamp of when the activity was logged
    val logDate: Long,
    val isCompleted: Boolean
)