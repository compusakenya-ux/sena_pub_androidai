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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Wallet
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.WalletTransaction
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaPlatinum
import com.example.ui.theme.SenaSuccessGreen
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary

@Composable
fun WalletScreen(
    balance: Double,
    driverTotalRides: Int,
    driverGrossEarnings: Double,
    userTier: String,
    transactions: List<WalletTransaction>,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val net85Earnings = driverGrossEarnings * 0.85
    val sena15Commission = driverGrossEarnings * 0.15

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // DRIVER WITHDRAWAL WALLET Glow Card (SDR Section 7.3)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SenaSurface)
                    .border(1.dp, SenaBorder, RoundedCornerShape(24.dp))
                    .padding(24.dp)
                    .testTag("wallet_balance_card")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "DRIVER ACCUMULATED WALLET (85% SHARE)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = SenaTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                            text = String.format("%,.2f", balance),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = SenaPeach
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // SDR Section 7.3: Direct B2C Driver Withdrawal Button
                    Button(
                        onClick = onWithdrawClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("driver_withdraw_b2c_button"),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SenaOrangeCTA)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Withdraw to M-Pesa",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "WITHDRAW TO M-PESA (B2C)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(SenaBorder)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "TOTAL RIDES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = SenaTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$driverTotalRides Rides",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SenaElectricCyan
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(SenaBorder)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "NET 85% SHARE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = SenaTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "KES ${String.format("%,.0f", net85Earnings)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SenaSuccessGreen
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(SenaBorder)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SENA 15% FEE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = SenaTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "KES ${String.format("%,.0f", sena15Commission)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SenaPlatinum
                            )
                        }
                    }
                }
            }
        }

        // Driver Ledger Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Driver Ledger & Payouts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenaTextPrimary
                )

                Text(
                    text = "DARAJA B2C ACTIVE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = SenaElectricCyan
                )
            }
        }

        // Transactions List
        items(transactions) { tx ->
            TransactionRowItem(transaction = tx)
        }

        // SDR 85/15 AUTOMATIC RECONCILIATION Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF121B28))
                    .border(1.dp, SenaElectricCyan.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "SDR Rules",
                            tint = SenaElectricCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AUTOMATIC 85/15 SPLIT RECONCILIATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = SenaElectricCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "85% of every M-Pesa or Cash ride is automatically deposited to your Driver Wallet. Withdraw anytime directly to M-Pesa.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = SenaTextPrimary
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun TransactionRowItem(transaction: WalletTransaction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SenaSurface)
            .border(1.dp, SenaBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("tx_item_${transaction.id}")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF222736)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        transaction.title.contains("Withdrawal", ignoreCase = true) -> Icons.Default.Send
                        transaction.title.contains("Cash", ignoreCase = true) -> Icons.Default.Money
                        transaction.title.contains("Tuk-Tuk", ignoreCase = true) -> Icons.Default.Moped
                        else -> Icons.Default.TwoWheeler
                    },
                    contentDescription = transaction.title,
                    tint = if (transaction.isCredit) SenaSuccessGreen else SenaPeach,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenaTextPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = transaction.subtitle,
                    fontSize = 12.sp,
                    color = SenaTextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.isCredit) "+" else "-"} KES ${String.format("%,.2f", transaction.amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.isCredit) SenaSuccessGreen else SenaPeach
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = transaction.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = if (transaction.status == "SUCCESS" || transaction.status == "COMPLETED") SenaSuccessGreen else SenaTextMuted
                )
            }
        }
    }
}
