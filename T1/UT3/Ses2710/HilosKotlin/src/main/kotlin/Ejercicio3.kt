package app

class MiHilo : Thread() {
    override fun run() {
        println("Ejecutando hilo personalizado: $name")
        sleep(1000)
        println("Hilo personalizado finalizado")
    }
}

fun main() {
    val hilo = MiHilo()
    hilo.name = "HiloPersonalizado"
    hilo.start()
    hilo.join()
}