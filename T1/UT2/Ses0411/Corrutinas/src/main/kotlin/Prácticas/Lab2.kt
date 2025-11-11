package app.Prácticas
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.net.URL

data class DownloadTask(val id: Int, val url: String, val filename: String)
data class DownloadProgress(val taskId: Int, val downloaded: Long, val total: Long?) {
    val percentage: String
        get() = if (total != null && total > 0)
            "${(downloaded * 100 / total)}%"
        else
            "Desconocido"
}

class DownloadManager {
    fun downloadFile(task: DownloadTask): Flow<DownloadProgress> = flow {
        val url = URL(task.url)
        val connection = url.openConnection()
        val total = connection.contentLengthLong.takeIf { it > 0 } // null si es -1
        val input = connection.getInputStream()
        val file = File(task.filename)
        val output = file.outputStream()

        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var downloaded = 0L
        var bytesRead: Int

        println("▶️ Iniciando descarga: ${task.filename}")

        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
            downloaded += bytesRead
            emit(DownloadProgress(task.id, downloaded, total))
        }

        output.flush()
        output.close()
        input.close()

        println("✅ Descarga completada: ${task.filename} (${downloaded / 1024} KB)")
    }.flowOn(Dispatchers.IO)

    fun downloadParallel(tasks: List<DownloadTask>): Flow<DownloadProgress> =
        tasks.map { downloadFile(it) }.merge()
}

fun main() = runBlocking {
    val manager = DownloadManager()

    val tasks = listOf(
        DownloadTask(1, "https://github.com/JetBrains/kotlin/archive/refs/heads/master.zip", "kotlin.zip"),
        DownloadTask(2, "https://github.com/google/guava/archive/refs/heads/master.zip", "guava.zip"),
        DownloadTask(3, "https://github.com/square/okhttp/archive/refs/heads/master.zip", "okhttp.zip")
    )

    println("=== Descarga paralela real ===")

    manager.downloadParallel(tasks).collect { progress ->
        println("Tarea ${progress.taskId}: ${progress.percentage} (${progress.downloaded / 1024} KB)")
    }

    println("\n🎉 ¡Todas las descargas finalizadas!")
}
