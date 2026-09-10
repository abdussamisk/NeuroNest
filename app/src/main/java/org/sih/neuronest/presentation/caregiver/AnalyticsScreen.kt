package org.sih.neuronest.presentation.caregiver

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary
import org.sih.neuronest.theme.PrimaryTeal
import org.sih.neuronest.theme.SecondaryWarmGold

@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CaregiverViewModel = hiltViewModel()
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
                text = "📈 Cognitive Health Analytics",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Accuracy Trend Chart Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Accuracy Performance Trend (%)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientTextPrimary
                )
                Text(
                    text = "30-Day Rolling Cognitive Accuracy",
                    fontSize = 13.sp,
                    color = PatientTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Compose Canvas Line Graph
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    val points = listOf(
                        Offset(0f, size.height * 0.4f),
                        Offset(size.width * 0.25f, size.height * 0.25f),
                        Offset(size.width * 0.5f, size.height * 0.35f),
                        Offset(size.width * 0.75f, size.height * 0.15f),
                        Offset(size.width, size.height * 0.2f)
                    )

                    val path = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) {
                            lineTo(points[i].x, points[i].y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = PrimaryTeal,
                        style = Stroke(width = 6f)
                    )

                    for (point in points) {
                        drawCircle(
                            color = SecondaryWarmGold,
                            radius = 8f,
                            center = point
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reaction Latency Chart Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reaction Time Latency (seconds)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientTextPrimary
                )
                Text(
                    text = "Lower latency indicates faster cognitive processing",
                    fontSize = 13.sp,
                    color = PatientTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    val barWidth = size.width / 6f
                    val barHeights = listOf(0.6f, 0.45f, 0.55f, 0.35f, 0.40f)

                    for (i in barHeights.indices) {
                        val x = i * (barWidth + 16f) + 16f
                        val barHeight = size.height * barHeights[i]
                        drawRect(
                            color = SecondaryWarmGold,
                            topLeft = Offset(x, size.height - barHeight),
                            size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                        )
                    }
                }
            }
        }
    }
}
