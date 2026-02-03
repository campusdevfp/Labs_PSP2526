# Ejercicio Práctico: Completar Cliente Retrofit con Operaciones CRUD

## Contexto

Has heredado un proyecto de cliente Android que consume una API REST construida con FastAPI. El proyecto ya tiene implementadas tres operaciones básicas:

- ✅ **GET /items/** - Listar todos los items
- ✅ **POST /items/** - Crear un nuevo item
- ✅ **DELETE /items/{id}** - Eliminar un item

Sin embargo, **faltan dos operaciones importantes** que el API ya soporta:

- ❌ **GET /items/{id}** - Obtener un item específico por ID
- ❌ **UPDATE /items/{id}** - Actualizar un item existente

## Objetivo

Tu tarea es completar el cliente Retrofit implementando las operaciones faltantes, siguiendo los patrones y buenas prácticas ya establecidos en el código existente.

---

## Parte 1: Implementar GET Item por ID

### Paso 1.1: Actualizar el modelo de datos

Crea el archivo `data/model/ItemUpdate.kt` para manejar las actualizaciones:

```kotlin
package com.dam2.cliente_retrofit_fa01.data.model

data class ItemUpdate(
    val nombre: String?,
    val descripcion: String?,
    val precio: Double?
)
```

### Paso 1.2: Añadir endpoint en ApiService

En el archivo `data/remote/ApiService.kt`, añade el método para obtener un item por ID:

```kotlin
@GET("items/{id}")
suspend fun getItemById(
    @Path("id") id: Int
): Item
```

**💡 Pista:** Observa cómo se usa `@Path` en el método `deleteItem()` como referencia.

### Paso 1.3: Añadir método en ViewModel

En `ui/theme/vm/ItemViewModel.kt`, añade:

1. Un StateFlow para almacenar el item seleccionado:
```kotlin
private val _selectedItem = MutableStateFlow<Item?>(null)
val selectedItem: StateFlow<Item?> = _selectedItem
```

2. Una función para cargar un item específico:
```kotlin
fun loadItemById(id: Int) {
    viewModelScope.launch {
        _loading.value = true
        try {
            _selectedItem.value = api.getItemById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            _selectedItem.value = null
        }
        _loading.value = false
    }
}
```

---

## Parte 2: Implementar UPDATE Item

### Paso 2.1: Añadir endpoint en ApiService

En `data/remote/ApiService.kt`, añade el método PUT:

```kotlin
@PUT("items/{id}")
suspend fun updateItem(
    @Path("id") id: Int,
    @Body item: ItemUpdate
): Item
```

**⚠️ Nota:** El API acepta actualizaciones parciales, por lo que puedes enviar solo los campos que quieras modificar.

### Paso 2.2: Añadir método en ViewModel

En `ui/theme/vm/ItemViewModel.kt`, implementa:

```kotlin
fun updateItem(id: Int, nombre: String?, descripcion: String?, precio: Double?) {
    viewModelScope.launch {
        try {
            val itemUpdate = ItemUpdate(nombre, descripcion, precio)
            api.updateItem(id, itemUpdate)
            loadItems() // Recargar la lista
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
```

---

## Parte 3: Crear UI para Editar Items

### Paso 3.1: Crear diálogo de edición

En `ui/theme/MainScreen.kt`, añade este composable después de `AddItemDialog`:

```kotlin
@Composable
fun EditItemDialog(
    item: Item,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var nombre by remember { mutableStateOf(item.nombre) }
    var descripcion by remember { mutableStateOf(item.descripcion ?: "") }
    var precio by remember { mutableStateOf(item.precio.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Item") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precioDouble = precio.toDoubleOrNull() ?: 0.0
                    onConfirm(nombre, descripcion, precioDouble)
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
```

### Paso 3.2: Añadir botón de edición en ItemRow

Modifica `ui/theme/ItemRow.kt` para añadir un botón de edición junto al de eliminar:

**💡 Pista:** Añade un parámetro `onEdit: () -> Unit` al composable `ItemRow` y crea un `IconButton` con un icono de edición (por ejemplo, `Icons.Default.Edit`).

### Paso 3.3: Integrar en MainScreen

Modifica `MainScreen` para manejar la edición:

1. Añade un estado para controlar el diálogo de edición:
```kotlin
var showEditDialog by remember { mutableStateOf(false) }
var itemToEdit by remember { mutableStateOf<Item?>(null) }
```

2. Actualiza el `ItemRow` en el `LazyColumn`:
```kotlin
items(items) { item ->
    ItemRow(
        item = item,
        onDelete = { viewModel.deleteItem(item.id) },
        onEdit = {
            itemToEdit = item
            showEditDialog = true
        }
    )
}
```

3. Añade el diálogo de edición después del diálogo de agregar:
```kotlin
if (showEditDialog && itemToEdit != null) {
    EditItemDialog(
        item = itemToEdit!!,
        onDismiss = { showEditDialog = false },
        onConfirm = { nombre, descripcion, precio ->
            viewModel.updateItem(itemToEdit!!.id, nombre, descripcion, precio)
            showEditDialog = false
        }
    )
}
```

---

## 🧪 Parte 4: Pruebas

### Checklist de funcionalidad:

- [ ] **Listar items:** La app muestra todos los items al iniciar
- [ ] **Crear item:** Al presionar "Agregar Item", se abre un diálogo y se crea correctamente
- [ ] **Editar item:** Al presionar el botón de editar, se abre un diálogo con los datos actuales y se actualizan
- [ ] **Eliminar item:** Al presionar eliminar, el item se borra de la lista
- [ ] **Manejo de errores:** La app no se cierra si hay un error de red
- [ ] **Indicador de carga:** Se muestra un spinner mientras se cargan los datos

### Escenarios de prueba:

1. **Crear un nuevo item:**
   - Nombre: "Laptop"
   - Descripción: "MacBook Pro M3"
   - Precio: 2499.99

2. **Editar el item creado:**
   - Cambiar precio a: 2299.99
   - Verificar que se actualiza en la lista

3. **Eliminar el item**
   - Verificar que desaparece de la lista

4. **Actualización parcial:**
   - Crear un item y editar solo el nombre
   - Verificar que descripción y precio se mantienen

---


---

## 📚 Recursos Adicionales

### Documentación de referencia:
- [Retrofit Annotations](https://square.github.io/retrofit/)
- [Jetpack Compose State](https://developer.android.com/jetpack/compose/state)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

### Anotaciones Retrofit útiles:
- `@GET`: Petición GET
- `@POST`: Petición POST
- `@PUT`: Petición PUT
- `@DELETE`: Petición DELETE
- `@Path("id")`: Variable de ruta
- `@Body`: Cuerpo de la petición
- `@Query("param")`: Parámetro de consulta

### Iconos Material3 útiles:
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
```

---

## 💡 Tips y Recomendaciones

1. **Sigue el patrón existente:** Observa cómo están implementados `createItem()` y `deleteItem()` como referencia.

2. **Manejo de errores:** Siempre usa try-catch en las corrutinas para evitar crashes.

3. **Testing con Postman/Thunder Client:** Antes de implementar en Android, prueba los endpoints directamente:
   ```
   GET http://localhost:8000/items/1
   PUT http://localhost:8000/items/1
   Body: {
     "nombre": "Item actualizado",
     "precio": 99.99
   }
   ```

4. **Debugging:** Usa `Log.d()` o `println()` para verificar las respuestas del API:
   ```kotlin
   try {
       val item = api.getItemById(id)
       println("Item obtenido: $item")
       _selectedItem.value = item
   } catch (e: Exception) {
       println("Error: ${e.message}")
   }
   ```

5. **Actualizaciones parciales:** El API soporta actualizar solo algunos campos. Podrías mejorar el `EditItemDialog` para permitir campos vacíos que no se actualicen.

---

## 🚀 Desafío Extra (Opcional - +1.0 punto)

Implementa una pantalla de detalle que:

1. Se abra al hacer clic en un item de la lista
2. Use `loadItemById()` para cargar los detalles
3. Muestre toda la información del item en un diseño atractivo
4. Incluya botones para editar o eliminar desde ahí

**Pista:** Necesitarás usar navegación con Compose Navigation.

---

## 📌 Entrega

1. Proyecto completo funcionando
2. Capturas de pantalla mostrando:
   - Lista de items
   - Crear nuevo item
   - Editar item existente
   - Item actualizado en la lista
3. Breve documento (máx. 1 página) explicando:
   - Dificultades encontradas
   - Cómo las resolviste
   - Mejoras que harías al código

**Fecha límite:** [Indicar fecha]

---

## ❓ Preguntas Frecuentes

**P: ¿Puedo cambiar la estructura del código existente?**  
R: Puedes mejorar el código existente, pero mantén la arquitectura básica (MVVM con Retrofit).

**P: ¿Qué hago si el API no responde?**  
R: Verifica que Docker esté corriendo: `docker-compose up` en la carpeta `api-fa-01`.

**P: ¿Puedo usar librerías adicionales?**  
R: Sí, siempre que las justifiques en tu documento de entrega.

**P: ¿Es necesario implementar validaciones?**  
R: Es recomendable validar que el precio sea un número válido y que el nombre no esté vacío.

---

## 📞 Soporte

Si tienes dudas durante el desarrollo:
- Revisa la documentación del API en: http://localhost:8000/docs
- Consulta el código ya implementado como referencia
- Usa `println()` o el debugger para entender el flujo de datos

¡Éxito con tu ejercicio! 🎯
