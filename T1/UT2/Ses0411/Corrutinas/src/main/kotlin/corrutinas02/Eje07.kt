package app.corrutinas02

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Inicio del scope padre")

    launch {
        println("Corrutina hija 1 iniciada")
        delay(1000)
        println("Corrutina hija 1 completada")
    }

    launch {
        println("Corrutina hija 2 iniciada")
        delay(500)
        println("Corrutina hija 2 completada")
    }

    println("Esperando a las hijas...")
    // runBlocking espera automáticamente a todas las corrutinas hijas
}