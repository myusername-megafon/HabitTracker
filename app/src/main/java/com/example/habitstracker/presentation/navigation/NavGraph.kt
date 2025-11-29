package com.example.habitstracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habitstracker.presentation.screens.addhabit.AddHabitScreen
import com.example.habitstracker.presentation.screens.habitdetail.HabitDetailScreen
import com.example.habitstracker.presentation.screens.habits.HabitsScreen
import com.example.habitstracker.presentation.screens.profile.ProfileScreen
import com.example.habitstracker.presentation.screens.settings.ReminderSettingsScreen

@Composable
fun HabitTrackerNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Habits.route
    ) {
        composable(Screen.Habits.route) {
            HabitsScreen(
                onNavigateToAddHabit = {
                    navController.navigate(Screen.AddHabit.route)
                },
                onNavigateToHabitDetail = { habitId ->
                    navController.navigate(Screen.HabitDetail.createRoute(habitId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.AddHabit.route) {
            AddHabitScreen(
                onNavigateBack = { navController.popBackStack() },
                onHabitSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.HabitDetail.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toLongOrNull()
            if (habitId != null) {
                HabitDetailScreen(
                    habitId = habitId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReminderSettings = {navController.navigate(Screen.ReminderSettings.route)}
            )
        }

        composable(Screen.ReminderSettings.route) {
            ReminderSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}