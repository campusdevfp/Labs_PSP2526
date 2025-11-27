import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

// ==============================
// TAREA 1 — 1.5 puntos
// ==============================

data class Saludo(val mensaje: String)

suspend fun saludar(nombre: String): Saludo {
    delay(300)
    return Saludo("Hola, $nombre")
}

// ==============================
// TAREA 3 — 2 puntos
// ==============================

data class Numero(val valor: Int)

suspend fun obtenerNumero(): Numero {
    delay(500)
    return Numero(7)
}

// ==============================
// MAIN — Resuelve todas las tareas
// ==============================

fun main() = runBlocking {

    // ------------------------------
    // Tarea 1
    // ------------------------------
    println("=== TAREA 1 ===")
    val saludo = saludar("Ana")
    println("Resultado: ${saludo.mensaje}")
    println()

    // ------------------------------
    // Tarea 2
    // ------------------------------
    println("=== TAREA 2 ===")
    val job1 = launch {
        println("Paso 1")
        delay(200)
        println("Paso 2")
        delay(200)
        println("Paso 3")
    }
    job1.join()
    println()

    // ------------------------------
    // Tarea 3
    // ------------------------------
    println("=== TAREA 3 ===")
    val numeroDeferred = async { obtenerNumero() }
    val numero = numeroDeferred.await()
    println("Número obtenido: ${numero.valor}")
    println()

    // ------------------------------
    // Tarea 4 — Cancelación
    // ------------------------------
    println("=== TAREA 4 ===")
    val job2 = launch {
        try {
            while (isActive) {
                println("Trabajando...")
                delay(150)
            }
        } finally {
            println("Tarea cancelada")
        }
    }

    delay(400)
    println("Cancelando...")
    job2.cancelAndJoin()
    println()

    // ------------------------------
    // Tarea 5 — Channel
    // ------------------------------
    println("=== TAREA 5 ===")
    val channel = Channel<Int>()

    // Productor
    val productor = launch {
        for (n in listOf(1, 2, 3)) {
            println("Enviando $n")
            channel.send(n)
            delay(100)
        }
        channel.close()
    }

    // Consumidor
    val consumidor = launch {
        for (valor in channel) {
            println("Recibido $valor")
        }
    }

    joinAll(productor, consumidor)

    println("\nFIN DEL EXAMEN")
}
