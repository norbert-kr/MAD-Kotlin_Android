package com.example.smarthabit.database
import android.content.Context
import androidx.room.Room


/**
 * Singleton provider for the Room database.
 * Ensures only one database instance exists during the app lifecycle.
 */
object DatabaseInstance {

    @Volatile
    private var INSTANCE: SmartHabitDatabase? = null

    fun getDatabase(context: Context): SmartHabitDatabase {

        // Create the database if it does not exist, otherwise return the existing instance
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