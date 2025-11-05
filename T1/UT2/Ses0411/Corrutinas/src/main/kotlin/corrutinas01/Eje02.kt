package app.corrutinas01

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val deferred1 = async {
        delay(1000)
        "Resultado 1"
    }

    val deferred2 = async {
        delay(500)
        "Resultado 2"
    }

    println("Esperando resultados...")
    println(deferred1.await()) // Espera y obtiene el resultado

    println(deferred2.await())
}