package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaSuccessGreen
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary

@Composable
fun CheckoutScreen(
    fareKes: Double,
    pickupLocation: String,
    destinationLocation: String,
    selectedPaymentMethod: String,
    walletBalance: Double,
    onPaymentMethodSelect: (String) -> Unit,
    onConfirmAndPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val driver85Share = fareKes * 0.85
    val sena15Fee = fareKes * 0.15

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // RIDER FARE Section
        Text(
            text = "TOTAL RIDE FARE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = SenaTextMuted
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "KES ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SenaPeach,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = String.format("%,.0f", fareKes),
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SenaPeach
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$pickupLocation → $destinationLocation",
            fontSize = 14.sp,
            color = SenaElectricCyan,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SDR 85/15 Auto-Split Breakdown Box (Section 7.2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF131724))
                .border(1.dp, SenaBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "⚡ AUTOMATIC 85/15 PAYMENT SPLIT (SDR Section 7.2)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = SenaElectricCyan
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "85% Driver Wallet Share:",
                        fontSize = 13.sp,
                        color = SenaTextSecondary
                    )
                    Text(
                        text = "KES ${String.format("%,.2f", driver85Share)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaSuccessGreen
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "15% Sena Platform Fee:",
                        fontSize = 13.sp,
                        color = SenaTextMuted
                    )
                    Text(
                        text = "KES ${String.format("%,.2f", sena15Fee)}",
                        fontSize = 13.sp,
                        color = SenaTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Select Payment Method
        Text(
            text = "Select Payment Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = SenaTextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        // Option 1: M-Pesa (STK Push) - Primary Digital Payment
        PaymentOptionCard(
            title = "M-Pesa (Daraja STK Push)",
            subtitle = "Instant prompt sent to your Safaricom line",
            badgeText = "PRIMARY",
            icon = Icons.Default.PhoneAndroid,
            isSelected = selectedPaymentMethod == "M-Pesa",
            onClick = { onPaymentMethodSelect("M-Pesa") },
            testTag = "pay_option_mpesa"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Option 2: Cash Payment (SDR Section 7.6: Cash Fallback)
        PaymentOptionCard(
            title = "Cash Payment",
            subtitle = "Pay directly to driver (85/15 auto-reconciled)",
            badgeText = "CASH",
            icon = Icons.Default.Money,
            isSelected = selectedPaymentMethod == "Cash Payment",
            onClick = { onPaymentMethodSelect("Cash Payment") },
            testTag = "pay_option_cash"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Driver Info Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SenaSurface)
                .border(1.dp, SenaBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF222738)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TwoWheeler,
                        contentDescription = "Driver Icon",
                        tint = SenaPeach,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Driver: Omar Juma",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "KMC-412A • 4.0★ Driver Rating",
                        fontSize = 12.sp,
                        color = SenaTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm & Pay CTA Orange Button
        Button(
            onClick = onConfirmAndPay,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("confirm_and_pay_button"),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SenaOrangeCTA
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Confirm & Initiate Payment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Proceed",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "M-PESA DARAJA NATIVE API • 85/15 AUTOMATIC SPLIT",
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            color = SenaTextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PaymentOptionCard(
    title: String,
    subtitle: String,
    badgeText: String? = null,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) SenaSurface else SenaSurface.copy(alpha = 0.5f))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) SenaPeach else SenaBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) SenaPeach.copy(alpha = 0.2f) else Color(0xFF222634)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) SenaPeach else SenaTextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaTextPrimary
                    )

                    if (badgeText != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2D3242))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SenaTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = SenaTextSecondary
                )
            }

            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = if (isSelected) "Selected" else "Unselected",
                tint = if (isSelected) SenaPeach else SenaTextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
