package com.example.habitstracker.presentation.utils
import com.example.habitstracker.R

object HabitIcons {

    val defaultIcons = listOf(
        R.drawable.smoking_icon to "Курение",
        R.drawable.stuff_icon to "Наркотики",
        R.drawable.fastfood_icon to "Фаст-Фуд",
        R.drawable.pills_icon to "Препараты",
        R.drawable.lazy_icon to "Прокрастинация",
        R.drawable.cenzor_icon to "Нецензура"
    )

    fun getIconResId(index: Int): Int =
        defaultIcons.getOrNull(index)?.first ?: R.drawable.smoking_icon

    fun getDisplayName(index: Int): String =
        defaultIcons.getOrNull(index)?.second ?: "Другое"

    fun getTotalIconsCount(): Int = defaultIcons.size
}
