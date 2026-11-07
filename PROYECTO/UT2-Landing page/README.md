# NexFlow Core · Landing page de EasyAutomation

Landing page del proyecto **NexFlow Core**, de **EasyAutomation**
Equipo: Lucas Herrera Moreno · Manuel Delgado Lopes — UT1, Parte 1, Tarea 2.

## Estructura

```
index.html            → la página completa (HTML + CSS + JS en un solo archivo)
assets/logo-full.jpg  → logotipo completo
assets/logo-symbol.png→ símbolo del logo (menú y pie)
assets/favicon.png    → icono de la pestaña
assets/lucas.jpg      → foto de Lucas Herrera Moreno (sección Equipo)
.nojekyll             → indica a GitHub Pages que publique los archivos tal cual
```

No necesita compilación ni servidor: es una web estática.

---

## 1. Dónde alojarla gratis (investigación)

| Servicio | Plan gratuito | Ventajas | Limitaciones |
|---|---|---|---|
| **GitHub Pages** | Gratis con cuenta de GitHub (repositorio público) | Muy sencillo, se integra con el repositorio de clase, HTTPS incluido, URL `usuario.github.io/repo` | Solo webs estáticas; tamaño máx. del sitio 1 GB; ancho de banda "blando" de 100 GB/mes; 10 builds/hora; no pensado para negocios online o SaaS comerciales |
| **Cloudflare Pages** | Gratis | Ancho de banda y peticiones ilimitados; CDN global; vistas previas por cada commit; hasta 100 dominios propios | 500 builds/mes, 1 build simultáneo; 20.000 archivos por sitio y 25 MB por archivo |
| **Netlify** | Gratis (sistema de créditos: 300/mes) | Fácil (incluso arrastrar y soltar la carpeta); formularios integrados | Las cuentas nuevas consumen créditos por despliegue y tráfico; si se agotan, el sitio se pausa |
| **Vercel** | Hobby (gratis) | Muy rápido, ideal para Next.js | Solo uso personal y **no comercial**; 100 GB de transferencia/mes |

### Conclusión
- **GitHub Pages** → la opción recomendada para clase: es gratis, el código ya vive en GitHub y la URL es fácil de compartir. Una landing académica es un uso perfectamente válido.
- **Cloudflare Pages** → la mejor alternativa si se quiere más rendimiento (CDN global, sin límite de tráfico) o si el proyecto llegara a ser real/comercial.

---

## 2. Publicar en GitHub Pages (recomendado)

1. Crea un repositorio **público** en GitHub, por ejemplo `nexflow-landing`.
2. Sube **todo el contenido** de esta carpeta (incluida la carpeta `assets/` y el archivo `.nojekyll`) a la raíz del repositorio:
   - Desde la web: *Add file → Upload files* y arrastra los archivos, o
   - Desde terminal:
     ```bash
     git init
     git add .
     git commit -m "Landing page NexFlow Core"
     git branch -M main
     git remote add origin https://github.com/USUARIO/nexflow-landing.git
     git push -u origin main
     ```
3. En el repositorio: **Settings → Pages**.
4. En *Build and deployment* elige **Source: Deploy from a branch**, rama **main** y carpeta **/ (root)**. Pulsa *Save*.
5. Espera 1–2 minutos. La web quedará en:
   `https://USUARIO.github.io/nexflow-landing/`

## 3. Publicar en Cloudflare Pages (alternativa)

**Opción A – conectando GitHub**
1. Entra en el panel de Cloudflare → **Workers & Pages → Create → Pages → Connect to Git**.
2. Elige el repositorio `nexflow-landing`.
3. *Framework preset*: **None**. *Build command*: vacío. *Build output directory*: `/`.
4. **Save and Deploy**. Quedará en `https://nexflow-landing.pages.dev`.

**Opción B – subida directa (sin Git)**
1. **Workers & Pages → Create → Pages → Upload assets**.
2. Pon un nombre al proyecto y arrastra la carpeta completa.
3. **Deploy site**.

Cada vez que hagáis `git push`, tanto GitHub Pages como Cloudflare Pages (opción A) actualizan la web automáticamente.

---

## Elementos interactivos

- Fondo de red animada en la cabecera (reacciona al ratón) y texto que se escribe solo.
- Editor de flujos de la cabecera: botones «Ejecutar» y «Turbo», contador en directo.
- Carrusel infinito de más de 40 conectores con icono por categoría: se pausa con el ratón, se puede arrastrar y en el móvil se desliza con el dedo.
- Logo con luces animadas que recorren el símbolo de infinito.
- Contadores animados, comparador «Manual / Con NexFlow».
- Simulador «Pruébalo tú mismo»: elige disparador y acciones, ejecuta y mira la consola (con opción de simular fallos y reintentos).
- Panel de control en vivo: KPIs, gráfica y ejecuciones que se actualizan; flujos que se activan/pausan, conectores que se conectan/desconectan y robots RPA en marcha.
- Calculadora de ahorro con controles deslizantes.
- Pestañas de usuarios por departamento, hoja de ruta, preguntas frecuentes desplegables.
- Formulario con validación, contador de caracteres, botón para copiar el correo, avisos emergentes, barra de progreso de lectura y botón de volver arriba.

## Contenido de la landing (checklist de la tarea)

- [x] Nombre de la aplicación — NexFlow Core (by EasyAutomation)
- [x] Logotipo — cabecera, sección "El proyecto" y favicon
- [x] Eslogan — "Conecta tus sistemas. Libera a tu equipo."
- [x] Descripción general — secciones Hero y "El proyecto"
- [x] Problema o necesidad — "El problema"
- [x] Público objetivo — "Público objetivo"
- [x] Justificación — "Por qué desarrollarla"
- [x] Funcionalidades — "Funcionalidades" + pasos de uso
- [x] Qué aporta — "Beneficios"
- [x] Imágenes / capturas — editor de flujos y panel de control (prototipos)
- [x] Perfil de usuarios potenciales — 4 perfiles (personas)
- [x] Necesidades e intereses — dentro de cada perfil
- [x] Beneficios para ellos — dentro de cada perfil + "Beneficios"
- [x] Qué la hace diferente — "Qué nos hace diferentes"
- [x] Ventajas frente a otras soluciones — tabla comparativa
- [x] Nombre del equipo e integrantes — "El equipo"
- [x] Contacto ficticio — "Contacto" (formulario de demostración)
