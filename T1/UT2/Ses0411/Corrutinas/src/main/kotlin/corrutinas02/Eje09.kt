package app.corrutinas02

import kotlinx.coroutines.*

class MyServiceList {
    // 👇 Creamos un scope independiente con su propio Job
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    fun doWork() {
        // 🔹 Lanzamos varias tareas async en paralelo
        val tasks = listOf(
            scope.async {
                println("🔹 Tarea 1: obteniendo usuario...")
                delay(10000)
                "👤 Usuario obtenido"
            },
            scope.async {

                delay(1500)
                "📦 Pedidos descargados"
                println("🔹 Tarea 2: descargando pedidos...")
            },
            scope.async {

                delay(500)
                "📊 Datos analizados"
                println("🔹 Tarea 3: analizando datos...")
            },
            scope.async {

                delay(5000)
                "🧾 Informe generado"
                println("🔹 Tarea 4: generando informe...")
            }
        )

        // 🔸 Recogemos todos los resultados en una corrutina separada
//        scope.launch {
//            println("⏳ Esperando resultados...")
//            val results = tasks.awaitAll()  // Espera a que todos los async terminen
//            println("✅ Todas las tareas completadas")
//            results.forEach { println(it) }
//        }
    }

    fun cleanup() {
        println("🛑 Cancelando todas las corrutinas del servicio...")
        scope.cancel()
    }
}

fun main() = runBlocking {
    val service = MyServiceList()
    service.doWork()
    service.cleanup()
    delay(2500) // Esperamos a que terminen todas las tareas

}
