package com.example.habitstracker

import android.app.Application
import com.example.habitstracker.data.database.HabitDatabase
import com.example.habitstracker.data.repository.HabitRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HabitsTrackerApplication : Application() {
    val database: HabitDatabase by lazy { HabitDatabase.getDatabase(this) }
    val repository: HabitRepository by lazy { HabitRepository(database.habitDao()) }
}