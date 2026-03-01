package com.example.smarthabit.database

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    @Volatile
    private var INSTANCE: SmartHabitDatabase? = null

    fun getDatabase(context: Context): SmartHabitDatabase {

        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                SmartHabitDatabase::class.java,
                "habit_tracker_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}