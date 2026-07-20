package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ApiClient
import com.example.ui.theme.SenaGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    apiClient: ApiClient,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var balance by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var amountText by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isWithdrawing by remember { mutableStateOf(false) }

    fun fetchBalance() {
        isLoading = true
        scope.launch {
            try {
                val response = apiClient.getWalletBalance()
                balance = response.balance
            } catch (e: Exception) {
                // Keep default balance on error
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(key1 = true) {
        fetchBalance()
    }

    fun validateAndWithdraw() {
        val cleanText = amountText.trim()
        val amount = cleanText.toIntOrNull()

        // SDD Section 5 & 8: Validates withdrawal amount client-side
        if (amount == null || amount <= 0) {
            validationError = "Please enter a valid amount."
            return
        }
        if (amount > balance) {
            validationError = "Insufficient balance."
            return
        }

        validationError = null
        isWithdrawing = true

        scope.launch {
            try {
                apiClient.requestWithdrawal(amount)
                amountText = ""
                scope.launch {
                    snackbarHostState.showSnackbar("Withdrawal requested successfully")
                }
                // Update local balance state immediately
                balance -= amount
            } catch (e: Exception) {
                scope.launch {
                    snackbarHostState.showSnackbar("Error: ${e.message}")
                }
            } finally {
                isWithdrawing = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Wallet") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Available Balance",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                CircularProgressIndicator(
                    color = SenaGreen,
                    modifier = Modifier.size(36.dp)
                )
            } else {
                Text(
                    text = "KES $balance",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Withdraw to M-Pesa",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    validationError = null // Clear validation error on type
                },
                prefix = { Text("KES ") },
                label = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = validationError != null,
                supportingText = {
                    if (validationError != null) {
                        Text(text = validationError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdrawal_amount_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { validateAndWithdraw() },
                enabled = !isLoading && !isWithdrawing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("withdraw_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SenaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isWithdrawing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Withdraw",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
