@id tema7
@numero 7
@titulo Componentes avanzados
@resumen Listas con LazyColumn y LazyRow, DropdownMenu, listas con estado (mutableStateListOf), Scaffold (TopAppBar, FAB, BottomAppBar) y State Hoisting.

## 1. Listas: LazyColumn y LazyRow

Las listas muestran **muchos elementos ordenados** (productos, películas, contactos…). Compose tiene dos componentes:

- **LazyColumn**: lista **vertical** con scroll.
- **LazyRow**: lista **horizontal** con scroll.

**Lazy** = "perezoso": solo crea los elementos **visibles** en pantalla y los va cargando **bajo demanda** al hacer scroll → menos memoria y mejor rendimiento con muchos datos.

| Parámetro | LazyColumn | LazyRow |
|---|---|---|
| `modifier` | tamaño, fondo, márgenes | igual |
| `contentPadding` | espacio alrededor de todo el contenido | igual |
| Alineación | `horizontalAlignment` (`Start`, `CenterHorizontally`, `End`) | `verticalAlignment` (`Top`, `CenterVertically`, `Bottom`) |
| Separación | `verticalArrangement = Arrangement.spacedBy(8.dp)` | `horizontalArrangement = Arrangement.spacedBy(8.dp)` |

### items e item

- **`items(lista) { elemento -> … }`**: recorre una lista y genera un componente por cada elemento.
- **`item { … }`**: añade **un único** elemento en una posición (un título, un espacio…).

```kotlin
val frutas = listOf("Manzana", "Pera", "Plátano", "Naranja", "Kiwi")

LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    item {
        Text("Mis frutas", style = MaterialTheme.typography.headlineMedium)
    }
    items(frutas) { fruta ->
        Text(fruta, style = MaterialTheme.typography.bodyLarge)
    }
}
```

```kotlin
val colores = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    items(colores) { color ->
        Box(modifier = Modifier.size(100.dp).background(color))
    }
}
```

> Importa `androidx.compose.foundation.lazy.items` para poder pasar la lista a `items(...)`. Si necesitas la posición usa `itemsIndexed(lista) { index, elemento -> }`.

:::ejercicio Ejercicio 01 · Listado de series con LazyColumn
Usa **LazyColumn** para mostrar un listado con **nombres de series de televisión**.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio1t7/MainActivity.kt
package com.example.ejercicio1t7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio1t7.ui.theme.Ejercicio1T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio1T7Theme {
                ListadoSeries()
            }
        }
    }
}

@Composable
fun ListadoSeries() {
    val series = listOf(
        "Breaking Bad", "Juego de Tronos", "Stranger Things", "La Casa de Papel",
        "The Office", "Friends", "Los Soprano", "The Mandalorian", "Dark",
        "Peaky Blinders", "The Last of Us", "El Ministerio del Tiempo",
        "Better Call Saul", "The Crown", "Arcane"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                stringResource(R.string.titulo),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
        itemsIndexed(series) { index, serie ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${index + 1}. $serie",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio1T7</string>
    <string name="titulo">Mis series favoritas</string>
</resources>
```
:::

## 2. DropdownMenu

Menú **desplegable** que aparece al pulsar un botón o icono. No se muestra solo: depende de un **estado** (abierto/cerrado) controlado desde fuera. Útil para elegir una opción sin ocupar espacio fijo.

- **DropdownMenu**: contenedor de las opciones.
- **DropdownMenuItem**: cada opción.

| DropdownMenu | DropdownMenuItem |
|---|---|
| `expanded`: si está abierto | `text`: contenido (un `Text`) |
| `onDismissRequest`: al pedir cerrarlo (clic fuera) | `onClick`: al elegir la opción |
| `modifier` | `leadingIcon` / `trailingIcon` (opcionales) |
| `offset`: desplazar su posición | |

```kotlin
var expandido by remember { mutableStateOf(false) }
var opcion by remember { mutableStateOf("Ninguna") }
val opciones = listOf("Rojo", "Verde", "Azul")

Box {                                            // el Box sirve de "ancla" del menú
    Button(onClick = { expandido = true }) {
        Text("Elegir color")
    }
    DropdownMenu(
        expanded = expandido,
        onDismissRequest = { expandido = false }
    ) {
        opciones.forEach { color ->
            DropdownMenuItem(
                text = { Text(color) },
                onClick = {
                    opcion = color
                    expandido = false            // cerramos al elegir
                }
            )
        }
    }
}
Text("Seleccionado: $opcion")
```

:::ejercicio Ejercicio 02 · Titulaciones de FP (DropdownMenu)
App para elegir una **titulación** de la oferta de FP del **IES Valle del Jerte**. Un botón abre el menú; al elegir, se actualiza un texto con la opción. Al principio: **"No se ha seleccionado titulación"**.
---solucion---
> La lista de titulaciones es un ejemplo: cámbiala por la oferta real del centro.

```kotlin file=app/src/main/java/com/example/ejercicio2t7/MainActivity.kt
package com.example.ejercicio2t7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejercicio2t7.ui.theme.Ejercicio2T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio2T7Theme {
                SelectorTitulacion()
            }
        }
    }
}

@Composable
fun SelectorTitulacion() {
    val titulaciones = listOf(
        "CFGB Informática y Comunicaciones",
        "CFGM Sistemas Microinformáticos y Redes",
        "CFGM Gestión Administrativa",
        "CFGS Desarrollo de Aplicaciones Multiplataforma",
        "CFGS Desarrollo de Aplicaciones Web",
        "CFGS Administración de Sistemas Informáticos en Red",
        "CFGS Administración y Finanzas"
    )
    val sinSeleccion = stringResource(R.string.sin_seleccion)
    var expandido by remember { mutableStateOf(false) }
    var seleccionada by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        Text(
            stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Box {
            Button(onClick = { expandido = true }) {
                Text(stringResource(R.string.boton))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                titulaciones.forEach { titulacion ->
                    DropdownMenuItem(
                        text = { Text(titulacion) },
                        onClick = {
                            seleccionada = titulacion
                            expandido = false
                        },
                        trailingIcon = {
                            if (titulacion == seleccionada) Icon(Icons.Filled.Check, contentDescription = null)
                        }
                    )
                }
            }
        }

        Text(
            text = if (seleccionada.isEmpty()) sinSeleccion else "Has seleccionado:\n$seleccionada",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio2T7</string>
    <string name="titulo">Oferta formativa FP\nIES Valle del Jerte</string>
    <string name="boton">Seleccionar titulación</string>
    <string name="sin_seleccion">No se ha seleccionado titulación</string>
</resources>
```
:::

## 3. Estados con listas: mutableStateListOf

Para una lista que **cambia durante la ejecución** usamos **`mutableStateListOf`**: una lista **observable** que avisa a Compose al **añadir, eliminar o modificar** elementos, y la UI se actualiza sola.

- Se declara con **`remember`** (**nunca** con `rememberSaveable`).
- Funciona con `LazyColumn` y `LazyRow`.

| Operación | Qué hace |
|---|---|
| `add(elemento)` | Añade al final |
| `add(posición, elemento)` | Inserta en la posición |
| `remove(elemento)` | Elimina la primera ocurrencia |
| `removeAt(posición)` | Elimina la de esa posición |
| `set(posición, elemento)` | Reemplaza |
| `clear()` | Vacía la lista |

```kotlin
val tareas = remember { mutableStateListOf("Estudiar Kotlin", "Hacer la compra") }
var nueva by remember { mutableStateOf("") }

Column {
    Row {
        OutlinedTextField(value = nueva, onValueChange = { nueva = it }, modifier = Modifier.weight(1f))
        Button(onClick = { if (nueva.isNotBlank()) { tareas.add(nueva); nueva = "" } }) { Text("Añadir") }
    }
    LazyColumn {
        items(tareas) { tarea ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(tarea, modifier = Modifier.weight(1f))
                IconButton(onClick = { tareas.remove(tarea) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                }
            }
        }
    }
}
```

:::ejercicio Ejercicio 03 · Ciudades de España (mutableStateListOf)
Gestiona una **lista de ciudades** con `mutableStateListOf`: título, campo de texto, botón **Añadir Ciudad** y el listado, donde cada ciudad se puede **eliminar con un botón de icono**. Al añadir, valida que haya nombre (si no, error); añade y muestra un **Toast**. Al borrar, elimina y muestra Toast. La funcionalidad de cada botón en una **función**.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio3t7/MainActivity.kt
package com.example.ejercicio3t7

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio3t7.ui.theme.Ejercicio3T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio3T7Theme {
                GestorCiudades()
            }
        }
    }
}

/** Botón "Añadir Ciudad". Devuelve true si se ha añadido (para limpiar la caja de texto). */
fun anadirCiudad(context: Context, ciudades: SnapshotStateList<String>, nombre: String): Boolean {
    val ciudad = nombre.trim()
    if (ciudad.isEmpty()) {
        Toast.makeText(context, "Introduce el nombre de una ciudad", Toast.LENGTH_SHORT).show()
        return false
    }
    ciudades.add(ciudad)
    Toast.makeText(context, "$ciudad añadida a la lista", Toast.LENGTH_SHORT).show()
    return true
}

/** Botón de icono "borrar" */
fun eliminarCiudad(context: Context, ciudades: SnapshotStateList<String>, ciudad: String) {
    ciudades.remove(ciudad)
    Toast.makeText(context, "$ciudad eliminada de la lista", Toast.LENGTH_SHORT).show()
}

@Composable
fun GestorCiudades() {
    val context = LocalContext.current
    val ciudades = remember { mutableStateListOf("Madrid", "Plasencia", "Sevilla", "Valencia") }
    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { if (anadirCiudad(context, ciudades, nombre)) nombre = "" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.anadir))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(ciudades) { ciudad ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(ciudad, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f).padding(start = 8.dp))
                        IconButton(onClick = { eliminarCiudad(context, ciudades, ciudad) }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.borrar), tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio3T7</string>
    <string name="titulo">Ciudades de España</string>
    <string name="label">Nombre de la ciudad</string>
    <string name="anadir">Añadir Ciudad</string>
    <string name="borrar">Borrar ciudad</string>
</resources>
```
:::

## 4. Scaffold

**Scaffold** ("andamio") es un contenedor que da la **estructura básica** de una pantalla y coloca cada zona en su sitio automáticamente:

- **TopAppBar**: barra superior de acciones.
- **BottomAppBar**: barra inferior de acciones.
- **NavigationBar**: barra inferior de navegación.
- **FloatingActionButton**: botón flotante.
- **Snackbar**: notificaciones temporales.
- **NavigationDrawer**: menú lateral (no va dentro del Scaffold, sino que **lo envuelve**).

| Parámetro | Para qué |
|---|---|
| `topBar` | Barra superior |
| `bottomBar` | Barra inferior |
| `floatingActionButton` | Botón flotante |
| `snackbarHost` | Contenedor del Snackbar |
| `containerColor` / `contentColor` | Colores |
| `content` | **Obligatorio**. Recibe `paddingValues`: el espacio a respetar para que el contenido no quede tapado por las barras |

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi app") }) },
        bottomBar = { BottomAppBar { Text("Barra inferior", modifier = Modifier.padding(16.dp)) } },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) { Icon(Icons.Filled.Add, contentDescription = "Añadir") }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {   // ¡aplicar el padding!
            Text("Contenido principal")
        }
    }
}
```

### TopAppBar

Barra superior con el **título** y acciones (buscar, ajustes, volver…). Requiere `@OptIn(ExperimentalMaterial3Api::class)`.

| Parámetro | Para qué |
|---|---|
| `title` | Título (normalmente un `Text`) |
| `navigationIcon` | Icono de la izquierda (menú, atrás) |
| `actions` | Iconos/botones de la derecha |
| `colors` | `TopAppBarDefaults.topAppBarColors(containerColor = …, titleContentColor = …)` |

```kotlin
TopAppBar(
    title = { Text("Ajustes") },
    navigationIcon = {
        IconButton(onClick = { }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
    },
    actions = {
        IconButton(onClick = { }) { Icon(Icons.Filled.Search, contentDescription = "Buscar") }
        IconButton(onClick = { }) { Icon(Icons.Filled.MoreVert, contentDescription = "Más") }
    },
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
)
```

### FloatingActionButton

Botón circular que **flota** sobre el contenido (normalmente abajo a la derecha) para la **acción principal** de la pantalla.

- Variantes: `FloatingActionButton`, `SmallFloatingActionButton`, `LargeFloatingActionButton`, `ExtendedFloatingActionButton` (icono + texto).
- Parámetros: `onClick`, `modifier`, `containerColor`, `contentColor`, `shape` (`CircleShape`), `elevation` (`FloatingActionButtonDefaults.elevation()`), `content`.

```kotlin
FloatingActionButton(
    onClick = { },
    containerColor = MaterialTheme.colorScheme.tertiary,
    contentColor = Color.White,
    shape = CircleShape,
    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
) {
    Icon(Icons.Filled.Add, contentDescription = "Añadir")
}

ExtendedFloatingActionButton(
    onClick = { },
    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
    text = { Text("Escribir") }
)
```

### BottomAppBar

Barra **inferior** de acciones (buscar, compartir, editar…).

- Parámetros: `modifier`, `containerColor`, `contentColor`, `actions` (iconos a la izquierda), `content` (usa `Row` para distribuirlos a tu gusto).

```kotlin
BottomAppBar(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    actions = {
        IconButton(onClick = { }) { Icon(Icons.Filled.Home, contentDescription = "Inicio") }
        IconButton(onClick = { }) { Icon(Icons.Filled.Share, contentDescription = "Compartir") }
        IconButton(onClick = { }) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
    }
)
```

:::ejercicio Ejercicio 04 · Mi receta favorita (Scaffold)
App de recetas que muestra la **imagen de un plato**. Con **Scaffold**:

- **TopAppBar** con el título "Mi receta favorita", icono de **atrás** a la izquierda (Toast "Volviendo al recetario") e icono de **favoritos** a la derecha (Toast "Receta añadida a favoritos").
- **FloatingActionButton** con icono de **información** (Toast "Ingredientes de la receta").
---solucion---
Copia la foto del plato como `res/drawable/receta.jpg`.

```kotlin file=app/src/main/java/com/example/ejercicio4t7/MainActivity.kt
package com.example.ejercicio4t7

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ejercicio4t7.ui.theme.Ejercicio4T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio4T7Theme {
                PantallaReceta()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReceta() {
    val context = LocalContext.current
    val msgVolver = stringResource(R.string.volviendo)
    val msgFavorito = stringResource(R.string.favorito)
    val msgInfo = stringResource(R.string.ingredientes)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo)) },
                navigationIcon = {
                    IconButton(onClick = { Toast.makeText(context, msgVolver, Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.atras))
                    }
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, msgFavorito, Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favoritos))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { Toast.makeText(context, msgInfo, Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.info))
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.receta),
                contentDescription = stringResource(R.string.desc_plato),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio4T7</string>
    <string name="titulo">Mi receta favorita</string>
    <string name="atras">Atrás</string>
    <string name="favoritos">Favoritos</string>
    <string name="info">Información</string>
    <string name="desc_plato">Plato de comida</string>
    <string name="volviendo">Volviendo al recetario</string>
    <string name="favorito">Receta añadida a favoritos</string>
    <string name="ingredientes">Ingredientes de la receta</string>
</resources>
```
:::

## 5. State Hoisting

**State Hoisting** ("elevación del estado") es un patrón que consiste en **subir el estado** de un componente al Composable que lo contiene (el "padre"). Así el padre controla el valor y puede compartirlo; el componente hijo queda **reutilizable** y **sin estado** (*stateless*).

Un Composable con State Hoisting:

1. **No guarda el estado** internamente: lo **recibe por parámetro**.
2. Expone un **callback** (una función por parámetro) para **avisar al padre** de que quiere cambiarlo.

```kotlin
// Componente de nivel inferior ("Hijo"): sin estado, reutilizable
@Composable
fun CampoNombre(nombre: String, onNombreChange: (String) -> Unit) {
    OutlinedTextField(
        value = nombre,                     // recibe el estado
        onValueChange = onNombreChange,     // avisa del cambio
        label = { Text("Nombre") }
    )
}

// Componente de nivel superior ("Padre"): dueño del estado
@Composable
fun PantallaSaludo() {
    var nombre by rememberSaveable { mutableStateOf("") }
    Column {
        CampoNombre(nombre = nombre, onNombreChange = { nombre = it })
        Text("Hola, $nombre")               // el padre puede compartir el estado
    }
}
```

```text
        estado (valor) ↓
 ┌───────── Padre ─────────┐
 │  var nombre by …        │
 └─────────────────────────┘
        ↑ evento (callback)
 ┌───────── Hijo ──────────┐
 │  CampoNombre(nombre,    │
 │    onNombreChange)      │
 └─────────────────────────┘
```

:::ejercicio Ejercicio 05 · Contador de clics con State Hoisting
Contador de clics: título, número de clics y dos botones (**Incrementar** +1 y **Decrementar** −1), empezando en 0. Aplica **State Hoisting** reutilizando **un único componente Button** que reciba el **texto** y la **función** que modifica el estado.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio5t7/MainActivity.kt
package com.example.ejercicio5t7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejercicio5t7.ui.theme.Ejercicio5T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio5T7Theme {
                ContadorClics()
            }
        }
    }
}

// Componente PADRE: dueño del estado
@Composable
fun ContadorClics() {
    var clics by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)
        Text("$clics", fontSize = 80.sp, color = MaterialTheme.colorScheme.primary)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Reutilizamos el MISMO componente con distinto texto y distinta acción
            BotonContador(texto = stringResource(R.string.decrementar), onClick = { clics-- })
            BotonContador(texto = stringResource(R.string.incrementar), onClick = { clics++ })
        }
    }
}

// Componente HIJO: sin estado. Recibe el texto y la función que modifica el estado.
@Composable
fun BotonContador(texto: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(texto)
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio5T7</string>
    <string name="titulo">Contador de clics</string>
    <string name="incrementar">Incrementar</string>
    <string name="decrementar">Decrementar</string>
</resources>
```
:::

## 6. Ejercicios de consolidación

:::ejercicio Ejercicio 06 · Películas de superhéroes (LazyColumn + Card)
Muestra un listado de **películas de superhéroes** con `LazyColumn` y `Card`. Cada tarjeta: **cartel**, **título** y **año**. Pista: lista **inmutable** de objetos de una data class con `titulo` (String), `anio` (Int) e `imagen` (Int).
---solucion---
:::tree
app/src/main/
├── java/com/example/ejercicio6t7/
│   ├── MainActivity.kt
│   └── Pelicula.kt
└── res/drawable/
    ├── vengadores.jpg
    ├── batman.jpg
    ├── spiderman.jpg
    ├── wonder_woman.jpg
    ├── black_panther.jpg
    └── iron_man.jpg
:::

```kotlin file=app/src/main/java/com/example/ejercicio6t7/Pelicula.kt
package com.example.ejercicio6t7

import androidx.annotation.DrawableRes

data class Pelicula(
    val titulo: String,
    val anio: Int,
    @DrawableRes val imagen: Int
)

// Lista inmutable con los datos
val peliculas: List<Pelicula> = listOf(
    Pelicula("Iron Man", 2008, R.drawable.iron_man),
    Pelicula("El caballero oscuro", 2008, R.drawable.batman),
    Pelicula("Los Vengadores", 2012, R.drawable.vengadores),
    Pelicula("Wonder Woman", 2017, R.drawable.wonder_woman),
    Pelicula("Black Panther", 2018, R.drawable.black_panther),
    Pelicula("Spider-Man: No Way Home", 2021, R.drawable.spiderman)
)
```
```kotlin file=app/src/main/java/com/example/ejercicio6t7/MainActivity.kt
package com.example.ejercicio6t7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio6t7.ui.theme.Ejercicio6T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio6T7Theme {
                ListadoPeliculas(peliculas)
            }
        }
    }
}

@Composable
fun ListadoPeliculas(lista: List<Pelicula>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)
        }
        items(lista) { pelicula ->
            TarjetaPelicula(pelicula)
        }
    }
}

@Composable
fun TarjetaPelicula(pelicula: Pelicula) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = pelicula.imagen),
                contentDescription = pelicula.titulo,
                modifier = Modifier.width(100.dp).height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(pelicula.titulo, style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(R.string.estreno, pelicula.anio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio6T7</string>
    <string name="titulo">Películas de superhéroes</string>
    <string name="estreno">Estreno: %1$d</string>
</resources>
```
> `%1$d` es un marcador para un **número entero**; `%1$s` sería para un texto.
:::

:::ejercicio Ejercicio 07 · Elige un deporte (DropdownMenu + Card)
Con un **DropdownMenu** elige un deporte: Fútbol, Baloncesto, Tenis, Natación, Atletismo y Ciclismo (cada opción con **icono y nombre**). Botón **"Seleccionar deporte"** para abrir el menú. Al elegir, el menú se cierra y aparece un **Card** con icono, nombre y descripción. Al principio: **"Todavía no has seleccionado ningún deporte"**. Los deportes en una lista de objetos de la clase **Deporte** (`nombre`, `descripcion`, `icono: ImageVector`).
---solucion---
Los iconos de deportes están en **material-icons-extended**:

```kotlin file=app/build.gradle.kts
dependencies {
    // ...
    implementation("androidx.compose.material:material-icons-extended")
}
```
```kotlin file=app/src/main/java/com/example/ejercicio7t7/Deporte.kt
package com.example.ejercicio7t7

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.graphics.vector.ImageVector

data class Deporte(
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector
)

val deportes = listOf(
    Deporte("Fútbol", "Dos equipos de 11 jugadores intentan meter el balón en la portería contraria.", Icons.Filled.SportsSoccer),
    Deporte("Baloncesto", "Dos equipos de 5 jugadores encestan el balón en el aro rival.", Icons.Filled.SportsBasketball),
    Deporte("Tenis", "Con una raqueta se golpea la pelota por encima de la red hacia el campo contrario.", Icons.Filled.SportsTennis),
    Deporte("Natación", "Desplazarse en el agua lo más rápido posible usando distintos estilos.", Icons.Filled.Pool),
    Deporte("Atletismo", "Carreras, saltos y lanzamientos: el deporte más antiguo.", Icons.AutoMirrored.Filled.DirectionsRun),
    Deporte("Ciclismo", "Recorrer distancias en bicicleta, en carretera, montaña o pista.", Icons.AutoMirrored.Filled.DirectionsBike)
)
```
```kotlin file=app/src/main/java/com/example/ejercicio7t7/MainActivity.kt
package com.example.ejercicio7t7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejercicio7t7.ui.theme.Ejercicio7T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio7T7Theme {
                SelectorDeporte()
            }
        }
    }
}

@Composable
fun SelectorDeporte() {
    var expandido by remember { mutableStateOf(false) }
    var seleccionado by remember { mutableStateOf<Deporte?>(null) }   // null = ninguno

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        Box {
            Button(onClick = { expandido = true }) {
                Text(stringResource(R.string.boton))
            }
            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                deportes.forEach { deporte ->
                    DropdownMenuItem(
                        text = { Text(deporte.nombre) },
                        leadingIcon = { Icon(deporte.icono, contentDescription = null) },
                        onClick = {
                            seleccionado = deporte
                            expandido = false
                        }
                    )
                }
            }
        }

        seleccionado?.let { deporte ->
            TarjetaDeporte(deporte)
        } ?: Text(
            stringResource(R.string.sin_seleccion),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TarjetaDeporte(deporte: Deporte) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = deporte.icono,
                contentDescription = deporte.nombre,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(deporte.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(deporte.descripcion, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio7T7</string>
    <string name="boton">Seleccionar deporte</string>
    <string name="sin_seleccion">Todavía no has seleccionado ningún deporte</string>
</resources>
```
> Si tu versión de la librería no tiene `Icons.AutoMirrored.Filled.DirectionsRun/DirectionsBike`, usa `Icons.Filled.DirectionsRun` y `Icons.Filled.DirectionsBike`.
:::

:::ejercicio Ejercicio 08 · Lista de actividades (Scaffold + mutableStateListOf)
Gestiona una lista de **actividades** con `mutableStateListOf`; cada una con su **nombre** y un **Checkbox** para marcarla como **favorita**. Con **Scaffold**:

- **TopAppBar** con título, icono **Favoritas** (Toast con las favoritas) e icono **Información** (Toast con el nº total).
- **FloatingActionButton** que **elimina todas** las actividades.
- Contenido: `OutlinedTextField` + `IconButton` para añadir, y el listado con `LazyColumn`.

Lista inicial: "Ir al cine", "Salir en bicicleta", "Hacer senderismo", "Leer un libro" y "Jugar a videojuegos". Cada funcionalidad en una **función**, validando que la lista no esté vacía y que haya nombre al añadir, con un **Toast** de feedback en cada operación.
---solucion---
Como cada actividad tiene **dos datos** (nombre y favorita) usamos una **data class**. Para cambiar el Checkbox reemplazamos el elemento con `set(posición, actividad.copy(favorita = …))`, así `mutableStateListOf` detecta el cambio.

```kotlin file=app/src/main/java/com/example/ejercicio8t7/Actividad.kt
package com.example.ejercicio8t7

data class Actividad(
    val nombre: String,
    val favorita: Boolean = false
)
```
```kotlin file=app/src/main/java/com/example/ejercicio8t7/MainActivity.kt
package com.example.ejercicio8t7

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio8t7.ui.theme.Ejercicio8T7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio8T7Theme {
                PantallaActividades()
            }
        }
    }
}

private fun toast(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

/* ------------------------- FUNCIONALIDADES ------------------------- */

fun mostrarFavoritas(context: Context, lista: List<Actividad>) {
    if (lista.isEmpty()) return toast(context, "La lista de actividades está vacía")
    val favoritas = lista.filter { it.favorita }.map { it.nombre }
    if (favoritas.isEmpty()) toast(context, "No hay actividades favoritas")
    else toast(context, "Favoritas: " + favoritas.joinToString(", "))
}

fun mostrarInformacion(context: Context, lista: List<Actividad>) {
    if (lista.isEmpty()) return toast(context, "La lista de actividades está vacía")
    toast(context, "Hay ${lista.size} actividades en la lista")
}

/** Devuelve true si se ha añadido (para limpiar la caja de texto) */
fun anadirActividad(context: Context, lista: SnapshotStateList<Actividad>, nombre: String): Boolean {
    if (nombre.isBlank()) {
        toast(context, "Por favor, introduzca el nombre de una actividad")
        return false
    }
    lista.add(Actividad(nombre.trim()))
    toast(context, "La actividad se ha añadido correctamente")
    return true
}

fun eliminarTodas(context: Context, lista: SnapshotStateList<Actividad>) {
    if (lista.isEmpty()) return toast(context, "La lista ya está vacía")
    lista.clear()
    toast(context, "Se han eliminado todas las actividades de la lista")
}

fun cambiarFavorita(lista: SnapshotStateList<Actividad>, posicion: Int, favorita: Boolean) {
    lista[posicion] = lista[posicion].copy(favorita = favorita)    // equivalente a lista.set(posicion, …)
}

/* ------------------------------ UI ------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaActividades() {
    val context = LocalContext.current
    val actividades = remember {
        mutableStateListOf(
            Actividad("Ir al cine"),
            Actividad("Salir en bicicleta"),
            Actividad("Hacer senderismo"),
            Actividad("Leer un libro"),
            Actividad("Jugar a videojuegos")
        )
    }
    var nueva by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo)) },
                actions = {
                    IconButton(onClick = { mostrarFavoritas(context, actividades) }) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favoritas))
                    }
                    IconButton(onClick = { mostrarInformacion(context, actividades) }) {
                        Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.informacion))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eliminarTodas(context, actividades) },
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.eliminar_todas))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = nueva,
                    onValueChange = { nueva = it },
                    label = { Text(stringResource(R.string.nueva)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { if (anadirActividad(context, actividades, nueva)) nueva = "" }) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = stringResource(R.string.anadir),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(actividades) { index, actividad ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(actividad.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                            Checkbox(
                                checked = actividad.favorita,
                                onCheckedChange = { cambiarFavorita(actividades, index, it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio8T7</string>
    <string name="titulo">Mis actividades</string>
    <string name="favoritas">Favoritas</string>
    <string name="informacion">Información</string>
    <string name="eliminar_todas">Eliminar todas</string>
    <string name="nueva">Nueva actividad</string>
    <string name="anadir">Añadir actividad</string>
</resources>
```
:::
