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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SenaMapView
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary
import com.example.ui.viewmodel.RideCategoryOption

@Composable
fun HomeScreen(
    destination: String,
    selectedCategory: String,
    rideCategories: List<RideCategoryOption>,
    surgeMultiplier: Float = 1.2f,
    onDestinationChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onProceedToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
    ) {
        // Fullscreen Custom Cyberpunk Map View of Mombasa
        SenaMapView(
            isTrackingMode = false,
            selectedDestination = destination,
            onLocationClick = { loc ->
                onDestinationChange(loc)
            }
        )

        // Overlay UI Controls
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Destination Search & Surge Index Banner
            Column {
                // Top Search Input Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(SenaSurface.copy(alpha = 0.94f))
                        .border(1.dp, SenaBorder, RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Destination",
                            tint = SenaTextMuted,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = destination,
                            onValueChange = onDestinationChange,
                            placeholder = {
                                Text(
                                    text = "Where to in Mombasa?",
                                    color = SenaTextMuted,
                                    fontSize = 15.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = SenaTextPrimary,
                                unfocusedTextColor = SenaTextPrimary
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("where_to_input")
                        )

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF232838))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Recent Places",
                                tint = SenaElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // SDR Dynamic Price Index Pill (Section 5)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF131926).copy(alpha = 0.92f))
                        .border(1.dp, SenaElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Dynamic Pricing Surge",
                            tint = SenaElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "⚡ Dynamic Pricing: ${surgeMultiplier}x Surge (Demand / Supply Index)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SenaElectricCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Quick Saved Location Pills (Home, Work, Nyali Beach)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SavedPlaceChip(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = destination.contains("Home", ignoreCase = true),
                    onClick = { onDestinationChange("Home - Nyali Villas") },
                    testTag = "place_home_chip"
                )
                SavedPlaceChip(
                    label = "Work",
                    icon = Icons.Default.Work,
                    isSelected = destination.contains("Work", ignoreCase = true),
                    onClick = { onDestinationChange("Work - City Trade Center") },
                    testTag = "place_work_chip"
                )
                SavedPlaceChip(
                    label = "Nyali Beach",
                    icon = Icons.Default.BeachAccess,
                    isSelected = destination.contains("Nyali Beach", ignoreCase = true),
                    onClick = { onDestinationChange("Nyali Beach Resort") },
                    testTag = "place_nyali_chip"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section Label: SELECT VEHICLE TYPE
            Text(
                text = "SELECT VEHICLE TYPE (2 OPTIONS)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = SenaTextMuted,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // SDR Rule: Exactly 2 Bike Options Cards (Standard Bike & 3 Seater Tuk-Tuk)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rideCategories.forEach { cat ->
                    val isSelected = cat.id == selectedCategory
                    val totalFare = (cat.fareKes * surgeMultiplier).toInt()
                    RideCategoryCard(
                        category = cat,
                        calculatedFare = totalFare,
                        isSelected = isSelected,
                        onClick = { onCategorySelect(cat.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Request Ride CTA Button
            Button(
                onClick = onProceedToCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("request_ride_button"),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SenaOrangeCTA
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "REQUEST RIDE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Proceed to Payment",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SavedPlaceChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) SenaPeach.copy(alpha = 0.2f) else SenaSurface.copy(alpha = 0.94f))
            .border(
                1.dp,
                if (isSelected) SenaPeach else SenaBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) SenaPeach else SenaTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) SenaPeach else SenaTextPrimary
            )
        }
    }
}

@Composable
private fun RideCategoryCard(
    category: RideCategoryOption,
    calculatedFare: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) SenaPeach.copy(alpha = 0.15f) else SenaSurface.copy(alpha = 0.95f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) SenaPeach else SenaBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .testTag("ride_cat_card_${category.id.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SenaPeach.copy(alpha = 0.25f) else Color(0xFF222736)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (category.id == "TukTuk") Icons.Default.Moped else Icons.Default.TwoWheeler,
                    contentDescription = category.name,
                    tint = if (isSelected) SenaPeach else SenaTextSecondary,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) SenaPeach else SenaTextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = category.tagline,
                fontSize = 10.sp,
                color = SenaTextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "KES $calculatedFare",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SenaPeach
            )
        }
    }
}

