@id tema3
@numero 3
@titulo Introducción al lenguaje Kotlin
@resumen Qué es Kotlin, Logcat, variables, tipos, null safety, funciones, clases, colecciones, estructuras de control, lambdas y control de errores.

## 1. ¿Qué es Kotlin?

**Kotlin** es un lenguaje de programación **fuertemente tipado** desarrollado por **JetBrains** (los creadores de IntelliJ IDEA, en el que se basa Android Studio).

- Recibe influencias de **Groovy, Scala y C#**.
- Genera código para la **JVM** (Java Virtual Machine), para **JavaScript** y, en las últimas versiones, **ejecutables nativos**.
- Tiene muchas menos características que Scala, pero es **más familiar** y **compila más rápido**.

### Historia

- Creado en **2010** por JetBrains. Sus IDE estaban escritos en Java, un lenguaje poco conciso, y buscaban algo más moderno.
- Descartaron **Scala** por eficiencia y por ser "demasiado potente" para lo que necesitaban.
- Como no querían salir de la JVM, crearon **su propia versión mejorada de Java: Kotlin**.
- Nació para aplicaciones de escritorio, pero hoy es **multiplataforma** y desde **2017** es **lenguaje oficial de Android** (anunciado por Google).

### Características principales

| Característica | Qué aporta |
|---|---|
| **Sintaxis** | Código más simple y fácil de leer que Java. |
| **Control de nulos** | Mecanismo para evitar los famosos `NullPointerException` de Java. |
| **Curva de aprendizaje** | Muy suave: se parece mucho a Java. |
| **Comunidad** | Muy enfocada al *open source*: mucha documentación y código libre. |

### Kotlin vs Java (de un vistazo)

```kotlin
// Java
// public final class Persona { private final String nombre; public Persona(String nombre) {...} public String getNombre() {...} }

// Kotlin: lo mismo en una línea (getters generados automáticamente)
class Persona(val nombre: String)
```

> Empresas como Google, Netflix, Pinterest, Uber, Trello o Coursera usan Kotlin en sus apps.

## 2. Uso de Logcat en Android Studio

**Logcat** es la herramienta de Android Studio que **vuelca en una consola los mensajes del sistema** al ejecutar una app, incluidos los que escribimos nosotros con la clase `Log`.

- Se abre en **View → Tool Windows → Logcat**.
- Para usar `Log` hay que importarla: `import android.util.Log`.

### Métodos de la clase Log

Todos reciben dos parámetros: la **etiqueta (tag)** de tipo `String?` y el **mensaje (msg)** de tipo `String`.

```kotlin
import android.util.Log

Log.v("TAG", "Mensaje verbose")
Log.d("TAG", "Este es un mensaje de debug")
Log.i("TAG", "Este es un mensaje de info")
Log.w("TAG", "Este es un mensaje de aviso")
Log.e("TAG", "Este es un mensaje de error")
```

### Prioridades (de menor a mayor)

| Letra | Nivel | Método |
|---|---|---|
| V | Verbose (la más baja) | `Log.v()` |
| D | Debug | `Log.d()` |
| I | Info | `Log.i()` |
| W | Warning | `Log.w()` |
| E | Error | `Log.e()` |
| F | Fatal | `Log.wtf()` |
| S | Silent (la más alta, nunca imprime nada) | — |

### Filtrar mensajes

- Escribe en el filtro el **paquete** de tu app: `package:com.example.miapp`
- Filtra por prioridad con `level:`, por ejemplo `level:debug`
- Busca por tu etiqueta con **Ctrl + F** (o filtrando `tag:TAG`).

> Consejo: **limpia la consola** de Logcat antes de volver a ejecutar para no mezclar mensajes de ejecuciones distintas.

## 3. Programación básica en Kotlin

### Comentarios

```kotlin
// Comentario de una línea
Log.d("TAG", "¡Hola!")

/* Comentario de bloque:
Log.d("TAG", "primer mensaje")
Log.d("TAG", "segundo mensaje") */
```

### Variables (`var`) y constantes (`val`)

- `var` → **variable**: su valor puede cambiar.
- `val` → **constante**: una vez inicializada **no se puede modificar**. Es la única diferencia con `var`.
- Si inicializas al declarar, Kotlin **infiere el tipo** (aun así es recomendable indicarlo).

```kotlin
var nombre: String          // declaración
var edad: Int
nombre = "Pepe"             // asignación
edad = 25

var ciudad: String = "Plasencia"   // tipo explícito
var pais = "España"                // tipo inferido -> String

val dni: String
dni = "12345678A"           // se puede asignar UNA vez
val curso = 2               // constante inferida como Int
// curso = 3                // ERROR: Val cannot be reassigned
```

### Imprimir variables en Logcat (plantillas de String)

El mensaje de `Log` es un `String`. Para otros tipos usamos `toString()` o, mejor, **plantillas** con `$`:

```kotlin
Log.d("TAG", nombre)
Log.d("TAG", edad.toString())
Log.d("TAG", "La edad es $edad")
Log.d("TAG", "El año que viene tendrá ${edad + 1}")   // expresiones entre ${ }
```

### Tipos de datos

| Tipo | Tamaño | Rango / precisión |
|---|---|---|
| `Byte` | 8 bits | -128 a 127 |
| `Short` | 16 bits | -32.768 a 32.767 |
| `Int` | 32 bits | -2.147.483.648 a 2.147.483.647 |
| `Long` | 64 bits | ±9,22 · 10^18 |
| `Float` | 32 bits | hasta 7 decimales (se escribe con `f`) |
| `Double` | 64 bits | hasta 16 decimales |
| `Boolean` | — | `true` / `false` |
| `String` | — | cadenas de texto |

```kotlin
var nombre: String = "Pepe"
var edad: Int = 5
var precio: Double = 15.50
var altura: Float = 1.73f        // Float SIEMPRE con f o F
var mayorDeEdad: Boolean = true
```

### Operadores

| Tipo | Operadores |
|---|---|
| Aritméticos | `+  -  *  /  %` |
| Incremento / decremento | `++  --` |
| Relacionales | `==  !=  <  >  <=  >=` |
| Lógicos | `&&  ||  !` |

> ¡Ojo! Dividir dos `Int` da un `Int` (`7 / 2 = 3`). Para decimales usa `Double`: `7 / 2.0 = 3.5`.

:::ejercicio Ejercicio 01 · Notas de una asignatura
Realiza un programa en Kotlin que muestre por pantalla el nombre de una asignatura, la nota del primer examen, la nota del segundo examen y la nota media. Almacena previamente el nombre y las notas en variables del tipo adecuado. Muéstralo en Logcat así:

```text
Asignatura: Matemáticas
Nota primer examen: 8.0
Nota segundo examen: 9.0
Nota media: 8.5
```
---solucion---
Crea un proyecto **Empty Activity** (p. ej. `Ejercicio01T3`). En este tema solo trabajamos con Logcat, así que toda la lógica va en `MainActivity.kt` y se llama desde `onCreate()`.

```kotlin file=app/src/main/java/com/example/ejercicio01t3/MainActivity.kt
package com.example.ejercicio01t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ejercicio01()
    }

    private fun ejercicio01() {
        val asignatura: String = "Matemáticas"
        val nota1: Double = 8.0
        val nota2: Double = 9.0
        val media: Double = (nota1 + nota2) / 2

        Log.d("TAG", "Asignatura: $asignatura")
        Log.d("TAG", "Nota primer examen: $nota1")
        Log.d("TAG", "Nota segundo examen: $nota2")
        Log.d("TAG", "Nota media: $media")
    }
}
```
```logcat
D  Asignatura: Matemáticas
D  Nota primer examen: 8.0
D  Nota segundo examen: 9.0
D  Nota media: 8.5
```
:::

:::ejercicio Ejercicio 02 · Billetes de la cartera
Llevas en tu cartera 130 euros en billetes de 50 y de 10. Partiendo de guardar los 130 € en una variable, el programa debe decir cuántos billetes hay de cada tipo:

```text
130 euros hacen un total de: 2 billetes de 50 euros y 3 billetes de 10 euros
```
---solucion---
La clave son la **división entera** (`/`) y el **resto** (`%`).

```kotlin file=app/src/main/java/com/example/ejercicio02t3/MainActivity.kt
package com.example.ejercicio02t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ejercicio02()
    }

    private fun ejercicio02() {
        val dinero: Int = 130
        val billetes50: Int = dinero / 50          // 130 / 50 = 2
        val resto: Int = dinero % 50               // 130 % 50 = 30
        val billetes10: Int = resto / 10           // 30 / 10 = 3

        Log.d("TAG", "$dinero euros hacen un total de: $billetes50 billetes de 50 euros y $billetes10 billetes de 10 euros")
    }
}
```
```logcat
D  130 euros hacen un total de: 2 billetes de 50 euros y 3 billetes de 10 euros
```
:::

### Null safety (seguridad frente a nulos)

En Kotlin **ninguna variable normal puede ser `null`**. Esto da error de compilación:

```kotlin
var nombre: String = null   // ERROR
var edad: Int = null        // ERROR
```

Para permitir nulos se añade `?` al tipo (**tipo nullable**):

```kotlin
var nombre: String? = null  // OK
var edad: Int? = null       // OK
```

Para operar con un nullable de forma segura usamos `?.let { } ?: run { }`:

```kotlin
nombre?.let {
    Log.d("TAG", it)          // se ejecuta si nombre != null (it = nombre)
} ?: run {
    Log.d("TAG", "La variable nombre no tiene valor o tiene un valor nulo")
}
```

> `?.` es el **operador de llamada segura** y `?:` es el **operador Elvis** ("si lo de la izquierda es null, haz lo de la derecha").

:::ejercicio Ejercicio 03 · Usuario y password con null safety
Guarda en una variable un nombre de usuario y en otra variable **nullable** un password. Controla con null safety que haya password para dar la bienvenida. Prueba ambos casos:

1. Si password tiene valor: `Bienvenido nombreUsuario` y `El password introducido es passwordUsuario`
2. Si password es null: `Error: nombreUsuario no ha introducido el password requerido`
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio03t3/MainActivity.kt
package com.example.ejercicio03t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comprobarPassword("Manuel", "kotlin123")   // caso 1: con password
        comprobarPassword("Manuel", null)          // caso 2: password nulo
    }

    private fun comprobarPassword(usuario: String, password: String?) {
        password?.let {
            Log.d("TAG", "Bienvenido $usuario")
            Log.d("TAG", "El password introducido es $it")
        } ?: run {
            Log.d("TAG", "Error: $usuario no ha introducido el password requerido")
        }
    }
}
```
Versión "como en los apuntes", con las variables directamente en `onCreate`:

```kotlin
val usuario: String = "Manuel"
var password: String? = "kotlin123"   // cambia a null para probar el otro caso

password?.let {
    Log.d("TAG", "Bienvenido $usuario")
    Log.d("TAG", "El password introducido es $password")
} ?: run {
    Log.d("TAG", "Error: $usuario no ha introducido el password requerido")
}
```
```logcat
D  Bienvenido Manuel
D  El password introducido es kotlin123
D  Error: Manuel no ha introducido el password requerido
```
:::

### Funciones

- Se declaran con `fun`. Por defecto son **públicas**; con `private` solo se pueden usar dentro de su clase.
- Parámetros: `nombre: Tipo`, separados por comas.
- Tipo de retorno después de `:`. Si no devuelve nada el tipo es `Unit` (se puede omitir).
- Si el cuerpo es una sola expresión se puede escribir con `=`.

```kotlin
fun saludar() {                                   // pública, sin parámetros
    Log.d("TAG", "Hola")
}

private fun datosAlumno(nombre: String, edad: Int) {   // : Unit implícito
    Log.d("TAG", "Nombre: $nombre")
    Log.d("TAG", "Edad: $edad")
}

private fun calcularDoble(numero: Int): Int {
    return numero * 2
}

private fun calcularTriple(numero: Int): Int = numero * 3   // función de una sola expresión

// Uso
saludar()
datosAlumno("Pepe", 25)
val resultado: Int = calcularDoble(5)   // 10
```

:::ejercicio Ejercicio 04 · Funciones con tres números
Crea tres variables enteras (`num1`, `num2`, `num3`) e implementa:

1. `mostrarNumeros` → recibe los tres números y los muestra.
2. `sumaNumeros` → guarda la suma en una variable y la muestra.
3. `calcularMedia` → **devuelve** la media; se guarda en una variable y se muestra.
4. `operaciones` → recibe la media y `num3`, guarda media × num3 y lo muestra.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio04t3/MainActivity.kt
package com.example.ejercicio04t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val num1: Int = 4
        val num2: Int = 7
        val num3: Int = 10

        mostrarNumeros(num1, num2, num3)
        sumaNumeros(num1, num2, num3)

        val media: Double = calcularMedia(num1, num2, num3)
        Log.d("TAG", "La media es: $media")

        operaciones(media, num3)
    }

    private fun mostrarNumeros(num1: Int, num2: Int, num3: Int) {
        Log.d("TAG", "Números: $num1, $num2 y $num3")
    }

    private fun sumaNumeros(num1: Int, num2: Int, num3: Int) {
        val suma: Int = num1 + num2 + num3
        Log.d("TAG", "La suma es: $suma")
    }

    // Dividimos entre 3.0 para obtener decimales
    private fun calcularMedia(num1: Int, num2: Int, num3: Int): Double = (num1 + num2 + num3) / 3.0

    private fun operaciones(media: Double, num3: Int) {
        val resultado: Double = media * num3
        Log.d("TAG", "Media x num3 = $resultado")
    }
}
```
```logcat
D  Números: 4, 7 y 10
D  La suma es: 21
D  La media es: 7.0
D  Media x num3 = 70.0
```
:::

### Clases

```kotlin
class Persona() {
    // constructores, propiedades y funciones (métodos)
    fun presentacion() {
        Log.d("TAG", "¡Hola! Me presento")
    }
}

val alumno = Persona()      // instancia (sin "new")
alumno.presentacion()
```

> Los métodos deben ser **públicos** para poder llamarlos desde fuera de la clase.

#### Propiedades, getters y setters

Kotlin genera **automáticamente** `get()` y `set()` de cada propiedad. Podemos personalizarlos (la propiedad debe ser `var` para tener `set`). `field` es el valor real almacenado:

```kotlin
class Persona() {
    var nombre: String = "Pepe"
        get() {
            return "Efectivamente, soy Pepe"
        }

    var edad: Int = 25
        set(value) {
            field = if (value >= 18) value else 25
        }
}

val persona1 = Persona()
Log.d("TAG", "¡Hola! Mi nombre es ${persona1.nombre}")  // ...Efectivamente, soy Pepe
persona1.edad = 50
Log.d("TAG", "¡Hola! Mi edad es ${persona1.edad}")      // ...50
persona1.edad = 10
Log.d("TAG", "Edad: ${persona1.edad}")                  // 25 (el setter lo impide)
```

#### Constructores

- **Principal**: va en la cabecera de la clase y **no tiene cuerpo**.
- **Secundario**: se declara dentro con `constructor`, **sí tiene cuerpo** y debe llamar al principal con `this(...)`.

```kotlin
class Persona(var nombre: String, var edad: Int) {      // constructor principal
    var telefono: Int = 111222333

    constructor(nombre: String, edad: Int, nuevoTelefono: Int) : this(nombre, edad) {
        telefono = nuevoTelefono                         // constructor secundario
    }
}

val a = Persona("Pepe", 25)               // usa el principal
val b = Persona("Ana", 30, 666777888)     // usa el secundario
```

### Data classes

Clases pensadas para **guardar datos como un paquete**. Generan solas `toString()`, `equals()`, `copy()`…

```kotlin
data class DatosPersona(
    var nombre: String,
    var edad: Int
)

class Persona(var data: DatosPersona) {
    fun presentacion() {
        Log.d("TAG", "Mi nombre es ${data.nombre} y mi edad es ${data.edad}")
    }
}

val datosAlumno = DatosPersona("Pepe", 26)
val alumno = Persona(datosAlumno)
alumno.presentacion()
Log.d("TAG", datosAlumno.toString())   // DatosPersona(nombre=Pepe, edad=26)
```

### Interfaces

Una **interfaz** solo declara las cabeceras de las funciones. La clase que la implementa (`: Interfaz`) está **obligada** a implementarlas con `override`.

```kotlin
interface InterfazPersona {
    fun mostrarEdad()
}

class Persona(private val nombre: String, private val edad: Int) : InterfazPersona {
    fun presentacion() {
        Log.d("TAG", "¡Hola! Mi nombre es $nombre y mi edad es $edad")
    }

    override fun mostrarEdad() {
        Log.d("TAG", "Tengo $edad años")
    }
}
```

:::ejercicio Ejercicio 05 · ProgramadorApp
Crea el proyecto **ProgramadorApp** con:

1. Una clase `Programador`.
2. Una interfaz `ProgramadorInterface`.
3. Una data class `ProgrammerData`.

Reglas: la data class tiene tres constantes `name`, `age` y `language`. La interfaz tiene `getProgrammerData()` que devuelve un `ProgrammerData`. `Programador` implementa la interfaz y tiene **tres funciones privadas** que devuelven nombre, edad y lenguaje; `getProgrammerData()` devuelve un `ProgrammerData` con esos datos. Desde un objeto `Programador` solo se podrá acceder a `getProgrammerData()`.
---solucion---
Puedes crear cada elemento en su propio fichero (clic derecho en el paquete → **New → Kotlin Class/File**).

:::tree
ProgramadorApp/app/src/main/java/com/example/programadorapp/
├── MainActivity.kt
├── Programador.kt
├── ProgramadorInterface.kt
└── ProgrammerData.kt
:::

```kotlin file=app/src/main/java/com/example/programadorapp/ProgrammerData.kt
package com.example.programadorapp

data class ProgrammerData(
    val name: String,
    val age: Int,
    val language: String
)
```
```kotlin file=app/src/main/java/com/example/programadorapp/ProgramadorInterface.kt
package com.example.programadorapp

interface ProgramadorInterface {
    fun getProgrammerData(): ProgrammerData
}
```
```kotlin file=app/src/main/java/com/example/programadorapp/Programador.kt
package com.example.programadorapp

class Programador : ProgramadorInterface {

    private fun getName(): String = "Manuel"

    private fun getAge(): Int = 20

    private fun getLanguage(): String = "Kotlin"

    override fun getProgrammerData(): ProgrammerData {
        return ProgrammerData(getName(), getAge(), getLanguage())
    }
}
```
```kotlin file=app/src/main/java/com/example/programadorapp/MainActivity.kt
package com.example.programadorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val programador = Programador()
        // programador.getName()  -> ERROR: es private, solo vemos getProgrammerData()
        val datos: ProgrammerData = programador.getProgrammerData()

        Log.d("TAG", "Nombre: ${datos.name}")
        Log.d("TAG", "Edad: ${datos.age}")
        Log.d("TAG", "Lenguaje: ${datos.language}")
        Log.d("TAG", datos.toString())
    }
}
```
```logcat
D  Nombre: Manuel
D  Edad: 20
D  Lenguaje: Kotlin
D  ProgrammerData(name=Manuel, age=20, language=Kotlin)
```
:::

## 4. Colecciones

Un **listado** es un conjunto **ordenado** de valores del **mismo tipo**; cada elemento tiene un **índice** (empieza en 0).

| Tipo | Mutabilidad | Se crea con |
|---|---|---|
| `List` | **Inmutable**: no se puede modificar | `listOf()` |
| `ArrayList` | **Mutable**: añadir, modificar, borrar, ordenar… | `arrayListOf()` |

### List (inmutable)

```kotlin
var list: List<String> = listOf()                  // vacía, indicando el tipo de listado
var list2 = listOf<String>()                       // vacía, indicando el tipo de valores

var numeros: List<String> = listOf("Uno", "Dos", "Tres")
var numeros2 = listOf<String>("Uno", "Dos", "Tres")

val valor: String = numeros[0]          // "Uno"
val valor2: String = numeros.get(1)     // "Dos"

numeros.size              // 3
numeros.indexOf("Dos")    // 1
numeros.contains("Tres")  // true
```

### ArrayList (mutable)

```kotlin
var arrayList: ArrayList<String> = arrayListOf("Uno", "Dos", "Tres")
var vacio = arrayListOf<String>()

val v: String = arrayList[0]            // leer por índice
val v2: String = arrayList.get(0)

arrayList[0] = "Cero"                   // modificar por índice
arrayList.add(0, "Inicio")              // insertar en una posición
arrayList.add("Final")                  // añadir al final
arrayList.set(1, "Uno bis")             // modificar con set
arrayList.remove("Dos")                 // eliminar por valor
arrayList.removeAt(0)                   // eliminar por posición
arrayList.isEmpty()                     // false
arrayList.contains("Tres")              // true
arrayList.size
arrayList.sort()                        // ordenar
```

## 5. Estructuras de control

### Condicionales

| Tipo | Palabra reservada |
|---|---|
| Simple | `if` |
| Doble | `if … else` |
| Múltiple | `when` |

```kotlin
// Simple (si solo hay una sentencia, las llaves son opcionales)
if (edad >= 18) {
    Log.d("TAG", "Es mayor de edad")
}

// Doble
if (lenguaje == "Kotlin") {
    Log.d("TAG", "El lenguaje de programación elegido es Kotlin")
} else {
    Log.d("TAG", "El lenguaje de programación no es Kotlin")
}

// if como expresión: su resultado se asigna a una variable
var edad: Int = 25
var esMayorDeEdad: Boolean = if (edad >= 18) {
    true
} else {
    false
}
```

#### when (la "navaja suiza", más potente que el switch de Java)

```kotlin
var lenguaje: String = "Kotlin"
when (lenguaje) {
    "Kotlin" -> {
        Log.d("TAG", "Se ha seleccionado Kotlin")      // varias sentencias -> llaves
        Log.d("TAG", "Esta es su primera opción")
    }
    "Java", "Scala" -> Log.d("TAG", "Lenguaje de la JVM")   // varios valores
    "Python" -> Log.d("TAG", "Se ha seleccionado Python")
    else -> Log.d("TAG", "Se ha seleccionado otro lenguaje")
}

var numero: Int = 10
when (numero) {
    0 -> Log.d("TAG", "El número es 0")
    1 -> Log.d("TAG", "El número es 1")
    in 5..15 -> Log.d("TAG", "El número está entre 5 y 15")   // rangos
}

when {                                       // when sin argumento = cadena de if
    numero == 0 -> Log.d("TAG", "El número es 0")
    numero > 1 -> Log.d("TAG", "El número es mayor que 1")
    numero in 5..15 -> Log.d("TAG", "El número está entre 5 y 15")
}
```

> En `when` solo se ejecuta la **primera** rama que cumple la condición.

:::ejercicio Ejercicio 06 · Calificación según la nota
Guarda la nota de un alumno (entero de 0 a 10) y muestra en Logcat su calificación: 0–4 Suspenso, 5–6 Bien, 7–8 Notable, 9–10 Sobresaliente.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio06t3/MainActivity.kt
package com.example.ejercicio06t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nota: Int = 7
        mostrarCalificacion(nota)
    }

    private fun mostrarCalificacion(nota: Int) {
        when (nota) {
            in 0..4 -> Log.d("TAG", "Nota $nota: Suspenso")
            in 5..6 -> Log.d("TAG", "Nota $nota: Bien")
            in 7..8 -> Log.d("TAG", "Nota $nota: Notable")
            in 9..10 -> Log.d("TAG", "Nota $nota: Sobresaliente")
            else -> Log.e("TAG", "Nota no válida: debe estar entre 0 y 10")
        }
    }
}
```
```logcat
D  Nota 7: Notable
```
:::

:::ejercicio Ejercicio 07 · Calculadora con opción
Guarda una opción del 1 al 4 y dos enteros (`num1`, `num2`). Según la opción: 1 suma, 2 resta, 3 multiplica, 4 divide num1 entre num2. Implementa cada operación en una función.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio07t3/MainActivity.kt
package com.example.ejercicio07t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val opcion: Int = 4
        val num1: Int = 20
        val num2: Int = 8

        when (opcion) {
            1 -> Log.d("TAG", "$num1 + $num2 = ${sumar(num1, num2)}")
            2 -> Log.d("TAG", "$num1 - $num2 = ${restar(num1, num2)}")
            3 -> Log.d("TAG", "$num1 * $num2 = ${multiplicar(num1, num2)}")
            4 -> {
                if (num2 == 0) {
                    Log.e("TAG", "No se puede dividir entre 0")
                } else {
                    Log.d("TAG", "$num1 / $num2 = ${dividir(num1, num2)}")
                }
            }
            else -> Log.e("TAG", "Opción no válida (1-4)")
        }
    }

    private fun sumar(a: Int, b: Int): Int = a + b
    private fun restar(a: Int, b: Int): Int = a - b
    private fun multiplicar(a: Int, b: Int): Int = a * b
    private fun dividir(a: Int, b: Int): Double = a.toDouble() / b
}
```
```logcat
D  20 / 8 = 2.5
```
:::

### Estructuras repetitivas (bucles)

#### for

Se usa cuando el número de repeticiones es **conocido**.

```kotlin
val arrayList = arrayListOf("Ana", "Luis", "Marta", "Pedro", "Sara", "Juan", "Eva", "Iván", "Lola", "Raúl", "Noa")

for (nombre: String in arrayList) {                  // "for-each": recorre los elementos
    Log.d("TAG", "Nombre: $nombre")
}

for (position: Int in 0..5) {                        // intervalo cerrado: 0,1,2,3,4,5
    Log.d("TAG", "Nombre: ${arrayList.get(position)}")
}

for (position: Int in 0 until arrayList.size) {      // until: hasta size - 1 (evita salirse)
    Log.d("TAG", "Nombre: ${arrayList[position]}")
}

for (position: Int in 0..10 step 2) {                // de 2 en 2: 0,2,4,6,8,10
    Log.d("TAG", "Nombre: ${arrayList[position]}")
}

for (position: Int in 10 downTo 0) {                 // hacia atrás: 10,9,...,0
    Log.d("TAG", "Nombre: ${arrayList[position]}")
}
```

#### while y do-while

```kotlin
var numero: Int = 1
while (numero <= 10) {          // 0 o más veces
    Log.d("TAG", "Número: $numero")
    numero++
}

var n: Int = 1
do {                            // al menos 1 vez
    Log.d("TAG", "Número: $n")
    n++
} while (n <= 10)
```

:::ejercicio Ejercicio 08 · Los 80 primeros números primos
Rellena un listado con los 80 primeros números primos y muéstralo en Logcat. Ejemplo del formato: `[0] -> 1; [1] -> 2; [2] -> 3; [3] -> 5; [4] -> 7; [5] -> 11; …`
---solucion---
Siguiendo el ejemplo de los apuntes, el listado empieza por el **1** (en matemáticas el 1 no se considera primo; si tu profesor lo quiere sin el 1, cambia `numero = 1` por `numero = 2`).

```kotlin file=app/src/main/java/com/example/ejercicio08t3/MainActivity.kt
package com.example.ejercicio08t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val primos: ArrayList<Int> = arrayListOf()
        var numero: Int = 1

        while (primos.size < 80) {
            if (esPrimo(numero)) {
                primos.add(numero)
            }
            numero++
        }
        mostrarListado(primos)
    }

    // Un número es "primo" si solo es divisible entre 1 y él mismo
    private fun esPrimo(numero: Int): Boolean {
        if (numero < 1) return false
        for (divisor: Int in 2 until numero) {
            if (numero % divisor == 0) {
                return false
            }
        }
        return true
    }

    private fun mostrarListado(lista: ArrayList<Int>) {
        var texto = ""
        for (i: Int in 0 until lista.size) {
            texto += "[$i] -> ${lista[i]}; "
        }
        Log.d("TAG", texto)
    }
}
```
```logcat
D  [0] -> 1; [1] -> 2; [2] -> 3; [3] -> 5; [4] -> 7; [5] -> 11; [6] -> 13; [7] -> 17; ... [79] -> 401;
```
:::

:::ejercicio Ejercicio 09 · Notas de 6 alumnos
Clase de 6 alumnos (Pepe, Juan, Ana, Marta, Pedro y María). Crea un listado con la nota media de cada uno y, según una opción guardada en una variable:

1. Muestra las notas del listado.
2. Muestra el nombre y la nota del alumno con la media más alta (usa otro listado con los nombres).

Cada operación en una función.
---solucion---
Los dos listados van "en paralelo": la posición `i` de `nombres` corresponde a la posición `i` de `notas`.

```kotlin file=app/src/main/java/com/example/ejercicio09t3/MainActivity.kt
package com.example.ejercicio09t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val nombres: List<String> = listOf("Pepe", "Juan", "Ana", "Marta", "Pedro", "María")
    private val notas: ArrayList<Double> = arrayListOf(6.5, 4.75, 8.25, 9.5, 7.0, 5.5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val opcion: Int = 2
        when (opcion) {
            1 -> mostrarNotas()
            2 -> mostrarMejorAlumno()
            else -> Log.e("TAG", "Opción no válida")
        }
    }

    private fun mostrarNotas() {
        for (i: Int in 0 until notas.size) {
            Log.d("TAG", "${nombres[i]}: ${notas[i]}")
        }
    }

    private fun mostrarMejorAlumno() {
        var posMejor: Int = 0
        for (i: Int in 1 until notas.size) {
            if (notas[i] > notas[posMejor]) {
                posMejor = i
            }
        }
        Log.d("TAG", "La media más alta es de ${nombres[posMejor]} con un ${notas[posMejor]}")
    }
}
```
```logcat
D  La media más alta es de Marta con un 9.5
```
:::

## 6. Lambdas y funciones de orden superior

### Lambdas

Una **lambda** es una función corta escrita **entre llaves** que se puede guardar en una variable o pasar como argumento.

```kotlin
{ numero -> numero * 2 }                                  // recibe un número y devuelve su doble

val doble: (Int) -> Int = { numero -> numero * 2 }        // tipo a la izquierda…
val doble2 = { numero: Int -> numero * 2 }                // …o en el parámetro
val resultado: Int = doble(5)                             // 10

// Con un único parámetro se puede usar "it"
val triple: (Int) -> Int = { it * 3 }
```

### Funciones de orden superior

Reciben (o devuelven) otra función. Se usan muchísimo con colecciones:

```kotlin
val numeros: List<Int> = listOf(1, 2, 3, 4, 5)

val pares: List<Int> = numeros.filter { it % 2 == 0 }     // [2, 4]       -> filtrar varios
val primero: Int? = numeros.find { it > 3 }               // 4 (o null)   -> buscar uno
val dobles: List<Int> = numeros.map { it * 2 }            // [2,4,6,8,10] -> transformar
numeros.forEach { Log.d("TAG", "Número: $it") }           // recorrer

// Encadenadas: cada una trabaja con el resultado de la anterior
numeros.filter { it > 2 }.map { it * 2 }.forEach { Log.d("TAG", "$it") }   // 6, 8, 10
```

| Función | Devuelve | Para qué |
|---|---|---|
| `filter { }` | `List` | Elementos que cumplen la condición |
| `find { }` | Elemento o `null` | El **primero** que cumple la condición |
| `map { }` | `List` | Nueva lista transformando cada elemento |
| `forEach { }` | `Unit` | Ejecutar una acción por cada elemento |

:::ejercicio Ejercicio 10 · Precios con funciones de orden superior
Precios de 6 productos: 8.50, 15.0, 4.50, 20.0, 12.0 y 7.0. Con funciones de orden superior:

1. Lista con los productos de 10 € o más.
2. A esos productos aplícales un 10 % de descuento.
3. Muestra el precio final de cada uno.
4. Busca en la lista original el primer producto de más de 18 € y muéstralo.
5. Haz 1, 2 y 3 encadenando las funciones.
---solucion---
```kotlin file=app/src/main/java/com/example/ejercicio10t3/MainActivity.kt
package com.example.ejercicio10t3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val precios: List<Double> = listOf(8.50, 15.0, 4.50, 20.0, 12.0, 7.0)

        // 1. Productos de 10 € o más
        val caros: List<Double> = precios.filter { it >= 10.0 }
        Log.d("TAG", "Productos >= 10 €: $caros")

        // 2. Aplicar un 10 % de descuento
        val conDescuento: List<Double> = caros.map { it * 0.90 }

        // 3. Mostrar el precio final de cada producto
        conDescuento.forEach { Log.d("TAG", "Precio final: $it €") }

        // 4. Primer producto de más de 18 €
        val mas18: Double? = precios.find { it > 18.0 }
        mas18?.let {
            Log.d("TAG", "Primer producto de más de 18 €: $it €")
        } ?: run {
            Log.d("TAG", "No hay productos de más de 18 €")
        }

        // 5. Apartados 1, 2 y 3 encadenados
        Log.d("TAG", "--- Encadenado ---")
        precios.filter { it >= 10.0 }
            .map { it * 0.90 }
            .forEach { Log.d("TAG", "Precio final: $it €") }
    }
}
```
```logcat
D  Productos >= 10 €: [15.0, 20.0, 12.0]
D  Precio final: 13.5 €
D  Precio final: 18.0 €
D  Precio final: 10.8 €
D  Primer producto de más de 18 €: 20.0 €
D  --- Encadenado ---
D  Precio final: 13.5 €
D  Precio final: 18.0 €
D  Precio final: 10.8 €
```
> Por la representación de los `Double`, alguno puede salir como `10.799999999999999`. Para redondear a 2 decimales: `String.format("%.2f", it)`.
:::

## 7. Control de errores

El **control de excepciones** permite gestionar los errores que ocurren **en tiempo de ejecución** sin que la app se cierre.

```kotlin
try {
    // Sentencia(s) que queremos proteger
} catch (e1: Excepcion1) {
    // Control de la excepción 1
} catch (e2: Excepcion2) {
    // Control de la excepción 2
} finally {
    // OPCIONAL: se ejecuta SIEMPRE (haya error o no)
}
```

### Ejemplo: IndexOutOfBoundsException

Si recorremos el listado **hasta su tamaño** (con `..`) intentamos acceder a un índice que no existe:

```kotlin
val nombres: ArrayList<String> = arrayListOf("Ana", "Luis", "Marta")

try {
    for (i: Int in 0..nombres.size) {          // 0..3 -> el índice 3 NO existe
        Log.d("TAG", "Nombre: ${nombres[i]}")
    }
} catch (e: IndexOutOfBoundsException) {
    Log.e("TAG", "Error: te has salido del listado -> ${e.message}")
} finally {
    Log.d("TAG", "Fin del recorrido")
}
```

Otros ejemplos habituales:

```kotlin
try {
    val numero: Int = "abc".toInt()            // NumberFormatException
} catch (e: NumberFormatException) {
    Log.e("TAG", "El texto no es un número")
}

try {
    val r = 10 / 0                             // ArithmeticException
} catch (e: ArithmeticException) {
    Log.e("TAG", "No se puede dividir entre 0")
} catch (e: Exception) {                       // captura cualquier otra
    Log.e("TAG", "Error inesperado: ${e.message}")
}
```

:::ejercicio Ejercicio 11 · BotSeguridadApp
Crea el proyecto **BotSeguridadApp** con:

1. Una función `botSeguridad()` llamada desde `onCreate()` del `MainActivity`, donde va la lógica del bot.
2. Una data class `Persona` con las constantes `name`, `age` y `hobbies` (listado de Strings).

Reglas: crea una variable `Persona` con tus datos. El bot debe:

- Si el nombre no es el tuyo → error y "Acceso denegado". Si lo es → mensaje de éxito y continúa.
- 0–17 años → menor de edad, "Acceso denegado".
- 65–100 años → demasiado mayor, acceso denegado.
- 18–64 años → acceso concedido y muestra el listado de hobbies.
---solucion---
:::tree
BotSeguridadApp/app/src/main/java/com/example/botseguridadapp/
├── MainActivity.kt
└── Persona.kt
:::

```kotlin file=app/src/main/java/com/example/botseguridadapp/Persona.kt
package com.example.botseguridadapp

data class Persona(
    val name: String,
    val age: Int,
    val hobbies: List<String>
)
```
```kotlin file=app/src/main/java/com/example/botseguridadapp/MainActivity.kt
package com.example.botseguridadapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val TAG = "BotSeguridad"
    private val miNombre = "Manuel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        botSeguridad()
    }

    private fun botSeguridad() {
        val persona = Persona("Manuel", 20, listOf("Programar en Kotlin", "Videojuegos", "Fútbol"))

        // 1. Comprobación del nombre
        if (persona.name != miNombre) {
            Log.e(TAG, "Error: ${persona.name} no es un usuario autorizado")
            Log.e(TAG, "Acceso denegado")
            return                          // no seguimos ejecutando
        }
        Log.i(TAG, "Nombre verificado correctamente. ¡Hola, ${persona.name}!")

        // 2. Comprobación de la edad
        when (persona.age) {
            in 0..17 -> {
                Log.e(TAG, "Eres menor de edad")
                Log.e(TAG, "Acceso denegado")
            }
            in 65..100 -> {
                Log.e(TAG, "Lo siento, eres demasiado mayor")
                Log.e(TAG, "Acceso denegado")
            }
            in 18..64 -> {
                Log.i(TAG, "Acceso concedido. ¡Bienvenido!")
                mostrarHobbies(persona.hobbies)
            }
            else -> Log.e(TAG, "Edad no válida. Acceso denegado")
        }
    }

    private fun mostrarHobbies(hobbies: List<String>) {
        try {
            Log.d(TAG, "Tus hobbies son:")
            hobbies.forEach { Log.d(TAG, " - $it") }
        } catch (e: Exception) {
            Log.e(TAG, "Error al mostrar los hobbies: ${e.message}")
        }
    }
}
```
```logcat
I  Nombre verificado correctamente. ¡Hola, Manuel!
I  Acceso concedido. ¡Bienvenido!
D  Tus hobbies son:
D   - Programar en Kotlin
D   - Videojuegos
D   - Fútbol
```
:::
