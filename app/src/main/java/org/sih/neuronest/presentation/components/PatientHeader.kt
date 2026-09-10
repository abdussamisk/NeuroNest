package org.sih.neuronest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sih.neuronest.theme.NERRegion
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary

@Composable
fun PatientHeader(
    patientName: String,
    region: NERRegion,
    voicePromptActive: Boolean,
    onVoiceSpeakClick: () -> Unit,
    onRegionSelectClick: () -> Unit,
    onSwitchToCaregiverClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeuroNestLogo(
                        size = LogoSize.SMALL,
                        showTagline = false,
                        showRegionalBadge = false
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(region.primaryColor)
                            .clickable { onRegionSelectClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📍 ${region.stateName}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Caregiver Mode Switcher Button
                Button(
                    onClick = onSwitchToCaregiverClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Caregiver Dashboard 👨‍⚕️", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = region.backgroundGreeting,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = region.primaryColor
                    )
                    Text(
                        text = "Welcome back, $patientName",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = PatientTextPrimary
                    )
                }

                // Voice Prompt Action Button
                IconButton(
                    onClick = onVoiceSpeakClick,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (voicePromptActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Voice Prompt",
                        tint = if (voicePromptActive) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle & Cultural Symbol
            Text(
                text = "Symbol: ${region.culturalSymbol}",
                fontSize = 14.sp,
                color = PatientTextSecondary
            )
        }
    }
}
