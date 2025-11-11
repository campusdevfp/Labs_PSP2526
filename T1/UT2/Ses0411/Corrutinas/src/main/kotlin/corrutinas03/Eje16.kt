package app.corrutinas03

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun simpleFlow(): Flow<Int> = flow {
    println("Flow iniciado")
    for (i in 1..3) {
        delay(100)
        emit(i) // Emite un valor
    }
}

fun main() = runBlocking {
    println("Llamando a flow...")
    val flow = simpleFlow()

    println("Colectando...")
    flow.collect { value ->
        println("Recibido $value")
    }

    println("Finalizado")
}