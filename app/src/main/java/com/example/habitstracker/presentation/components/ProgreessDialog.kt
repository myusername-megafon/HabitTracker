package com.example.habitstracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressDialog(
    date: Long,
    currentStatus: Boolean?,
    onDismiss: () -> Unit,
    onStatusSelected: (Boolean, String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus ?: true) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(date)),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                // Выбор статуса
                Text(
                    text = "Отметить день как:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusChoice(
                        selected = selectedStatus == true,
                        onClick = { selectedStatus = true },
                        text = "Успех",
                        color = Color(0xFF4CAF50),
                        icon = Icons.Default.Check
                    )

                    StatusChoice(
                        selected = selectedStatus == false,
                        onClick = { selectedStatus = false },
                        text = "Срыв",
                        color = Color(0xFFF44336),
                        icon = Icons.Default.Close
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Комментарий
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Комментарий (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onStatusSelected(selectedStatus, comment) }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun StatusChoice(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    color: Color,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) color.copy(alpha = 0.2f) else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) color else color.copy(alpha = 0.5f),
                    shape = CircleShape
                ),
            contentAlignment = Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}