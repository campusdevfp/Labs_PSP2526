package com.example.lab01_chat.network

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
        val req = Request.Builder().url(url).build()
        ws = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onState(true)
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessage(text)
            }
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                onState(false)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onState(false)
            }
        })
    }

    fun sendCmd(cmd: String) {
        val msg = JSONObject().apply {
            put("type", "CMD")
            put("payload", JSONObject().put("cmd", cmd))
        }
        ws?.send(msg.toString())
    }

    fun close() {
        ws?.close(1000, "bye")
    }
}
