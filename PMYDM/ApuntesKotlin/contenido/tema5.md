@id tema5
@numero 5
@titulo Diseño de interfaces de usuario
@resumen Jetpack Compose y funciones Composable, Material Design 3 (Color.kt, Type.kt, Theme.kt), modificadores y layouts Column, Row y Box.

## 1. Jetpack Compose

**Jetpack Compose** es el kit moderno de Google para crear interfaces en apps Android nativas. En lugar de XML, la interfaz se define **con código Kotlin**.

**Características:**

- Enfoque **declarativo**.
- Código más **simple, limpio y conciso**.
- Mejora la productividad y facilita **reutilizar** partes de la interfaz.
- Se integra con las herramientas y librerías nativas de Android.
- Es eficiente gracias a la **recomposición**: solo se redibuja lo que cambia.

### Declarativo vs imperativo

- **Imperativo** (XML clásico): creas el componente, lo buscas (`findViewById`) y le cambias el texto paso a paso.
- **Declarativo** (Compose): describes **qué** quieres ver. `Text("Hola")` = "la interfaz debe mostrar un texto que diga Hola".

| | XML (tradicional) | Jetpack Compose |
|---|---|---|
| Lenguaje de la UI | XML + Kotlin/Java | Solo Kotlin |
| Paradigma | Imperativo | Declarativo |
| Actualizar la UI | Manualmente (`findViewById`, `setText`…) | Automático al cambiar el estado |
| Reutilización | Layouts `include`, vistas custom | Funciones Composable |

### Funciones Composable

- Bloque de código Kotlin que construye una parte de la interfaz. Se marca con **`@Composable`**.
- Compose trae componentes ya hechos (cada uno es una función Composable): `Text`, `Button`, `Image`, `Box`, `Row`, `Column`…
- Podemos crear **las nuestras** y llamarlas desde `setContent`.

```kotlin
@Composable
fun Saludo(nombre: String) {
    Text(text = "Hola, $nombre")
}

// En la MainActivity:
setContent {
    MiAppTheme {
        Saludo("Manuel")
    }
}
```

### Cómo nombrar las funciones Composable

1. **PascalCase** (cada palabra con mayúscula, sin espacios ni guiones): `PantallaPrincipal`, `TarjetaDeProducto`, `BotonSuperior`.
2. Un **sustantivo** o "adjetivo + sustantivo", **sin verbos** (no `GetText`): construyen UI, no hacen acciones.
3. **Públicas** en su fichero para poder reutilizarlas.

### Vista previa con @Preview

```kotlin
@Preview(showBackground = true)
@Composable
fun SaludoPreview() {
    MiAppTheme { Saludo("Manuel") }
}
```

:::ejercicio Ejercicio 01 · MiPrimeraPantalla
Crea un proyecto con Jetpack Compose e implementa una función Composable **`MiPrimeraPantalla`** que muestre el texto **¡Hola, Jetpack Compose!**
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio1t5/MainActivity.kt
package com.example.ejercicio1t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicio1t5.ui.theme.Ejercicio1T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio1T5Theme {
                MiPrimeraPantalla()
            }
        }
    }
}

@Composable
fun MiPrimeraPantalla() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.saludo),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MiPrimeraPantallaPreview() {
    Ejercicio1T5Theme { MiPrimeraPantalla() }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio1T5</string>
    <string name="saludo">¡Hola, Jetpack Compose!</string>
</resources>
```
:::

## 2. Material Design y Material 3

**Material Design** es el sistema de diseño de Google (2014) con principios, guías y componentes para crear interfaces **consistentes, intuitivas y atractivas**. Ha evolucionado: Material Design 1, 2 y, actualmente, **Material 3**.

### Principios de Material Design

- **Metáfora del material**: inspirada en papel y tinta; sombras, profundidad y movimiento dan sensación física.
- **Movimiento significativo**: animaciones suaves que dan contexto sin distraer.
- **Diseño adaptativo**: se adapta a móviles, tablets…
- **Énfasis en el contenido**: jerarquía con color, tipografía y espaciado.

### Novedades de Material 3 (personalización)

- **Paletas dinámicas** (*dynamic color*): los colores se adaptan al fondo de pantalla del usuario.
- Componentes **rediseñados** y más accesibles.
- **Modo oscuro** mejorado.
- Enfoque en **accesibilidad** (contrastes, tamaños, espacios).
- **Consistencia visual** entre plataformas y **flexibilidad** para apps únicas.

### La API MaterialTheme

Define en un sitio central los **colores**, la **tipografía** y las **formas** de toda la app. Se configura con los ficheros del paquete **`ui.theme`**: `Color.kt`, `Type.kt` y `Theme.kt`.

```kotlin file=app/src/main/java/com/example/miapp/ui/theme/Color.kt
package com.example.miapp.ui.theme

import androidx.compose.ui.graphics.Color

// Colores generados por defecto
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores propios (formato 0xAARRGGBB: FF = opaco)
val DarkRed = Color(0xFF800000)
val VerdeKotlin = Color(0xFF7F52FF)
```
```kotlin file=app/src/main/java/com/example/miapp/ui/theme/Type.kt
package com.example.miapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)
```
```kotlin file=app/src/main/java/com/example/miapp/ui/theme/Theme.kt
package com.example.miapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 1) Esquemas ESTÁTICOS: colores definidos en Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun MiAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),      // modo claro/oscuro del sistema
    dynamicColor: Boolean = true,                    // 2) DYNAMIC COLOR (Android 12+)
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

> Si pones `dynamicColor = false`, la app usará **siempre tus colores** de `LightColorScheme`/`DarkColorScheme` en lugar de los del fondo de pantalla.

### Usar el tema en los componentes

```kotlin
Text(
    text = "Título",
    color = MaterialTheme.colorScheme.primary,          // colores del tema
    style = MaterialTheme.typography.titleLarge         // tipografía del tema
)
```

| Colores (`colorScheme`) | Tipografías (`typography`) |
|---|---|
| `primary`, `secondary`, `tertiary` (los más importantes) | `displayLarge` / `displayMedium` / `displaySmall` |
| `onPrimary`, `onSecondary` (contenido encima) | `headlineLarge` / `headlineMedium` / `headlineSmall` |
| `background`, `onBackground` | `titleLarge` / `titleMedium` / `titleSmall` |
| `surface`, `onSurface` | `bodyLarge` / `bodyMedium` / `bodySmall` |
| `error`, `onError` | `labelLarge` / `labelMedium` / `labelSmall` |

:::ejercicio Ejercicio 02 · MisTextos con MaterialTheme
Crea una función Composable **`MisTextos`** que muestre cuatro textos con estilos de MaterialTheme: un **título mediano**, un **encabezado grande**, un **cuerpo mediano** y una **etiqueta pequeña**. Los dos primeros con color **primario** y los dos últimos **secundario**. Usa una `Column`.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio2t5/MainActivity.kt
package com.example.ejercicio2t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio2t5.ui.theme.Ejercicio2T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio2T5Theme {
                MisTextos()
            }
        }
    }
}

@Composable
fun MisTextos() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.titulo),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.encabezado),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.cuerpo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = stringResource(R.string.etiqueta),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio2T5</string>
    <string name="titulo">Título mediano</string>
    <string name="encabezado">Encabezado grande</string>
    <string name="cuerpo">Este es un cuerpo de texto mediano</string>
    <string name="etiqueta">Etiqueta pequeña</string>
</resources>
```
:::

## 3. Modificadores

Un **Modifier** permite **personalizar y transformar** un Composable: apariencia, comportamiento o posición. Se **encadenan** con `.`

**Características:**

- **Inmutables**: cada modificador crea un objeto nuevo.
- **Encadenables**: cada uno devuelve un Modifier con todo lo anterior.
- **El orden importa**: se aplican de arriba a abajo.
- **Reutilizables**: puedes guardar un Modifier en una variable y usarlo en varios sitios.

```kotlin
Text(
    text = "Hola",
    modifier = Modifier
        .padding(16.dp)            // margen exterior
        .background(Color.Yellow)  // fondo
        .padding(8.dp)             // relleno interior (va después del fondo)
        .border(2.dp, Color.Red)
)

// ¡El orden cambia el resultado!
Modifier.background(Color.Yellow).padding(16.dp)   // el amarillo incluye el padding
Modifier.padding(16.dp).background(Color.Yellow)   // el padding queda fuera del amarillo

// Modifier reutilizable
val estiloCaja = Modifier.size(100.dp).border(1.dp, Color.Black)
```

| Modificador | Qué hace |
|---|---|
| `.padding()` | Espacio alrededor del contenido |
| `.width()` / `.height()` | Ancho / alto |
| `.size()` | Ancho y alto |
| `.fillMaxWidth()` / `.fillMaxHeight()` / `.fillMaxSize()` | Ocupa todo el ancho / alto / espacio disponible |
| `.border()` | Borde (grosor, color, forma) |
| `.background()` | Color de fondo |
| `.weight()` | Reparte el espacio dentro de `Row` o `Column` |
| `.clickable { }` | Hace el componente pulsable |

:::ejercicio Ejercicio 03 · MisTextosPersonalizados
Crea **`MisTextosPersonalizados`** que, usando modificadores, muestre dos textos como en el ejemplo: el primero ("Primer texto") con **fondo verde y borde negro**, ocupando todo el ancho; el segundo ("Segundo texto") en un **cuadrado cian con borde azul grueso**. Usa una `Column` (`fillMaxSize`, `padding(16.dp)`, centrada).
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio3t5/MainActivity.kt
package com.example.ejercicio3t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio3t5.ui.theme.Ejercicio3T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio3T5Theme {
                MisTextosPersonalizados()
            }
        }
    }
}

@Composable
fun MisTextosPersonalizados() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.primer_texto),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
                .border(1.dp, Color.Black)
                .padding(8.dp)
        )
        Text(
            text = stringResource(R.string.segundo_texto),
            modifier = Modifier
                .size(150.dp)
                .border(6.dp, Color.Blue)
                .padding(6.dp)
                .background(Color.Cyan)
                .padding(4.dp)
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio3T5</string>
    <string name="primer_texto">Primer texto</string>
    <string name="segundo_texto">Segundo texto</string>
</resources>
```
> Fíjate en el segundo texto: `border` → `padding` (deja el borde fuera) → `background` (el cian empieza dentro del borde) → `padding` (separa el texto del borde).
:::

## 4. Layouts

Un **layout** organiza otros elementos en la interfaz. En Compose usaremos sobre todo **Column**, **Row** y **Box**, y los combinaremos.

### Column (vertical)

| Parámetro | Valores |
|---|---|
| `modifier` | tamaño, padding, background… |
| `horizontalAlignment` | `Alignment.Start`, `Alignment.CenterHorizontally`, `Alignment.End` |
| `verticalArrangement` | `Arrangement.Top`, `Center`, `Bottom`, `SpaceBetween`, `SpaceEvenly`, `SpaceAround`, `spacedBy(8.dp)` |
| `content` | los hijos, entre llaves |

```kotlin
Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    Text("Uno")
    Text("Dos")
    Text("Tres")
}
```

### Row (horizontal)

| Parámetro | Valores |
|---|---|
| `horizontalArrangement` | `Arrangement.Start`, `Center`, `End`, `SpaceEvenly`, `SpaceAround`, `SpaceBetween`, `spacedBy(…)` |
| `verticalAlignment` | `Alignment.Top`, `Alignment.CenterVertically`, `Alignment.Bottom` |

```kotlin
Row(
    modifier = Modifier.fillMaxWidth().padding(16.dp),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
) {
    Text("Izquierda")
    Text("Centro")
    Text("Derecha")
}
```

> Truco: en `Column` el **eje principal** es vertical → se "reparte" con `verticalArrangement` y se "alinea" en horizontal. En `Row` es al revés.

### Box (apilar)

- Apila elementos **uno encima de otro**, en el orden en que se declaran (el último queda arriba). Ideal para **texto sobre imagen**.
- `contentAlignment`: `Alignment.TopStart`, `TopCenter`, `TopEnd`, `CenterStart`, `Center`, `CenterEnd`, `BottomStart`, `BottomCenter`, `BottomEnd`.
- Cada hijo puede colocarse por separado con **`Modifier.align(...)`**.

```kotlin
Box(
    modifier = Modifier.size(200.dp).background(Color.LightGray),
    contentAlignment = Alignment.Center
) {
    Text("Centro")
    Text("Arriba izq.", modifier = Modifier.align(Alignment.TopStart))
    Text("Abajo dcha.", modifier = Modifier.align(Alignment.BottomEnd))
}
```

### Repartir espacio con weight

```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Red))   // 1/3
    Box(modifier = Modifier.fillMaxWidth().weight(2f).background(Color.Blue))  // 2/3
}
```

:::ejercicio Ejercicio 04 · Pantalla dividida en dos (Column + Box)
Con `Column` y `Box`, crea una interfaz dividida en **dos mitades**: arriba **rojo** (`Color.Red`) y abajo un **rojo oscuro personalizado** `#800000` creado en `Color.kt` (`DarkRed`).
---solucion---
1. Creamos el color en `Color.kt`.
2. `Column` con dos `Box` del **mismo peso** (`weight(1f)`) → dos partes iguales.
3. Llamamos a la función en `setContent`.

```kotlin file=app/src/main/java/com/example/ejercicio4t5/ui/theme/Color.kt
package com.example.ejercicio4t5.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Color personalizado del ejercicio
val DarkRed = Color(0xFF800000)
```
```kotlin file=app/src/main/java/com/example/ejercicio4t5/MainActivity.kt
package com.example.ejercicio4t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ejercicio4t5.ui.theme.DarkRed
import com.example.ejercicio4t5.ui.theme.Ejercicio4T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio4T5Theme {
                PantallaDividida()
            }
        }
    }
}

@Composable
fun PantallaDividida() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(DarkRed)
        )
    }
}
```
:::

## 5. Ejercicios de consolidación

Ejercicios para practicar todo lo anterior. En todos: proyecto **Empty Activity**, la función Composable en `MainActivity.kt` y llamada desde `setContent`. Los ficheros `Theme.kt` y `Type.kt` son los que genera Android Studio salvo que se indique lo contrario.

:::ejercicio Ejercicio 05 · Tres colores (Column, Row y Box)
Con `Column`, `Row` y `Box` crea esta interfaz: la mitad superior dividida en dos (**rojo** a la izquierda y **magenta** a la derecha) y la mitad inferior **azul**.
---solucion---
```text
┌─────────┬─────────┐
│  ROJO   │ MAGENTA │   Row (weight 1f) con dos Box (weight 1f)
├─────────┴─────────┤
│       AZUL        │   Box (weight 1f)
└───────────────────┘
```
```kotlin file=app/src/main/java/com/example/ejercicio5t5/MainActivity.kt
package com.example.ejercicio5t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ejercicio5t5.ui.theme.Ejercicio5T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio5T5Theme {
                TresColores()
            }
        }
    }
}

@Composable
fun TresColores() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.Magenta)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Blue)
        )
    }
}
```
:::

:::ejercicio Ejercicio 06 · Cuatro cuadrantes con textos
Con `Column`, `Row`, `Box` y `Text`, divide la pantalla en **cuatro partes** (verde, rojo claro, azul claro y amarillo) con los textos **Texto 1** a **Texto 4**. Crea **colores personalizados** para cada parte y usa **MaterialTheme** para el estilo de los textos.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio6t5/ui/theme/Color.kt
package com.example.ejercicio6t5.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores personalizados del ejercicio
val VerdeClaro = Color(0xFF8CF08C)
val RojoClaro = Color(0xFFF46D6D)
val AzulClaro = Color(0xFFADD8F7)
val AmarilloClaro = Color(0xFFFAF77A)
```
```kotlin file=app/src/main/java/com/example/ejercicio6t5/MainActivity.kt
package com.example.ejercicio6t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.ejercicio6t5.ui.theme.AmarilloClaro
import com.example.ejercicio6t5.ui.theme.AzulClaro
import com.example.ejercicio6t5.ui.theme.Ejercicio6T5Theme
import com.example.ejercicio6t5.ui.theme.RojoClaro
import com.example.ejercicio6t5.ui.theme.VerdeClaro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio6T5Theme {
                CuatroCuadrantes()
            }
        }
    }
}

@Composable
fun CuatroCuadrantes() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Cuadrante(stringResource(R.string.texto1), VerdeClaro)
            Cuadrante(stringResource(R.string.texto2), RojoClaro)
        }
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Cuadrante(stringResource(R.string.texto3), AzulClaro)
            Cuadrante(stringResource(R.string.texto4), AmarilloClaro)
        }
    }
}

// Función auxiliar: un cuadrante con su color y su texto centrado.
// Es una extensión de RowScope para poder usar .weight() dentro de la Row.
@Composable
fun RowScope.Cuadrante(texto: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio6T5</string>
    <string name="texto1">Texto 1</string>
    <string name="texto2">Texto 2</string>
    <string name="texto3">Texto 3</string>
    <string name="texto4">Texto 4</string>
</resources>
```
> Si prefieres no usar `RowScope`, escribe los cuatro `Box` directamente dentro de cada `Row` con `Modifier.weight(1f)`.
:::

:::ejercicio Ejercicio 07 · Pantalla de login (Column, Text y Button)
Con `Column`, `Text` y `Button` diseña: arriba un **espacio amarillo de publicidad** ("Espacio reservado para publicidad ¡Anúnciate aquí!"), debajo los textos **Email:** y **Contraseña:** y un botón **INICIAR SESIÓN** a todo lo ancho. Usa MaterialTheme en los textos y en el texto del botón.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio7t5/MainActivity.kt
package com.example.ejercicio7t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ejercicio7t5.ui.theme.Ejercicio7T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio7T5Theme {
                PantallaLogin()
            }
        }
    }
}

@Composable
fun PantallaLogin() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Zona de publicidad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color.Yellow)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.publicidad),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
        // Formulario
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = stringResource(R.string.email), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.password), style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = { /* Acción al pulsar */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_text),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio7T5</string>
    <string name="publicidad">Espacio reservado para publicidad\n¡Anúnciate aquí!</string>
    <string name="email">Email:</string>
    <string name="password">Contraseña:</string>
    <string name="button_text">INICIAR SESIÓN</string>
</resources>
```
> `displaySmall` es lo que sugiere la pista de los apuntes; si te parece muy grande, usa `titleMedium`.
:::

:::ejercicio Ejercicio 08 · Rejilla de botones (Column, Row y Button)
Con `Column`, `Row` y `Button` crea **cuatro franjas** de colores (amarillo, verde, cian y magenta), cada una con **tres botones**: en la amarilla pegados **abajo**, en la verde **ocupando todo el alto**, en la cian **centrados** y en la magenta **arriba**. Usa MaterialTheme en el texto de los botones.
---solucion---
La clave es `verticalAlignment` de cada `Row` y, en la franja verde, `fillMaxHeight()` en los botones.

```kotlin file=app/src/main/java/com/example/ejercicio8t5/MainActivity.kt
package com.example.ejercicio8t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio8t5.ui.theme.Ejercicio8T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio8T5Theme {
                RejillaBotones()
            }
        }
    }
}

@Composable
fun RejillaBotones() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Franja 1: amarilla, botones abajo
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Yellow).padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(3) { BotonTexto() }
        }
        // Franja 2: verde, botones ocupando todo el alto
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Green).padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) { BotonTexto(Modifier.fillMaxHeight()) }
        }
        // Franja 3: cian, botones centrados
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Cyan).padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { BotonTexto() }
        }
        // Franja 4: magenta, botones arriba
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Magenta).padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            repeat(3) { BotonTexto() }
        }
    }
}

@Composable
fun BotonTexto(modifier: Modifier = Modifier) {
    Button(onClick = { }, modifier = modifier) {
        Text(
            text = stringResource(R.string.button),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio8T5</string>
    <string name="button">Button</string>
</resources>
```
> `repeat(3) { ... }` es una función de Kotlin que repite el bloque 3 veces. También puedes escribir los tres `BotonTexto()` a mano.
:::

:::ejercicio Ejercicio 09 · Invitación (Column, Box, Text e Image)
Con `Column`, `Box`, `Text` e `Image` diseña una **invitación**: arriba una imagen (flores) con el texto **INVITACIÓN** encima, y debajo una columna con fondo de MaterialTheme y los textos **Evento, Fecha, Hora, Dirección y Teléfono** repartidos.
---solucion---
Copia la imagen como `res/drawable/invitacion.jpg`.

```kotlin file=app/src/main/java/com/example/ejercicio9t5/MainActivity.kt
package com.example.ejercicio9t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ejercicio9t5.ui.theme.Ejercicio9T5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio9T5Theme {
                Invitacion()
            }
        }
    }
}

@Composable
fun Invitacion() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Parte superior: imagen con el título encima
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.invitacion),
                contentDescription = stringResource(id = R.string.image_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(R.string.titulo),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        // Parte inferior: datos del evento
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DatoInvitacion(stringResource(R.string.evento))
            DatoInvitacion(stringResource(R.string.fecha))
            DatoInvitacion(stringResource(R.string.hora))
            DatoInvitacion(stringResource(R.string.direccion))
            DatoInvitacion(stringResource(R.string.telefono))
        }
    }
}

@Composable
fun DatoInvitacion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio9T5</string>
    <string name="image_description">Flores de la invitación</string>
    <string name="titulo">INVITACIÓN</string>
    <string name="evento">Evento</string>
    <string name="fecha">Fecha</string>
    <string name="hora">Hora</string>
    <string name="direccion">Dirección</string>
    <string name="telefono">Teléfono</string>
</resources>
```
:::

:::ejercicio Ejercicio 10 · Mando con imagen (Column, Row, Box, Button e Image)
Con `Column`, `Row`, `Box`, `Button` e `Image`: arriba una franja verde con dos botones altos **IZQUIERDA** y **DERECHA**; en el centro una **imagen** (Android); abajo otra franja verde con dos botones a todo lo ancho **ARRIBA** y **ABAJO**. MaterialTheme para el texto de los botones y **colores personalizados** para los layouts.
---solucion---
Copia la imagen como `res/drawable/android.jpg`.

```kotlin file=app/src/main/java/com/example/ejercicio10t5/ui/theme/Color.kt
package com.example.ejercicio10t5.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores personalizados
val VerdeAndroid = Color(0xFF8BD346)
val FondoImagen = Color(0xFF1E1E1E)
```
```kotlin file=app/src/main/java/com/example/ejercicio10t5/MainActivity.kt
package com.example.ejercicio10t5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ejercicio10t5.ui.theme.Ejercicio10T5Theme
import com.example.ejercicio10t5.ui.theme.FondoImagen
import com.example.ejercicio10t5.ui.theme.VerdeAndroid

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio10T5Theme {
                Mando()
            }
        }
    }
}

@Composable
fun Mando() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Franja superior: IZQUIERDA / DERECHA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(VerdeAndroid)
                .padding(top = 32.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { }, modifier = Modifier.width(110.dp).fillMaxHeight()) {
                Text(stringResource(R.string.izquierda), style = MaterialTheme.typography.labelSmall)
            }
            Button(onClick = { }, modifier = Modifier.width(110.dp).fillMaxHeight()) {
                Text(stringResource(R.string.derecha), style = MaterialTheme.typography.labelSmall)
            }
        }
        // Centro: imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .background(FondoImagen),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.android),
                contentDescription = stringResource(R.string.desc_android),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        // Franja inferior: ARRIBA / ABAJO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(VerdeAndroid)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.arriba), style = MaterialTheme.typography.labelSmall)
            }
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.abajo), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio10T5</string>
    <string name="izquierda">IZQUIERDA</string>
    <string name="derecha">DERECHA</string>
    <string name="arriba">ARRIBA</string>
    <string name="abajo">ABAJO</string>
    <string name="desc_android">Muñeco de Android</string>
</resources>
```
:::
