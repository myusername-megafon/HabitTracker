package com.example.habitstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.habitstracker.presentation.navigation.HabitTrackerNavGraph
import com.example.habitstracker.presentation.theme.HabitsTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HabitsTrackerTheme {
                val navController = rememberNavController()

                HabitTrackerNavGraph(navController = navController)
            }
        }
    }
}