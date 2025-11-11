package app.corrutinas03

import app.corrutinas01.User
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun fetchUser(): User {
    delay(1000)
    return User(1, "Juan")
}

suspend fun fetchPosts(): List<Post> {
    delay(1500)
    return listOf(Post(1, "Post 1"), Post(2, "Post 2"))
}

data class Post(val id: Int, val title: String)

fun main(): kotlin.Unit = runBlocking {
    val startTime = System.currentTimeMillis()

    // Secuencial (lento)
    val user = fetchUser()
    val posts = fetchPosts()
    println("Secuencial: ${System.currentTimeMillis() - startTime}ms") // ~2500ms

    // Paralelo (rápido)
    val startTime2 = System.currentTimeMillis()
    val userDeferred = async { fetchUser() }
    val postsDeferred = async { fetchPosts() }
    val user2 = userDeferred.await()
    val posts2 = postsDeferred.await()
    println("Paralelo: ${System.currentTimeMillis() - startTime2}ms") // ~1500ms
}