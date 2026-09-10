package org.sih.neuronest.navigation

sealed class Screen(val route: String, val title: String) {
    // Patient Hub & Navigation
    object PatientHome : Screen("patient_home", "Patient Hub")
    
    // 7 Adaptive Cognitive Games
    object MemoryMatchingGame : Screen("game_memory_matching", "Memory Matching")
    object SequenceRecallGame : Screen("game_sequence_recall", "Sequence Recall")
    object PatternRecognitionGame : Screen("game_pattern_recognition", "Pattern Recognition")
    object PersonObjectRecognitionGame : Screen("game_person_object", "Person & Object Memory")
    object AttentionFocusGame : Screen("game_attention_focus", "Attention & Concentration")
    object DailyRoutineRecallGame : Screen("game_routine_recall", "Daily Routine Recall")
    object WordGame : Screen("game_word_matching", "Word & Regional Language")

    // Memory & Routine Assistance
    object RoutineManager : Screen("routine_manager", "Daily Routine Assistance")
    object TaskGuidance : Screen("task_guidance", "Step-by-Step Task Guide")
    object MemoryVault : Screen("memory_vault", "Personal Memory Vault")

    // Caregiver & Doctor Monitoring Dashboard
    object CaregiverDashboard : Screen("caregiver_dashboard", "Caregiver Dashboard")
    object Analytics : Screen("caregiver_analytics", "Cognitive Health Analytics")
    object Alerts : Screen("caregiver_alerts", "Alerts & Anomaly Logs")
    object Settings : Screen("caregiver_settings", "Settings & Regional Configuration")
}
