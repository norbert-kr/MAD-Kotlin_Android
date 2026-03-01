package com.example.smarthabit.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habitlogs",
    foreignKeys = [
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
    val logDate: Long,
    val isCompleted: Boolean
)