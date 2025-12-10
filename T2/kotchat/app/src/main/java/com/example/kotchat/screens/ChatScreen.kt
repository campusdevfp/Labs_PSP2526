package com.example.kotchat.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotchat.viewmodel.ChatViewModel

@Composable
fun ChatScreen(vm: ChatViewModel) {

    LaunchedEffect(Unit) {
        vm.connect()
    }

    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(vm.messages) { msg ->
                Text(text = msg)
            }
        }

        Row {
            TextField(
                value = input,
                onValueChange = { newValue ->
                    input = newValue
                },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    vm.sendMessage(input)
                    input = ""
                }
            ) {
                Text(text = "Enviar")
            }
        }
    }
}
