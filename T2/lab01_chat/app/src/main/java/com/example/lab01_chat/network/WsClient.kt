package com.example.lab01_chat.network

import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class WsClient(
    private val url: String,
    private val onMessage: (String) -> Unit,
    private val onState: (Boolean) -> Unit
) {
    private val client = OkHttpClient()
    private var ws: WebSocket? = null

    fun connect() {
        Log.d("WsClient", "Connecting to $url")
        val req = Request.Builder().url(url).build()
        ws = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WsClient", "WebSocket opened")
                onState(true)
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WsClient", "Received message: $text")
                onMessage(text)
            }
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WsClient", "WebSocket closing: $code $reason")
                onState(false)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("WsClient", "WebSocket failure: ${t.message}")
                onState(false)
            }
        })
    }

    fun sendCmd(cmd: String) {
        val msg = JSONObject().apply {
            put("type", "CMD")
            put("payload", JSONObject().put("cmd", cmd))
        }
        Log.d("WsClient", "Sending command: $cmd")
        ws?.send(msg.toString())
    }

    fun close() {
        ws?.close(1000, "bye")
    }
}
