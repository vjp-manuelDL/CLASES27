/* ==========================================================================
   config.js — Dónde se guardan las cuentas compartidas
   Las cuentas, contraseñas y permisos se guardan en un archivo JSON de tu
   repositorio de GitHub. Así, cualquier cambio (por ejemplo una contraseña)
   se aplica a la vez en el PC, en GitHub Pages y en cualquier navegador.
   Si mueves o renombras la carpeta del proyecto en el repo, cambia "path".
   ========================================================================== */
window.APUNTES_CONFIG = {
  github: {
    owner: "vjp-manuelDL",                              // usuario de GitHub
    repo: "CLASES27",                                   // repositorio
    branch: "main",                                     // rama
    path: "PMYDM/ApuntesKotlin/datos/cuentas.json"      // archivo con las cuentas (se crea solo)
  },
  // Cada cuántos segundos se comprueba si ha habido cambios
  // (las comprobaciones sin cambios no gastan el límite de la API de GitHub)
  intervaloSegundos: 10
};
