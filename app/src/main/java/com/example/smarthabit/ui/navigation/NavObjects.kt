package com.example.smarthabit.ui.navigation
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavKeySerializer
import kotlinx.serialization.Serializable
import com.example.smarthabit.database.entity.HabitItem


/**
 * Defines the navigation routes used throughout the app.
 * Each object represents a screen that can be navigated to.
 */
sealed interface NavObjects : NavKey {


    // All existing screen routes used for navigation e.g. login, list, add, edit habit screens
    @Serializable
    data object LoginScreen : NavObjects

    @Serializable
    data object ListHabitScreen : NavObjects

    @Serializable
    data object AddHabitScreen : NavObjects


    // Specialized screens that require a habit object to be passed e.g. edit / view
    @Serializable
    data class EditHabitScreen(val habit: HabitItem) : NavObjects
    @Serializable
    data class ViewHabitScreen(val habit: HabitItem) : NavObjects
}