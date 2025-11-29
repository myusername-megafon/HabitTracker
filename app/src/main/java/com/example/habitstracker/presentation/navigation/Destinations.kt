package com.example.habitstracker.presentation.navigation

sealed class Screen(val route: String) {
    object Habits : Screen("habits")
    object AddHabit : Screen("add_habit")
    object HabitDetail : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: Long) = "habit_detail/$habitId"
    }
    object Profile : Screen("profile")
    object ReminderSettings : Screen("reminder_settings")
}