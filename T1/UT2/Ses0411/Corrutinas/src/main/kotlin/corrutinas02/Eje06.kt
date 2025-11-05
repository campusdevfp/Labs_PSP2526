package app.corrutinas02

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

suspend fun loadData(): String = withContext(Dispatchers.IO) {
    // Ejecuta en hilo de I/O
    println("Cargando en: ${Thread.currentThread().name}")
    delay(1000)
    "Datos cargados"
}

fun main() = runBlocking {
    println("Main: ${Thread.currentThread().name}")
    val data = loadData()
    println("Datos recibidos: $data en ${Thread.currentThread().name}")
}