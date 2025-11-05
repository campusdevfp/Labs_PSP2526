package app.corrutinas01

import app.corrutinas02.fetchDataFromServer
import kotlinx.coroutines.*

suspend fun fetchDataFromServer(id: Int): String {
    println("[${Thread.currentThread().name}] Fetching data $id...")
    delay(1000) // Simula operación de red
    return "Data $id"
}

fun main() = runBlocking {
    println("Programa iniciado")

    // Lanzar múltiples corrutinas
    val job1 = launch {
        val data = fetchDataFromServer(1)
        println("Recibido: $data")
    }

    val job2 = launch {
        val data = fetchDataFromServer(2)
        println("Recibido: $data")
    }

    // Esperar a que terminen
    job1.join()
    job2.join()

    println("Programa finalizado")
}