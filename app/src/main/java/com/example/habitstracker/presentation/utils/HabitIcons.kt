package com.example.habitstracker.presentation.utils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

object HabitIcons {
    // Используем только базовые иконки, которые точно есть
    val defaultIcons = listOf(
        Icons.Default.Warning to "Курение",
        Icons.Default.Warning to "Алкоголь",
        Icons.Default.Warning to "Фастфуд",
        Icons.Default.Phone to "Телефон/Соцсети",
        Icons.Default.ShoppingCart to "Шопинг",
        Icons.Default.Warning to "Сладости",
        Icons.Default.Warning to "Кофе/Напитки",
        Icons.Default.Warning to "Настроение",
        Icons.Default.Star to "Другое",
        Icons.Default.Favorite to "Любимое",
        Icons.Default.Home to "Дом",
        Icons.Default.Warning to "Работа",
        Icons.Default.Warning to "Учеба",
        Icons.Default.Warning to "Спорт",
        Icons.Default.Warning to "Здоровье"
    )

    fun getIcon(index: Int): ImageVector {
        return defaultIcons.getOrNull(index)?.first ?: Icons.Default.Warning
    }

    fun getDisplayName(index: Int): String {
        return defaultIcons.getOrNull(index)?.second ?: "Другое"
    }

    fun getTotalIconsCount(): Int {
        return defaultIcons.size
    }
}