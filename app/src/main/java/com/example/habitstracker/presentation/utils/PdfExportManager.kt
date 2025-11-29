package com.example.habitstracker.presentation.utils

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.habitstracker.R
import com.example.habitstracker.data.model.HabitWithProgress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class PdfExportManager(private val context: Context) {

    fun exportToPdf(habits: List<HabitWithProgress>) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "${context.getString(R.string.app_name)}_${System.currentTimeMillis()}"

        // Создаем WebView для рендеринга HTML в PDF
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Когда страница загружена, создаем PDF
                createPdf(printManager, jobName, webView)
            }
        }

        // Генерируем HTML контент
        val htmlContent = generateHtmlContent(habits)
        webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)
    }

    private fun createPdf(printManager: PrintManager, jobName: String, webView: WebView) {
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }

    private fun generateHtmlContent(habits: List<HabitWithProgress>): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; margin-bottom: 30px; }
                .title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .date { color: #666; font-size: 14px; }
                .habit { margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
                .habit-name { font-size: 18px; font-weight: bold; color: #333; }
                .stats { display: flex; justify-content: space-between; margin-top: 10px; }
                .stat-item { text-align: center; }
                .stat-value { font-size: 16px; font-weight: bold; }
                .stat-label { font-size: 12px; color: #666; }
                .summary { margin-top: 30px; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="title">Статистика привычек</div>
                <div class="date">Сгенерировано: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(
            Date()
        )}</div>
            </div>
            
            ${generateHabitsHtml(habits)}
            
            ${generateSummaryHtml(habits)}
        </body>
        </html>
        """.trimIndent()
    }

    private fun generateHabitsHtml(habits: List<HabitWithProgress>): String {
        return habits.joinToString("") { habitWithProgress ->
            """
            <div class="habit">
                <div class="habit-name">${habitWithProgress.habit.name}</div>
                ${if (habitWithProgress.habit.description.isNotBlank()) "<div>${habitWithProgress.habit.description}</div>" else ""}
                <div class="stats">
                    <div class="stat-item">
                        <div class="stat-value">${habitWithProgress.currentStreak}</div>
                        <div class="stat-label">Текущий стрик</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">${habitWithProgress.habit.targetDuration}</div>
                        <div class="stat-label">Цель</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">${calculateSuccessRate(habitWithProgress)}%</div>
                        <div class="stat-label">Успешность</div>
                    </div>
                </div>
            </div>
            """.trimIndent()
        }
    }

    private fun generateSummaryHtml(habits: List<HabitWithProgress>): String {
        val totalHabits = habits.size
        val totalStreak = habits.sumOf { it.currentStreak }
        val avgStreak = if (habits.isNotEmpty()) totalStreak / habits.size else 0
        val completedHabits = habits.count { it.currentStreak >= it.habit.targetDuration }

        return """
        <div class="summary">
            <div style="font-weight: bold; margin-bottom: 10px;">Общая статистика</div>
            <div class="stats">
                <div class="stat-item">
                    <div class="stat-value">$totalHabits</div>
                    <div class="stat-label">Всего привычек</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">$avgStreak</div>
                    <div class="stat-label">Средний стрик</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">$completedHabits</div>
                    <div class="stat-label">Завершено</div>
                </div>
            </div>
        </div>
        """.trimIndent()
    }

    private fun calculateSuccessRate(habitWithProgress: HabitWithProgress): Int {
        val habit = habitWithProgress.habit
        val daysPassed = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - habit.startDate).toInt() + 1
        return if (daysPassed > 0) {
            (habitWithProgress.currentStreak * 100 / daysPassed)
        } else {
            0
        }
    }
}