package com.example.smarthabit.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavKeySerializer
import kotlinx.serialization.Serializable
import com.example.smarthabit.database.entity.HabitItem



sealed interface NavObjects : NavKey {

    @Serializable
    data object LoginScreen : NavObjects, NavKey

    @Serializable
    data object ListHabitScreen : NavObjects, NavKey

    @Serializable
    data object AddHabitScreen : NavObjects, NavKey

    @Serializable
    data class EditHabitScreen(val habit: HabitItem) : NavObjects, NavKey

}