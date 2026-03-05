package com.example.smarthabit.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smarthabit.database.dao.HabitDao
import com.example.smarthabit.database.dao.LogDao
import com.example.smarthabit.database.entity.HabitItem
import com.example.smarthabit.database.entity.LogItem


/**
 * Main Room database for the application.
 * Registers the entities and provides access to the DAOs.
 */

@Database(
    entities = [HabitItem::class, LogItem::class],
    version = 4,
    exportSchema = false
)
abstract class SmartHabitDatabase : RoomDatabase() {

    // DAO access points
    abstract fun habitDao(): HabitDao
    abstract fun logDao(): LogDao
}