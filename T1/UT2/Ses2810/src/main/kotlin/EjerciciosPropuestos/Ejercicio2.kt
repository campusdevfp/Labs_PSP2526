package org.example.EjerciciosPropuestos

import kotlin.concurrent.thread

fun main(){
    val lista1 = List(3){
        i ->
        thread(start = true) {

            println("Hilo $i iniciado: ${Thread.currentThread().name}")
            Thread.sleep(500)
            println("Hiloa $i finalizado: ${Thread.currentThread().name}")

        }
    }

    lista1.forEach { it.join() }
    println("Todos los hilos han terminado")

    println(lista1)
}