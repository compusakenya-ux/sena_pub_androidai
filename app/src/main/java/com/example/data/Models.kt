package com.example.data

data class Ride(
    val rideId: String,
    val riderId: String,
    val driverId: String?,
    val status: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val fare: Int,
    val surgeMultiplier: Double,
    val requestedAt: String,
    val completedAt: String?
)

data class Payment(
    val paymentId: String,
    val rideId: String,
    val amount: Int,
    val driverAmount: Int,
    val platformAmount: Int,
    val method: String,
    val status: String,
    val mpesaReceiptNumber: String?
)

data class RatingSubmission(
    val rideId: String,
    val stars: Int,
    val comment: String?
) {
    init {
        require(stars in 1..4) { "Rating must be between 1 and 4" }
    }
}

data class WalletBalanceResponse(
    val balance: Int
)

data class WithdrawalRequest(
    val amount: Int
)

data class MpesaPaymentRequest(
    val rideId: String,
    val amount: Int
)
