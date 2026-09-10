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
import org.sih.neuronest.theme.ManipurSangaiGreen
import org.sih.neuronest.theme.PatientTextPrimary

@Composable
fun PersonObjectRecognitionScreen(
    onNavigateBack: () -> Unit,
    viewModel: PersonObjectRecognitionViewModel = hiltViewModel()
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
                text = "🖼️ Person & Object Memory",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Score: ${uiState.score}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ManipurSangaiGreen
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

        if (!uiState.isCompleted && uiState.cards.isNotEmpty()) {
            val currentCard = uiState.cards[uiState.currentIndex]

            // Cue Card Image/Emoji Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = currentCard.iconEmoji, fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 Clue: ${currentCard.clueText}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = PatientTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = currentCard.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Multiple choice buttons
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentCard.options.forEach { option ->
                    LargeAccessibilityButton(
                        text = option,
                        onClick = { viewModel.onOptionSelected(option) },
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = Color.White
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
            LargeAccessibilityButton(
                text = "Play Again 🔄",
                onClick = { viewModel.loadCards() }
            )
        }
    }
}
