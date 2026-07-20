package com.example.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class RideSocketService {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    
    private val _stream = MutableSharedFlow<Map<String, Any>>(extraBufferCapacity = 64)
    val stream: SharedFlow<Map<String, Any>> = _stream.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var simulationJob: Job? = null

    fun connect(rideId: String, wsBaseUrl: String = "wss://api.sena.co.ke", token: String = "demo_token") {
        val url = "$wsBaseUrl/ws/location/$rideId?token=$token"
        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val map = mutableMapOf<String, Any>()
                    val keys = json.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        map[key] = json.get(key)
                    }
                    scope.launch {
                        _stream.emit(map)
                    }
                } catch (e: Exception) {
                    Log.e("RideSocketService", "Failed to parse websocket message", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("RideSocketService", "WebSocket connection failure, starting local simulation fallback", t)
                // If real WebSocket fails, we simulate payments & updates locally
                startLocalSimulation(rideId)
            }
        })

        // Also start local simulation by default for robust demo testing
        startLocalSimulation(rideId)
    }

    private fun startLocalSimulation(rideId: String) {
        simulationJob?.cancel()
        simulationJob = scope.launch {
            // Emulate driver status updates periodically, or trigger payment completion after a request
            delay(1000)
            _stream.emit(mapOf("type" to "ride_status", "status" to "accepted", "rideId" to rideId))
        }
    }

    fun triggerMockPaymentCompletion() {
        scope.launch {
            delay(4000) // 4 seconds delay to simulate user entering M-Pesa pin
            _stream.emit(mapOf(
                "type" to "payment_status",
                "status" to "completed",
                "message" to "M-Pesa transaction completed successfully"
            ))
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        simulationJob?.cancel()
        simulationJob = null
    }
}
