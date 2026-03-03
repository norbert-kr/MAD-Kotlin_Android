package com.example.smarthabit.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavKeySerializer
import kotlinx.serialization.Serializable
import com.example.smarthabit.database.entity.HabitItem



sealed interface NavObjects : NavKey {

    @Serializable
    data object LoginScreen : NavObjects

    @Serializable
    data object ListHabitScreen : NavObjects

    @Serializable
    data object AddHabitScreen : NavObjects

    @Serializable
    data class EditHabitScreen(val habit: HabitItem) : NavObjects

    @Serializable
    data class ViewHabitScreen(val habit: HabitItem) : NavObjects
}