package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.DriverWithdrawDialog
import com.example.ui.components.SenaBottomNav
import com.example.ui.components.SenaNavTab
import com.example.ui.components.SenaTopAppBar
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LaunchScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RatingScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.SenaBackground
import com.example.ui.theme.SenaBorder
import com.example.ui.theme.SenaElectricCyan
import com.example.ui.theme.SenaPeach
import com.example.ui.theme.SenaSurface
import com.example.ui.theme.SenaTextMuted
import com.example.ui.theme.SenaTextPrimary
import com.example.ui.theme.SenaTextSecondary
import com.example.ui.theme.SenaTheme
import com.example.ui.viewmodel.ScreenState
import com.example.ui.viewmodel.SenaViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SenaTheme {
                SenaMainApp()
            }
        }
    }
}

@Composable
fun SenaMainApp(
    viewModel: SenaViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    val pickupLocation by viewModel.pickupLocation.collectAsState()
    val destinationLocation by viewModel.destinationLocation.collectAsState()
    val selectedRideCategory by viewModel.selectedRideCategory.collectAsState()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
    val surgeMultiplier by viewModel.surgeMultiplier.collectAsState()

    val driverWalletBalance by viewModel.driverWalletBalance.collectAsState()
    val driverTotalRides by viewModel.driverTotalRides.collectAsState()
    val driverGrossEarnings by viewModel.driverGrossEarnings.collectAsState()
    val userXp by viewModel.userXp.collectAsState()
    val userTier = viewModel.userTier

    val minutesLeft by viewModel.trackingMinutesLeft.collectAsState()

    val ratingGiven by viewModel.ratingGiven.collectAsState()
    val selectedChips by viewModel.selectedFeedbackChips.collectAsState()
    val reviewText by viewModel.reviewText.collectAsState()

    val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsState()
    val withdrawAmount by viewModel.withdrawAmount.collectAsState()
    val withdrawPhoneNumber by viewModel.withdrawPhoneNumber.collectAsState()
    val isWithdrawProcessing by viewModel.isWithdrawProcessing.collectAsState()
    val withdrawSuccessMessage by viewModel.withdrawSuccessMessage.collectAsState()
    val withdrawErrorMessage by viewModel.withdrawErrorMessage.collectAsState()

    val rideHistory by viewModel.rideHistory.collectAsState()
    val walletTransactions by viewModel.walletTransactions.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (currentScreen == ScreenState.LAUNCH) {
        LaunchScreen(
            onLaunchClick = { viewModel.launchJourney() }
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = SenaSurface,
                    drawerContentColor = SenaTextPrimary,
                    modifier = Modifier.width(300.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SenaSurface)
                            .padding(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))

                        // Drawer Header
                        Text(
                            text = "SENA TRANSIT",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = SenaPeach
                        )
                        Text(
                            text = "Powering Mombasa Mobility",
                            fontSize = 12.sp,
                            color = SenaTextSecondary
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = SenaBorder)
                        Spacer(modifier = Modifier.height(24.dp))

                        // Drawer Items
                        DrawerMenuItem(
                            label = "Home & Map Screen",
                            icon = Icons.Default.Explore,
                            onClick = {
                                viewModel.selectNavTab(SenaNavTab.HOME)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerMenuItem(
                            label = "Ride History",
                            icon = Icons.Default.History,
                            onClick = {
                                viewModel.selectNavTab(SenaNavTab.HISTORY)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerMenuItem(
                            label = "Driver Wallet & Payouts",
                            icon = Icons.Default.AccountBalanceWallet,
                            onClick = {
                                viewModel.selectNavTab(SenaNavTab.WALLET)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerMenuItem(
                            label = "Account Profile",
                            icon = Icons.Default.Person,
                            onClick = {
                                viewModel.selectNavTab(SenaNavTab.PROFILE)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerMenuItem(
                            label = "Mombasa Eco-Fleet Info",
                            icon = Icons.Default.ElectricBike,
                            onClick = {
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerMenuItem(
                            label = "Safety & Emergency SOS",
                            icon = Icons.Default.Shield,
                            onClick = {
                                scope.launch { drawerState.close() }
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF131722))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "⚡ Mombasa E-Mobility Fleet",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SenaElectricCyan
                                )
                                Text(
                                    text = "Standard Boda & 3-Seater Tuk-Tuk",
                                    fontSize = 11.sp,
                                    color = SenaTextMuted,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    SenaTopAppBar(
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        onProfileClick = {
                            viewModel.selectNavTab(SenaNavTab.PROFILE)
                        }
                    )
                },
                bottomBar = {
                    SenaBottomNav(
                        selectedTab = selectedTab,
                        onTabSelected = { tab ->
                            viewModel.selectNavTab(tab)
                        }
                    )
                },
                containerColor = SenaBackground
            ) { innerPadding ->
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        ScreenState.LAUNCH -> LaunchScreen(
                            onLaunchClick = { viewModel.launchJourney() }
                        )

                        ScreenState.HOME -> HomeScreen(
                            destination = destinationLocation,
                            selectedCategory = selectedRideCategory,
                            rideCategories = viewModel.rideCategories,
                            surgeMultiplier = surgeMultiplier,
                            onDestinationChange = { loc -> viewModel.selectDestination(loc) },
                            onCategorySelect = { cat -> viewModel.selectRideCategory(cat) },
                            onProceedToCheckout = { viewModel.proceedToCheckout() }
                        )

                        ScreenState.CHECKOUT -> {
                            val activeCat = viewModel.rideCategories.find { it.id == selectedRideCategory }
                                ?: viewModel.rideCategories.first()
                            CheckoutScreen(
                                fareKes = activeCat.fareKes * surgeMultiplier,
                                pickupLocation = pickupLocation,
                                destinationLocation = destinationLocation,
                                selectedPaymentMethod = selectedPaymentMethod,
                                walletBalance = driverWalletBalance,
                                onPaymentMethodSelect = { method ->
                                    viewModel.selectPaymentMethod(method)
                                },
                                onConfirmAndPay = { viewModel.confirmAndPay() }
                            )
                        }

                        ScreenState.TRACKING -> TrackingScreen(
                            minutesLeft = minutesLeft,
                            destination = destinationLocation,
                            onSafetyCenterClick = { },
                            onShareTripClick = { }
                        )

                        ScreenState.RATING -> RatingScreen(
                            rating = ratingGiven,
                            selectedChips = selectedChips,
                            reviewText = reviewText,
                            onRatingChange = { r -> viewModel.setRating(r) },
                            onChipToggle = { chip -> viewModel.toggleFeedbackChip(chip) },
                            onReviewTextChange = { txt -> viewModel.updateReviewText(txt) },
                            onSubmitFeedback = { viewModel.submitFeedback() }
                        )

                        ScreenState.WALLET -> WalletScreen(
                            balance = driverWalletBalance,
                            driverTotalRides = driverTotalRides,
                            driverGrossEarnings = driverGrossEarnings,
                            userTier = userTier,
                            transactions = walletTransactions,
                            onWithdrawClick = { viewModel.openDriverWithdrawDialog() }
                        )

                        ScreenState.HISTORY -> HistoryScreen(
                            rideList = rideHistory
                        )

                        ScreenState.PROFILE -> ProfileScreen(
                            userXp = userXp,
                            userTier = userTier
                        )
                    }
                }
            }

            if (showWithdrawDialog) {
                DriverWithdrawDialog(
                    availableBalance = driverWalletBalance,
                    amount = withdrawAmount,
                    phoneNumber = withdrawPhoneNumber,
                    isProcessing = isWithdrawProcessing,
                    successMessage = withdrawSuccessMessage,
                    errorMessage = withdrawErrorMessage,
                    onAmountChange = { viewModel.setWithdrawAmount(it) },
                    onPhoneNumberChange = { viewModel.setWithdrawPhoneNumber(it) },
                    onTriggerWithdrawal = { viewModel.triggerDriverB2cWithdrawal() },
                    onDismiss = { viewModel.dismissDriverWithdrawDialog() }
                )
            }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
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

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SenaTextPrimary
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = SenaTextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
