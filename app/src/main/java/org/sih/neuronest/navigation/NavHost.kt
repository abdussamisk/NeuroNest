package org.sih.neuronest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.sih.neuronest.presentation.caregiver.AlertsScreen
import org.sih.neuronest.presentation.caregiver.AnalyticsScreen
import org.sih.neuronest.presentation.caregiver.CaregiverDashboardScreen
import org.sih.neuronest.presentation.caregiver.CaregiverSettingsScreen
import org.sih.neuronest.presentation.patient.PatientHomeScreen
import org.sih.neuronest.presentation.patient.games.*
import org.sih.neuronest.presentation.patient.routine.RoutineManagerScreen
import org.sih.neuronest.presentation.patient.vault.MemoryVaultScreen

@Composable
fun NeuroNestNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PatientHome.route
    ) {
        // Patient Hub
        composable(Screen.PatientHome.route) {
            PatientHomeScreen(
                onNavigateToScreen = { route -> navController.navigate(route) }
            )
        }

        // 7 Adaptive Cognitive Games
        composable(Screen.MemoryMatchingGame.route) {
            MemoryMatchingScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.SequenceRecallGame.route) {
            SequenceRecallScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.PatternRecognitionGame.route) {
            PatternRecognitionScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.PersonObjectRecognitionGame.route) {
            PersonObjectRecognitionScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.AttentionFocusGame.route) {
            AttentionFocusScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.DailyRoutineRecallGame.route) {
            DailyRoutineRecallScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.WordGame.route) {
            WordGameScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Routine Manager & Memory Vault
        composable(Screen.RoutineManager.route) {
            RoutineManagerScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.MemoryVault.route) {
            MemoryVaultScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Caregiver Dashboard & Sub-screens
        composable(Screen.CaregiverDashboard.route) {
            CaregiverDashboardScreen(
                onNavigateToScreen = { route -> navController.navigate(route) },
                onNavigateBackToPatient = { navController.popBackStack() }
            )
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Alerts.route) {
            AlertsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            CaregiverSettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
