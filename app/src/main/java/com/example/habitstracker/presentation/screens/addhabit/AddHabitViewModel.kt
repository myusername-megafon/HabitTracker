package com.example.habitstracker.presentation.screens.addhabit

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.data.repository.HabitRepository
import com.example.habitstracker.presentation.theme.HabitColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddHabitState())
    val uiState: StateFlow<AddHabitState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onTargetDurationChange(duration: String) {
        _uiState.update { it.copy(targetDuration = duration.toIntOrNull() ?: 0) }
    }

    fun onIconSelect(iconIndex: Int) {
        _uiState.update { it.copy(selectedIconIndex = iconIndex) }
    }

    fun onColorSelect(colorIndex: Int) {
        _uiState.update { it.copy(selectedColorIndex = colorIndex) }
    }

    suspend fun saveHabit(): Boolean {
        if (_uiState.value.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите название привычки") }
            return false
        }

        if (_uiState.value.targetDuration <= 0) {
            _uiState.update { it.copy(errorMessage = "Укажите длительность цели") }
            return false
        }

        val habit = Habit(
            name = _uiState.value.name,
            description = _uiState.value.description,
            iconIndex = _uiState.value.selectedIconIndex,
            startDate = System.currentTimeMillis(),
            targetDuration = _uiState.value.targetDuration,
            color = HabitColors[_uiState.value.selectedColorIndex].toArgb()
        )

        return try {
            repository.insertHabit(habit)
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Ошибка сохранения: ${e.message}") }
            false
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class AddHabitState(
    val name: String = "",
    val description: String = "",
    val targetDuration: Int = 30,
    val selectedIconIndex: Int = 0,
    val selectedColorIndex: Int = 0,
    val errorMessage: String? = null
)