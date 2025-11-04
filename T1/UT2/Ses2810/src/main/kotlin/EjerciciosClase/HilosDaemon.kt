package org.example.EjerciciosClase

import kotlin.concurrent.thread

fun main() {
    // Hilo daemon
    thread(isDaemon = true, name = "Daemon") {
        repeat(10) {
            println("Hilo daemon ejecutándose... $it")
            Thread.sleep(500)
        }
    }

    // Hilo normal
    thread(isDaemon = false, name = "Normal") {
        repeat(3) {
            println("Hilo normal ejecutándose... $it")
            Thread.sleep(500)
        }
    }

    println("Hilo principal finaliza")
    // El programa termina cuando el hilo normal termina,
    // sin esperar al hilo daemon
}