

# Proyecto RetrofitExample

Este proyecto es una aplicación Android desarrollada en Kotlin que utiliza Jetpack Compose para la interfaz de usuario y Retrofit2 para consumir una API REST pública. En concreto, consume la API pública JSONPlaceholder para mostrar una lista de posts.

---

## Estructura General del Proyecto

- **MainActivity.kt**: Actividad principal que contiene la UI con Jetpack Compose y conecta con el ViewModel.
- **MainViewModel.kt**: ViewModel que maneja la lógica de negocio y la obtención de datos desde la API.
- **ApiService.kt**: Interfaz que define los endpoints de la API usando Retrofit2.
- **Post.kt**: Modelo de datos que representa un post.
- **AndroidManifest.xml**: Configuración del manifiesto, permisos y declaración de la actividad.
- **build.gradle.kts**: Archivo de configuración con las dependencias necesarias.

---

## ¿Qué es Retrofit2?

Retrofit2 es una librería cliente HTTP para Android y Java que facilita la comunicación con APIs REST. Permite definir interfaces para los endpoints, manejar la serialización/deserialización automática de JSON a objetos Kotlin/Java, y soporta integración con corutinas para llamadas asíncronas.

---

## Cómo funciona Retrofit en este proyecto

1. **Definición de la API**

   En `ApiService.kt` definimos una interfaz con los endpoints que queremos consumir. Por ejemplo:

   ```kotlin
   interface ApiService {
       @GET("posts")
       suspend fun getPosts(): List<Post>
   }
   ```

   Aquí, `@GET("posts")` indica que se hará una petición HTTP GET a la ruta `/posts` de la base URL.

2. **Creación de la instancia Retrofit**

   Retrofit se configura con la base URL de la API y un convertidor JSON (Gson):

   ```kotlin
   val retrofit = Retrofit.Builder()
       .baseUrl("https://jsonplaceholder.typicode.com/")
       .addConverterFactory(GsonConverterFactory.create())
       .build()

   val apiService = retrofit.create(ApiService::class.java)
   ```

3. **Llamada a la API**

   En el ViewModel, usamos corutinas para llamar a la función suspend `getPosts()` de forma asíncrona:

   ```kotlin
   viewModelScope.launch {
       val posts = apiService.getPosts()
       _posts.value = posts
   }
   ```

4. **Actualización de la UI**

   La lista de posts obtenida se expone como un `StateFlow` que la UI observa con `collectAsState()`. Cuando los datos cambian, Compose vuelve a renderizar la lista.

---

## Detalles importantes del proyecto

### Jetpack Compose

- UI declarativa y reactiva.
- `LazyColumn` para listas eficientes.
- Uso de `@Composable` para funciones UI.
- `viewModel()` para obtener el ViewModel dentro de Compose.

### ViewModel y Corutinas

- `MainViewModel` usa `viewModelScope` para lanzar corutinas.
- Maneja la obtención de datos y estado.
- Expone datos con `StateFlow` para que Compose los observe.

### Retrofit2

- Simplifica las llamadas HTTP.
- Usa anotaciones para definir endpoints.
- Convierte JSON automáticamente a objetos Kotlin.
- Soporta corutinas para llamadas asíncronas limpias.

### Permisos y Configuración

- Permiso de internet en `AndroidManifest.xml`.
- Dependencias necesarias en `build.gradle.kts` para Retrofit, Gson, Compose, Lifecycle, etc.

---

## Flujo de ejecución

1. La app inicia y `MainActivity` se crea.
2. `MainActivity` obtiene el `MainViewModel`.
3. `MainViewModel` lanza una corutina para llamar a `apiService.getPosts()`.
4. Retrofit hace la petición HTTP GET a `https://jsonplaceholder.typicode.com/posts`.
5. La respuesta JSON se convierte en lista de objetos `Post`.
6. El ViewModel actualiza el `StateFlow` con la lista.
7. La UI observa el `StateFlow` y muestra la lista de posts en pantalla.

---

## Ventajas de usar Retrofit2

- Código limpio y declarativo para llamadas HTTP.
- Manejo automático de JSON con Gson.
- Integración sencilla con corutinas.
- Fácil de extender para autenticación, interceptores, etc.

---

## Código clave resumido

### ApiService.kt

```kotlin
interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    companion object {
        operator fun invoke(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
```

### MainViewModel.kt

```kotlin
class MainViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val apiService = ApiService()

    init {
        viewModelScope.launch {
            try {
                _posts.value = apiService.getPosts()
            } catch (e: Exception) {
                // Manejo de errores
            }
        }
    }
}
```

### MainActivity.kt (UI)

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val posts = viewModel.posts.collectAsState().value

            PostList(posts = posts)
        }
    }
}
```

---
