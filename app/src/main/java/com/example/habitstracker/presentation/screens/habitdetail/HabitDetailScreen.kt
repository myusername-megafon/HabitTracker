package com.example.habitstracker.presentation.screens.habitdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitstracker.data.model.Habit
import com.example.habitstracker.presentation.components.ProgressCalendar
import com.example.habitstracker.presentation.components.ProgressDialog
import com.example.habitstracker.presentation.components.StatItem
import com.example.habitstracker.presentation.components.isSameDay
import com.example.habitstracker.presentation.utils.HabitIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Long,
    onNavigateBack: () -> Unit,
    viewModel: HabitDetailViewModel = hiltViewModel()
) {
    val habit by viewModel.habit.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()
    val totalSuccessDays by viewModel.totalSuccessDays.collectAsState()
    val progressEntries by viewModel.progress.collectAsState()

    var showProgressDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedStatus by remember { mutableStateOf<Boolean?>(null) }

    // Загружаем данные при первом открытии
    LaunchedEffect(habitId) {
        viewModel.loadHabit(habitId)
    }

    // Обработчик для диалога прогресса
    LaunchedEffect(selectedDate) {
        if (selectedDate != null) {
            showProgressDialog = true
        }
    }

    if (showProgressDialog && selectedDate != null) {
        ProgressDialog(
            date = selectedDate!!,
            currentStatus = selectedStatus,
            onDismiss = {
                showProgressDialog = false
                selectedDate = null
            },
            onStatusSelected = { status, comment ->
                viewModel.markProgress(selectedDate!!, status, comment)
                showProgressDialog = false
                selectedDate = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.name ?: "Загрузка...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (habit == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Заголовок с информацией о привычке
                HabitHeader(
                    habit = habit!!,
                    currentStreak = currentStreak,
                    totalSuccessDays = totalSuccessDays
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Календарь прогресса
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Календарь прогресса",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        ProgressCalendar(
                            progressEntries = progressEntries,
                            onDateClick = { date, status ->
                                selectedDate = date
                                selectedStatus = status
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Легенда календаря
                CalendarLegend(modifier = Modifier.padding(16.dp, 0.dp))

                // Быстрая отметка на сегодня
                val today = System.currentTimeMillis()
                val todayStatus = progressEntries.find { it.date.isSameDay(today) }?.isSuccess

                Button(
                    onClick = {
                        selectedDate = today
                        selectedStatus = todayStatus
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (todayStatus) {
                            true -> Color(0xFF4CAF50)
                            false -> Color(0xFFF44336)
                            null -> MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Text(
                        text = when (todayStatus) {
                            true -> "Сегодня: Успех ✓"
                            false -> "Сегодня: Срыв ✗"
                            null -> "Отметить сегодняшний день"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HabitHeader(
    habit: Habit,
    currentStreak: Int,
    totalSuccessDays: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(habit.color),
                            shape = CircleShape
                        ),
                    contentAlignment = Center
                ) {
                    Icon(
                        imageVector = HabitIcons.getIcon(habit.iconIndex),
                        contentDescription = habit.name,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (habit.description.isNotBlank()) {
                        Text(
                            text = habit.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Статистика
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatItem(
                    title = "Текущий стрик",
                    value = "$currentStreak дн.",
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    title = "Успешных дней",
                    value = "$totalSuccessDays дн.",
                    color = MaterialTheme.colorScheme.secondary
                )
                StatItem(
                    title = "Цель",
                    value = "${habit.targetDuration} дн.",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LegendItem(
            color = Color(0xFF4CAF50),
            text = "Успех"
        )
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem(
            color = Color(0xFFF44336),
            text = "Срыв"
        )
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            text = "Сегодня",
            showBorder = true
        )
    }
}

@Composable
fun LegendItem(
    color: Color,
    text: String,
    showBorder: Boolean = false
) {
    Row(
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
                .border(
                    width = if (showBorder) 1.dp else 0.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}