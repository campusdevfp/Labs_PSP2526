

# Práctica Reto

## Cliente WebSocket con Ktor Client (modo guiado)

### Objetivo de la práctica

Crear una **app Android en Kotlin con Jetpack Compose** que se conecte a un **WebSocket remoto**, reciba **estadísticas falsas en tiempo real** y permita:

* Conectarse a una IP indicada por el usuario
* Mostrar las estadísticas que llegan
* Pausar la actualización
* Cancelar la conexión

⚠️ **No se toca el backend en Python**.

---

## 1. Qué hace el backend (solo para entenderlo)

El backend:

* Expone un WebSocket en:

```
ws://<IP>:8000/ws/stats
```

* Envía cada pocos segundos un JSON como este:

```json
{
  "cpu": 55,
  "memory": 61,
  "users": 18,
  "timestamp": "2025-03-10T10:30:21"
}
```

👉 **No hay que enviar mensajes**, solo recibirlos.

---

## 2. Pantalla obligatoria

La app tendrá **UNA pantalla** con:

### Parte superior

* Un **TextField**

    * Para escribir la URL del WebSocket
    * Ejemplo:

      ```
      ws://192.168.1.100:8000/ws/stats
      ```

### Parte central

* Un bloque donde se muestren:

    * CPU
    * Memoria
    * Usuarios
* Al inicio pueden mostrar:

  ```
  --
  ```

### Parte inferior

Tres botones:

| Botón    | Qué hace               |
| -------- | ---------------------- |
| Conectar | Abre el WebSocket      |
| Parar    | Pausa la actualización |
| Cancelar | Cierra la conexión     |

---

## 3. Estados que debe tener la app (PISTA MUY IMPORTANTE)

La aplicación **NO puede funcionar solo con true / false**.

Debe existir algo parecido a:

* **DESCONNECTED**
* **CONNECTED**
* **PAUSED**

👉 Pista clara:

> Un `enum class` es buena idea.

---

## 4. Arquitectura (obligatoria)

NO se permite:

* Código de red en `@Composable`
* Lógica en botones directamente

### Estructura recomendada

```
MainScreen (Compose)
   ↓
StatsViewModel
   ↓
StatsWebSocketClient (Ktor)
```

---

## 5. Cliente WebSocket con Ktor (muy guiado)

### 5.1 Dependencias (copiar tal cual)

```gradle
implementation("io.ktor:ktor-client-core:2.3.7")
implementation("io.ktor:ktor-client-cio:2.3.7")
implementation("io.ktor:ktor-client-websockets:2.3.7")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
```

---

### 5.2 Crear el cliente Ktor

👉 Pista: esto **NO va en la UI**.

```kotlin
val client = HttpClient(CIO) {
    install(WebSockets)
}
```

---

### 5.3 Conectarse al WebSocket

👉 Pista muy directa:

```kotlin
client.webSocket(urlString = wsUrl) {
    for (frame in incoming) {
        if (frame is Frame.Text) {
            val text = frame.readText()
            // aquí llega el JSON
        }
    }
}
```

---

## 6. Parsear los datos (muy fácil)

### 6.1 Modelo de datos

```kotlin
data class Stats(
    val cpu: Int,
    val memory: Int,
    val users: Int,
    val timestamp: String
)
```

---

### 6.2 Convertir JSON → objeto

Pista:

```kotlin
Json.decodeFromString<Stats>(text)
```

---

## 7. Pausar (PISTA CLAVE)

❌ **NO cierres el WebSocket**

✔ Simplemente **no actualices los valores**

Pista muy clara:

```kotlin
if (state == PAUSED) return
```

O:

* Ignorar los mensajes cuando esté en pausa

---

## 8. Cancelar (PISTA CLAVE)

Cancelar significa:

* Cerrar el WebSocket
* Cancelar la coroutine
* Volver a estado **DESCONNECTED**

👉 Pista directa:

* Guarda la `Job`
* Usa `job.cancel()`

---

## 9. ViewModel (qué debe tener)

El `ViewModel` debe exponer:

* Estado de conexión
* Últimas estadísticas
* Funciones:

    * `connect(url)`
    * `pause()`
    * `cancel()`

👉 **No imprime nada por consola**, todo va a la UI.

---

## 10. UI con Compose (pista final)

* Los valores deben actualizarse solos
* No hay que refrescar manualmente

Pista:

> Si el ViewModel usa `StateFlow` o `mutableStateOf`, Compose se redibuja automáticamente.

---

## 11. Errores típicos (ya avisados)

❌ Crear el cliente Ktor dentro de un Composable
❌ Abrir varios WebSockets sin cerrar
❌ Usar `runBlocking`
❌ Manejar estados con varios booleanos

---

## 12. Requisitos mínimos (aprobado)

✔ Se conecta usando **Ktor Client**
✔ Recibe datos
✔ Se actualiza la pantalla
✔ Parar funciona
✔ Cancelar funciona
✔ Código ordenado

---

## 13. Reto opcional (para nota)

* Texto de estado:

    * “Conectado”
    * “En pausa”
    * “Desconectado”
* Contador de mensajes
* Botón deshabilitado según estado

---

## Esquema general del proyecto

```
┌───────────────────────┐
│     MainActivity      │
│───────────────────────│
│ setContent {          │
│   MainScreen()        │
│ }                     │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│      MainScreen       │   (UI - Compose)
│───────────────────────│
│ - TextField (URL)     │
│ - StatsView           │
│ - ControlButtons      │
│                       │
│ Llama a ViewModel     │
└───────────┬───────────┘
            │
            ▼
┌────────────────────────────┐
│      StatsViewModel         │   (Lógica y estado)
│────────────────────────────│
│ - connectionState           │
│ - currentStats              │
│                             │
│ connect(url)                │
│ pause()                     │
│ cancel()                    │
└───────────┬────────────────┘
            │
            ▼
┌────────────────────────────┐
│   StatsWebSocketClient      │   (Red - Ktor)
│────────────────────────────│
│ - HttpClient (Ktor)         │
│ - WebSocket                 │
│ - Coroutine / Job           │
│                             │
│ connect(url)                │
│ disconnect()                │
└───────────┬────────────────┘
            │
            ▼
┌────────────────────────────┐
│        Backend FastAPI      │
│────────────────────────────│
│ ws://IP:8000/ws/stats       │
│ Envía JSON de estadísticas │
└────────────────────────────┘
```

---

## Esquema de paquetes (estructura física)

```
com.iesfp.statsws
│
├── ui
│   ├── MainScreen.kt
│   └── components
│       ├── StatsView.kt
│       └── ControlButtons.kt
│
├── viewmodel
│   └── StatsViewModel.kt
│
├── network
│   └── StatsWebSocketClient.kt
│
├── model
│   ├── Stats.kt
│   └── ConnectionState.kt
│
└── MainActivity.kt
```

---

## Esquema de responsabilidades (clave didáctica)

```
UI (Compose)
│
│  - Muestra datos
│  - Botones
│  - NO lógica
│
▼
ViewModel
│
│  - Estado de la app
│  - Decide qué hacer
│
▼
Network (Ktor)
│
│  - WebSocket
│  - JSON
│  - Conexión
```

---

## Esquema del flujo de datos

```
[ WebSocket JSON ]
        ↓
StatsWebSocketClient
        ↓
StatsViewModel
        ↓
State / StateFlow
        ↓
Jetpack Compose
        ↓
Pantalla se actualiza
```

---

## Esquema de estados

```
┌───────────────┐
│ DISCONNECTED  │
└───────┬───────┘
        │ Conectar
        ▼
┌───────────────┐
│  CONNECTED    │
└───────┬───────┘
        │ Parar
        ▼
┌───────────────┐
│   PAUSED      │
└───────┬───────┘
        │ Cancelar
        ▼
┌───────────────┐
│ DISCONNECTED  │
└───────────────┘
```

---

## Esquema mental para el alumno (frase clave)

> **Compose pinta → ViewModel manda → Ktor conecta**

Si entienden esa frase, aprueban.

---

## Recomendación final (para el enunciado)

Puedes cerrar con algo como:

> Si colocas código WebSocket en la UI, el proyecto está mal estructurado.
> Si todo pasa por el ViewModel, vas bien.

---

