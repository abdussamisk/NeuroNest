package org.sih.neuronest.presentation.patient.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import org.sih.neuronest.presentation.components.LargeAccessibilityButton
import org.sih.neuronest.theme.GamePurpleAccent
import org.sih.neuronest.theme.PatientTextPrimary

@Composable
fun PatternRecognitionScreen(
    onNavigateBack: () -> Unit,
    viewModel: PatternRecognitionViewModel = hiltViewModel()
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "🧩 Pattern Recognition",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Score: ${uiState.score}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GamePurpleAccent
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = uiState.feedbackText,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!uiState.isCompleted && uiState.questions.isNotEmpty()) {
            val currentQ = uiState.questions[uiState.currentQuestionIndex]

            Text(
                text = currentQ.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PatientTextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Pattern Cards Display Row
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    currentQ.patternSequence.forEach { symbol ->
                        Text(text = symbol, fontSize = 42.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Select the missing pattern icon:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Option Buttons
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQ.options.chunked(2).forEach { optionPair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        optionPair.forEach { option ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .clickable { viewModel.onOptionSelected(option) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = option, fontSize = 38.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            LargeAccessibilityButton(
                text = "Play Again 🔄",
                onClick = { viewModel.loadQuestions() }
            )
        }
    }
}
