
package com.example.smarthabit



fun getSampleHabits(): List<Habit> {
    return listOf(
        Habit(1, "Morning Run", "Fitness", "Daily"),
        Habit(2, "Read 20 mins", "Study", "Daily"),
        Habit(3, "Meal Prep", "Health", "Weekly"),
        Habit(4, "Meditation", "Wellbeing", "Daily"),
        Habit(5, "Gym Session", "Fitness", "Weekly")
    )
}