from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import mysql.connector
from mysql.connector import Error
import os

app = FastAPI(title="CRUD API", version="1.0.0")

# Configurar CORS para permitir conexiones desde Android/Retrofit
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Configuración de la base de datos
DB_CONFIG = {
    "host": os.getenv("DB_HOST", "mysql"),
    "user": os.getenv("DB_USER", "root"),
    "password": os.getenv("DB_PASSWORD", "rootpassword"),
    "database": os.getenv("DB_NAME", "crud_db"),
}

# Modelos Pydantic
class ItemBase(BaseModel):
    nombre: str
    descripcion: Optional[str] = None
    precio: float

class ItemCreate(ItemBase):
    pass

class ItemUpdate(BaseModel):
    nombre: Optional[str] = None
    descripcion: Optional[str] = None
    precio: Optional[float] = None

class Item(ItemBase):
    id: int

# Función para obtener conexión a la base de datos
def get_db_connection():
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        return connection
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error de conexión a la base de datos: {str(e)}")

@app.get("/")
def read_root():
    return {"message": "API CRUD con FastAPI y MySQL", "status": "running"}

@app.post("/items/", response_model=Item, status_code=201)
def create_item(item: ItemCreate):
    connection = get_db_connection()
    cursor = connection.cursor()

    try:
        query = "INSERT INTO items (nombre, descripcion, precio) VALUES (%s, %s, %s)"
        cursor.execute(query, (item.nombre, item.descripcion, item.precio))
        connection.commit()

        item_id = cursor.lastrowid
        cursor.close()
        connection.close()

        return Item(id=item_id, **item.model_dump())
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error al crear item: {str(e)}")

@app.get("/items/", response_model=List[Item])
def read_items():
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    try:
        cursor.execute("SELECT id, nombre, descripcion, precio FROM items")
        items = cursor.fetchall()
        cursor.close()
        connection.close()

        return items
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error al obtener items: {str(e)}")

@app.get("/items/{item_id}", response_model=Item)
def read_item(item_id: int):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    try:
        cursor.execute("SELECT id, nombre, descripcion, precio FROM items WHERE id = %s", (item_id,))
        item = cursor.fetchone()
        cursor.close()
        connection.close()

        if item is None:
            raise HTTPException(status_code=404, detail="Item no encontrado")

        return item
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error al obtener item: {str(e)}")

@app.put("/items/{item_id}", response_model=Item)
def update_item(item_id: int, item: ItemUpdate):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    try:
        cursor.execute("SELECT * FROM items WHERE id = %s", (item_id,))
        existing_item = cursor.fetchone()

        if existing_item is None:
            raise HTTPException(status_code=404, detail="Item no encontrado")

        update_data = item.model_dump(exclude_unset=True)
        if not update_data:
            return {"id": existing_item["id"], "nombre": existing_item["nombre"], "descripcion": existing_item["descripcion"], "precio": float(existing_item["precio"])}

        set_clause = ", ".join([f"{key} = %s" for key in update_data.keys()])
        query = f"UPDATE items SET {set_clause} WHERE id = %s"
        values = list(update_data.values()) + [item_id]

        cursor.execute(query, values)
        connection.commit()

        cursor.execute("SELECT id, nombre, descripcion, precio FROM items WHERE id = %s", (item_id,))
        updated_item = cursor.fetchone()

        cursor.close()
        connection.close()

        return updated_item
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error al actualizar item: {str(e)}")

@app.delete("/items/{item_id}", status_code=204)
def delete_item(item_id: int):
    connection = get_db_connection()
    cursor = connection.cursor()

    try:
        cursor.execute("SELECT id FROM items WHERE id = %s", (item_id,))
        if cursor.fetchone() is None:
            raise HTTPException(status_code=404, detail="Item no encontrado")

        cursor.execute("DELETE FROM items WHERE id = %s", (item_id,))
        connection.commit()
        cursor.close()
        connection.close()

        return None
    except Error as e:
        raise HTTPException(status_code=500, detail=f"Error al eliminar item: {str(e)}")