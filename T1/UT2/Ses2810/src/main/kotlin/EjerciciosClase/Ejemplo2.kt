package app

import kotlin.concurrent.thread

fun main() {
    val myThread = thread(start = true, name = "MiHilo") {
        println("Ejecutando en: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        println("Tarea completada")
    }

    myThread.join()
}