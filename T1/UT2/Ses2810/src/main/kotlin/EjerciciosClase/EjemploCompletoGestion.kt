package org.example.EjerciciosClase

import kotlin.concurrent.thread

fun main() {
    // Obtener información sobre el hilo actual
    val mainThread = Thread.currentThread()
    println("Hilo principal: ${mainThread.name}")
    println("Prioridad: ${mainThread.priority}")
    println("Estado: ${mainThread.state}")

    // Crear múltiples hilos con diferentes configuraciones
    val hilos = List(3) { i ->
        thread(
            start = false,
            name = "Trabajador-$i",
            priority = Thread.NORM_PRIORITY,
            isDaemon = false
        ) {
            println("[${Thread.currentThread().name}] Iniciando tarea")
            repeat(3) { j ->
                println("[${Thread.currentThread().name}] Paso $j")
                Thread.sleep(500)
            }
            println("[${Thread.currentThread().name}] Tarea completada")
        }
    }

    // Versión 1: Concurrentes
    // Iniciar todos los hilos
    println("\nIniciando hilos...")
    hilos.forEach { it.start() }

    // Esperar a que todos terminen
    println("Esperando a que los hilos terminen...")
    hilos.forEach { it.join() }

    println("\nTodos los hilos han terminado")

    // Versión 2: Iniciar y esperar cada hilo secuencialmente (sin tocar la creación)
//    println("\nIniciando hilos y esperando secuencialmente...")
//    for (t in hilos) {
//        t.start()
//        println("Esperando a ${t.name}...")
//        t.join()
//        println("${t.name} ha terminado")
//    }
//
//    println("\nTodos los hilos han terminado")
}