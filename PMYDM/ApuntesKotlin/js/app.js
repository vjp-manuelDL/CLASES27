/* ==========================================================================
   app.js — Login, navegación, visor de apuntes y buscador
   ========================================================================== */
(function () {
  "use strict";
  const $ = (s, el = document) => el.querySelector(s);
  const $$ = (s, el = document) => Array.from(el.querySelectorAll(s));
  const esc = (s) => String(s).replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
  const TEMAS = window.TEMAS || [];

  /* ---------------- utilidades compartidas ---------------- */
  function toast(msg, isErr) {
    const t = $("#toast");
    t.textContent = msg;
    t.className = "toast show" + (isErr ? " err" : "");
    clearTimeout(toast._t);
    toast._t = setTimeout(() => (t.className = "toast"), 2600);
  }
  window.UI = { $, $$, esc, toast };

  /* ---------------- modo claro / oscuro ---------------- */
  const THEME_KEY = "apuntesKotlin.theme";
  function applyTheme(t) { document.documentElement.dataset.theme = t; }
  applyTheme(localStorage.getItem(THEME_KEY) || (matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light"));
  $("#btn-tema").addEventListener("click", () => {
    const t = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
    localStorage.setItem(THEME_KEY, t);
    applyTheme(t);
  });

  /* ---------------- login ---------------- */
  $("#form-login").addEventListener("submit", async (e) => {
    e.preventDefault();
    const btn = $("#form-login button[type=submit]");
    btn.disabled = true;
    btn.textContent = "Comprobando…";
    let r;
    try { r = await DB.login($("#login-user").value, $("#login-pass").value); }
    finally { btn.disabled = false; btn.textContent = "Entrar"; }
    if (!r.ok) {
      $("#login-error").textContent = r.error;
      $("#login-pass").select();
      return;
    }
    $("#login-error").textContent = "";
    $("#form-login").reset();
    location.hash = "#/inicio";
    boot();
  });
  $(".ver-pass").addEventListener("click", () => {
    const i = $("#login-pass");
    i.type = i.type === "password" ? "text" : "password";
  });
  $("#btn-salir").addEventListener("click", () => {
    DB.logout();
    $("#buscador").value = "";
    $("#resultados").hidden = true;
    location.hash = "";
    boot();
  });

  /* ---------------- helpers de contenido ---------------- */
  const visibleTemas = (u) => TEMAS.filter((t) => DB.canSee(u, t.id));
  const findTema = (id) => TEMAS.find((t) => t.id === id);
  const countEj = (t) => t.secciones.reduce((a, s) => a + s.ejercicios, 0);

  /* ---------------- sidebar ---------------- */
  function renderSidebar(u, route) {
    const temas = visibleTemas(u);
    let h = `<a class="nav-home ${route.view === "inicio" ? "active" : ""}" href="#/inicio">🏠 Inicio</a>`;
    if (u.role === "admin") h += `<a class="nav-home ${route.view === "admin" ? "active" : ""}" href="#/admin">🛡️ Panel de administración</a>`;
    if (!temas.length) h += `<p class="nav-empty">No tienes temas visibles. Pide al administrador que te los habilite.</p>`;
    temas.forEach((t) => {
      const cur = route.tema === t.id;
      h += `<div class="nav-tema ${cur ? "open current" : ""}" data-tema="${t.id}">
        <button type="button"><span class="num">${t.numero}</span><span>${esc(t.titulo)}</span><span class="chev">›</span></button>
        <ul>${t.secciones.map((s) => `<li><a href="#/${t.id}/${s.id}" class="${route.sec === s.id ? "active" : ""}"><span>${esc(s.titulo)}</span>${s.ejercicios ? `<span class="cnt" title="Ejercicios">${s.ejercicios} ej.</span>` : ""}</a></li>`).join("")}</ul>
      </div>`;
    });
    const sb = $("#sidebar");
    sb.innerHTML = h;
    $$(".nav-tema > button", sb).forEach((b) => b.addEventListener("click", () => b.parentElement.classList.toggle("open")));
    const act = $("a.active", sb);
    if (act && act.scrollIntoView) act.scrollIntoView({ block: "nearest" });
  }

  /* ---------------- vistas ---------------- */
  function viewInicio(u) {
    const temas = visibleTemas(u);
    const totalEj = temas.reduce((a, t) => a + countEj(t), 0);
    return `<div class="hero"><h1>¡Hola, ${esc(u.username)}! 👋</h1>
      <p>Apuntes de Kotlin y Jetpack Compose resumidos por apartados, con ejemplos de código y todos los ejercicios resueltos (con cada fichero del proyecto).</p></div>
      <div class="meta" style="margin-bottom:1rem"><span>${temas.length} temas disponibles</span><span>${temas.reduce((a, t) => a + t.secciones.length, 0)} apartados</span><span>${totalEj} ejercicios resueltos</span></div>
      ${temas.length ? `<div class="grid">${temas.map((t) => `<a class="card" href="#/${t.id}/${t.secciones[0].id}">
        <span class="num">Tema ${t.numero}</span><h3>${esc(t.titulo)}</h3><p>${esc(t.resumen)}</p>
        <div class="meta"><span>${t.secciones.length} apartados</span><span>${countEj(t)} ejercicios</span></div></a>`).join("")}</div>`
        : `<div class="callout aviso">Ahora mismo no tienes ningún tema visible. El administrador puede habilitártelos desde su panel.</div>`}
      <h2>Cómo usar los apuntes</h2>
      <ul><li>Usa el menú lateral para moverte por <strong>temas y apartados</strong>. El número naranja indica cuántos ejercicios hay en ese apartado.</li>
      <li>En cada ejercicio pulsa <strong>«Ver solución»</strong>: verás la solución completa con una pestaña por fichero (<code>MainActivity.kt</code>, <code>strings.xml</code>, <code>Color.kt</code>…).</li>
      <li>Cada bloque de código tiene un botón <strong>Copiar</strong> para pegarlo directamente en Android Studio.</li>
      <li>Busca cualquier cosa con <strong>Ctrl + K</strong> (por ejemplo: <code>remember</code>, <code>LazyColumn</code>, <code>when</code>).</li></ul>`;
  }

  function viewSeccion(u, tema, sec) {
    const all = visibleTemas(u).flatMap((t) => t.secciones.map((s) => ({ t, s })));
    const idx = all.findIndex((x) => x.s.id === sec.id);
    const prev = all[idx - 1], next = all[idx + 1];
    return `<nav class="crumbs"><a href="#/inicio">Inicio</a><span>›</span><a href="#/${tema.id}/${tema.secciones[0].id}">Tema ${tema.numero}. ${esc(tema.titulo)}</a></nav>
      <h1>${esc(sec.titulo)}</h1>
      <div class="section-body">${sec.html}</div>
      <nav class="pager">
        ${prev ? `<a class="prev" href="#/${prev.t.id}/${prev.s.id}"><small>← Anterior</small>${esc(prev.s.titulo)}</a>` : "<span></span>"}
        ${next ? `<a class="next" href="#/${next.t.id}/${next.s.id}"><small>Siguiente →</small>${esc(next.s.titulo)}</a>` : ""}
      </nav>`;
  }

  /* "Firma" de lo que ve el usuario: si un cambio remoto no la altera, no se redibuja la página */
  let firma = "";
  const firmaActual = (u) => u ? [u.username, u.role, visibleTemas(u).map((t) => t.id).join(",")].join("|") : "";

  /* ---------------- router ---------------- */
  function parseRoute() {
    const parts = location.hash.replace(/^#\/?/, "").split("/").filter(Boolean);
    if (!parts.length || parts[0] === "inicio") return { view: "inicio" };
    if (parts[0] === "admin") return { view: "admin", tab: parts[1] || "cuentas" };
    return { view: "seccion", tema: parts[0], sec: parts[1] };
  }

  function render() {
    const u = DB.currentUser();
    if (!u) return boot();
    firma = firmaActual(u);
    const route = parseRoute();
    const main = $("#contenido");
    let html;
    if (route.view === "admin") {
      if (u.role !== "admin") { location.hash = "#/inicio"; return; }
      renderSidebar(u, route);
      window.Admin.render(main, route.tab);
      afterRender();
      return;
    } else if (route.view === "seccion") {
      const tema = findTema(route.tema);
      if (!tema || !DB.canSee(u, tema.id)) {
        html = `<div class="callout aviso">Este tema no existe o no tienes permiso para verlo.</div><p><a href="#/inicio">Volver al inicio</a></p>`;
      } else {
        const sec = tema.secciones.find((s) => s.id === route.sec) || tema.secciones[0];
        route.sec = sec.id;
        html = viewSeccion(u, tema, sec);
      }
    } else {
      html = viewInicio(u);
    }
    renderSidebar(u, route);
    main.innerHTML = html;
    afterRender();
  }

  function afterRender() {
    window.scrollTo(0, 0);
    $("#sidebar").classList.remove("open");
    $("#overlay").classList.remove("show");
  }

  /* ---------------- interacciones del contenido (delegadas) ---------------- */
  document.addEventListener("click", (e) => {
    const copy = e.target.closest(".copy");
    if (copy) {
      const code = copy.closest(".code").querySelector("pre").innerText;
      const done = () => { copy.textContent = "¡Copiado!"; setTimeout(() => (copy.textContent = "Copiar"), 1400); };
      if (navigator.clipboard && window.isSecureContext) navigator.clipboard.writeText(code).then(done, () => fallbackCopy(code, done));
      else fallbackCopy(code, done);
      return;
    }
    const tab = e.target.closest(".tab");
    if (tab) {
      const box = tab.closest(".tabs");
      $$(":scope > .tab-heads > .tab", box).forEach((b) => b.classList.toggle("active", b === tab));
      $$(":scope > .tab-body", box).forEach((b) => b.classList.toggle("active", b.dataset.i === tab.dataset.i));
      return;
    }
    const tog = e.target.closest(".ej-toggle");
    if (tog) {
      const sol = tog.nextElementSibling;
      sol.hidden = !sol.hidden;
      tog.textContent = sol.hidden ? "Ver solución" : "Ocultar solución";
    }
  });
  function fallbackCopy(text, done) {
    const ta = document.createElement("textarea");
    ta.value = text;
    ta.style.position = "fixed"; ta.style.opacity = "0";
    document.body.appendChild(ta); ta.select();
    try { document.execCommand("copy"); done(); } catch (err) { toast("No se pudo copiar", true); }
    ta.remove();
  }

  /* Buscador en móvil: se abre a pantalla completa sobre la barra superior */
  const caja = $("#caja-buscar");
  function abrirBuscador(abrir) {
    caja.classList.toggle("open", abrir);
    if (abrir) { input.focus(); input.select(); }
    else { input.value = ""; box.hidden = true; input.blur(); }
  }
  $("#btn-buscar").addEventListener("click", () => abrirBuscador(!caja.classList.contains("open")));
  $("#cerrar-buscar").addEventListener("click", () => abrirBuscador(false));

  $("#btn-menu").addEventListener("click", () => { $("#sidebar").classList.toggle("open"); $("#overlay").classList.toggle("show"); });
  $("#overlay").addEventListener("click", () => { $("#sidebar").classList.remove("open"); $("#overlay").classList.remove("show"); });

  /* ---------------- buscador ---------------- */
  let index = null;
  function buildIndex() {
    const tmp = document.createElement("div");
    index = [];
    TEMAS.forEach((t) => t.secciones.forEach((s) => {
      tmp.innerHTML = s.html;
      index.push({ t, s, text: (s.titulo + " " + tmp.textContent).replace(/\s+/g, " ") });
    }));
  }
  const norm = (s) => s.toLowerCase().normalize("NFD").replace(/[̀-ͯ]/g, "");
  const input = $("#buscador"), box = $("#resultados");
  input.addEventListener("input", () => {
    const q = input.value.trim();
    if (q.length < 2) { box.hidden = true; return; }
    if (!index) buildIndex();
    const u = DB.currentUser();
    const nq = norm(q);
    const res = index.filter((x) => DB.canSee(u, x.t.id) && norm(x.text).includes(nq)).slice(0, 25);
    box.innerHTML = res.length ? res.map((x, i) => {
      const pos = norm(x.text).indexOf(nq);
      const frag = x.text.slice(Math.max(0, pos - 40), pos + q.length + 60);
      const fpos = norm(frag).indexOf(nq);
      const snip = esc(frag.slice(0, fpos)) + "<mark>" + esc(frag.slice(fpos, fpos + q.length)) + "</mark>" + esc(frag.slice(fpos + q.length));
      return `<a href="#/${x.t.id}/${x.s.id}" class="${i === 0 ? "sel" : ""}">${esc(x.s.titulo)}<small>Tema ${x.t.numero} · …${snip}…</small></a>`;
    }).join("") : `<a>Sin resultados para «${esc(q)}»</a>`;
    box.hidden = false;
  });
  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") { const a = $("a.sel", box); if (a && a.getAttribute("href")) { location.hash = a.getAttribute("href"); box.hidden = true; input.blur(); } }
    if (e.key === "Escape") { box.hidden = true; input.blur(); caja.classList.remove("open"); }
  });
  box.addEventListener("click", () => { box.hidden = true; caja.classList.remove("open"); });
  document.addEventListener("click", (e) => { if (!e.target.closest(".search")) box.hidden = true; });
  document.addEventListener("keydown", (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "k" && DB.currentUser()) { e.preventDefault(); input.focus(); input.select(); }
  });

  /* ---------------- arranque ---------------- */
  function boot() {
    const u = DB.currentUser();
    $("#vista-login").hidden = !!u;
    $("#vista-app").hidden = !u;
    if (!u) {
      // ¿Se ha cerrado la sesión porque cambió la contraseña o se deshabilitó la cuenta?
      const motivo = DB.takeMotivoCierre();
      if (motivo) $("#login-error").textContent = motivo;
      setTimeout(() => $("#login-user").focus(), 0);
      return;
    }
    $("#user-chip").innerHTML = `<span class="avatar">${esc(u.username[0].toUpperCase())}</span><span class="name">${esc(u.username)}</span><span class="role ${u.role}">${u.role === "admin" ? "admin" : "usuario"}</span>`;
    $("#btn-admin").hidden = u.role !== "admin";
    render();
  }
  window.addEventListener("hashchange", render);
  // Cambios de datos (de GitHub, de otra pestaña o del panel admin):
  // - si la sesión ya no es válida (contraseña cambiada, cuenta deshabilitada…) se cierra al instante
  // - si cambian los temas visibles de este usuario, se redibuja
  window.addEventListener("db-change", () => {
    const u = DB.currentUser();
    if (!u) { if (!$("#vista-app").hidden) boot(); return; }
    if (parseRoute().view === "admin") { window.Admin.refresh(); return; }
    if (firmaActual(u) !== firma) boot();
  });
  window.App = { render, boot };
  boot();
})();
