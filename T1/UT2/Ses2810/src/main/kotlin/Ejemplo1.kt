package app

fun main() {
    val thread = Thread {
        println("Hilo ejecutándose: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        println("Hilo finalizado")
    }

    thread.start() // Inicia la ejecución del hilo
    println("Hilo principal continúa")
    thread.join() // Espera a que el hilo termine
    println("Programa finalizado")
}