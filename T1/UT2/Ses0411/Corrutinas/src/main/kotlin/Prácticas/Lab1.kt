package app.Prácticas

import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

fun main() = runBlocking {
    println("🔹 Iniciando descarga...")

    // 1️⃣ Descarga en segundo plano
    val deferred = async(Dispatchers.IO) {
        val url = URL("https://jsonplaceholder.typicode.com/posts/1")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        val responseCode = conn.responseCode
        println("🌐 HTTP $responseCode")
        val text = conn.inputStream.bufferedReader().use { it.readText() }
        conn.disconnect()
        text
    }

    // 2️⃣ Esperar resultado
    println("⏳ Esperando resultado...")
    val result = deferred.await()

    // 3️⃣ Mostrar
    println("✅ Contenido recibido (${result.length} bytes)")
    println(result.take(200) + " ...")
}

