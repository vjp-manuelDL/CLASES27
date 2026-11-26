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
  // Enlaces de entrega en Moodle: "<tema>-<numero de ejercicio>"
  entregas: {
    "tema3-5":  "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49311",
    "tema3-11": "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49312",
    "tema5-6":  "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49671",
    "tema5-9":  "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49672",
    "tema6-8":  "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49868",
    "tema6-10": "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49869",
    "tema7-8":  "https://moodle.educarex.es/ccff_iesvjp/mod/assign/view.php?id=49998"
  },

  // Cada cuántos segundos se comprueba si ha habido cambios
  // (las comprobaciones sin cambios no gastan el límite de la API de GitHub)
  intervaloSegundos: 10
};
