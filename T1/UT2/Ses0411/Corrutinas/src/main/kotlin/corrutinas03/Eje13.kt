package app.corrutinas03

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val deferreds = (1..10).map { id ->
        async {
            delay((100..1000).random().toLong())
            "Resultado $id"
        }
    }

    val results = deferreds.awaitAll()
    results.forEach { println(it) }
}