package com.example.habitstracker.presentation.screens.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.model.HabitWithProgress
import com.example.habitstracker.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<HabitWithProgress>>(emptyList())
    val habits: StateFlow<List<HabitWithProgress>> = _habits.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            repository.getAllHabits().collect { habitsList ->
                // Для каждой привычки рассчитываем стрик
                val habitsWithProgress = mutableListOf<HabitWithProgress>()

                habitsList.forEach { habit ->
                    val streak = repository.getCurrentStreak(habit.id)
                    habitsWithProgress.add(HabitWithProgress(habit, streak))
                }

                _habits.value = habitsWithProgress
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
            // Перезагружаем список после удаления
            loadHabits()
        }
    }

    fun refreshHabits() {
        loadHabits()
    }
}