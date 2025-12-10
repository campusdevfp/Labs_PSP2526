from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from typing import List

app = FastAPI()

class ConnectionManager:
    def __init__(self):
        self.active_connections: List[WebSocket] = []

    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)

    def disconnect(self, websocket: WebSocket):
        self.active_connections.remove(websocket)

    async def broadcast(self, message: str):
        print(f"📢 Broadcasting mensaje a {len(self.active_connections)} cliente(s): {message}")
        for connection in self.active_connections:
            await connection.send_text(message)

manager = ConnectionManager()

@app.websocket("/ws/chat")
async def websocket_endpoint(websocket: WebSocket):
    await manager.connect(websocket)
    client_info = f"{websocket.client.host}:{websocket.client.port}"
    print(f"✅ Cliente conectado: {client_info}")
    print(f"📊 Total de conexiones activas: {len(manager.active_connections)}")

    # Enviar mensaje de bienvenida
    await websocket.send_text(f"¡Bienvenido al chat! Tu ID: {client_info}")
    # Notificar a todos los demás
    await manager.broadcast(f"🟢 {client_info} se ha unido al chat")

    try:
        while True:
            message = await websocket.receive_text()
            print(f"📨 Mensaje recibido de {client_info}: {message}")
            await manager.broadcast(f"{client_info}: {message}")
    except WebSocketDisconnect:
        manager.disconnect(websocket)
        print(f"❌ Cliente desconectado: {client_info}")
        print(f"📊 Total de conexiones activas: {len(manager.active_connections)}")
        await manager.broadcast(f"🔴 {client_info} ha salido del chat")

@app.get("/health")
async def health_check():
    return {"status": "ok", "active_connections": len(manager.active_connections)}


"""
uvicorn main:app --host 0.0.0.0 --port 8000

"""