package com.example.lab01_chat.models

data class WsEnvelope(
    val type: String,
    val payload: Map<String, Any?>
)
