package com.example.habitstracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.habitstracker.data.model.ProgressEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ProgressCalendar(
    progressEntries: List<ProgressEntry>,
    onDateClick: (Long, Boolean?) -> Unit,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    val today = System.currentTimeMillis()

    // Получаем первый день месяца и количество дней
    calendar.timeInMillis = today
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)

    // Устанавливаем первый день месяца
    calendar.set(currentYear, currentMonth, 1)
    val firstDayOfMonth = calendar.timeInMillis

    // Устанавливаем последний день месяца
    calendar.add(Calendar.MONTH, 1)
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    val daysInMonth = calendar.get(Calendar.DAY_OF_MONTH)

    // Получаем день недели первого дня (1-7, где 1 - воскресенье)
    calendar.timeInMillis = firstDayOfMonth
    val firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val days = List(42) { index -> // 6 недель * 7 дней
        val dayIndex = index - (firstDayWeek - 1)
        if (dayIndex >= 0 && dayIndex < daysInMonth) {
            calendar.set(currentYear, currentMonth, dayIndex + 1)
            calendar.timeInMillis to (dayIndex + 1)
        } else {
            null
        }
    }

    Column(modifier = modifier) {
        // Заголовок с месяцем и годом
        Text(
            text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(today)),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Дни недели
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Сетка календаря
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(200.dp)
        ) {
            items(days) { dayInfo ->
                DayCell(
                    dayInfo = dayInfo,
                    progressEntries = progressEntries,
                    today = today,
                    onDateClick = onDateClick
                )
            }
        }
    }
}

@Composable
fun DayCell(
    dayInfo: Pair<Long, Int>?,
    progressEntries: List<ProgressEntry>,
    today: Long,
    onDateClick: (Long, Boolean?) -> Unit
) {
    val dayDate = dayInfo?.first
    val dayNumber = dayInfo?.second

    // Проверяем статус дня
    val dayStatus = dayDate?.let { date ->
        progressEntries.find { it.date.isSameDay(date) }?.isSuccess
    }

    val isToday = dayDate?.isSameDay(today) == true
    val isFuture = dayDate?.let { it > today } == true

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(
                color = when {
                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    dayStatus == true -> Color(0xFF4CAF50) // Зеленый для успеха
                    dayStatus == false -> Color(0xFFF44336) // Красный для срыва
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .border(
                width = if (isToday) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable(
                enabled = !isFuture && dayDate != null,
                onClick = { onDateClick(dayDate!!, dayStatus) }
            ),
        contentAlignment = Center
    ) {
        if (dayNumber != null) {
            Text(
                text = dayNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    isFuture -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

// Extension function для сравнения дней
fun Long.isSameDay(other: Long): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = this@isSameDay }
    val cal2 = Calendar.getInstance().apply { timeInMillis = other }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}