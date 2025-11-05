package app.corrutinas02

import kotlinx.coroutines.*

fun main() = runBlocking {
    // Dispatchers.Default: Pool de hilos para CPU intensivo
    launch(Dispatchers.Default) {
        println("Default: ${Thread.currentThread().name}")
        // Cálculos intensivos
    }

    // Dispatchers.IO: Pool de hilos para operaciones I/O
    launch(Dispatchers.IO) {
        println("IO: ${Thread.currentThread().name}")
        // Operaciones de red, archivos, BD
    }

    // Dispatchers.Main: Hilo principal (Android/UI)
    // launch(Dispatchers.Main) { ... }

    // Dispatchers.Unconfined: No confinado a ningún hilo
    launch(Dispatchers.Unconfined) {
        println("Unconfined: ${Thread.currentThread().name}")
    }

    delay(100)
}