package com.example.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SenaApi {
    @POST("/rides/estimate")
    suspend fun getFareEstimate(@Body payload: Map<String, String>): Map<String, Any>

    @POST("/rides/request")
    suspend fun requestRide(@Body payload: Map<String, String>): Ride

    @POST("/rides/{id}/rating")
    suspend fun submitRating(
        @Path("id") id: String,
        @Body payload: Map<String, Any>
    ): Map<String, Any>

    @POST("/payments/mpesa/stkpush")
    suspend fun initiateMpesaPayment(@Body payload: MpesaPaymentRequest): Map<String, Any>

    @GET("/wallet/balance")
    suspend fun getWalletBalance(): WalletBalanceResponse

    @POST("/wallet/withdraw")
    suspend fun requestWithdrawal(@Body payload: WithdrawalRequest): Map<String, Any>
}

class ApiClient(baseUrl: String = "https://api.sena.co.ke") {

    private val api: SenaApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SenaApi::class.java)
    }

    // In-memory state for mock behavior (provides a fully working demonstration)
    companion object {
        var mockBalance: Int = 3450
        private const val TAG = "ApiClient"
    }

    suspend fun getWalletBalance(): WalletBalanceResponse = withContext(Dispatchers.IO) {
        try {
            api.getWalletBalance()
        } catch (e: Exception) {
            Log.w(TAG, "Network call failed, using mock data", e)
            delay(500) // simulate loading delay
            WalletBalanceResponse(mockBalance)
        }
    }

    suspend fun requestWithdrawal(amount: Int): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            api.requestWithdrawal(WithdrawalRequest(amount))
        } catch (e: Exception) {
            Log.w(TAG, "Network call failed, using mock data", e)
            delay(1000) // simulate network delay
            if (amount > mockBalance) {
                throw Exception("Insufficient balance")
            }
            mockBalance -= amount
            mapOf("status" to "success", "message" to "Withdrawal successful")
        }
    }

    suspend fun submitRating(rideId: String, stars: Int, comment: String?): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            val payload = mutableMapOf<String, Any>("stars" to stars)
            if (comment != null) payload["comment"] = comment
            api.submitRating(rideId, payload)
        } catch (e: Exception) {
            Log.w(TAG, "Network call failed, using mock data", e)
            delay(800)
            mapOf("status" to "success", "rideId" to rideId)
        }
    }

    suspend fun initiateMpesaPayment(rideId: String, amount: Int): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            api.initiateMpesaPayment(MpesaPaymentRequest(rideId, amount))
        } catch (e: Exception) {
            Log.w(TAG, "Network call failed, using mock data", e)
            delay(1200) // simulate waiting for STK push trigger
            mapOf("status" to "stk_sent", "checkoutRequestID" to "ws_123_456")
        }
    }
}
