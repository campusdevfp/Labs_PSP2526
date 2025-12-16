package com.example.lab01_chat.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lab01_chat.network.WsClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

data class DashboardState(
    val connected: Boolean = false,
    val running: Boolean = false,
    val counter: Int = 0,
    val cpu: Int = 0,
    val ram: Int = 0
)

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    private lateinit var ws: WsClient

    fun start(wsUrl: String) {
        ws = WsClient(
            url = wsUrl,
            onMessage = ::handleMessage,
            onState = { ok -> _state.value = _state.value.copy(connected = ok) }
        )
        ws.connect()
    }

    private fun handleMessage(text: String) {
        val obj = JSONObject(text)
        val type = obj.getString("type")
        val payload = obj.getJSONObject("payload")

        if (type == "SNAPSHOT" || type == "METRICS") {
            _state.value = DashboardState(
                connected = _state.value.connected,
                running = payload.getBoolean("running"),
                counter = payload.getInt("counter"),
                cpu = payload.getInt("cpu"),
                ram = payload.getInt("ram")
            )
        }
    }

    fun cmdStart() = ws.sendCmd("START")
    fun cmdStop() = ws.sendCmd("STOP")
    fun cmdReset() = ws.sendCmd("RESET")
}
