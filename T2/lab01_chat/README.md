## Proyecto paso a paso: “Panel en tiempo real” (FastAPI WS + Jetpack Compose)

Idea: el servidor emite **métricas/eventos** (CPU/RAM simuladas, contador, estado de sistema) y los móviles lo ven en **stream**. Los clientes pueden enviar comandos (start/stop/reset). **No es chat.**

---

# 1) Backend FastAPI (WebSocket + broadcast)

### 1.1 Estructura

```
backend/
  main.py
```

### 1.2 Instala

```bash
pip install fastapi uvicorn
```

### 1.3 `main.py`

```python
import asyncio
import json
import random
from fastapi import FastAPI, WebSocket, WebSocketDisconnect

app = FastAPI()

class WSManager:
    def __init__(self):
        self.clients: set[WebSocket] = set()

    async def connect(self, ws: WebSocket):
        await ws.accept()
        self.clients.add(ws)

    def disconnect(self, ws: WebSocket):
        self.clients.discard(ws)

    async def broadcast(self, message: dict):
        dead = []
        data = json.dumps(message)
        for ws in self.clients:
            try:
                await ws.send_text(data)
            except Exception:
                dead.append(ws)
        for ws in dead:
            self.disconnect(ws)

mgr = WSManager()

state = {
    "running": True,
    "counter": 0,
    "cpu": 0,
    "ram": 0,
}

@app.get("/health")
def health():
    return {"ok": True}

async def ticker():
    while True:
        await asyncio.sleep(1)
        if state["running"]:
            state["counter"] += 1
            state["cpu"] = random.randint(1, 100)
            state["ram"] = random.randint(1, 100)
            await mgr.broadcast({
                "type": "METRICS",
                "payload": state
            })

@app.on_event("startup")
async def on_startup():
    asyncio.create_task(ticker())

@app.websocket("/ws")
async def ws_endpoint(ws: WebSocket):
    await mgr.connect(ws)

    # manda snapshot inicial
    await ws.send_text(json.dumps({"type": "SNAPSHOT", "payload": state}))

    try:
        while True:
            msg = await ws.receive_text()
            data = json.loads(msg)

            if data.get("type") == "CMD":
                cmd = data.get("payload", {}).get("cmd")
                if cmd == "START":
                    state["running"] = True
                elif cmd == "STOP":
                    state["running"] = False
                elif cmd == "RESET":
                    state["counter"] = 0

                await mgr.broadcast({"type": "SNAPSHOT", "payload": state})
    except WebSocketDisconnect:
        mgr.disconnect(ws)
```

### 1.4 Ejecuta

```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

---

# 2) Android Jetpack Compose (OkHttp WebSocket)

## 2.1 Dependencias (app `build.gradle`)

Añade OkHttp:

```gradle
implementation("com.squareup.okhttp3:okhttp:4.12.0")
```

---

## 2.2 Modelos de mensajes (tipado simple)

`WsMessages.kt`

```kotlin
data class WsEnvelope(
    val type: String,
    val payload: Map<String, Any?>
)
```

*(Para ir rápido evitamos librerías JSON extra; parsearemos con JSONObject.)*

---

## 2.3 Cliente WebSocket

`WsClient.kt`

```kotlin
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class WsClient(
    private val url: String,
    private val onMessage: (String) -> Unit,
    private val onState: (Boolean) -> Unit
) {
    private val client = OkHttpClient()
    private var ws: WebSocket? = null

    fun connect() {
        val req = Request.Builder().url(url).build()
        ws = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onState(true)
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessage(text)
            }
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                onState(false)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onState(false)
            }
        })
    }

    fun sendCmd(cmd: String) {
        val msg = JSONObject().apply {
            put("type", "CMD")
            put("payload", JSONObject().put("cmd", cmd))
        }
        ws?.send(msg.toString())
    }

    fun close() {
        ws?.close(1000, "bye")
    }
}
```

---

## 2.4 ViewModel (estado reactivo)

`DashboardViewModel.kt`

```kotlin
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

data class DashboardState(
    val connected: Boolean = false,
    val running: Boolean = false,
    val counter: Int = 0,
    val cpu: Int = 0,
    val ram: Int = 0
)

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    private lateinit var ws: WsClient

    fun start(wsUrl: String) {
        ws = WsClient(
            url = wsUrl,
            onMessage = ::handleMessage,
            onState = { ok -> _state.value = _state.value.copy(connected = ok) }
        )
        ws.connect()
    }

    private fun handleMessage(text: String) {
        val obj = JSONObject(text)
        val type = obj.getString("type")
        val payload = obj.getJSONObject("payload")

        if (type == "SNAPSHOT" || type == "METRICS") {
            _state.value = DashboardState(
                connected = _state.value.connected,
                running = payload.getBoolean("running"),
                counter = payload.getInt("counter"),
                cpu = payload.getInt("cpu"),
                ram = payload.getInt("ram")
            )
        }
    }

    fun cmdStart() = ws.sendCmd("START")
    fun cmdStop() = ws.sendCmd("STOP")
    fun cmdReset() = ws.sendCmd("RESET")
}
```

---

## 2.5 UI Compose

`DashboardScreen.kt`

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectAsState

@Composable
fun DashboardScreen(
    wsUrl: String,
    vm: DashboardViewModel = viewModel()
) {
    val s by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.start(wsUrl) }

    Column(Modifier.padding(16.dp)) {
        Text("Connected: ${s.connected}")
        Spacer(Modifier.height(8.dp))
        Text("Running: ${s.running}")
        Text("Counter: ${s.counter}")
        Text("CPU: ${s.cpu}%")
        Text("RAM: ${s.ram}%")

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = vm::cmdStart, enabled = s.connected) { Text("START") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = vm::cmdStop, enabled = s.connected) { Text("STOP") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = vm::cmdReset, enabled = s.connected) { Text("RESET") }
        }
    }
}
```

En tu `MainActivity` llama:

```kotlin
setContent {
    DashboardScreen(wsUrl = "ws://10.0.2.2:8000/ws") // emulador -> host
}
```

---

# 3) Prueba rápida

1. Ejecuta backend.
2. Abre 2 emuladores / móvil + emulador.
3. Verás métricas en vivo y comandos sincronizados (start/stop/reset) en todos.

---

# Reto para alumnos (mini-proyecto)

## “Tablero colaborativo en tiempo real”

Parten del proyecto anterior.

### Requisitos mínimos

* Estado compartido: lista de “items” (id, texto, estado).
* Eventos WS:

    * `ADD_ITEM`
    * `TOGGLE_ITEM`
    * `DELETE_ITEM`
* Servidor autoritativo (valida y reemite snapshot).
* UI Compose reactiva + ViewModel + StateFlow.
* (Extra) reconexión simple o “salas” (`roomId`).

Con esto aprenden WS de verdad: **eventos, broadcast, sincronización, estado**.

# Backend FastAPI con WebSockets 

## 1. Qué papel juega el backend

El backend **no es una app web** aquí. Es:

> Un **servidor de estado en tiempo real**
> que mantiene la verdad y sincroniza a todos los clientes.

Los móviles **no hablan entre ellos**.
Todos hablan **solo con el servidor**.

---

## 2. WebSocket ≠ HTTP

Esto es clave y hay que dejarlo muy claro.

| HTTP                 | WebSocket            |
| -------------------- | -------------------- |
| petición → respuesta | conexión persistente |
| se cierra            | queda abierta        |
| cliente pregunta     | servidor empuja      |
| stateless            | estado en memoria    |

En este proyecto:

* HTTP → solo para `/health`
* WebSocket → **todo lo importante**

---

## 3. Qué mantiene el servidor

El servidor guarda un **estado global**:

```text
running   -> si el sistema está activo
counter   -> contador común
cpu       -> valor simulado
ram       -> valor simulado
```

Ese estado **no pertenece a ningún móvil**.

Todos ven **el mismo**.

---

## 4. Conexiones WebSocket (idea clave)

El servidor:

* acepta conexiones
* guarda quién está conectado
* puede enviar mensajes a todos

Conceptualmente:

```text
cliente A ─┐
cliente B ─┼──► servidor ───► broadcast
cliente C ─┘
```

Esto se llama **broadcast**.

---

## 5. Connection Manager (sin código)

Hay una pieza llamada *gestor de conexiones* que hace solo 3 cosas:

1. **Aceptar** un cliente nuevo
2. **Recordar** qué clientes están conectados
3. **Enviar mensajes a todos**

Los alumnos solo deben quedarse con esta idea:

> “El servidor sabe quién está conectado y puede hablar con todos a la vez”.

---

## 6. Snapshot inicial

Cuando un móvil se conecta:

1. Abre WebSocket
2. El servidor **le manda el estado completo**
3. El móvil ya está sincronizado

Esto evita inconsistencias.

Mensaje conceptual:

```json
{
  "type": "SNAPSHOT",
  "payload": { estado completo }
}
```

---

## 7. Eventos, no mensajes

Esto es **lo importante del proyecto**.

El servidor **no recibe texto libre**.

Recibe **eventos**:

* `CMD: START`
* `CMD: STOP`
* `CMD: RESET`

El móvil **no dice qué estado quiere**.
Solo dice **qué acción solicita**.

Esto se llama:

> **servidor autoritativo**

---

## 8. Flujo completo de un evento

Ejemplo: un alumno pulsa RESET.

1. Móvil envía:

```json
{ "type": "CMD", "payload": { "cmd": "RESET" } }
```

2. Servidor:

    * valida el comando
    * modifica el estado
    * genera un nuevo snapshot

3. Servidor envía a todos:

```json
{ "type": "SNAPSHOT", "payload": { estado actualizado } }
```

Todos los móviles cambian **a la vez**.

---

## 9. Emisión periódica (ticker)

El servidor tiene una tarea interna que:

* se ejecuta cada segundo
* actualiza métricas
* emite eventos automáticamente

Esto demuestra algo clave:

> Con WebSockets el servidor **puede hablar solo**, sin que el cliente pregunte.

---

## 10. Por qué FastAPI

No por Python.

Sino porque:

* WebSockets nativos
* Código muy pequeño
* Ideal para **servicios de tiempo real**
* Perfecto para backend “tonto pero autoritativo”

---

