package com.example.habitstracker.presentation.screens.habitdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.model.ProgressEntry
import com.example.habitstracker.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habit = MutableStateFlow<Habit?>(null)
    val habit: StateFlow<Habit?> = _habit.asStateFlow()

    val progress = MutableStateFlow<List<ProgressEntry>>(emptyList())
    val currentStreak = MutableStateFlow(0)
    val totalSuccessDays = MutableStateFlow(0)

    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            repository.getHabitById(habitId).collect { habit ->
                _habit.value = habit
                habit?.let {
                    repository.getProgressForHabit(it.id).collect { progressList ->
                        progress.value = progressList
                        currentStreak.value = repository.getCurrentStreak(it.id)
                        totalSuccessDays.value = repository.getTotalSuccessDays(it.id)
                    }
                }
            }
        }
    }

    fun markProgress(date: Long, isSuccess: Boolean, comment: String = "") {
        viewModelScope.launch {
            _habit.value?.let { habit ->
                repository.markProgress(habit.id, date, isSuccess, comment)
            }
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            _habit.value?.let { habit ->
                repository.deleteHabit(habit)
            }
        }
    }
}