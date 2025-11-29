package com.example.habitstracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.model.ProgressEntry

@Database(
    entities = [Habit::class, ProgressEntry::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var Instance: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    HabitDatabase::class.java,
                    "habit_database"
                ).build().also { Instance = it }
            }
        }
    }
}