package org.example.EjerciciosClase

import kotlin.concurrent.thread

fun main() {
    val hilo = thread {
        try {
            repeat(10) { i ->
                println("Trabajando... $i")
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            println("Hilo interrumpido!")
            return@thread
        }
    }

    Thread.sleep(3000)
    println("Solicitando interrupción...")
    hilo.interrupt()
    hilo.join()
    println("Programa finalizado")
}