package org.sih.neuronest.presentation.patient.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
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
import org.sih.neuronest.presentation.patient.PatientViewModel
import org.sih.neuronest.theme.GameSuccessGreen
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary
import org.sih.neuronest.theme.PrimaryTeal

@Composable
fun RoutineManagerScreen(
    onNavigateBack: () -> Unit,
    viewModel: PatientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "🏡 Daily Memory Assistance",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Today's Routine Checklist",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientTextPrimary
                    )
                    Text(
                        text = "Medicine, meals, hydration & daily activities",
                        fontSize = 14.sp,
                        color = PatientTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.reminders) { reminder ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (reminder.isAcknowledged) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "⏰ ${reminder.scheduledTimeFormatted}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryTeal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = reminder.title,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = PatientTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = reminder.audioPromptText,
                                fontSize = 13.sp,
                                color = PatientTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        if (reminder.isAcknowledged) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = GameSuccessGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        } else {
                            Button(
                                onClick = { viewModel.acknowledgeReminder(reminder.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Done ✅", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
