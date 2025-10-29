package app

fun main() {
    val hilo = Thread {
        println("Nombre: ${Thread.currentThread().name}")
        println("Prioridad: ${Thread.currentThread().priority}")
        println("¿Es daemon?: ${Thread.currentThread().isDaemon}")
        println("Estado: ${Thread.currentThread().state}")
        Thread.sleep(500)
        println("¿Está vivo?: ${Thread.currentThread().isAlive}")
    }
    hilo.name = "PropiedadesHilo"
    hilo.priority = Thread.MAX_PRIORITY
    hilo.isDaemon = false

    hilo.start()
    hilo.join()
    println("El hilo ha terminado")
}