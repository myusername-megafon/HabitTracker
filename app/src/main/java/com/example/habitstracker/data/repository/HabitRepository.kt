package com.example.habitstracker.data.repository

import com.example.habitstracker.data.database.HabitDao
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.model.ProgressEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Calendar

class HabitRepository(private val habitDao: HabitDao) {

    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    fun getHabitById(habitId: Long): Flow<Habit?> = habitDao.getHabitById(habitId)

    suspend fun insertHabit(habit: Habit): Long = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteProgressForHabit(habit.id)
        habitDao.deleteHabit(habit)
    }

    fun getProgressForHabit(habitId: Long): Flow<List<ProgressEntry>> =
        habitDao.getProgressForHabit(habitId)

    suspend fun markProgress(habitId: Long, date: Long, isSuccess: Boolean, comment: String = "") {
        val existingProgress = habitDao.getProgressForDate(habitId, date)
        if (existingProgress != null) {
            habitDao.updateProgress(existingProgress.copy(isSuccess = isSuccess, comment = comment))
        } else {
            habitDao.insertProgress(ProgressEntry(habitId = habitId, date = date, isSuccess = isSuccess, comment = comment))
        }
    }

    suspend fun getCurrentStreak(habitId: Long): Int {
        val progress = habitDao.getProgressForHabit(habitId).first()
        var streak = 0
        val calendar = Calendar.getInstance()

        for (entry in progress.sortedByDescending { it.date }) {
            if (entry.isSuccess) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    suspend fun getTotalSuccessDays(habitId: Long): Int {
        val progress = habitDao.getProgressForHabit(habitId).first()
        return progress.count { it.isSuccess }
    }
}