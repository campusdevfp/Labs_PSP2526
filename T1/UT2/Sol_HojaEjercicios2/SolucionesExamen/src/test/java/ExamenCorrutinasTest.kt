import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExamenCorrutinasTest {

    // =============
    // TAREA 1
    // =============

    @org.junit.Test
    fun `saludar() debe devolver un Saludo con el mensaje correcto`() = runTest {
        val nombre = "Ana"
        val saludo = saludar(nombre)

        assertEquals("Hola, Ana", saludo.mensaje)
    }


    // =============
    // TAREA 2
    // =============

    @Test
    fun `launch imprime correctamente los pasos`() = runTest {
        val logs = mutableListOf<String>()

        val job = launch {
            logs += "Paso 1"
            delay(200)
            logs += "Paso 2"
            delay(200)
            logs += "Paso 3"
        }

        job.join()

        assertEquals(listOf("Paso 1", "Paso 2", "Paso 3"), logs)
    }


    // =============
    // TAREA 3
    // =============

    @Test
    fun `obtenerNumero() devuelve Numero con valor 7`() = runTest {
        val numero = obtenerNumero()
        assertEquals(7, numero.valor)
    }


    // =============
    // TAREA 4
    // =============

    @Test
    fun `corrutina debe cancelarse correctamente`() = runTest {
        var cancelado = false

        val job = launch {
            try {
                while (isActive) {
                    delay(150)
                }
            } finally {
                cancelado = true
            }
        }

        delay(400)
        job.cancelAndJoin()

        assertTrue(cancelado, "La corrutina no se canceló correctamente")
    }


    // =============
    // TAREA 5 — CHANNEL
    // =============

    @Test
    fun `productor y consumidor deben enviar y recibir correctamente`() = runTest {
        val channel = Channel<Int>()
        val recibidos = mutableListOf<Int>()

        val productor = launch {
            for (n in listOf(1, 2, 3)) {
                channel.send(n)
                delay(100)
            }
            channel.close()
        }

        val consumidor = launch {
            for (valor in channel) {
                recibidos += valor
            }
        }

        joinAll(productor, consumidor)

        assertEquals(listOf(1, 2, 3), recibidos)
    }
}
