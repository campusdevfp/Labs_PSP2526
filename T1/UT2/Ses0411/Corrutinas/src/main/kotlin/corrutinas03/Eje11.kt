package app.corrutinas03

import kotlinx.coroutines.*

fun main() = runBlocking {
    supervisorScope {
        val child1 = launch {
            delay(100)
            println("Hija 1 completada")
        }

        val child2 = launch {
            try {
                delay(50)
                throw RuntimeException("Hija 2 falla")
            } catch (e: Exception) {
                println("Excepción interna: ${e.message}")
            }
        }

        val child3 = launch {
            delay(150)
            println("Hija 3 completada")
        }

        child1.join()
        child2.join()
        child3.join()
    }
}
