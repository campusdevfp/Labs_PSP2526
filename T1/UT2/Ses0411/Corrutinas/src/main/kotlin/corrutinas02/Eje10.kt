package app.corrutinas02

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("Trabajando $i...")
            delay(500)
        }
    }

    delay(2000)
    println("Cancelando...")
    job.cancel() // Solicita cancelación
    job.join()   // Espera a que termine
    println("Cancelado")
}