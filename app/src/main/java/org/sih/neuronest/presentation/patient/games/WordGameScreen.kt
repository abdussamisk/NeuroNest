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
import org.sih.neuronest.theme.GameInfoIndigo
import org.sih.neuronest.theme.PatientTextPrimary

@Composable
fun WordGameScreen(
    onNavigateBack: () -> Unit,
    viewModel: WordGameViewModel = hiltViewModel()
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
                text = "🔤 Regional Words",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Score: ${uiState.score}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GameInfoIndigo
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

        Spacer(modifier = Modifier.height(24.dp))

        if (!uiState.isCompleted && uiState.pairs.isNotEmpty()) {
            val currentPair = uiState.pairs[uiState.currentIndex]

            // Regional Word Display Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
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
                    Text(
                        text = "Language: ${currentPair.languageName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GameInfoIndigo
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentPair.regionalWord,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "What is the meaning of this word?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Options List
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentPair.options.forEach { option ->
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
                onClick = { viewModel.loadWordPairs() }
            )
        }
    }
}
