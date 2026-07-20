package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.RideSocketService
import com.example.ui.theme.SenaGreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PaymentState { IDLE, AWAITING_PIN, CONFIRMING, SUCCESS, FAILED }

@Composable
fun PaymentScreen(
    rideId: String,
    apiClient: ApiClient,
    socketService: RideSocketService,
    onNavigateToMap: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf(PaymentState.IDLE) }
    var failsafeJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(key1 = rideId) {
        socketService.connect(rideId)
        socketService.stream.collect { frame ->
            if (frame["type"] == "payment_status") {
                when (frame["status"]) {
                    "completed" -> {
                        state = PaymentState.SUCCESS
                        failsafeJob?.cancel()
                    }
                    "failed" -> {
                        state = PaymentState.FAILED
                        failsafeJob?.cancel()
                    }
                }
            }
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
            failsafeJob?.cancel()
            socketService.disconnect()
        }
    }

    fun startFailsafeTimer() {
        failsafeJob?.cancel()
        failsafeJob = scope.launch {
            delay(60000) // 60-second failsafe timeout as per SDD section 8
            if (state == PaymentState.CONFIRMING) {
                state = PaymentState.FAILED
            }
        }
    }

    fun handleMpesaPayment() {
        state = PaymentState.AWAITING_PIN
        scope.launch {
            try {
                apiClient.initiateMpesaPayment(rideId, 150)
                state = PaymentState.CONFIRMING
                startFailsafeTimer()
                socketService.triggerMockPaymentCompletion()
            } catch (e: Exception) {
                state = PaymentState.FAILED
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                PaymentState.IDLE -> {
                    Text(
                        text = "Payment Total",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "KES 150",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = { handleMpesaPayment() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("mpesa_pay_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SenaGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Pay with M-Pesa",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                PaymentState.AWAITING_PIN -> {
                    CircularProgressIndicator(
                        color = SenaGreen,
                        modifier = Modifier
                            .size(64.dp)
                            .testTag("awaiting_pin_loader")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Initiating M-Pesa payment...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please check your phone for the STK push",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                PaymentState.CONFIRMING -> {
                    CircularProgressIndicator(
                        color = SenaGreen,
                        modifier = Modifier
                            .size(64.dp)
                            .testTag("confirming_loader")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Confirming payment on your phone...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please enter your M-Pesa PIN",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                PaymentState.SUCCESS -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = SenaGreen,
                        modifier = Modifier
                            .size(96.dp)
                            .testTag("success_icon")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Payment Successful!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = onNavigateToMap,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("done_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Done",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                PaymentState.FAILED -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = "Failed",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .size(96.dp)
                            .testTag("failed_icon")
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Payment Failed",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { handleMpesaPayment() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("retry_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SenaGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Try Again",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = onNavigateToMap,
                        modifier = Modifier.testTag("cash_button")
                    ) {
                        Text(
                            text = "Pay with Cash instead",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
