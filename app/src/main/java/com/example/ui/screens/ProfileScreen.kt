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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaOrangeCTA
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaPlatinum
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary

@Composable
fun ProfileScreen(
    userXp: Int,
    userTier: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SenaBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Large Profile Header Avatar
        Box(
            modifier = Modifier
                .size(110.dp)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF22283A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    tint = SenaPeach,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Khamis Al-Mombasa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SenaTextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "+254 712 345 678  •  khamis@sena.co.ke",
            fontSize = 12.sp,
            color = SenaTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Member Tier & XP Badge Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SenaSurface)
                .border(1.dp, SenaBorder, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MEMBERSHIP STATUS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = SenaTextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$userTier Tier Rider",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SenaPlatinum
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SenaElectricCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = SenaElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$userXp XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SenaElectricCyan
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Settings & Options List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileSettingRow(
                label = "Saved Emergency Contacts",
                icon = Icons.Default.Phone,
                testTag = "profile_setting_contacts"
            )
            ProfileSettingRow(
                label = "Sena Eco Bike Preferences",
                icon = Icons.Default.ElectricBike,
                testTag = "profile_setting_eco_pref"
            )
            ProfileSettingRow(
                label = "Security & Passcode",
                icon = Icons.Default.Lock,
                testTag = "profile_setting_security"
            )
            ProfileSettingRow(
                label = "Safety Center & SOS",
                icon = Icons.Default.Security,
                testTag = "profile_setting_safety"
            )
            ProfileSettingRow(
                label = "Help & Support Center",
                icon = Icons.Default.Help,
                testTag = "profile_setting_help"
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun ProfileSettingRow(
    label: String,
    icon: ImageVector,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SenaSurface)
            .border(1.dp, SenaBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF222736)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = SenaPeach,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SenaTextPrimary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = SenaTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
