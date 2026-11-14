# Apuntes Kotlin · PMYDM

Web de apuntes (Temas 3 a 7) con login, panel de administración, resumen de cada apartado,
ejemplos de código y **todos los ejercicios resueltos** con cada fichero del proyecto
(`MainActivity.kt`, `strings.xml`, `Color.kt`, `build.gradle.kts`…).

## Cómo abrirla

- **Doble clic en `index.html`** (se abre en el navegador), o
- En VS Code: abre `Apuntes Kotlin.code-workspace` (File → Open Workspace from File…) → clic derecho en `index.html` →
  **Open with Live Server** (extensión recomendada al abrir el proyecto).

## Cuentas iniciales



## Panel de administración (solo admin)

- **Cuentas**: crear, eliminar, habilitar/deshabilitar, cambiar rol y contraseña.
- **Visibilidad de temas**: ocultar o mostrar cada tema
  - para **todas las cuentas** (incluidos admins),
  - para **todas las cuentas que no son admin**,
  - o para **una cuenta concreta**.
- **Datos**: descargar/importar copia de seguridad (.json), restablecer y registro de cambios.

> Las cuentas y permisos se guardan en el `localStorage` del navegador. Si abres la web en otro
> navegador u ordenador empezará con las 4 cuentas iniciales: usa *Descargar copia* / *Importar copia*.
> Es una web local sin servidor: el login sirve para organizar quién ve qué, no es seguridad real.

## Estructura

```
Apuntes Kotlin/
├── index.html              ← página principal (login + app)
├── css/estilos.css
├── js/
│   ├── db.js               ← cuentas, sesión y visibilidad (localStorage)
│   ├── app.js              ← navegación, visor de apuntes y buscador
│   ├── admin.js            ← panel de administración
│   └── contenido.js        ← apuntes generados (no editar a mano)
├── contenido/tema3.md … tema7.md   ← los apuntes en Markdown (edítalos aquí)
└── herramientas/build.py   ← regenera js/contenido.js
```

## Editar o ampliar los apuntes

1. Edita `contenido/temaX.md` (o crea `tema8.md` copiando la cabecera `@id`, `@numero`, `@titulo`, `@resumen`).
2. Ejecuta en la terminal de VS Code: `python herramientas/build.py`
   (o **Terminal → Run Task… → Generar apuntes**).
3. Recarga la página.

Formato de un ejercicio:

````
:::ejercicio Ejercicio 01 · Título
Enunciado…
---solucion---
```kotlin file=app/src/main/java/com/example/app/MainActivity.kt
// código
```
:::
````
