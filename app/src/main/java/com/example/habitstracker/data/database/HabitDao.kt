package com.example.habitstracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.model.ProgressEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Long): Flow<Habit?>

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM progress_entries WHERE habitId = :habitId ORDER BY date DESC")
    fun getProgressForHabit(habitId: Long): Flow<List<ProgressEntry>>

    @Query("SELECT * FROM progress_entries WHERE habitId = :habitId AND date = :date")
    suspend fun getProgressForDate(habitId: Long, date: Long): ProgressEntry?

    @Insert
    suspend fun insertProgress(progress: ProgressEntry)

    @Update
    suspend fun updateProgress(progress: ProgressEntry)

    @Query("DELETE FROM progress_entries WHERE habitId = :habitId")
    suspend fun deleteProgressForHabit(habitId: Long)
}