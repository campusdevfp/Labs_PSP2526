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
