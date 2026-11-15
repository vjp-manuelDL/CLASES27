@id tema6
@numero 6
@titulo Componentes básicos
@resumen Text, Image, Icon, Spacer, HorizontalDivider, Button, Toast, Card, reutilización, estados (remember), TextField, RadioButton, Checkbox y Switch.

## 1. Text

`Text` muestra texto en pantalla: títulos, subtítulos, párrafos, etiquetas…

> 📌 **Buena práctica:** en los fragmentos cortos de los apuntes el texto se escribe directamente para que se lea mejor, pero en los **ejercicios** (y en tus prácticas) **todos los textos van en `res/values/strings.xml`** y se usan con `stringResource()` o `context.getString()`. Lo tienes resumido en *Tema 4 › 4. Acceso a recursos*.

| Parámetro | Para qué |
|---|---|
| `text` | Contenido |
| `modifier` | Tamaño, fondo, posición… |
| `color` | Color del texto |
| `fontSize` | Tamaño (`20.sp`) |
| `fontWeight` | Grosor: `FontWeight.Bold`, `Normal`, `Light`… |
| `fontStyle` | `FontStyle.Normal` o `FontStyle.Italic` |
| `textAlign` | `TextAlign.Center`, `TextAlign.Start`… |
| `maxLines` | Nº máximo de líneas |
| `style` | Un `TextStyle` (p. ej. `MaterialTheme.typography.titleLarge`) |

```kotlin
Text(
    text = "Kotlin mola",
    modifier = Modifier.fillMaxWidth().padding(8.dp),
    color = Color(0xFF7F52FF),
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    fontStyle = FontStyle.Italic,
    textAlign = TextAlign.Center,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis      // pone "…" si no cabe
)
```

## 2. Image e Icon

### Image

Muestra imágenes locales (`res/drawable`: `.png`, `.jpg`, vectores `.xml`) o remotas.

| Parámetro | Para qué |
|---|---|
| `painter` | La imagen (`painterResource(R.drawable.foto)`) |
| `contentDescription` | Descripción para accesibilidad |
| `modifier` | Tamaño, posición, forma (`.clip(CircleShape)`) |
| `contentScale` | `ContentScale.Fit`, `Crop`, `Inside`… |
| `alignment` | `Alignment.Center`, `TopStart`… |

### Icon

Muestra un **icono vectorial** (navegación, favoritos, buscar, ajustes, borrar…).

| Parámetro | Para qué |
|---|---|
| `imageVector` | Vector del icono (`Icons.Filled.Favorite`) |
| `painter` | Alternativa: un recurso gráfico |
| `contentDescription` | Accesibilidad |
| `modifier` | Tamaño (`.size(32.dp)`) |
| `tint` | Color |

```kotlin
Image(
    painter = painterResource(id = R.drawable.perfil),
    contentDescription = "Foto de perfil",
    modifier = Modifier.size(120.dp).clip(CircleShape),
    contentScale = ContentScale.Crop
)

Icon(
    imageVector = Icons.Filled.Favorite,
    contentDescription = "Favorito",
    tint = Color.Red,
    modifier = Modifier.size(32.dp)
)
```

> Los iconos básicos (`Favorite`, `Home`, `Info`, `Settings`, `Star`, `Call`, `Delete`, `Search`…) vienen incluidos. Para iconos extra (`DirectionsCar`, `SportsSoccer`…) añade en `app/build.gradle.kts` la dependencia `implementation("androidx.compose.material:material-icons-extended")`.

## 3. Spacer y HorizontalDivider

- **Spacer**: espacio **vacío** entre componentes (en `Row`, `Column` o `Box`) usando `height`, `width`, `weight`, `fillMaxSize`. No muestra nada; solo separa.
- **HorizontalDivider**: dibuja una **línea horizontal** para separar secciones. Parámetros: `modifier`, `thickness`, `color`.

```kotlin
Column {
    Text("Arriba")
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(thickness = 2.dp, color = Color.Gray)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Abajo")
}
```

:::ejercicio Ejercicio 01 · Ficha técnica de un móvil
Con `Text`, `Image`, `Icon`, `Spacer` y `HorizontalDivider`, diseña una interfaz que muestre la **ficha técnica resumida de un dispositivo móvil**.
---solucion---
Copia una foto del móvil como `res/drawable/movil.png`.

```kotlin file=app/src/main/java/com/example/ejercicio1t6/MainActivity.kt
package com.example.ejercicio1t6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejercicio1t6.ui.theme.Ejercicio1T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio1T6Theme {
                FichaTecnica()
            }
        }
    }
}

@Composable
fun FichaTecnica() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.modelo),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.movil),
            contentDescription = stringResource(R.string.desc_movil),
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        FilaDato(Icons.Filled.Phone, stringResource(R.string.pantalla))
        FilaDato(Icons.Filled.Settings, stringResource(R.string.procesador))
        FilaDato(Icons.Filled.Build, stringResource(R.string.memoria))
        FilaDato(Icons.Filled.Star, stringResource(R.string.camara))
        FilaDato(Icons.Filled.Lock, stringResource(R.string.bateria))
        FilaDato(Icons.Filled.Info, stringResource(R.string.sistema))

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun FilaDato(icono: ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = texto, style = MaterialTheme.typography.bodyLarge)
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio1T6</string>
    <string name="modelo">Pixel 9 Pro</string>
    <string name="desc_movil">Imagen del dispositivo</string>
    <string name="pantalla">Pantalla: 6,3\" OLED 120 Hz</string>
    <string name="procesador">Procesador: Google Tensor G4</string>
    <string name="memoria">Memoria: 16 GB RAM · 256 GB</string>
    <string name="camara">Cámara: 50 MP + 48 MP + 48 MP</string>
    <string name="bateria">Batería: 4700 mAh</string>
    <string name="sistema">Sistema: Android 15</string>
</resources>
```
> En `strings.xml` las comillas dobles se escriben `\"` y el apóstrofo `\'`.
:::

## 4. Button

Elemento interactivo que ejecuta una acción al pulsarlo. Variantes de Material 3:

| Variante | Aspecto |
|---|---|
| `Button` | Estándar, con fondo relleno |
| `OutlinedButton` | Solo contorno |
| `TextButton` | Solo texto |
| `IconButton` | Solo un icono |

| Parámetro | Para qué |
|---|---|
| `onClick` | Lambda `() -> Unit` que se ejecuta al pulsar |
| `modifier` | Tamaño, posición |
| `enabled` | `true` por defecto; con `false` aparece inactivo |
| `colors` | `ButtonDefaults.buttonColors()`, `ButtonDefaults.outlinedButtonColors()`, `IconButtonDefaults.iconButtonColors()` |
| `contentPadding` | Espaciado interior |
| `shape` | `RoundedCornerShape(8.dp)`, `CircleShape`… |
| `content` | Lo que va dentro (un `Text`, un `Icon`…) |

### Toast

Mensaje que aparece unos segundos y desaparece solo, sin recibir el foco. Se crea con `Toast.makeText(contexto, texto, duración).show()`:

- **Contexto**: `LocalContext.current` (se obtiene dentro de un Composable).
- **Texto**: un String o el ID de `strings.xml`.
- **Duración**: `Toast.LENGTH_SHORT` o `Toast.LENGTH_LONG`.

```kotlin
@Composable
fun Botones() {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            // El texto viene de strings.xml: <string name="mensaje_toast">Este es el mensaje del Toast</string>
            onClick = { Toast.makeText(context, R.string.mensaje_toast, Toast.LENGTH_SHORT).show() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF), contentColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Button")
        }
        OutlinedButton(onClick = { }) { Text("OutlinedButton") }
        TextButton(onClick = { }) { Text("TextButton") }
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Favorite, contentDescription = "Me gusta", tint = Color.Red)
        }
        Button(onClick = { }, enabled = false) { Text("Deshabilitado") }
    }
}
```

## 5. Card y ElevatedCard

- **Card**: contenedor que **agrupa información relacionada** con sombra y elevación. Puede contener texto, imágenes y otros Composables.
- **ElevatedCard**: variante con **más elevación**, para destacarla del fondo.

| Parámetro | Card | ElevatedCard |
|---|---|---|
| `colors` | `CardDefaults.cardColors()` | `CardDefaults.elevatedCardColors()` |
| `elevation` | `CardDefaults.cardElevation()` | `CardDefaults.elevatedCardElevation()` |
| `shape` | forma | forma |
| `border` | `BorderStroke(1.dp, Color.Gray)` | — |
| `modifier`, `content` | sí | sí |

```kotlin
Card(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7FF)),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    border = BorderStroke(1.dp, Color(0xFF7F52FF))
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Título de la tarjeta", style = MaterialTheme.typography.titleLarge)
        Text("Contenido de la tarjeta")
    }
}

ElevatedCard(
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
) {
    Text("Tarjeta elevada", modifier = Modifier.padding(16.dp))
}
```

:::ejercicio Ejercicio 02 · Tarjeta de videojuego
Muestra una **Card** con información de un videojuego: **imagen, título, categoría y breve descripción**, y un **Button** que muestra un **Toast** con información adicional.
---solucion---
Copia la carátula como `res/drawable/videojuego.jpg`.

```kotlin file=app/src/main/java/com/example/ejercicio2t6/MainActivity.kt
package com.example.ejercicio2t6

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio2t6.ui.theme.Ejercicio2T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio2T6Theme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TarjetaVideojuego()
                }
            }
        }
    }
}

@Composable
fun TarjetaVideojuego() {
    val context = LocalContext.current
    val infoExtra = stringResource(R.string.info_extra)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.videojuego),
            contentDescription = stringResource(R.string.titulo),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.titulo), style = MaterialTheme.typography.headlineSmall)
            Text(
                text = stringResource(R.string.categoria),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.descripcion), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { Toast.makeText(context, infoExtra, Toast.LENGTH_LONG).show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.boton))
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio2T6</string>
    <string name="titulo">The Legend of Zelda: Breath of the Wild</string>
    <string name="categoria">Aventura · Mundo abierto</string>
    <string name="descripcion">Explora el inmenso reino de Hyrule con total libertad, resuelve santuarios y derrota a Ganon.</string>
    <string name="boton">Más información</string>
    <string name="info_extra">Nintendo · 2017 · PEGI 12 · Nintendo Switch</string>
</resources>
```
> `stringResource()` solo se puede llamar dentro de un Composable, por eso guardamos `infoExtra` antes y lo usamos dentro del `onClick`.
:::

## 6. Reutilización de componentes

Podemos crear **componentes reutilizables**: funciones Composable que representan un elemento concreto y que usamos tantas veces como queramos, pasando por **parámetro** lo que cambia.

```kotlin
@Composable
fun TextoAzul(
    mensaje: String,                 // obligatorio (sin valor por defecto)
    tamano: TextUnit = 20.sp,        // opcional (tiene valor por defecto)
    color: Color = Color.Blue
) {
    Text(text = mensaje, fontSize = tamano, color = color)
}

// Uso
TextoAzul("Hola")                                  // usa 20.sp y azul
TextoAzul("Grande", tamano = 32.sp)                // cambia solo el tamaño
TextoAzul(mensaje = "Rojo", color = Color.Red)     // argumentos con nombre
```

- Si un parámetro **tiene valor por defecto**, solo lo pasas si quieres cambiarlo.
- Si **no lo tiene** (como `mensaje`), es obligatorio.
- Estos componentes pueden ir en **otros ficheros Kotlin** (p. ej. `Componentes.kt`) para separarlos de la pantalla principal.

:::tree
app/src/main/java/com/example/miapp/
├── MainActivity.kt
└── ui/
    ├── components/Componentes.kt   ← TextoAzul, BotonRedondo…
    └── theme/…
:::

## 7. Estados en Jetpack Compose

Un **estado** es cualquier valor que **puede cambiar** y que influye en lo que se ve. Cuando cambia, Compose **recompone solo las partes** que lo leen.

- **Estado local**: dentro de un Composable, con `remember` o `rememberSaveable`.
- **Estado externo**: en un `ViewModel` con `MutableState` o `StateFlow` (más adelante).

### remember + mutableStateOf

`remember` **conserva** el valor entre recomposiciones (si no, se reiniciaría cada vez que se redibuja). Lo combinamos con un **estado observable**:

| Función | Tipo |
|---|---|
| `mutableIntStateOf` | `Int` |
| `mutableLongStateOf` | `Long` |
| `mutableFloatStateOf` | `Float` |
| `mutableDoubleStateOf` | `Double` |
| `mutableStateOf` | `String`, `Boolean`, objetos, data classes, colecciones |

```kotlin
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
fun Contador() {
    var count by remember { mutableIntStateOf(0) }   // "by" delega get/set en remember

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Has pulsado $count veces")              // solo este Text se recompone
        Button(onClick = { count++ }) {
            Text("Incrementar")
        }
    }
}
```

> `by` es una palabra clave de Kotlin que **delega** el get/set de la variable. Necesita los imports `getValue` y `setValue`.

### rememberSaveable

`remember` pierde el valor si la Activity se **destruye y recrea** (p. ej. al **girar la pantalla**). `rememberSaveable` lo guarda en un **Bundle** y lo recupera:

```kotlin
var count by rememberSaveable { mutableIntStateOf(0) }
```

- El Bundle guarda pares clave-valor (`Int`, `Long`, `Float`, `Double`, `Boolean`, `Byte`, `Char`, `Short`, `String`…).
- **No es una base de datos**: úsalo solo para lo que interesa recuperar (datos de un formulario, selección de un Checkbox o Switch…).

:::ejercicio Ejercicio 03 · Contador de visitas
App que simula un **contador de visitas**: título, número actual y tres botones. Empieza en **0**; **Incrementar** suma 1; **Decrementar** resta 1 pero **nunca negativo**; **Reiniciar** lo pone a 0. Usa estados e implementa el código de cada botón en una función.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio3t6/MainActivity.kt
package com.example.ejercicio3t6

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ejercicio3t6.ui.theme.Ejercicio3T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio3T6Theme {
                ContadorVisitas()
            }
        }
    }
}

// --- Lógica de cada botón en una función ---
fun incrementar(valor: Int): Int = valor + 1
fun decrementar(valor: Int): Int = if (valor > 0) valor - 1 else 0
fun reiniciar(): Int = 0

@Composable
fun ContadorVisitas() {
    var visitas by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "$visitas",
            fontSize = 96.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { visitas = decrementar(visitas) }, enabled = visitas > 0) {
                Text(stringResource(R.string.decrementar))
            }
            Button(onClick = { visitas = incrementar(visitas) }) {
                Text(stringResource(R.string.incrementar))
            }
        }
        OutlinedButton(
            onClick = { visitas = reiniciar() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.reiniciar))
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio3T6</string>
    <string name="titulo">Contador de visitas</string>
    <string name="incrementar">Incrementar</string>
    <string name="decrementar">Decrementar</string>
    <string name="reiniciar">Reiniciar</string>
</resources>
```
> Usamos `rememberSaveable` para que el contador **no se pierda al girar** la pantalla.
:::

## 8. TextField

Permite al usuario **escribir y modificar texto** (formularios, buscadores, comentarios…). Es un componente **controlado con estado**: lo que muestra viene de `value` y los cambios llegan por `onValueChange` (lambda `(String) -> Unit`).

- `TextField`: relleno con línea inferior.
- `OutlinedTextField`: fondo transparente y borde completo.

| Parámetro | Para qué |
|---|---|
| `value` / `onValueChange` | Texto actual / lambda con el nuevo texto |
| `label` | Etiqueta |
| `placeholder` | Texto de ayuda cuando está vacío |
| `leadingIcon` / `trailingIcon` | Icono al inicio / al final |
| `isError` | Lo marca como erróneo (borde rojo) |
| `enabled` / `readOnly` | Deshabilitado / no editable pero no deshabilitado |
| `singleLine` | Una sola línea |
| `visualTransformation` | `PasswordVisualTransformation()` para contraseñas |
| `keyboardOptions` | Tipo de teclado: `KeyboardOptions(keyboardType = KeyboardType.Number)` |
| `colors` | `TextFieldDefaults.colors()` / `OutlinedTextFieldDefaults.colors()` |

```kotlin
var nombre by rememberSaveable { mutableStateOf("") }
var password by rememberSaveable { mutableStateOf("") }

TextField(
    value = nombre,
    onValueChange = { nombre = it },
    label = { Text("Nombre") },
    placeholder = { Text("Escribe tu nombre") },
    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
    singleLine = true
)

OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("Contraseña") },
    visualTransformation = PasswordVisualTransformation(),
    isError = password.isNotEmpty() && password.length < 4,
    trailingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
    singleLine = true
)
```

## 9. RadioButton

Permite elegir **una sola opción** de un grupo excluyente. **No incluye texto**: se combina con `Row` + `Text`. La opción elegida se guarda en un **estado**.

| Parámetro | Para qué |
|---|---|
| `selected` | Si está seleccionado |
| `onClick` | Lambda al pulsar |
| `enabled`, `modifier` | — |
| `colors` | `RadioButtonDefaults.colors(selectedColor = …)` |

```kotlin
val opciones = listOf("Kotlin", "Java", "Python")
var seleccion by rememberSaveable { mutableStateOf(opciones[0]) }

Column {
    opciones.forEach { opcion ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable { seleccion = opcion }   // toda la fila pulsable
        ) {
            RadioButton(selected = seleccion == opcion, onClick = { seleccion = opcion })
            Text(opcion)
        }
    }
    Text("Has elegido: $seleccion")
}
```

:::ejercicio Ejercicio 04 · Medio de transporte (RadioButton)
App para elegir el **medio de transporte** preferido para un viaje: título, subtítulo, **tres opciones con RadioButton** (con icono) y un texto que muestra la opción seleccionada. Pista: usa una lista de objetos de una **data class** con `texto` (String) e `icono` (ImageVector).
---solucion---
Los iconos `DirectionsCar`, `Train` y `Flight` están en la librería de **iconos extendidos**: añade la dependencia y pulsa **Sync Now**.

```kotlin file=app/build.gradle.kts
dependencies {
    // ... dependencias que ya tiene el proyecto ...
    implementation("androidx.compose.material:material-icons-extended")
}
```
```kotlin file=app/src/main/java/com/example/ejercicio4t6/MainActivity.kt
package com.example.ejercicio4t6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio4t6.ui.theme.Ejercicio4T6Theme

data class Transporte(val texto: String, val icono: ImageVector)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio4T6Theme {
                SelectorTransporte()
            }
        }
    }
}

@Composable
fun SelectorTransporte() {
    val opciones = listOf(
        Transporte(stringResource(R.string.coche), Icons.Filled.DirectionsCar),
        Transporte(stringResource(R.string.tren), Icons.Filled.Train),
        Transporte(stringResource(R.string.avion), Icons.Filled.Flight)
    )
    var seleccionado by rememberSaveable { mutableStateOf(opciones[0].texto) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 64.dp)) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)
        Text(
            stringResource(R.string.subtitulo),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { seleccionado = opcion.texto }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = seleccionado == opcion.texto,
                    onClick = { seleccionado = opcion.texto }
                )
                Icon(imageVector = opcion.icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(opcion.texto, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.seleccion, seleccionado),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio4T6</string>
    <string name="titulo">Planifica tu viaje</string>
    <string name="subtitulo">Elige tu medio de transporte preferido:</string>
    <string name="coche">Coche</string>
    <string name="tren">Tren</string>
    <string name="avion">Avión</string>
    <!-- %1$s se sustituye por el primer argumento -->
    <string name="seleccion">Has seleccionado: %1$s</string>
</resources>
```
> `stringResource(R.string.seleccion, seleccionado)` rellena el `%1$s` del recurso con el valor del estado.
:::

## 10. Checkbox

Permite **marcar varias opciones** a la vez, de forma independiente (aceptar condiciones, preferencias, listas…). Cada Checkbox necesita **su propio estado**.

| Parámetro | Para qué |
|---|---|
| `checked` | Marcado (`true`) o no (`false`) |
| `onCheckedChange` | Lambda `(Boolean) -> Unit` cuando cambia |
| `enabled`, `modifier` | — |
| `colors` | `CheckboxDefaults.colors(checkedColor = …)` |

```kotlin
var aceptaCondiciones by rememberSaveable { mutableStateOf(false) }
var recibirNoticias by rememberSaveable { mutableStateOf(true) }

Row(verticalAlignment = Alignment.CenterVertically) {
    Checkbox(checked = aceptaCondiciones, onCheckedChange = { aceptaCondiciones = it })
    Text("Acepto las condiciones")
}
Row(verticalAlignment = Alignment.CenterVertically) {
    Checkbox(checked = recibirNoticias, onCheckedChange = { recibirNoticias = it })
    Text("Quiero recibir noticias")
}
Button(onClick = { }, enabled = aceptaCondiciones) { Text("Continuar") }
```

:::ejercicio Ejercicio 05 · Calculadora con Checkbox
Introduce **dos números** con `OutlinedTextField`, elige con **Checkbox** las operaciones (sumar, restar, ambas o ninguna), pulsa **Calcular** y muestra el resultado. La lógica del botón va en una **función** que primero valida que haya dos números válidos (si no, **Toast** de error). Si son válidos devuelve el texto con los resultados; si no hay ninguna operación marcada devuelve **"Seleccione al menos una operación"**.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio5t6/MainActivity.kt
package com.example.ejercicio5t6

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ejercicio5t6.ui.theme.Ejercicio5T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio5T6Theme {
                Calculadora()
            }
        }
    }
}

/**
 * Lógica del botón "Calcular".
 * Devuelve el texto a mostrar o null si los números no son válidos (y avisa con un Toast).
 */
fun calcular(context: Context, texto1: String, texto2: String, sumar: Boolean, restar: Boolean): String? {
    val num1 = texto1.trim().toDoubleOrNull()
    val num2 = texto2.trim().toDoubleOrNull()

    if (num1 == null || num2 == null) {
        Toast.makeText(context, R.string.error_numeros, Toast.LENGTH_SHORT).show()
        return null
    }
    if (!sumar && !restar) {
        return context.getString(R.string.sin_operacion)
    }

    val lineas = mutableListOf<String>()
    if (sumar) lineas.add(context.getString(R.string.resultado_suma, num1, num2, num1 + num2))
    if (restar) lineas.add(context.getString(R.string.resultado_resta, num1, num2, num1 - num2))
    return lineas.joinToString("\n")
}

@Composable
fun Calculadora() {
    val context = LocalContext.current
    var numero1 by rememberSaveable { mutableStateOf("") }
    var numero2 by rememberSaveable { mutableStateOf("") }
    var sumar by rememberSaveable { mutableStateOf(false) }
    var restar by rememberSaveable { mutableStateOf(false) }
    var resultado by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = numero1,
            onValueChange = { numero1 = it },
            label = { Text(stringResource(R.string.numero1)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = numero2,
            onValueChange = { numero2 = it },
            label = { Text(stringResource(R.string.numero2)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = sumar, onCheckedChange = { sumar = it })
            Text(stringResource(R.string.sumar))
            Checkbox(checked = restar, onCheckedChange = { restar = it })
            Text(stringResource(R.string.restar))
        }

        Button(
            onClick = {
                calcular(context, numero1, numero2, sumar, restar)?.let { resultado = it }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.calcular))
        }

        Text(
            text = resultado,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio5T6</string>
    <string name="titulo">Calculadora</string>
    <string name="numero1">Primer número</string>
    <string name="numero2">Segundo número</string>
    <string name="sumar">Sumar</string>
    <string name="restar">Restar</string>
    <string name="calcular">Calcular</string>
    <string name="error_numeros">Introduce dos números válidos</string>
    <string name="sin_operacion">Seleccione al menos una operación</string>
    <!-- %1$s, %2$s y %3$s se sustituyen por los números -->
    <string name="resultado_suma">Suma: %1$s + %2$s = %3$s</string>
    <string name="resultado_resta">Resta: %1$s - %2$s = %3$s</string>
</resources>
```
> `toDoubleOrNull()` devuelve `null` si el texto no es un número: así validamos sin que la app se cierre (alternativa al `try/catch` con `NumberFormatException`).
:::

## 11. Switch

Interruptor **encendido/apagado** (valor binario), controlado con estado. Ideal para configuraciones rápidas.

| Parámetro | Para qué |
|---|---|
| `checked` | Activado o no |
| `onCheckedChange` | Lambda `(Boolean) -> Unit` |
| `enabled`, `modifier` | — |
| `thumbContent` | Contenido del círculo que se mueve (opcional, p. ej. un icono) |
| `colors` | `SwitchDefaults.colors(checkedThumbColor = …, checkedTrackColor = …)` |

```kotlin
var modoOscuro by rememberSaveable { mutableStateOf(false) }

Row(verticalAlignment = Alignment.CenterVertically) {
    Text("Modo oscuro", modifier = Modifier.weight(1f))
    Switch(
        checked = modoOscuro,
        onCheckedChange = { modoOscuro = it },
        thumbContent = if (modoOscuro) {
            { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(SwitchDefaults.IconSize)) }
        } else null
    )
}
```

:::ejercicio Ejercicio 06 · Mr. Potato con Switch
Personaliza a **Mr. Potato** con dos Switch (**Gafas** y **Sombrero**), desactivados al inicio. Según la combinación se muestra una imagen distinta: sin nada, con gafas, con sombrero o con ambos. Implementa una **función** que reciba el estado de los dos Switch y **devuelva el ID de la imagen**.
---solucion---
Copia las 4 imágenes en `res/drawable/`:

:::tree
app/src/main/res/drawable/
├── potato.png                ← sin gafas ni sombrero
├── potato_gafas.png          ← con gafas
├── potato_sombrero.png       ← con sombrero
└── potato_gafas_sombrero.png ← con gafas y sombrero
:::

```kotlin file=app/src/main/java/com/example/ejercicio6t6/MainActivity.kt
package com.example.ejercicio6t6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio6t6.ui.theme.Ejercicio6T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio6T6Theme {
                MrPotato()
            }
        }
    }
}

/** Devuelve el ID de la imagen según el estado de los dos Switch */
@DrawableRes
fun imagenPotato(gafas: Boolean, sombrero: Boolean): Int {
    return when {
        gafas && sombrero -> R.drawable.potato_gafas_sombrero
        gafas -> R.drawable.potato_gafas
        sombrero -> R.drawable.potato_sombrero
        else -> R.drawable.potato
    }
}

@Composable
fun MrPotato() {
    var gafas by rememberSaveable { mutableStateOf(false) }
    var sombrero by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = painterResource(id = imagenPotato(gafas, sombrero)),
            contentDescription = stringResource(R.string.desc_potato),
            modifier = Modifier.size(300.dp)
        )
        OpcionSwitch(stringResource(R.string.gafas), gafas) { gafas = it }
        OpcionSwitch(stringResource(R.string.sombrero), sombrero) { sombrero = it }
    }
}

@Composable
fun OpcionSwitch(texto: String, activado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(texto, style = MaterialTheme.typography.titleLarge)
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio6T6</string>
    <string name="titulo">Personaliza a Mr. Potato</string>
    <string name="desc_potato">Mr. Potato</string>
    <string name="gafas">Gafas</string>
    <string name="sombrero">Sombrero</string>
</resources>
```
:::

## 12. Ejercicios de consolidación

:::ejercicio Ejercicio 07 · Tarjeta de contacto: Walter White
Crea una app con la **tarjeta de contacto** de Walter White que simule una llamada: un **Card** con imagen, nombre, teléfono y dos **IconButton** (llamar y colgar). Al pulsar **llamar** se muestra "Llamando a Walter White…"; al pulsar **colgar**, "Llamada finalizada". Al principio el texto está vacío.
---solucion---
Copia la foto como `res/drawable/walter.jpg`.

```kotlin file=app/src/main/java/com/example/ejercicio7t6/MainActivity.kt
package com.example.ejercicio7t6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio7t6.ui.theme.Ejercicio7T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio7T6Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    TarjetaContacto()
                }
            }
        }
    }
}

@Composable
fun TarjetaContacto() {
    var estadoLlamada by rememberSaveable { mutableStateOf("") }
    val textoLlamando = stringResource(R.string.llamando)
    val textoFin = stringResource(R.string.finalizada)

    Card(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.walter),
                contentDescription = stringResource(R.string.nombre),
                modifier = Modifier.size(140.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(stringResource(R.string.nombre), style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(R.string.telefono), style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(
                    onClick = { estadoLlamada = textoLlamando },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Call, contentDescription = stringResource(R.string.llamar))
                }
                IconButton(
                    onClick = { estadoLlamada = textoFin },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFC62828), contentColor = Color.White),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.colgar))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(estadoLlamada, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio7T6</string>
    <string name="nombre">Walter White</string>
    <string name="telefono">+1 505 555 0198</string>
    <string name="llamar">Llamar</string>
    <string name="colgar">Colgar</string>
    <string name="llamando">Llamando a Walter White…</string>
    <string name="finalizada">Llamada finalizada</string>
</resources>
```
> Si añadiste `material-icons-extended`, puedes usar `Icons.Filled.CallEnd` para el botón de colgar.
:::

:::ejercicio Ejercicio 08 · Jugador de Commander (TextField + RadioButton)
Introduce el **nombre del jugador** con un `TextField` y elige un **tipo de personaje** del juego Commander con `RadioButton` (al principio ninguno seleccionado). Al pulsar **Guardar** se muestra un **Toast** con nombre y tipo. Si falta el nombre o el tipo, Toast de error. La funcionalidad de Guardar va en una **función**.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio8t6/MainActivity.kt
package com.example.ejercicio8t6

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio8t6.ui.theme.Ejercicio8T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio8T6Theme {
                FormularioJugador()
            }
        }
    }
}

/** Lógica del botón Guardar */
fun guardarJugador(context: Context, nombre: String, personaje: String) {
    val mensaje = when {
        nombre.isBlank() && personaje.isEmpty() -> context.getString(R.string.error_todo)
        nombre.isBlank() -> context.getString(R.string.error_nombre)
        personaje.isEmpty() -> context.getString(R.string.error_personaje)
        else -> context.getString(R.string.jugador_guardado, nombre.trim(), personaje)
    }
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
}

@Composable
fun FormularioJugador() {
    val context = LocalContext.current
    val personajes = stringArrayResource(R.array.personajes)   // lista en strings.xml
    var nombre by rememberSaveable { mutableStateOf("") }
    var personaje by rememberSaveable { mutableStateOf("") }   // "" = ninguno seleccionado

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.nombre)) },
            placeholder = { Text(stringResource(R.string.placeholder)) },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(stringResource(R.string.tipo), style = MaterialTheme.typography.titleMedium)
        personajes.forEach { tipo ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { personaje = tipo },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = personaje == tipo, onClick = { personaje = tipo })
                Text(tipo)
            }
        }

        Button(
            onClick = { guardarJugador(context, nombre, personaje) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.guardar))
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio8T6</string>
    <string name="titulo">Commander: nuevo jugador</string>
    <string name="nombre">Nombre del jugador</string>
    <string name="placeholder">Escribe tu nick</string>
    <string name="tipo">Tipo de personaje:</string>
    <string name="guardar">Guardar</string>
    <string name="error_todo">Introduce un nombre y selecciona un personaje</string>
    <string name="error_nombre">Error: introduce el nombre del jugador</string>
    <string name="error_personaje">Error: selecciona un tipo de personaje</string>
    <string name="jugador_guardado">Jugador %1$s guardado como %2$s</string>

    <string-array name="personajes">
        <item>Guerrero</item>
        <item>Mago</item>
        <item>Arquero</item>
        <item>Explorador</item>
    </string-array>
</resources>
```
:::

:::ejercicio Ejercicio 09 · Adivina el número (1–10)
Juego para adivinar un número aleatorio entre 1 y 10: título, `OutlinedTextField`, botones **Probar Suerte** y **Reiniciar**, y mensaje de enhorabuena al acertar.

- **Probar Suerte**: valida el número (Toast si no es válido). Si es válido, llama a una función que dice si ha acertado; si no, Toast con pista ("Introduce un número mayor/menor"). Incrementa los intentos.
- Al acertar: enhorabuena con el número de intentos (al principio vacío).
- **Reiniciar**: nuevo número secreto y resetea número introducido, intentos y acierto.
- Pista: `(1..10).random()`.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio9t6/MainActivity.kt
package com.example.ejercicio9t6

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejercicio9t6.ui.theme.Ejercicio9T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio9T6Theme {
                AdivinaNumero()
            }
        }
    }
}

/** Devuelve true si el usuario ha acertado el número secreto */
fun comprobarNumero(numero: Int, secreto: Int): Boolean = numero == secreto

@Composable
fun AdivinaNumero() {
    val context = LocalContext.current
    var secreto by rememberSaveable { mutableIntStateOf((1..10).random()) }
    var entrada by rememberSaveable { mutableStateOf("") }
    var intentos by rememberSaveable { mutableIntStateOf(0) }
    var acertado by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = entrada,
            onValueChange = { entrada = it },
            label = { Text(stringResource(R.string.label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !acertado,
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                enabled = !acertado,
                onClick = {
                    val numero = entrada.trim().toIntOrNull()
                    if (numero == null || numero !in 1..10) {
                        Toast.makeText(context, R.string.error_numero, Toast.LENGTH_SHORT).show()
                    } else {
                        intentos++
                        if (comprobarNumero(numero, secreto)) {
                            acertado = true
                        } else {
                            val pista = if (numero < secreto) R.string.pista_mayor else R.string.pista_menor
                            Toast.makeText(context, pista, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.probar))
            }
            OutlinedButton(onClick = {
                secreto = (1..10).random()
                entrada = ""
                intentos = 0
                acertado = false
            }) {
                Text(stringResource(R.string.reiniciar))
            }
        }

        Text(
            text = if (acertado) stringResource(R.string.enhorabuena, secreto, intentos) else "",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio9T6</string>
    <string name="titulo">Adivina el número (1 - 10)</string>
    <string name="label">Introduce un número</string>
    <string name="probar">Probar Suerte</string>
    <string name="reiniciar">Reiniciar</string>
    <string name="error_numero">Introduce un número válido entre 1 y 10</string>
    <string name="pista_mayor">Pista: Introduce un número mayor</string>
    <string name="pista_menor">Pista: Introduce un número menor</string>
    <!-- %1$d = número secreto, %2$d = intentos -->
    <string name="enhorabuena">¡Enhorabuena! Has acertado el número %1$d en %2$d intentos</string>
</resources>
```
:::

:::ejercicio Ejercicio 10 · Visita guiada por Roma (Checkbox + Image)
Selecciona los **monumentos de Roma** a visitar: título, **cuatro imágenes** con un **Checkbox** cada una y un botón **Guardar Visita**. Al pulsarlo, una función muestra un **Toast** con los seleccionados o "No se han seleccionado monumentos".
---solucion---
:::tree
app/src/main/res/drawable/
├── coliseo.jpg
├── fontana_trevi.jpg
├── panteon.jpg
└── foro_romano.jpg
:::

```kotlin file=app/src/main/java/com/example/ejercicio10t6/MainActivity.kt
package com.example.ejercicio10t6

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio10t6.ui.theme.Ejercicio10T6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio10T6Theme {
                VisitaRoma()
            }
        }
    }
}

/** Muestra un Toast con los monumentos seleccionados */
fun guardarVisita(context: Context, seleccionados: List<String>) {
    val mensaje = if (seleccionados.isEmpty()) {
        context.getString(R.string.sin_monumentos)
    } else {
        context.getString(R.string.visita_guardada, seleccionados.joinToString(", "))
    }
    Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
}

@Composable
fun VisitaRoma() {
    val context = LocalContext.current
    // Un estado independiente para cada Checkbox
    var coliseo by rememberSaveable { mutableStateOf(false) }
    var fontana by rememberSaveable { mutableStateOf(false) }
    var panteon by rememberSaveable { mutableStateOf(false) }
    var foro by rememberSaveable { mutableStateOf(false) }

    val nColiseo = stringResource(R.string.coliseo)
    val nFontana = stringResource(R.string.fontana)
    val nPanteon = stringResource(R.string.panteon)
    val nForo = stringResource(R.string.foro)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(stringResource(R.string.titulo), style = MaterialTheme.typography.headlineMedium)

        Monumento(R.drawable.coliseo, nColiseo, coliseo) { coliseo = it }
        Monumento(R.drawable.fontana_trevi, nFontana, fontana) { fontana = it }
        Monumento(R.drawable.panteon, nPanteon, panteon) { panteon = it }
        Monumento(R.drawable.foro_romano, nForo, foro) { foro = it }

        Button(
            onClick = {
                val lista = mutableListOf<String>()
                if (coliseo) lista.add(nColiseo)
                if (fontana) lista.add(nFontana)
                if (panteon) lista.add(nPanteon)
                if (foro) lista.add(nForo)
                guardarVisita(context, lista)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.guardar))
        }
    }
}

@Composable
fun Monumento(@DrawableRes imagen: Int, nombre: String, marcado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = nombre,
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Checkbox(checked = marcado, onCheckedChange = onCambio)
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio10T6</string>
    <string name="titulo">Visita guiada por Roma</string>
    <string name="coliseo">Coliseo</string>
    <string name="fontana">Fontana di Trevi</string>
    <string name="panteon">Panteón de Agripa</string>
    <string name="foro">Foro Romano</string>
    <string name="guardar">Guardar Visita</string>
    <string name="sin_monumentos">No se han seleccionado monumentos</string>
    <string name="visita_guardada">Visita guardada: %1$s</string>
</resources>
```
:::
