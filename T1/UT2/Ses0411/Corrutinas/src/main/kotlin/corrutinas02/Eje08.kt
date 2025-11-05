package app.corrutinas02

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyService {
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    fun doWork() {
        scope.launch {
            // Trabajo asíncrono
            println("Trabajando...")
            delay(1000)
            println("Trabajo completado")
        }
    }

    fun cleanup() {

        scope.cancel() // Cancela todas las corrutinas
    }
}

fun main() = runBlocking {
    val service = MyService()
    service.doWork()
    delay(500)
    service.cleanup() // Cancela la corrutina antes de que termine
    delay(1000)
}