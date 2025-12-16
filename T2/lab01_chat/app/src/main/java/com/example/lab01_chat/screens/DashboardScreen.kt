package com.example_lab01_chat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example_lab01_chat.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    wsUrl: String,
    vm: DashboardViewModel = viewModel()
) {
    val s by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.start(wsUrl) }

    Column(Modifier.padding(16.dp)) {
        Text("Connected: ${s.connected}")
        Spacer(Modifier.height(8.dp))
        Text("Running: ${s.running}")
        Text("Counter: ${s.counter}")
        Text("CPU: ${s.cpu}%")
        Text("RAM: ${s.ram}%")

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = vm::cmdStart, enabled = s.connected) { Text("START") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = vm::cmdStop, enabled = s.connected) { Text("STOP") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = vm::cmdReset, enabled = s.connected) { Text("RESET") }
        }
    }
}
