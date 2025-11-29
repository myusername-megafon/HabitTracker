package com.example.habitstracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val iconIndex: Int = 0, // индекс иконки в списке
    val startDate: Long,
    val targetDuration: Int,
    val color: Int,
    val createdAt: Long = System.currentTimeMillis()
)