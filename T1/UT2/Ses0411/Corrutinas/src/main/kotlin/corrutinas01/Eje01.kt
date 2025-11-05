package app.corrutinas01

import kotlinx.coroutines.*

suspend fun fetchUser(userId: Int): User {
    delay(1000) // Suspende por 1 segundo sin bloquear el hilo
    return User(userId, "Usuario $userId")
}

suspend fun fetchOrders(userId: Int): List<Order> {
    delay(500)
    return listOf(Order(1, "Pedido 1"), Order(2, "Pedido 2"))
}

data class User(val id: Int, val name: String)
data class Order(val id: Int, val description: String)


// Versión secuencial
fun main() = runBlocking {
    println("Iniciando fetchUser()...")
    val user = fetchUser(1)
    println("Usuario obtenido: $user")

    println("Iniciando fetchOrders()...")
    val orders = fetchOrders(user.id)
    println("Pedidos: $orders")

    println("Programa finalizado.")
}

// Versión con launch

//fun main() = runBlocking {
//    println("Inicio del programa...")
//
//    // 🔹 Lanza corrutina para obtener el usuario
//    launch {
//        println("Iniciando fetchUser()...")
//        val user = fetchUser(1)
//        println("Usuario obtenido: $user")
//    }
//
//    // 🔹 Lanza otra corrutina para obtener los pedidos
//    launch {
//        println("Iniciando fetchOrders()...")
//        val orders = fetchOrders(1)
//        println("Pedidos: $orders")
//    }
//
//    println("Programa finalizado.")
//}
