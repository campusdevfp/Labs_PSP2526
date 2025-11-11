package app.corrutinas02

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("🌱 Inicio del scope padre")

    val jobPadre = launch {
        println("➡️ Corrutina padre iniciada")

        launch {
            println("👶 Hija 1 iniciada")
            try {
                delay(2000)
                println("👶 Hija 1 completada")
            } catch (e: CancellationException) {
                println("💀 Hija 1 cancelada")
            }
        }

        launch {
            println("👶 Hija 2 iniciada")
            try {
                delay(3000)
                println("👶 Hija 2 completada")
            } catch (e: CancellationException) {
                println("💀 Hija 2 cancelada")
            }
        }
    }

    delay(1000)
    println("💥 Cancelando al padre")
    jobPadre.cancelAndJoin()   // 🔥 Aquí se cancela todo el árbol de corrutinas
    println("🏁 Fin del scope padre")
}
