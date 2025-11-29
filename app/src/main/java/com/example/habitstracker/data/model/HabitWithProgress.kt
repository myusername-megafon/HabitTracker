package com.example.habitstracker.data.model

data class HabitWithProgress(
    val habit: Habit,
    val currentStreak: Int
)