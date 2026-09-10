package org.sih.neuronest.presentation.caregiver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.navigation.Screen
import org.sih.neuronest.theme.*

@Composable
fun CaregiverDashboardScreen(
    onNavigateToScreen: (String) -> Unit,
    onNavigateBackToPatient: () -> Unit,
    viewModel: CaregiverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val chi = uiState.profile.cognitiveHealthIndex

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Dashboard Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBackToPatient) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "👨‍⚕️ Caregiver Dashboard",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = { onNavigateToScreen(Screen.Settings.route) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Cognitive Health Index (CHI) Metric Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Cognitive Health Index (CHI)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PatientTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${chi.toInt()} / 100",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryTeal
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    if (chi >= 85) GameSuccessGreen else if (chi >= 65) GameWarningAmber else GameDangerCoral,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (chi >= 85) "Optimal Cognitive Engagement" else "Baseline Monitor Active",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricSmallBox("Avg Accuracy", "${uiState.averageAccuracy.toInt()}%", PrimaryTeal)
                        MetricSmallBox("Reaction Time", "${(uiState.averageReactionTimeMs / 1000.0).toString().take(3)}s", SecondaryWarmGold)
                        MetricSmallBox("AI Difficulty", "Level ${uiState.profile.currentDifficultyLevel}", GamePurpleAccent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Quick Navigation Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onNavigateToScreen(Screen.Analytics.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Analytics", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onNavigateToScreen(Screen.Alerts.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = NagalandHornbillRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Alerts (${uiState.alerts.size})", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section Title: Session History
        item {
            Text(
                text = "📊 Recent Cognitive Activity Sessions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Recent Cognitive Sessions
        if (uiState.sessions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No recorded sessions yet. Completed patient exercises will appear here in real-time.",
                        modifier = Modifier.padding(18.dp),
                        color = PatientTextSecondary,
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            items(uiState.sessions) { session ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = session.gameType.replace("_", " "),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PatientTextPrimary
                            )
                            Text(
                                text = "Score: ${session.score} | Level ${session.difficultyLevel}",
                                fontSize = 13.sp,
                                color = PatientTextSecondary
                            )
                        }

                        Text(
                            text = "${session.accuracyPercentage.toInt()}% Acc",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryTeal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricSmallBox(label: String, value: String, accentColor: Color) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 11.sp, color = PatientTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = accentColor)
        }
    }
}
