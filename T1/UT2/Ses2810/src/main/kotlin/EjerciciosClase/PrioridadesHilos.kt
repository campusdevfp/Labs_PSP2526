package org.example.EjerciciosClase

import kotlin.concurrent.thread

fun main() {
    val hiloPrioridadAlta = thread(
        start = false,
        name = "Alta",
        priority = Thread.MAX_PRIORITY
    ) {
        repeat(5) {
            println("[ALTA] Iteración $it")
        }
    }

    val hiloPrioridadBaja = thread(
        start = false,
        name = "Baja",
        priority = Thread.MIN_PRIORITY
    ) {
        repeat(5) {
            println("[BAJA] Iteración $it")
        }
    }

    hiloPrioridadBaja.start()
    hiloPrioridadAlta.start()

    hiloPrioridadAlta.join()
    hiloPrioridadBaja.join()
    
    /**/
}