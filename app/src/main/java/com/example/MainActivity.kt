package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.ApiClient
import com.example.data.LocationService
import com.example.data.RideSocketService
import com.example.ui.screens.MapScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.RatingScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.SenaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge()

        val apiClient = ApiClient()
        val locationService = LocationService(applicationContext)
        val socketService = RideSocketService()

        setContent {
            SenaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onNavigateToMap = {
                                    navController.navigate("map") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("map") {
                            MapScreen(
                                locationService = locationService,
                                onNavigateToWallet = {
                                    navController.navigate("wallet")
                                },
                                onNavigateToRating = { rideId ->
                                    navController.navigate("rating/$rideId")
                                }
                            )
                        }

                        composable(
                            route = "rating/{rideId}",
                            arguments = listOf(navArgument("rideId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val rideId = backStackEntry.arguments?.getString("rideId") ?: "unknown"
                            RatingScreen(
                                rideId = rideId,
                                apiClient = apiClient,
                                onNavigateToPayment = { id ->
                                    navController.navigate("payment/$id") {
                                        popUpTo("rating/$id") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "payment/{rideId}",
                            arguments = listOf(navArgument("rideId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val rideId = backStackEntry.arguments?.getString("rideId") ?: "unknown"
                            PaymentScreen(
                                rideId = rideId,
                                apiClient = apiClient,
                                socketService = socketService,
                                onNavigateToMap = {
                                    navController.navigate("map") {
                                        popUpTo("payment/$rideId") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("wallet") {
                            WalletScreen(
                                apiClient = apiClient,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
