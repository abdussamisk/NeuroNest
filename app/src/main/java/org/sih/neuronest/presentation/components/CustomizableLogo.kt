package org.sih.neuronest.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Universal Brand & Logo Component for Jetpack Compose
 * Allows replacing the logo image, title, colors, and badge for any Android project.
 */
@Composable
fun CustomizableLogo(
    modifier: Modifier = Modifier,
    // 1. IMAGE CONFIGURATION (Pass any drawable ID or ImageVector)
    @DrawableRes logoDrawableRes: Int? = null,
    logoImageVector: ImageVector? = null,
    
    // 2. BRAND NAME CONFIGURATION
    titleFirstPart: String = "App",
    titleSecondPart: String = "Name",
    titleEmoji: String = "🚀",
    firstPartColor: Color = Color(0xFF0F766E),
    secondPartColor: Color = Color(0xFFD97706),
    
    // 3. TAGLINE & BADGE CONFIGURATION
    tagline: String? = "Your Application Subtitle Here",
    badgeText: String? = "v1.0 Edition",
    badgeColor: Color = Color(0xFFF59E0B),
    
    // 4. DESIGN & ANIMATION CONFIGURATION
    logoBackgroundGradient: List<Color> = listOf(Color(0xFF0F766E), Color(0xFF0F5157)),
    size: LogoSize = LogoSize.MEDIUM,
    enablePulseAnimation: Boolean = true
) {
    val iconSize: Dp = when (size) {
        LogoSize.SMALL -> 44.dp
        LogoSize.MEDIUM -> 72.dp
        LogoSize.LARGE -> 108.dp
    }

    val titleFontSize = when (size) {
        LogoSize.SMALL -> 20.sp
        LogoSize.MEDIUM -> 28.sp
        LogoSize.LARGE -> 36.sp
    }

    // Optional Pulse Animation
    val infiniteTransition = rememberInfiniteTransition(label = "PulseAnimation")
    val pulseScale by if (enablePulseAnimation) {
        infiniteTransition.animateFloat(
            initialValue = 0.96f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "PulseScale"
        )
    } else {
        rememberUpdatedState(1f)
    }

    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- LOGO IMAGE CONTAINER ---
        Box(
            modifier = Modifier
                .scale(pulseScale)
                .size(iconSize)
                .clip(CircleShape)
                .background(Brush.radialGradient(colors = logoBackgroundGradient)),
            contentAlignment = Alignment.Center
        ) {
            when {
                logoDrawableRes != null -> {
                    Icon(
                        painter = painterResource(id = logoDrawableRes),
                        contentDescription = "$titleFirstPart $titleSecondPart Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(iconSize * 0.75f)
                    )
                }
                logoImageVector != null -> {
                    Icon(
                        imageVector = logoImageVector,
                        contentDescription = "$titleFirstPart $titleSecondPart Logo",
                        tint = Color.White,
                        modifier = Modifier.size(iconSize * 0.65f)
                    )
                }
                else -> {
                    // Fallback Text Initial if no image is passed
                    Text(
                        text = "${titleFirstPart.take(1)}${titleSecondPart.take(1)}".uppercase(),
                        fontSize = (iconSize.value * 0.35).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- BRAND TITLE ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = titleFirstPart,
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = firstPartColor
            )
            Text(
                text = titleSecondPart,
                fontSize = titleFontSize,
                fontWeight = FontWeight.ExtraBold,
                color = secondPartColor
            )
            if (titleEmoji.isNotEmpty()) {
                Text(
                    text = " $titleEmoji",
                    fontSize = titleFontSize * 0.8f
                )
            }
        }

        // --- OPTIONAL TAGLINE ---
        if (!tagline.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tagline,
                fontSize = if (size == LogoSize.SMALL) 12.sp else 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        // --- OPTIONAL BADGE ---
        if (!badgeText.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeColor.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
        }
    }
}
