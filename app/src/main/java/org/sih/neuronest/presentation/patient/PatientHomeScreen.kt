package org.sih.neuronest.presentation.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.navigation.Screen
import org.sih.neuronest.presentation.components.GameCard
import org.sih.neuronest.presentation.components.LargeAccessibilityButton
import org.sih.neuronest.presentation.components.PatientHeader
import org.sih.neuronest.theme.*

@Composable
fun PatientHomeScreen(
    onNavigateToScreen: (String) -> Unit,
    viewModel: PatientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val level = uiState.profile.currentDifficultyLevel

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Patient Header Component
        item {
            PatientHeader(
                patientName = uiState.profile.name,
                region = uiState.currentRegion,
                voicePromptActive = uiState.isVoiceSpeaking,
                onVoiceSpeakClick = { viewModel.speakHomeGreeting() },
                onRegionSelectClick = { viewModel.selectNextRegion() },
                onSwitchToCaregiverClick = { onNavigateToScreen(Screen.CaregiverDashboard.route) }
            )
        }

        // Daily Assistance Quick Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Routine Assistance Shortcut
                    Button(
                        onClick = { onNavigateToScreen(Screen.RoutineManager.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = "Routine")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Routine Tasks", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Memory Vault Shortcut
                    Button(
                        onClick = { onNavigateToScreen(Screen.MemoryVault.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryWarmGold),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PhotoAlbum, contentDescription = "Vault")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Memory Vault", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section Title: Adaptive Cognitive Games
        item {
            PaddingValues(horizontal = 20.dp, vertical = 10.dp).let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧠 Cognitive Exercises",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientTextPrimary
                    )
                    Text(
                        text = "AI Level $level",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = uiState.currentRegion.primaryColor
                    )
                }
            }
        }

        // Game 1: Memory Matching (NER Flora/Fauna)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Memory Matching",
                    subtitle = "NER Flora, Fauna & Cultural Icons",
                    iconEmoji = "🌸",
                    difficultyLevel = level,
                    badgeColor = PrimaryTeal,
                    onClick = { onNavigateToScreen(Screen.MemoryMatchingGame.route) }
                )
            }
        }

        // Game 2: Sequence Recall (Bihu Dhol Rhythms)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Sequence & Rhythm Recall",
                    subtitle = "Audio-Visual Bihu Dhol drum patterns",
                    iconEmoji = "🥁",
                    difficultyLevel = level,
                    badgeColor = AssamBihuGold,
                    onClick = { onNavigateToScreen(Screen.SequenceRecallGame.route) }
                )
            }
        }

        // Game 3: Pattern Recognition (Mekhela Chador Weaving)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Pattern Recognition",
                    subtitle = "Traditional textile weaving patterns",
                    iconEmoji = "🧩",
                    difficultyLevel = level,
                    badgeColor = GamePurpleAccent,
                    onClick = { onNavigateToScreen(Screen.PatternRecognitionGame.route) }
                )
            }
        }

        // Game 4: Person & Object Recognition (ML Kit Memory Cues)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Person & Object Memory",
                    subtitle = "Match family cards & household objects",
                    iconEmoji = "🖼️",
                    difficultyLevel = level,
                    badgeColor = ManipurSangaiGreen,
                    onClick = { onNavigateToScreen(Screen.PersonObjectRecognitionGame.route) }
                )
            }
        }

        // Game 5: Attention & Concentration Exercises
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Attention & Concentration",
                    subtitle = "Visual track & tap with ambient nature sounds",
                    iconEmoji = "🎯",
                    difficultyLevel = level,
                    badgeColor = MeghalayaRootBlue,
                    onClick = { onNavigateToScreen(Screen.AttentionFocusGame.route) }
                )
            }
        }

        // Game 6: Daily Routine Recall
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Daily Routine Recall",
                    subtitle = "Reconstruct morning & evening schedules",
                    iconEmoji = "📅",
                    difficultyLevel = level,
                    badgeColor = TripuraTeaAmber,
                    onClick = { onNavigateToScreen(Screen.DailyRoutineRecallGame.route) }
                )
            }
        }

        // Game 7: Simple Language & Word Games
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GameCard(
                    title = "Regional Language & Words",
                    subtitle = "Assamese, Manipuri & Bengali word pairs",
                    iconEmoji = "🔤",
                    difficultyLevel = level,
                    badgeColor = GameInfoIndigo,
                    onClick = { onNavigateToScreen(Screen.WordGame.route) }
                )
            }
        }
    }
}
