package com.example.habitstracker.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Запускаем сервис для показа уведомления
        val serviceIntent = Intent(context, ReminderService::class.java)
        context.startService(serviceIntent)
    }
}