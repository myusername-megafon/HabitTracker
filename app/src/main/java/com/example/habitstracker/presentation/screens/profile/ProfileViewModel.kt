package com.example.habitstracker.presentation.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.model.HabitWithProgress
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.presentation.utils.PdfExportManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    val habits = repository.getAllHabits().map { habits ->
        habits.map { habit ->
            HabitWithProgress(habit, repository.getCurrentStreak(habit.id))
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun exportToPdf(context: Context) {
        val habitsList = habits.value
        if (habitsList.isNotEmpty()) {
            PdfExportManager(context).exportToPdf(habitsList)
        }
    }
}