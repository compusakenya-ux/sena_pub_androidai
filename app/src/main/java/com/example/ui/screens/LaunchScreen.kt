package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextSecondary
import kotlin.random.Random

@Composable
fun LaunchScreen(
    onLaunchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "launch_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
    ) {
        // Space / Starry particles background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val random = Random(42)
            for (i in 0..60) {
                val cx = random.nextFloat() * size.width
                val cy = random.nextFloat() * size.height
                val radius = random.nextFloat() * 2.5f + 1f
                val alpha = random.nextFloat() * 0.5f + 0.2f
                drawCircle(
                    color = SenaPeach.copy(alpha = alpha),
                    radius = radius,
                    center = Offset(cx, cy)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // SENA Main Headline Branding with Electric Badge
            Box(contentAlignment = Alignment.TopEnd) {
                Text(
                    text = "SENA",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp,
                    color = SenaPeach,
                    modifier = Modifier.padding(end = 16.dp)
                )

                // Electric symbol badge
                Box(
                    modifier = Modifier
                        .offset(x = 12.dp, y = 4.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF141926))
                        .border(1.dp, SenaElectricCyan.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Electric Power",
                        tint = SenaElectricCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Next-gen transit, hyper-fluid speed.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = SenaTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-tagline: POWERING MOMBASA
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(1.dp)
                        .background(SenaElectricCyan)
                )
                Text(
                    text = "POWERING MOMBASA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = SenaElectricCyan,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(1.dp)
                        .background(SenaElectricCyan)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // CTA Button: LAUNCH JOURNEY with glowing aura
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow shadow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    SenaPeach.copy(alpha = glowAlpha),
                                    SenaOrangeCTA.copy(alpha = glowAlpha)
                                )
                            )
                        )
                )

                Button(
                    onClick = onLaunchClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("launch_journey_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SenaPeach
                    )
                ) {
                    Text(
                        text = "LAUNCH JOURNEY",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = SenaBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

