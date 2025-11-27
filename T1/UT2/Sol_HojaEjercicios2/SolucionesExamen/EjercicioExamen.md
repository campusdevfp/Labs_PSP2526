

# 📄 Examen — Corrutinas en Kotlin (10 puntos)

**Duración:** 30 minutos
**Instrucciones:** Resuelve cada ejercicio en Kotlin utilizando corrutinas.
Todas las tareas deben ejecutarse desde `main()` usando `runBlocking`.

---

## ⭐ Tarea 1 — Función `suspend` + `data class` (1.5 puntos)

Crea una `data class`:

```kotlin
data class Saludo(val mensaje: String)
```

Implementa:

```kotlin
suspend fun saludar(nombre: String): Saludo
```

Requisitos:

* Debe esperar **300 ms** (`delay(300)`).
* Debe devolver `Saludo("Hola, <nombre>")`.

En `main()` imprime:

```
Resultado: Hola, X
```

---

## ⭐ Tarea 2 — Corrutina `launch` muy simple (1.5 puntos)

En `main()`:

1. Lanza una corrutina con `launch`.
2. Dentro imprime, con **200 ms** entre cada línea:

```
Paso 1
Paso 2
Paso 3
```

Usa `job.join()` para esperar a que la corrutina termine.

---

## ⭐ Tarea 3 — Función `async` + `data class` (2 puntos)

Crea:

```kotlin
data class Numero(val valor: Int)
```

Implementa:

```kotlin
suspend fun obtenerNumero(): Numero
```

Requisitos:

* Debe esperar **500 ms**.
* Debe devolver `Numero(7)`.

En `main()`:

* Llamar a esta función usando `async`.
* Recoger el resultado con `await()`.
* Mostrar:

```
Número obtenido: 7
```

---

## ⭐ Tarea 4 — Cancelación de corrutina (2.5 puntos)

En `main()`:

1. Lanza una corrutina que imprima `"Trabajando..."` cada **150 ms**.
2. Tras **400 ms**, cancélala.
3. En el bloque `finally`, imprime:

```
Tarea cancelada
```

Salida esperada:

```
Trabajando...
Trabajando...
Cancelando...
Tarea cancelada
```

---

## ⭐ Tarea 5 — Comunicación básica con `Channel` (2.5 puntos)

En `main()`:

1. Crea un canal:

```kotlin
val channel = Channel<Int>()
```

2. Productor (corrutina con `launch`):

    * Envía los números **1, 2 y 3**.
    * Entre cada envío: `delay(100)`.
    * Muestra:

      ```
      Enviando n
      ```
    * Cierra el canal al terminar.

3. Consumidor (corrutina con `launch`):

    * Recibe valores del canal.
    * Muestra:

      ```
      Recibido n
      ```

Salida esperada:

```
Enviando 1
Recibido 1
Enviando 2
Recibido 2
Enviando 3
Recibido 3
```

---

# 🎓 Puntuación total: **10 puntos**

* Tarea 1 → 1.5
* Tarea 2 → 1.5
* Tarea 3 → 2
* Tarea 4 → 2.5
* Tarea 5 → 2.5

---

