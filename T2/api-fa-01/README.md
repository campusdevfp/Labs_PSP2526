Para verificar que todo funciona correctamente, sigue estos pasos:

## 1️⃣ Levantar los contenedores

```bash
docker-compose up -d --build
```

## 2️⃣ Verificar que los contenedores están corriendo

```bash
docker-compose ps
```

Deberías ver algo así:
```
NAME                IMAGE               STATUS              PORTS
fastapi_app         ...                 Up                  0.0.0.0:8000->8000/tcp
mysql_db            mysql:8.0           Up (healthy)        0.0.0.0:3306->3306/tcp
```

## 3️⃣ Ver los logs

```bash
docker-compose logs -f fastapi
```

Deberías ver que Uvicorn está corriendo sin errores.

## 4️⃣ Probar la API

### Opción A: Navegador
Abre tu navegador y ve a:
- **Documentación Swagger**: http://localhost:8000/docs
- **Estado de la API**: http://localhost:8000/

### Opción B: cURL (desde terminal)

**1. Verificar estado:**
```bash
curl http://localhost:8000/
```

**2. Obtener todos los items:**
```bash
curl http://localhost:8000/items/
```

**3. Obtener un item específico:**
```bash
curl http://localhost:8000/items/1
```

**4. Crear un nuevo item:**
```bash
curl -X POST "http://localhost:8000/items/" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Webcam",
    "descripcion": "Webcam HD 1080p",
    "precio": 75.50
  }'
```

**5. Actualizar un item:**
```bash
curl -X PUT "http://localhost:8000/items/1" \
  -H "Content-Type: application/json" \
  -d '{
    "precio": 1100.00
  }'
```

**6. Eliminar un item:**
```bash
curl -X DELETE "http://localhost:8000/items/6"
```

### Opción C: Swagger UI (Recomendado)

1. Ve a http://localhost:8000/docs
2. Verás todos los endpoints disponibles
3. Haz clic en cualquier endpoint
4. Haz clic en "Try it out"
5. Completa los parámetros si es necesario
6. Haz clic en "Execute"
7. Verás la respuesta abajo

## 5️⃣ Verificar la base de datos

```bash
docker exec -it mysql_db mysql -u root -prootpassword crud_db -e "SELECT * FROM items;"
```

Deberías ver los 5 items iniciales.

## 6️⃣ Troubleshooting

Si algo falla:

```bash
# Ver logs de ambos servicios
docker-compose logs

# Ver logs solo de FastAPI
docker-compose logs fastapi

# Ver logs solo de MySQL
docker-compose logs mysql

# Reiniciar todo
docker-compose restart

# Parar y eliminar todo (incluyendo datos)
docker-compose down -v
docker-compose up -d --build
```

---
