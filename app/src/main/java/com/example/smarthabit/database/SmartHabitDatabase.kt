package com.example.smarthabit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smarthabit.database.dao.HabitDao
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.database.entity.LogItem

@Database(
    entities = [HabitItem::class, LogItem::class],
    version = 4,
    exportSchema = false
)
abstract class SmartHabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun logDao(): LogDao
}