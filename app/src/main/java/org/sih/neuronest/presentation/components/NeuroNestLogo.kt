package org.sih.neuronest.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sih.neuronest.R
import org.sih.neuronest.theme.AssamBihuGold
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary
import org.sih.neuronest.theme.PrimaryTeal
import org.sih.neuronest.theme.SecondaryWarmGold

enum class LogoSize {
    SMALL, MEDIUM, LARGE
}

@Composable
fun NeuroNestLogo(
    modifier: Modifier = Modifier,
    size: LogoSize = LogoSize.MEDIUM,
    showTagline: Boolean = true,
    showRegionalBadge: Boolean = true
) {
    val iconSize: Dp = when (size) {
        LogoSize.SMALL -> 44.dp
        LogoSize.MEDIUM -> 72.dp
        LogoSize.LARGE -> 100.dp
    }

    val titleFontSize = when (size) {
        LogoSize.SMALL -> 22.sp
        LogoSize.MEDIUM -> 30.sp
        LogoSize.LARGE -> 38.sp
    }

    // Gentle pulse animation for the neural core
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo Badge Icon Box
        Box(
            modifier = Modifier
                .scale(pulseScale)
                .size(iconSize)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PrimaryTeal,
                            Color(0xFF0F5157)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_neuronest_logo),
                contentDescription = "NeuroNest Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize * 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title Branding: "NeuroNest"
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Neuro",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = PrimaryTeal
            )
            Text(
                text = "Nest",
                fontSize = titleFontSize,
                fontWeight = FontWeight.ExtraBold,
                color = SecondaryWarmGold
            )
            Text(
                text = " 🧠",
                fontSize = titleFontSize * 0.8f
            )
        }

        if (showTagline) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "AI Cognitive Gaming & Memory Assistance",
                fontSize = if (size == LogoSize.SMALL) 12.sp else 14.sp,
                fontWeight = FontWeight.Medium,
                color = PatientTextSecondary,
                textAlign = TextAlign.Center
            )
        }

        if (showRegionalBadge) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AssamBihuGold.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "🏞️ North Eastern Region Edition",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AssamBihuGold
                )
            }
        }
    }
}
