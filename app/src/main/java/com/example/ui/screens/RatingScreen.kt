package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ApiClient
import com.example.ui.theme.SenaGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    rideId: String,
    apiClient: ApiClient,
    onNavigateToPayment: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedStars by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    fun submitRating() {
        if (selectedStars == 0) return
        isSubmitting = true
        scope.launch {
            try {
                apiClient.submitRating(rideId, selectedStars, comment.ifEmpty { null })
            } catch (e: Exception) {
                // Caught silently; ride still proceeds to payment as per SDD section 5 & 8
            } finally {
                isSubmitting = false
                onNavigateToPayment(rideId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rate your ride") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "How was your trip?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Rating Stars Row (Strictly 1 to 4 Stars as per Flutter Model assert)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                for (starIndex in 1..4) {
                    IconButton(
                        onClick = { selectedStars = starIndex },
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("star_$starIndex")
                    ) {
                        if (starIndex <= selectedStars) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star $starIndex",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_border),
                                contentDescription = "Star $starIndex",
                                tint = Color.LightGray,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Leave a comment (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("comment_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { submitRating() },
                enabled = selectedStars > 0 && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SenaGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Submit & Pay",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
