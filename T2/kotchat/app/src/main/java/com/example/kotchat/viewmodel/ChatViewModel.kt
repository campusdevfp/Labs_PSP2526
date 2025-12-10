package com.example.kotchat.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    val messages = mutableStateListOf<String>()
    private var session: DefaultClientWebSocketSession? = null

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            client.webSocket("ws://192.168.1.34:8000/ws/chat") {
                session = this
                messages.add("Conectado al servidor")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val text = frame.readText()
                    messages.add(text)
                }
            }
        }
    }

    fun sendMessage(msg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            session?.send(msg)
        }
    }
}
