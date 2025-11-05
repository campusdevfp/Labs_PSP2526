package app.corrutinas02

import kotlinx.coroutines.*

suspend fun fetchDataFromServer(id: Int): String {
    println("[${Thread.currentThread().name}] Fetching data $id...")
    delay(1000) // Simula operación de red o consulta a API
    return "Data $id"
}

fun main() = runBlocking {
    println("Programa iniciado")

    // Lanzar corrutinas concurrentes con async
    val deferred1 = async {
        fetchDataFromServer(1)
    }
    val deferred2 = async {
        fetchDataFromServer(2)
    }

    // Esperar resultados (await bloquea solo dentro de la corrutina actual)
    val result1 = deferred1.await()
    val result2 = deferred2.await()

    println("Recibido: $result1")
    println("Recibido: $result2")

    println("Programa finalizado")
}
