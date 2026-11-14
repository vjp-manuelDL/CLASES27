@id tema4
@numero 4
@titulo Primer contacto con el código. Ciclo de vida de una App
@resumen Componentes de una app Android, la Activity y Jetpack Compose, ciclo de vida, recursos (strings, imágenes) e idiomas.

## 1. Componentes de una aplicación Android

Una app Android se construye combinando varios **componentes**. Los principales son:

| Componente | Qué es | Ejemplo |
|---|---|---|
| **Activity** | Punto de entrada y contenedor de la interfaz de usuario. Con Jetpack Compose se suele usar **una única Activity** con todas las pantallas. | `MainActivity` |
| **Composable** | Función que describe una parte de la interfaz. Se combinan para formar pantallas. | `Text`, `Button`, `Image`, `Column` |
| **Service** | Realiza operaciones **sin interfaz**, normalmente en segundo plano. | Reproducir música, notificaciones, subir/descargar archivos |
| **Content Provider** | Permite **compartir datos entre apps** de forma controlada. | Leer los contactos del móvil (con permisos) |
| **Broadcast Receiver** | Recibe y responde a **eventos globales** del sistema u otras apps. | Batería baja, cambio de red, modo avión |
| **Widget** | Elemento visual de la app que se muestra **fuera de ella** (p. ej. pantalla de inicio). | Reloj, clima, calendario, notas |
| **Intent** | Mensaje para pedir al sistema que **otra app realice una acción**. | Abrir una web, llamar, ver un mapa, enviar un email |

### Intents: aprovechar otras apps

Si nuestra app necesita abrir una web **no hace falta programar un navegador**: enviamos un *Intent* y Android busca una app instalada capaz de hacerlo. Para el usuario parece que todo forma parte de nuestra app.

```kotlin
// Ejemplo: abrir una página web con un Intent
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"))
startActivity(intent)
```

### No hay función main()

- Las apps Android **no tienen un punto de entrada único** (`main()`).
- Cada app se ejecuta en un **proceso independiente con permisos restringidos**: no puede activar directamente componentes de otra app; lo hace **el sistema operativo**.
- Al abrir una app, el sistema inicia la **Activity** que actúa como punto de entrada (declarada en el `AndroidManifest.xml`).

```xml file=app/src/main/AndroidManifest.xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:theme="@style/Theme.MiApp">
    <intent-filter>
        <!-- Esto la convierte en la Activity de entrada (icono del launcher) -->
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

> Más información: [Manifiesto de la app](https://developer.android.com/guide/topics/manifest/manifest-intro?hl=es-419)

## 2. El componente Activity

La **Activity** es el componente fundamental: proporciona la **ventana** donde se muestra la interfaz y es el **punto de entrada** de la app.

- La actividad principal suele llamarse **`MainActivity`**.
- Hereda de **`ComponentActivity`**.
- Android ejecuta **`onCreate()`** al iniciar la actividad. Dentro, **`setContent { }`** establece la interfaz hecha con Jetpack Compose.

```kotlin file=app/src/main/java/com/example/miapp/MainActivity.kt
package com.example.miapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.example.miapp.ui.theme.MiAppTheme

class MainActivity : ComponentActivity() {          // hereda de ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {                                // aquí va la interfaz (Compose)
            MiAppTheme {
                Text("¡Hola desde mi Activity!")
            }
        }
    }
}
```

### Single-Activity Architecture

- **Modelo tradicional (XML)**: cada pantalla solía ser una Activity distinta.
- **Jetpack Compose**: **una sola Activity** contiene varias pantallas hechas con funciones Composable. La **navegación** cambia entre ellas.
- Por tanto, **cambiar de pantalla no significa cambiar de Activity**.

### La pila de navegación

1. Al iniciar, se muestra la pantalla que está en la **cima** de la pila.
2. Al navegar a otra pantalla, esta se **apila** encima y pasa a mostrarse; la anterior queda debajo.
3. Al pulsar **"atrás"** se **desapila** la de arriba y vuelve a verse la anterior.

```text
 navegar a B        navegar a C        pulsar "atrás"
 ┌───┐              ┌───┐              ┌───┐
 │ B │ ← visible    │ C │ ← visible    │ B │ ← visible
 │ A │              │ B │              │ A │
 └───┘              │ A │              └───┘
                    └───┘
```

### ¿Dónde está cada cosa en el proyecto?

:::tree
app/
├── manifests/AndroidManifest.xml
├── java/com/example/miapp/   (Project → app → src → main → java → paquete)
│   ├── MainActivity.kt       ← funciones Composable + lógica de la Activity
│   └── ui/theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── res/
    ├── drawable/             ← imágenes (.png, .jpg, .xml vectoriales)
    ├── values/strings.xml    ← textos de la app
    └── values-es/strings.xml ← traducción al castellano (si la hay)
:::

### Unidades de medida

| Unidad | Uso |
|---|---|
| **dp** (density-independent pixels) | Tamaños y dimensiones de los elementos (`16.dp`). |
| **sp** (scale-independent pixels) | **Tamaño de texto** (`60.sp`); respeta el tamaño de fuente elegido por el usuario. |
| **px** (píxeles físicos) | No se recomienda usarlos directamente. |

> En Jetpack Compose usaremos principalmente **dp** y **sp** (`import androidx.compose.ui.unit.dp` / `sp`).

:::ejercicio Ejercicio 01 · Ejercicio1T4: ¡Hola mundo! sobre imagen de fondo
Crea el proyecto **Ejercicio1T4** con una única Activity (Jetpack Compose). Tendrá una **imagen de fondo** y el texto **"¡Hola mundo!"** centrado, de **60 sp** y color **rojo**.
---solucion---
**Pasos:**

1. **New Project → Empty Activity** → nombre `Ejercicio1T4`.
2. Copia tu imagen (p. ej. `fondo.jpg`) en `app/src/main/res/drawable/`. El nombre debe ir **en minúsculas, sin espacios ni tildes**.
3. Añade la cadena en `strings.xml` (los textos **siempre** en recursos).
4. En `onCreate` → `setContent`: un **`Box`** que ocupa toda la pantalla; dentro, primero la **`Image`** (queda al fondo) y después el **`Text`** (se dibuja encima). `contentAlignment = Alignment.Center` lo centra.

:::tree
Ejercicio1T4/app/src/main/
├── java/com/example/ejercicio1t4/MainActivity.kt
└── res/
    ├── drawable/fondo.jpg
    └── values/strings.xml
:::

```kotlin file=app/src/main/java/com/example/ejercicio1t4/MainActivity.kt
package com.example.ejercicio1t4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.ejercicio1t4.ui.theme.Ejercicio1T4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio1T4Theme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // 1º la imagen -> queda al fondo
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = stringResource(id = R.string.descripcion_fondo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // 2º el texto -> se dibuja encima de la imagen
                    Text(
                        text = stringResource(id = R.string.saludo),
                        color = Color.Red,
                        fontSize = 60.sp
                    )
                }
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio1T4</string>
    <string name="saludo">¡Hola mundo!</string>
    <string name="descripcion_fondo">Imagen de fondo</string>
</resources>
```
> `ContentScale.Crop` hace que la imagen rellene toda la pantalla recortando lo que sobre (sin deformarse).
:::

:::ejercicio Ejercicio 02 · Ejercicio2T4: Bienvenida con tu nombre
Crea **Ejercicio2T4** con una imagen de fondo y dos textos: **"Bienvenido a mi app"** (azul, 60 sp) y justo debajo **tu nombre** (color a elegir, 32 sp).
---solucion---
Para poner un texto **debajo** de otro usamos una **`Column`** dentro del `Box` (en el tema 5 se ve en detalle).

```kotlin file=app/src/main/java/com/example/ejercicio2t4/MainActivity.kt
package com.example.ejercicio2t4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ejercicio2t4.ui.theme.Ejercicio2T4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio2T4Theme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = stringResource(id = R.string.descripcion_fondo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(id = R.string.bienvenida),
                            color = Color.Blue,
                            fontSize = 60.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 64.sp
                        )
                        Text(
                            text = stringResource(id = R.string.mi_nombre),
                            color = Color.Magenta,
                            fontSize = 32.sp
                        )
                    }
                }
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Ejercicio2T4</string>
    <string name="bienvenida">Bienvenido a mi app</string>
    <string name="mi_nombre">Manuel</string>
    <string name="descripcion_fondo">Imagen de fondo</string>
</resources>
```
> Recuerda copiar `fondo.jpg` en `res/drawable/`.
:::

:::ejercicio Ejercicio 03 · Ejercicio3T4: App en dos idiomas
Crea **Ejercicio3T4** con un texto centrado, verde y de 40 sp, **en inglés (por defecto) y en castellano**.
---solucion---
**Pasos:**

1. Escribe los textos **en inglés** en `res/values/strings.xml` (idioma por defecto).
2. Abre el **Translations Editor**: clic derecho sobre `strings.xml` → **Open Translations Editor**.
3. Pulsa el **globo (+)** → **Spanish (es)** y rellena la columna. Android Studio crea `res/values-es/strings.xml`.
4. Para probarlo, cambia el idioma del emulador/dispositivo (Ajustes → Sistema → Idiomas).

:::tree
Ejercicio3T4/app/src/main/res/
├── values/strings.xml      ← inglés (por defecto)
└── values-es/strings.xml   ← castellano
:::

```kotlin file=app/src/main/java/com/example/ejercicio3t4/MainActivity.kt
package com.example.ejercicio3t4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.ejercicio3t4.ui.theme.Ejercicio3T4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio3T4Theme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.saludo),
                        color = Color.Green,
                        fontSize = 40.sp
                    )
                }
            }
        }
    }
}
```
```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">Exercise3T4</string>
    <string name="saludo">Hello, how are you?</string>
</resources>
```
```xml file=app/src/main/res/values-es/strings.xml
<resources>
    <string name="app_name">Ejercicio3T4</string>
    <string name="saludo">Hola, ¿qué tal estás?</string>
</resources>
```
> El código **no cambia**: `stringResource(R.string.saludo)` coge automáticamente la versión del idioma del dispositivo.
:::

## 3. Ciclo de vida de una Activity

Una Activity pasa por distintos **estados** desde que Android la crea hasta que la destruye. Al conjunto de estados y transiciones se le llama **ciclo de vida**.

| Estado | ¿En memoria? | ¿Visible (UI)? | ¿Primer plano? |
|---|---|---|---|
| **Inexistente** | No | No | No |
| **Detenida** | Sí | No | No |
| **Pausada** | Sí | Parcialmente | No |
| **En marcha** | Sí | Sí | Sí (el usuario interactúa) |

### Métodos del ciclo de vida

```text
          ┌──────────── onRestart() ◄──────────┐
          ▼                                    │
onCreate() → onStart() → onResume() → [EN MARCHA] → onPause() → onStop() → onDestroy()
                             ▲                          │
                             └──────────────────────────┘ (vuelve a primer plano)
```

| Método | Cuándo se ejecuta |
|---|---|
| `onCreate()` | Al crear la instancia de la Activity. Tareas iniciales; con Compose aquí va `setContent`. |
| `onStart()` | La Activity empieza a ser **visible** (raramente se usa). |
| `onResume()` | Pasa a **primer plano** y empieza a interactuar con el usuario → **En marcha**. |
| `onPause()` | Deja el primer plano → **Pausada**. |
| `onStop()` | Deja de ser visible → **Detenida**. |
| `onDestroy()` | Justo antes de **destruir** la instancia. |
| `onRestart()` | Una Activity **detenida** vuelve a iniciarse. |

- Todos son **opcionales**: los sobrescribimos con `override` solo si los necesitamos y **siempre llamando a `super`**.
- Aunque no los escribamos **siguen existiendo** en la clase Activity (simplemente están "vacíos").

### ¿Para qué sirve?

- Iniciar/detener tareas al pasar a primer o segundo plano.
- Pausar recursos que no se necesitan cuando la app no se ve (cámara, sensores, música…).
- Liberar recursos cuando la Activity deja de usarse.
- Prepararse para cambios de configuración (por ejemplo, **girar la pantalla** destruye y recrea la Activity).
- Detectar cuándo la app vuelve a estar disponible.

> Vídeo recomendado: [Ciclo de vida de una aplicación en Android](https://youtu.be/E1vAobJ8Fxo)

:::ejercicio Ejercicio 04 · Ejercicio4T4: Métodos del ciclo de vida en Logcat
Crea **Ejercicio4T4** y sobrescribe `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()` y `onDestroy()` mostrando con `Log.d()` cuándo se ejecuta cada uno. Después prueba:

1. Ejecutar la app (secuencia inicial).
2. Pulsar el botón **Inicio**.
3. Volver a abrir la app.
4. **Girar** el dispositivo.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio4t4/MainActivity.kt
package com.example.ejercicio4t4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.ejercicio4t4.ui.theme.Ejercicio4T4Theme

class MainActivity : ComponentActivity() {

    private val TAG = "CicloVida"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() -> Se crea la Activity")
        enableEdgeToEdge()
        setContent {
            Ejercicio4T4Theme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Mira la consola de Logcat", fontSize = 24.sp)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() -> La Activity empieza a ser visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() -> En marcha: el usuario puede interactuar")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() -> La Activity pierde el primer plano")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() -> La Activity ya no es visible")
    }

    override fun onRestart() {           // opcional, ayuda a entender el paso 3
        super.onRestart()
        Log.d(TAG, "onRestart() -> La Activity detenida vuelve a iniciarse")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() -> La Activity se destruye")
    }
}
```
**Resultados esperados** (filtra Logcat por `tag:CicloVida`):

| Prueba | Métodos que se ejecutan |
|---|---|
| 1. Ejecutar la app | `onCreate` → `onStart` → `onResume` |
| 2. Pulsar Inicio | `onPause` → `onStop` |
| 3. Volver a abrir | `onRestart` → `onStart` → `onResume` |
| 4. Girar el dispositivo | `onPause` → `onStop` → `onDestroy` → `onCreate` → `onStart` → `onResume` |

```logcat
D  onCreate() -> Se crea la Activity
D  onStart() -> La Activity empieza a ser visible
D  onResume() -> En marcha: el usuario puede interactuar
D  onPause() -> La Activity pierde el primer plano
D  onStop() -> La Activity ya no es visible
D  onRestart() -> La Activity detenida vuelve a iniciarse
D  onStart() -> La Activity empieza a ser visible
D  onResume() -> En marcha: el usuario puede interactuar
```
> Al **girar** la pantalla la Activity se **destruye y se vuelve a crear**: por eso más adelante usaremos `rememberSaveable` para no perder datos.
:::

## 4. Acceso a recursos

Al crear recursos, Android genera automáticamente la clase **`R`**, que contiene los **identificadores (ID)** de todo lo que hay en `res/`. `R` no guarda el contenido, solo el ID para acceder a él.

- Textos → `res/values/strings.xml`
- Imágenes → `res/drawable/`

### Formato del ID

`R.<tipo>.<nombre>` → por ejemplo **`R.string.saludo`** (texto) o **`R.drawable.logo`** (imagen).

### stringResource() y painterResource()

```xml file=app/src/main/res/values/strings.xml
<resources>
    <string name="app_name">MiApp</string>
    <string name="saludo">¡Hola!</string>
    <string name="desc_logo">Logotipo de la app</string>
</resources>
```
```kotlin file=MainActivity.kt
@Composable
fun Cabecera() {
    Column {
        // Texto desde strings.xml
        Text(text = stringResource(id = R.string.saludo))

        // Imagen desde res/drawable/logo.png
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.desc_logo)
        )
    }
}
```

> Reglas de nombres de recursos: **minúsculas, números y guion bajo** (`fondo_principal.png`). Nada de espacios, mayúsculas, guiones ni tildes.
