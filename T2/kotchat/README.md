# KotChat - Aplicación de Chat en Tiempo Real con WebSockets

## 📚 Índice

1. [Introducción a WebSockets](#introducción-a-websockets)
2. [Descripción del Proyecto](#descripción-del-proyecto)
3. [Arquitectura del Sistema](#arquitectura-del-sistema)
4. [Tecnologías Utilizadas](#tecnologías-utilizadas)
5. [Estructura del Proyecto](#estructura-del-proyecto)
6. [Cómo Funciona](#cómo-funciona)
7. [Guía de Instalación](#guía-de-instalación)
8. [Cómo Lanzar la Aplicación](#cómo-lanzar-la-aplicación)
9. [Uso de la Aplicación](#uso-de-la-aplicación)
10. [Solución de Problemas](#solución-de-problemas)

---

## 🌐 Introducción a WebSockets

### ¿Qué son los WebSockets?

**WebSocket** es un protocolo de comunicación que proporciona canales de comunicación **bidireccionales** y **full-duplex** sobre una única conexión TCP. A diferencia del protocolo HTTP tradicional (request-response), WebSocket permite que tanto el cliente como el servidor envíen mensajes en cualquier momento sin necesidad de que uno solicite primero.

### Diferencia entre HTTP y WebSocket

#### HTTP Tradicional (Request-Response):
```
Cliente  →  Solicitud (Request)  →  Servidor
Cliente  ←  Respuesta (Response)  ←  Servidor
```
- El cliente siempre inicia la comunicación
- Cada interacción requiere una nueva conexión
- No es eficiente para comunicación en tiempo real

#### WebSocket (Bidireccional):
```
Cliente  ⇄  Conexión Permanente  ⇄  Servidor
```
- Conexión persistente y bidireccional
- Ambos pueden enviar mensajes en cualquier momento
- Ideal para aplicaciones en tiempo real

### Casos de Uso de WebSockets

- **Aplicaciones de Chat** (como este proyecto)
- Juegos multijugador en tiempo real
- Notificaciones push en tiempo real
- Dashboards con datos en vivo
- Colaboración en tiempo real (como Google Docs)
- Trading y mercados financieros
- Monitoreo de sistemas en tiempo real

### Ventajas de WebSockets

✅ **Baja latencia**: Comunicación instantánea sin overhead de HTTP  
✅ **Eficiencia**: Una sola conexión para toda la sesión  
✅ **Bidireccional**: El servidor puede enviar datos sin que el cliente lo solicite  
✅ **Tiempo real**: Perfecto para aplicaciones interactivas  

### Cómo Funciona la Conexión WebSocket

1. **Handshake Inicial (HTTP)**
   ```
   Cliente: "Upgrade: websocket"
   Servidor: "101 Switching Protocols"
   ```

2. **Conexión Establecida**
   - Se crea un canal de comunicación bidireccional
   - La conexión permanece abierta

3. **Intercambio de Mensajes**
   - Cliente y servidor pueden enviar mensajes libremente
   - Los mensajes se envían como frames

4. **Cierre de Conexión**
   - Cualquiera de las partes puede cerrar la conexión
   - Se envía un frame de cierre

---

## 📱 Descripción del Proyecto

**KotChat** es una aplicación de chat en tiempo real que demuestra la implementación de comunicación bidireccional usando WebSockets. El proyecto consta de dos componentes principales:

1. **Cliente Android** (Kotlin + Jetpack Compose)
2. **Servidor Backend** (Python + FastAPI)

El proyecto permite que múltiples usuarios se conecten simultáneamente y chateen en tiempo real, con notificaciones de entrada/salida de usuarios y broadcasting de mensajes a todos los clientes conectados.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────┐
│   Cliente Android   │
│   (Kotlin/Compose)  │
│                     │
│   - ChatScreen      │
│   - ChatViewModel   │
│   - Ktor Client     │
└──────────┬──────────┘
           │
           │ WebSocket (ws://)
           │ Puerto 8000
           │
           ▼
┌─────────────────────┐
│  Servidor Python    │
│  (FastAPI/uvicorn)  │
│                     │
│   - WebSocket       │
│   - ConnectionMgr   │
│   - Broadcast       │
└─────────────────────┘
           │
           │ Broadcast
           ▼
┌─────────────────────┐
│  Otros Clientes     │
│  Conectados         │
└─────────────────────┘
```

### Flujo de Comunicación

1. **Conexión Inicial**
   ```
   Android App → WebSocket Connect → Servidor Python
   Servidor → Mensaje Bienvenida → Android App
   Servidor → Broadcast "Usuario unido" → Todos los clientes
   ```

2. **Envío de Mensaje**
   ```
   Usuario escribe mensaje → Click "Enviar"
   Android App → WebSocket send → Servidor
   Servidor → Broadcast mensaje → Todos los clientes
   Todos reciben → Actualización UI
   ```

3. **Desconexión**
   ```
   Android App cierra → WebSocket disconnect
   Servidor detecta → Broadcast "Usuario salió"
   ```

---

## 💻 Tecnologías Utilizadas

### Cliente Android (Kotlin)

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Kotlin** | 1.9+ | Lenguaje de programación principal |
| **Jetpack Compose** | Latest | Framework moderno de UI declarativa |
| **Ktor Client** | 2.3.2 | Cliente HTTP/WebSocket |
| **Ktor CIO Engine** | 2.3.2 | Motor asíncrono para Ktor |
| **Lifecycle ViewModel** | 2.6.2+ | Gestión de estado y ciclo de vida |
| **Coroutines** | Built-in | Programación asíncrona |

### Servidor Python

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Python** | 3.8+ | Lenguaje de programación |
| **FastAPI** | Latest | Framework web moderno y rápido |
| **Uvicorn** | Latest | Servidor ASGI de alto rendimiento |
| **WebSockets** | Built-in | Protocolo de comunicación |

---

## 📂 Estructura del Proyecto

```
kotchat/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/kotchat/
│   │       │   ├── MainActivity.kt          # Activity principal
│   │       │   ├── screens/
│   │       │   │   └── ChatScreen.kt        # UI del chat (Compose)
│   │       │   └── viewmodel/
│   │       │       └── ChatViewModel.kt     # Lógica y estado
│   │       ├── res/                         # Recursos Android
│   │       └── AndroidManifest.xml          # Configuración y permisos
│   ├── server/
│   │   └── main.py                          # Servidor WebSocket FastAPI
│   ├── build.gradle.kts                     # Configuración Gradle
│   └── README.md                            # Este archivo
├── gradle/
│   └── libs.versions.toml                   # Versiones de dependencias
└── settings.gradle.kts                      # Configuración del proyecto
```

---

## ⚙️ Cómo Funciona

### 1. Cliente Android (ChatViewModel.kt)

```kotlin
class ChatViewModel : ViewModel() {
    // Cliente Ktor con soporte WebSocket
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }
    
    // Lista observable de mensajes (UI se actualiza automáticamente)
    val messages = mutableStateListOf<String>()
    
    // Referencia a la sesión WebSocket activa
    private var session: DefaultClientWebSocketSession? = null
    
    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            // Conectar al servidor WebSocket
            client.webSocket("ws://192.168.1.34:8000/ws/chat") {
                session = this
                messages.add("Conectado al servidor")
                
                // Escuchar mensajes entrantes
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val text = frame.readText()
                    messages.add(text)  // Actualiza UI automáticamente
                }
            }
        }
    }
    
    fun sendMessage(msg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            session?.send(msg)  // Enviar mensaje al servidor
        }
    }
}
```

**Puntos clave:**
- `viewModelScope`: Maneja coroutines ligadas al ciclo de vida del ViewModel
- `Dispatchers.IO`: Ejecuta operaciones de red en un hilo de I/O
- `mutableStateListOf`: Lista observable que actualiza la UI automáticamente
- `session?.send()`: Envía mensajes de forma asíncrona

### 2. UI con Jetpack Compose (ChatScreen.kt)

```kotlin
@Composable
fun ChatScreen(vm: ChatViewModel) {
    // Conectar al iniciar
    LaunchedEffect(Unit) {
        vm.connect()
    }
    
    var input by remember { mutableStateOf("") }
    
    Column {
        // Lista de mensajes con scroll automático
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(vm.messages) { msg ->
                Text(text = msg)
            }
        }
        
        // Input y botón de envío
        Row {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                vm.sendMessage(input)
                input = ""
            }) {
                Text("Enviar")
            }
        }
    }
}
```

**Características:**
- UI declarativa con Compose
- Actualización automática cuando `vm.messages` cambia
- LaunchedEffect ejecuta la conexión una sola vez
- TextField con binding bidireccional

### 3. Servidor Python (main.py)

```python
class ConnectionManager:
    def __init__(self):
        self.active_connections: List[WebSocket] = []
    
    async def connect(self, websocket: WebSocket):
        await websocket.accept()  # Aceptar conexión
        self.active_connections.append(websocket)
    
    def disconnect(self, websocket: WebSocket):
        self.active_connections.remove(websocket)
    
    async def broadcast(self, message: str):
        # Enviar mensaje a todos los clientes conectados
        for connection in self.active_connections:
            await connection.send_text(message)

@app.websocket("/ws/chat")
async def websocket_endpoint(websocket: WebSocket):
    await manager.connect(websocket)
    client_info = f"{websocket.client.host}:{websocket.client.port}"
    
    # Mensaje de bienvenida
    await websocket.send_text(f"¡Bienvenido! Tu ID: {client_info}")
    await manager.broadcast(f"🟢 {client_info} se unió")
    
    try:
        while True:
            # Esperar mensajes del cliente
            message = await websocket.receive_text()
            print(f"📨 Recibido de {client_info}: {message}")
            # Reenviar a todos
            await manager.broadcast(f"{client_info}: {message}")
    except WebSocketDisconnect:
        manager.disconnect(websocket)
        await manager.broadcast(f"🔴 {client_info} salió")
```

**Componentes:**
- `ConnectionManager`: Gestiona lista de clientes conectados
- `broadcast()`: Envía mensaje a todos los clientes
- `async/await`: Operaciones asíncrinas no bloqueantes
- Logging detallado con emojis para debugging

---

## 📥 Guía de Instalación

### Requisitos Previos

#### Para el Cliente Android:
- **Android Studio** (Arctic Fox o superior)
- **JDK 11** o superior
- **Gradle 8.0+**
- Dispositivo Android con **API 24+** (Android 7.0) o emulador

#### Para el Servidor Python:
- **Python 3.8** o superior
- **pip** (gestor de paquetes de Python)

### Instalación del Servidor Python

1. **Navegar al directorio del servidor:**
   ```bash
   cd C:\Users\madrid\ws\androidprojects\kotchat\app\src\main\java\com\example\kotchat\server
   ```

2. **Crear entorno virtual (recomendado):**
   ```bash
   python -m venv venv
   venv\Scripts\activate  # Windows
   ```

3. **Instalar dependencias:**
   ```bash
   pip install fastapi uvicorn websockets
   ```

### Configuración del Cliente Android

1. **Abrir proyecto en Android Studio:**
   - Abre Android Studio
   - File → Open → Selecciona la carpeta `kotchat`

2. **Sincronizar Gradle:**
   - Android Studio sincronizará automáticamente
   - O manualmente: File → Sync Project with Gradle Files

3. **Configurar IP del servidor:**
   - Edita `ChatViewModel.kt`
   - Cambia la IP en la línea:
     ```kotlin
     client.webSocket("ws://TU_IP_LOCAL:8000/ws/chat")
     ```
   - Usa tu IP local (averígüala con `ipconfig` en Windows)

---

## 🚀 Cómo Lanzar la Aplicación

### Paso 1: Iniciar el Servidor Python

1. **Abrir terminal en el directorio del servidor:**
   ```bash
   cd C:\Users\madrid\ws\androidprojects\kotchat\app\src\main\java\com\example\kotchat\server
   ```

2. **Activar entorno virtual (si lo creaste):**
   ```bash
   venv\Scripts\activate
   ```

3. **Ejecutar servidor:**
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8000 --reload
   ```

4. **Verificar que está corriendo:**
   ```
   INFO:     Uvicorn running on http://0.0.0.0:8000
   INFO:     Application startup complete.
   ```

5. **Prueba opcional - Health check:**
   - Abre navegador: `http://localhost:8000/health`
   - Deberías ver: `{"status":"ok","active_connections":0}`

### Paso 2: Ejecutar la Aplicación Android

#### Opción A: Usando Dispositivo Físico

1. **Habilitar Modo Desarrollador:**
   - Configuración → Acerca del teléfono
   - Pulsa 7 veces en "Número de compilación"

2. **Habilitar Depuración USB:**
   - Configuración → Opciones de desarrollador
   - Activa "Depuración USB"

3. **Conectar dispositivo:**
   - Conecta el teléfono por USB
   - Acepta depuración en el teléfono

4. **Verificar conexión a la misma red:**
   - Tu teléfono y PC deben estar en la misma red WiFi

5. **Ejecutar en Android Studio:**
   - Click en Run ▶️ (o Shift + F10)
   - Selecciona tu dispositivo

#### Opción B: Usando Emulador

1. **Crear emulador:**
   - Tools → Device Manager
   - Create Device → Selecciona Pixel 4 o similar
   - API Level 28+ (recomendado API 33)

2. **Configurar red:**
   - El emulador tiene acceso a la red del host
   - Usa la IP de tu PC, NO `localhost` ni `127.0.0.1`
   - Para acceder a localhost del host: usa `10.0.2.2`

3. **Ejecutar:**
   - Click en Run ▶️
   - Selecciona el emulador

### Paso 3: Verificar Funcionamiento

**En la consola del servidor verás:**
```
✅ Cliente conectado: 192.168.1.50:45678
📊 Total de conexiones activas: 1
📢 Broadcasting mensaje a 1 cliente(s): 🟢 192.168.1.50:45678 se ha unido al chat
```

**En la app Android verás:**
```
Conectado al servidor
¡Bienvenido al chat! Tu ID: 192.168.1.50:45678
🟢 192.168.1.50:45678 se ha unido al chat
```

---

## 📖 Uso de la Aplicación

### Enviar Mensajes

1. Escribe tu mensaje en el campo de texto inferior
2. Pulsa el botón "Enviar"
3. El mensaje aparecerá en tu pantalla y en la de todos los usuarios conectados

### Múltiples Usuarios

1. Abre la app en varios dispositivos/emuladores
2. Todos estarán en el mismo chat
3. Verás notificaciones cuando usuarios entren/salgan
4. Todos los mensajes se recibirán en tiempo real

### Logs del Servidor

El servidor muestra logs detallados:
- ✅ **Conexiones**: Cuando alguien se conecta
- 📨 **Mensajes recibidos**: Con ID del remitente
- 📢 **Broadcasts**: Cuántos clientes reciben el mensaje
- ❌ **Desconexiones**: Cuando alguien sale

---

## 🔧 Solución de Problemas

### Problema: "Operation not permitted"

**Causa:** Falta permiso de Internet en AndroidManifest.xml

**Solución:**
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Problema: "Cleartext communication not permitted"

**Causa:** Android 9+ bloquea conexiones HTTP/WS no seguras

**Solución:** Agrega en `<application>`:
```xml
android:usesCleartextTraffic="true"
```

### Problema: No se conecta al servidor

**Verificaciones:**

1. **Servidor corriendo:**
   ```bash
   # Verifica que uvicorn esté activo
   netstat -an | findstr 8000
   ```

2. **IP correcta:**
   - NO usar `localhost` ni `127.0.0.1` desde Android
   - Usar IP local: `ipconfig` → IPv4

3. **Misma red:**
   - PC y teléfono en la misma WiFi
   - Firewall no bloquea puerto 8000

4. **Firewall Windows:**
   ```bash
   # Permitir conexiones en puerto 8000
   netsh advfirewall firewall add rule name="WebSocket" dir=in action=allow protocol=TCP localport=8000
   ```

### Problema: Mensajes no se reciben

**Verificar:**
- Los logs del servidor muestran el mensaje recibido
- El broadcast se ejecuta
- No hay excepciones en Logcat (Android Studio)

**Debugging:**
```bash
# Ver logs en tiempo real
adb logcat | findstr "kotchat"
```

### Problema: App se cierra al enviar mensaje

**Causa:** Posible excepción en coroutine

**Solución:**
- Revisa Logcat en Android Studio
- Verifica que `session` no sea null
- Asegúrate de estar en `Dispatchers.IO`

---

## 🎯 Mejoras Futuras

### Funcionalidades:
- [ ] Autenticación de usuarios
- [ ] Nombres de usuario personalizados
- [ ] Salas de chat separadas
- [ ] Envío de imágenes
- [ ] Notificaciones push
- [ ] Historial de mensajes persistente
- [ ] Cifrado end-to-end

### Técnicas:
- [ ] Usar WSS (WebSocket Secure) con TLS
- [ ] Implementar reconnect automático
- [ ] Base de datos para mensajes
- [ ] UI mejorada con Material 3
- [ ] Tests unitarios e integración
- [ ] Docker para el servidor
- [ ] CI/CD pipeline

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 👨‍💻 Autor

Desarrollado como proyecto educativo para aprender WebSockets, Kotlin, Jetpack Compose y FastAPI.

**Fecha:** Diciembre 2025

---

## 📚 Referencias y Recursos

### Documentación Oficial:
- [WebSocket Protocol (RFC 6455)](https://datatracker.ietf.org/doc/html/rfc6455)
- [Ktor Client Documentation](https://ktor.io/docs/client.html)
- [FastAPI WebSockets](https://fastapi.tiangolo.com/advanced/websockets/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

### Tutoriales:
- [Understanding WebSockets](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API)
- [Building Real-Time Apps](https://www.youtube.com/watch?v=vQjiN8Qgs3c)

---

## ❓ FAQ (Preguntas Frecuentes)

**Q: ¿Puedo usar wss:// en lugar de ws://?**  
A: Sí, pero necesitas configurar certificados SSL en el servidor.

**Q: ¿Funciona en producción?**  
A: Este es un proyecto educativo. Para producción, considera seguridad, escalabilidad y manejo de errores robusto.

**Q: ¿Puedo conectar desde Internet?**  
A: Necesitas exponer el puerto 8000 en tu router (port forwarding) o usar servicios como ngrok.

**Q: ¿Cuántos usuarios soporta?**  
A: Depende del servidor. FastAPI/Uvicorn puede manejar miles con la configuración adecuada.

**Q: ¿Por qué no usar Socket.IO?**  
A: Socket.IO es excelente pero más pesado. WebSockets nativos son más ligeros y suficientes para este caso.

---

¡Disfruta construyendo aplicaciones en tiempo real! 🚀

