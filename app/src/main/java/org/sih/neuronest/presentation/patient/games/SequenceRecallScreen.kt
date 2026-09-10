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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.presentation.components.LargeAccessibilityButton
import org.sih.neuronest.theme.AssamBihuGold
import org.sih.neuronest.theme.MeghalayaRootBlue
import org.sih.neuronest.theme.MizoBambooEmerald
import org.sih.neuronest.theme.NagalandHornbillRed

@Composable
fun SequenceRecallScreen(
    onNavigateBack: () -> Unit,
    viewModel: SequenceRecallViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val padColors = listOf(
        AssamBihuGold,
        MizoBambooEmerald,
        NagalandHornbillRed,
        MeghalayaRootBlue
    )
    val padLabels = listOf("Dhol 🥁", "Pepa 🎺", "Gogona 🎵", "Taka 🎶")

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
                text = "🥁 Sequence & Rhythm",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Round: ${uiState.round}/4",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AssamBihuGold
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

        // 2x2 Drum Pads Grid
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DrumPad(
                    label = padLabels[0],
                    color = padColors[0],
                    isHighlighted = uiState.activeHighlightedPad == 0,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onPadClick(0) }
                )
                DrumPad(
                    label = padLabels[1],
                    color = padColors[1],
                    isHighlighted = uiState.activeHighlightedPad == 1,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onPadClick(1) }
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DrumPad(
                    label = padLabels[2],
                    color = padColors[2],
                    isHighlighted = uiState.activeHighlightedPad == 2,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onPadClick(2) }
                )
                DrumPad(
                    label = padLabels[3],
                    color = padColors[3],
                    isHighlighted = uiState.activeHighlightedPad == 3,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onPadClick(3) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isGameOver) {
            LargeAccessibilityButton(
                text = "Try Again 🔄",
                onClick = { viewModel.startNewGame() }
            )
        }
    }
}

@Composable
fun DrumPad(
    label: String,
    color: Color,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) Color.White else color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 12.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.Black else Color.White
            )
        }
    }
}
