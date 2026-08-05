package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingScreen(
    rating: Float,
    selectedChips: Set<String>,
    reviewText: String,
    onRatingChange: (Float) -> Unit,
    onChipToggle: (String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onSubmitFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val availableChips = listOf(
        "Safe Driving", "Clean Bike/Tuk-Tuk", "Fast Arrival", "Polite Driver", "Good Route"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Circular Driver Photo with Glowing Ring
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(SenaPeach, SenaOrangeCTA, SenaBackground)
                    )
                )
                .border(2.dp, SenaPeach, CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300")
                    .crossfade(true)
                    .build(),
                contentDescription = "Omar Juma Driver Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Omar Juma",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SenaTextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "FAST BOLT  •  KMC-412A",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = SenaElectricCyan
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Feedback Main Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(SenaSurface)
                .border(1.dp, SenaBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Rate Your Driver (1 to 4 Stars)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenaTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your feedback improves safety across Mombasa.",
                    fontSize = 12.sp,
                    color = SenaTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // SDR Rule: Star Rating Bar (1 to 4 Stars: 1=Worse, 2=Fair, 3=Good, 4=Best)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (starIndex in 1..4) {
                        val isFilled = starIndex <= rating
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = "Rate $starIndex stars",
                            tint = if (isFilled) SenaPeach else SenaTextMuted,
                            modifier = Modifier
                                .size(42.dp)
                                .clickable { onRatingChange(starIndex.toFloat()) }
                                .testTag("star_rating_$starIndex")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Star Rating Description Label
                val ratingLabel = when (rating.toInt()) {
                    1 -> "1 Star • Worse / Poor Experience"
                    2 -> "2 Stars • Fair Experience"
                    3 -> "3 Stars • Good Ride"
                    4 -> "4 Stars • Best / Excellent Ride"
                    else -> "4 Stars • Best / Excellent Ride"
                }

                Text(
                    text = ratingLabel,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenaPeach
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Tag Chips (FlowRow)
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    availableChips.forEach { chip ->
                        val isSelected = selectedChips.contains(chip)
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFF262C3E) else Color(0xFF1E2230))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) SenaPeach else SenaBorder,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { onChipToggle(chip) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("feedback_chip_${chip.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = chip,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) SenaPeach else SenaTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Detailed Review Input Box
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Detailed Review",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = onReviewTextChange,
                        placeholder = {
                            Text(
                                text = "Tell us more about your ride...",
                                color = SenaTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SenaPeach,
                            unfocusedBorderColor = SenaBorder,
                            focusedTextColor = SenaTextPrimary,
                            unfocusedTextColor = SenaTextPrimary,
                            focusedContainerColor = Color(0xFF131622),
                            unfocusedContainerColor = Color(0xFF131622)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("detailed_review_input")
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Feedback Button
                Button(
                    onClick = onSubmitFeedback,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_feedback_button"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SenaOrangeCTA
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Submit Rating & Finish",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Submit",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

