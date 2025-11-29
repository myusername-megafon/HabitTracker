package com.example.habitstracker.presentation.screens.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.habitstracker.service.ReminderManager
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val reminderManager = remember { ReminderManager(context) }

    var isReminderEnabled by remember {
        mutableStateOf(reminderManager.isReminderScheduled())
    }
    var reminderTime by remember { mutableStateOf(LocalTime.of(20, 0)) } // 20:00 по умолчанию

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Напоминания") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Включение/выключение напоминаний
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ежедневные напоминания",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = { enabled ->
                        isReminderEnabled = enabled
                        if (enabled) {
                            reminderManager.scheduleDailyReminder(
                                reminderTime.hour,
                                reminderTime.minute
                            )
                            // Показываем сообщение об успехе
                            Toast.makeText(
                                context,
                                "Напоминание установлено на ${reminderTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            reminderManager.cancelReminder()
                            Toast.makeText(
                                context,
                                "Напоминание отключено",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }

            // Выбор времени напоминания
            if (isReminderEnabled) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Время напоминания",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Простой выбор времени (можно заменить на TimePicker)
                        var showTimePicker by remember { mutableStateOf(false) }

                        Button(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Установить время: ${reminderTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                        }

                        if (showTimePicker) {
                            // TODO: Реализовать TimePicker диалог
                            AlertDialog(
                                onDismissRequest = { showTimePicker = false },
                                title = { Text("Выберите время") },
                                text = {
                                    // Простой выбор из списка
                                    Column {
                                        listOf(
                                            LocalTime.of(8, 0) to "Утро (8:00)",
                                            LocalTime.of(12, 0) to "Обед (12:00)",
                                            LocalTime.of(18, 0) to "Вечер (18:00)",
                                            LocalTime.of(20, 0) to "Ночь (20:00)"
                                        ).forEach { (time, label) ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        reminderTime = time
                                                        if (isReminderEnabled) {
                                                            reminderManager.scheduleDailyReminder(
                                                                time.hour,
                                                                time.minute
                                                            )
                                                        }
                                                        showTimePicker = false
                                                    }
                                                    .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = reminderTime == time,
                                                    onClick = {
                                                        reminderTime = time
                                                        if (isReminderEnabled) {
                                                            reminderManager.scheduleDailyReminder(
                                                                time.hour,
                                                                time.minute
                                                            )
                                                        }
                                                        showTimePicker = false
                                                    }
                                                )
                                                Text(
                                                    text = label,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { showTimePicker = false }) {
                                        Text("Отмена")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Информация о напоминаниях
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "О напоминаниях",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Ежедневные напоминания помогут вам не забывать отмечать прогресс по вашим привычкам. Вы получите уведомление в выбранное время.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}