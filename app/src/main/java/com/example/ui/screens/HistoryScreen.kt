package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.RideBooking
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaSuccessGreen
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary

@Composable
fun HistoryScreen(
    rideList: List<RideBooking>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Ride History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SenaTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your completed transit journeys across Mombasa",
                fontSize = 13.sp,
                color = SenaTextSecondary
            )
        }

        if (rideList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No ride history found. Book your first journey!",
                        fontSize = 14.sp,
                        color = SenaTextMuted
                    )
                }
            }
        } else {
            items(rideList) { ride ->
                RideHistoryCard(ride = ride)
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun RideHistoryCard(ride: RideBooking) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SenaSurface)
            .border(1.dp, SenaBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
            .testTag("ride_history_card_${ride.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF222736)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (ride.rideType.contains("Electric", ignoreCase = true)) Icons.Default.ElectricBike else Icons.Default.TwoWheeler,
                            contentDescription = ride.rideType,
                            tint = if (ride.rideType.contains("Electric", ignoreCase = true)) SenaElectricCyan else SenaPeach,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = ride.rideType,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SenaTextPrimary
                        )
                        Text(
                            text = "Driver: ${ride.driverName} (${ride.driverVehicleNumber})",
                            fontSize = 12.sp,
                            color = SenaTextMuted
                        )
                    }
                }

                Text(
                    text = "KES ${String.format("%,.0f", ride.fareAmount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SenaPeach
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SenaBorder)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${ride.pickupLocation} → ${ride.destinationLocation}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SenaElectricCyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SenaSuccessGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = ride.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaSuccessGreen
                    )
                }

                if (ride.ratingGiven != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${ride.ratingGiven.toInt()} Stars",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SenaPeach
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = SenaPeach,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
