import asyncio
import json
import random
import websockets
from websockets.exceptions import ConnectionClosed

# Estado global
state = {
    "running": False,
    "counter": 0,
    "cpu": 0,
    "ram": 0
}

connected_clients = set()

async def send_to_all(message):
    if connected_clients:
        await asyncio.gather(*[client.send(message) for client in connected_clients], return_exceptions=True)

async def send_snapshot():
    snapshot = {
        "type": "SNAPSHOT",
        "payload": state.copy()
    }
    await send_to_all(json.dumps(snapshot))

async def send_metrics():
    metrics = {
        "type": "METRICS",
        "payload": state.copy()
    }
    await send_to_all(json.dumps(metrics))

async def handle_client(websocket, path):
    if path != "/ws":
        await websocket.close()
        return
    connected_clients.add(websocket)
    try:
        # Enviar snapshot inicial
        await websocket.send(json.dumps({
            "type": "SNAPSHOT",
            "payload": state.copy()
        }))

        async for message in websocket:
            data = json.loads(message)
            if data.get("type") == "CMD":
                cmd = data["payload"]["cmd"]
                if cmd == "START":
                    state["running"] = True
                elif cmd == "STOP":
                    state["running"] = False
                elif cmd == "RESET":
                    state["counter"] = 0
                await send_snapshot()
    except ConnectionClosed:
        pass
    finally:
        connected_clients.remove(websocket)

async def metrics_ticker():
    while True:
        await asyncio.sleep(1)
        if state["running"]:
            state["counter"] += 1
        state["cpu"] = random.randint(0, 100)
        state["ram"] = random.randint(0, 100)
        await send_metrics()

async def main():
    server = await websockets.serve(handle_client, "0.0.0.0", 8000)
    print("Servidor WebSocket corriendo en ws://0.0.0.0:8000")
    asyncio.create_task(metrics_ticker())
    await server.wait_closed()

if __name__ == "__main__":
    asyncio.run(main())
