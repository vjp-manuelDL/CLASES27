# Apuntes Kotlin · PMYDM

Web de apuntes (Temas 3 a 7) con login, panel de administración, resumen de cada apartado,
ejemplos de código y **todos los ejercicios resueltos** con cada fichero del proyecto
(`MainActivity.kt`, `strings.xml`, `Color.kt`, `build.gradle.kts`…).

## Cómo abrirla

- **Doble clic en `index.html`** (se abre en el navegador), o
- En VS Code: abre `Apuntes Kotlin.code-workspace` (File → Open Workspace from File…) → clic derecho en `index.html` →
  **Open with Live Server** (extensión recomendada al abrir el proyecto).

## Cuentas iniciales

Hay 4 cuentas: `Manuel` (admin), `Manuel2`, `Manuel3` y `Manuel4`.

## Panel de administración (solo admin)

- **Cuentas**: crear, eliminar, habilitar/deshabilitar, cambiar rol y contraseña.
- **Visibilidad de temas**: ocultar o mostrar cada tema
  - para **todas las cuentas** (incluidos admins),
  - para **todas las cuentas que no son admin**,
  - o para **una cuenta concreta**.
- **Sincronización y datos**: token de GitHub, copia de seguridad (.json), restablecer y registro de cambios.

## Cuentas compartidas (cambios al instante en todos los dispositivos)

Las cuentas, contraseñas y permisos se guardan en `datos/cuentas.json` dentro del repositorio
(configurado en `js/config.js`). Todos los navegadores leen ese archivo y comprueban cambios cada
10 segundos y al volver a la pestaña. Por eso:

- Si cambias una contraseña, **la nueva vale al momento** en el PC, en GitHub Pages y en cualquier navegador.
- Las sesiones abiertas con la contraseña antigua **se cierran solas** (también si deshabilitas o eliminas la cuenta).
- Los cambios de visibilidad de temas aparecen sin recargar.

**Configuración (una vez, solo el admin):**

1. Crea un token: GitHub → Settings → Developer settings → **Fine-grained tokens** → Generate new token →
   *Only select repositories*: `CLASES27` → *Repository permissions → Contents*: **Read and write**.
2. Entra en la web como `Manuel` → **Panel admin → Sincronización y datos** → pega el token → **Guardar token**.
   La primera vez se crea `datos/cuentas.json` en el repo con las cuentas actuales.
3. Haz lo mismo en cada navegador desde el que quieras **administrar**. Los usuarios normales no necesitan nada.

Cada cambio del panel admin crea un commit en el repo («Apuntes Kotlin: …»). Haz *Pull* en GitHub Desktop
antes de subir tus cambios para no tener conflictos con `datos/cuentas.json`.

### Si una contraseña nueva no funciona en el móvil

1. ¿Aparece el aviso naranja **«Ahora mismo las cuentas solo existen en este navegador»** en el panel admin?
   Entonces falta el token: haz los 3 pasos de arriba.
2. Comprueba que la carpeta del repo se llama exactamente **`ApuntesKotlin`** (sin espacio), igual que en
   `js/config.js`. Si la renombras, cambia también esa ruta.
3. En el panel admin → *Sincronización y datos* debe poner **☁️ Sincronizado con GitHub**.
   Pulsa **🔄 Comprobar ahora** para forzar la sincronización.
4. En el móvil, cierra y vuelve a abrir la pestaña (GitHub Pages tarda ~1 minuto en publicar los cambios de archivos,
   pero las cuentas se leen de la API al momento).

> El repo debe ser **público** para que los usuarios sin token puedan leer las cuentas (y para GitHub Pages gratis).
> En el archivo solo se guardan *hashes* de las contraseñas, pero es una web estática: el login sirve para organizar
> quién ve qué, no es seguridad real. Sin conexión se usan los últimos datos guardados en el navegador.

## Estructura

```
ApuntesKotlin/
├── index.html              ← página principal (login + app)
├── css/estilos.css
├── js/
│   ├── config.js           ← repositorio/archivo donde se guardan las cuentas
│   ├── db.js               ← cuentas, sesión, visibilidad y sincronización con GitHub
│   ├── app.js              ← navegación, visor de apuntes y buscador
│   ├── admin.js            ← panel de administración
│   └── contenido.js        ← apuntes generados (no editar a mano)
├── contenido/tema3.md … tema7.md   ← los apuntes en Markdown (edítalos aquí)
├── herramientas/build.py   ← regenera js/contenido.js
└── datos/cuentas.json      ← cuentas compartidas (lo crea y actualiza la web; no editar a mano)
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
