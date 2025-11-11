package app.corrutinas03

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<Int>()

    // Productor
    launch {
        repeat(5) { i ->
            println("Enviando $i")
            channel.send(i)
            delay(100)
        }
        channel.close() // Importante cerrar el channel
    }

    // Consumidor
    for (value in channel) {
        println("Recibido $value")
    }

    println("Finalizado")
}