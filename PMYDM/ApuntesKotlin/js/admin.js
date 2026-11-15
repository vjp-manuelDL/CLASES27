/* ==========================================================================
   admin.js — Panel de administración
   - Cuentas: crear, eliminar, habilitar/deshabilitar, cambiar rol y contraseña
   - Visibilidad: ocultar/mostrar cada tema para todas las cuentas,
     para todas las cuentas no-admin o para una cuenta concreta
   - Datos: copia de seguridad, importar, restablecer y registro de cambios
   ========================================================================== */
(function () {
  "use strict";
  const esc = (s) => String(s).replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
  let host = null, tab = "cuentas";

  const fmt = (iso) => (iso ? new Date(iso).toLocaleString("es-ES", { dateStyle: "short", timeStyle: "short" }) : "—");

  /* Ejecuta una acción (síncrona o asíncrona), muestra el resultado y redibuja el panel */
  let ocupado = false;
  function run(fn, okMsg) {
    if (ocupado) return Promise.resolve();
    ocupado = true;
    document.body.classList.add("busy");
    const s = DB.sync;
    if (s.tieneToken || s.publicado) window.UI.toast("Guardando en GitHub…");
    return Promise.resolve()
      .then(fn)
      .then((r) => window.UI.toast(typeof r === "string" ? r : (okMsg || "Cambios guardados")))
      .catch((e) => window.UI.toast((e && e.message) || String(e), true))
      .finally(() => { ocupado = false; document.body.classList.remove("busy"); draw(); });
  }

  /* Estado de la sincronización con GitHub */
  function syncHtml() {
    const s = DB.sync;
    const hora = s.ultima ? s.ultima.toLocaleTimeString("es-ES") : "—";
    if (!s.config) return '<span class="pill off">Sin configuración de GitHub (js/config.js)</span>';
    if (s.ok === false) return `<span class="pill off">⚠️ Sin conexión con GitHub</span> <small class="legend">${esc(s.mensaje)}</small>`;
    if (s.publicado) return `<span class="pill on">☁️ Sincronizado con GitHub</span> <small class="legend">última comprobación ${hora}` +
      (s.tieneToken ? "" : " · falta tu token para poder guardar cambios") + "</small>";
    return `<span class="pill me">💻 Modo local</span> <small class="legend">${esc(s.mensaje)}</small>`;
  }
  /* Aviso grande cuando los cambios NO salen de este navegador */
  function avisoLocal() {
    const s = DB.sync;
    if (!s.config || s.publicado) return "";
    return `<div class="callout aviso banner-local">
      <strong>⚠️ Ahora mismo las cuentas solo existen en este navegador.</strong>
      Si cambias una contraseña aquí, en el móvil u otro ordenador seguirá valiendo la antigua.
      Para compartirlas, añade una vez tu token de GitHub.
      <a class="btn small primary" href="#/admin/datos">🔑 Configurar ahora</a>
    </div>`;
  }
  window.addEventListener("db-sync", () => {
    document.querySelectorAll(".sync-line").forEach((el) => (el.innerHTML = syncHtml()));
    const b = document.querySelector(".banner-local");
    if (b && DB.sync.publicado) b.remove();
  });

  /* Redibuja si llegan cambios de otro dispositivo (sin interrumpir si estás escribiendo) */
  function refresh() {
    if (!host || !document.body.contains(host) || ocupado) return;
    const dlg = document.getElementById("dialogo");
    const act = document.activeElement;
    if ((dlg && dlg.open) || (act && host.contains(act) && /INPUT|SELECT|TEXTAREA/.test(act.tagName) && act.type !== "checkbox")) return;
    draw();
  }

  function render(el, t) {
    host = el;
    tab = ["cuentas", "visibilidad", "datos"].includes(t) ? t : "cuentas";
    draw();
  }

  function draw() {
    if (!host) return;
    const me = DB.currentUser();
    if (!me || me.role !== "admin") { location.hash = "#/inicio"; return; }
    const U = window.UI;
    host.innerHTML = `
      <nav class="crumbs"><a href="#/inicio">Inicio</a><span>›</span><span>Administración</span></nav>
      <h1>🛡️ Panel de administración</h1>
      <p class="sync-line">${syncHtml()}</p>
      ${avisoLocal()}
      <div class="admin-tabs">
        <button data-t="cuentas" class="${tab === "cuentas" ? "active" : ""}">👥 Cuentas</button>
        <button data-t="visibilidad" class="${tab === "visibilidad" ? "active" : ""}">👁️ Visibilidad de temas</button>
        <button data-t="datos" class="${tab === "datos" ? "active" : ""}">☁️ Sincronización y datos</button>
      </div>
      <div id="admin-body"></div>`;
    U.$$(".admin-tabs button", host).forEach((b) => b.addEventListener("click", () => { location.hash = "#/admin/" + b.dataset.t; }));
    const body = U.$("#admin-body", host);
    if (tab === "cuentas") drawCuentas(body, me);
    else if (tab === "visibilidad") drawVisibilidad(body, me);
    else drawDatos(body);
  }

  /* ============================ CUENTAS ============================ */
  function drawCuentas(body, me) {
    const U = window.UI, users = DB.data.users, temas = window.TEMAS || [];

    // Datos de cada cuenta, para pintarlos en la tabla (pantalla ancha) y en las tarjetas (móvil)
    const info = users.map((u) => ({
      u,
      yo: u.username === me.username,
      vis: temas.filter((t) => DB.canSee(u, t.id)).length
    }));

    const selRol = (x) => `<select data-act="rol" aria-label="Rol de ${esc(x.u.username)}" ${x.yo ? "disabled" : ""}>
        <option value="user" ${x.u.role === "user" ? "selected" : ""}>Usuario</option>
        <option value="admin" ${x.u.role === "admin" ? "selected" : ""}>Admin</option></select>`;
    const botones = (x) => `
        <button class="btn small" data-act="toggle" ${x.yo ? "disabled" : ""}>${x.u.enabled ? "🚫 Deshabilitar" : "✅ Habilitar"}</button>
        <button class="btn small" data-act="pass">🔑 Contraseña</button>
        <button class="btn small" data-act="vis">👁️ Temas</button>
        <button class="btn small danger" data-act="del" ${x.yo ? "disabled" : ""}>🗑️ Eliminar</button>`;
    const estado = (x) => `<span class="pill ${x.u.enabled ? "on" : "off"}">${x.u.enabled ? "Habilitada" : "Deshabilitada"}</span>`;

    body.innerHTML = `
      <section class="panel">
        <h3>Añadir cuenta</h3>
        <form id="f-add" class="form-row" autocomplete="off">
          <label>Usuario<input name="u" required placeholder="p. ej. Laura"></label>
          <label>Contraseña<input name="p" type="password" required minlength="4" autocomplete="new-password"></label>
          <label>Rol<select name="r"><option value="user">Usuario</option><option value="admin">Administrador</option></select></label>
          <button class="btn primary" type="submit">➕ Crear cuenta</button>
        </form>
      </section>
      <section class="panel">
        <h3>Cuentas (${users.length})</h3>

        <div class="table-wrap solo-ancho"><table>
          <thead><tr><th>Usuario</th><th>Rol</th><th>Estado</th><th>Temas visibles</th><th>Creada</th><th>Acciones</th></tr></thead>
          <tbody>${info.map((x) => `<tr data-u="${esc(x.u.username)}">
              <td><strong>${esc(x.u.username)}</strong> ${x.yo ? '<span class="pill me">tú</span>' : ""}</td>
              <td>${selRol(x)}</td>
              <td>${estado(x)}</td>
              <td>${x.vis} / ${temas.length}</td>
              <td>${fmt(x.u.created)}</td>
              <td><div class="actions">${botones(x)}</div></td></tr>`).join("")}</tbody></table></div>

        <div class="cards solo-movil">${info.map((x) => `<article class="ucard" data-u="${esc(x.u.username)}">
            <header>
              <span class="avatar">${esc(x.u.username[0].toUpperCase())}</span>
              <div class="ucard-id"><strong>${esc(x.u.username)}</strong>
                <small>${x.vis} de ${temas.length} temas · ${fmt(x.u.created)}</small></div>
              ${x.yo ? '<span class="pill me">tú</span>' : ""}${estado(x)}
            </header>
            <label class="sw-row"><span>Rol</span>${selRol(x)}</label>
            <div class="actions">${botones(x)}</div>
          </article>`).join("")}</div>

        <p class="legend">No puedes deshabilitarte, eliminarte ni quitarte el rol de admin a ti mismo, y siempre tiene que quedar al menos un administrador habilitado.</p>
      </section>`;

    U.$("#f-add", body).addEventListener("submit", (e) => {
      e.preventDefault();
      const f = e.target;
      run(() => DB.addUser(f.u.value, f.p.value, f.r.value), `Cuenta «${f.u.value.trim()}» creada`);
    });

    // Un solo manejador para la tabla y para las tarjetas
    body.addEventListener("click", (e) => {
      const b = e.target.closest("button[data-act]");
      if (!b || !body.contains(b)) return;
      const name = b.closest("[data-u]").dataset.u;
      const u = DB.findUser(name);
      if (!u) return;
      if (b.dataset.act === "toggle") run(() => DB.setEnabled(name, !u.enabled), `Cuenta ${u.enabled ? "deshabilitada" : "habilitada"}`);
      if (b.dataset.act === "del") confirmar(`¿Eliminar la cuenta «${name}»?`, "Esta acción no se puede deshacer.", () => run(() => DB.deleteUser(name), "Cuenta eliminada"));
      if (b.dataset.act === "pass") cambiarPass(name);
      if (b.dataset.act === "vis") verTemasUsuario(name);
    });
    body.addEventListener("change", (e) => {
      const s = e.target.closest("select[data-act=rol]");
      if (!s) return;
      run(() => DB.setRole(s.closest("[data-u]").dataset.u, s.value), "Rol actualizado");
    });
  }

  function dialog(html, onSubmit) {
    const d = document.getElementById("dialogo");
    d.innerHTML = html;
    const f = d.querySelector("form");
    f.addEventListener("submit", (e) => {
      e.preventDefault();
      if (e.submitter && e.submitter.value === "cancel") return d.close();
      if (onSubmit(f) !== false) d.close();
    });
    d.showModal();
    const first = d.querySelector("input");
    if (first) first.focus();
  }

  function confirmar(titulo, texto, ok) {
    dialog(`<form><h3>${esc(titulo)}</h3><p>${esc(texto)}</p><div class="actions">
      <button class="btn" value="cancel">Cancelar</button><button class="btn danger" value="ok">Sí, continuar</button></div></form>`, () => ok());
  }

  function cambiarPass(name) {
    const compartido = DB.sync.publicado || DB.sync.tieneToken;
    dialog(`<form><h3>Nueva contraseña para «${esc(name)}»</h3>
      <p class="legend">${compartido
        ? "Se aplicará en todos los dispositivos en unos segundos y cerrará las sesiones abiertas con la antigua."
        : "⚠️ Solo cambiará en este navegador. En el móvil seguirá valiendo la antigua hasta que configures GitHub."}</p>
      <label>Contraseña nueva<input name="p" type="password" required minlength="4" autocomplete="new-password"></label>
      <label>Repetir contraseña<input name="p2" type="password" required minlength="4" autocomplete="new-password"></label>
      <div class="actions"><button class="btn" value="cancel" formnovalidate>Cancelar</button><button class="btn primary" value="ok">Guardar</button></div></form>`,
      (f) => {
        if (f.p.value !== f.p2.value) { window.UI.toast("Las contraseñas no coinciden", true); return false; }
        const global = DB.sync.publicado || DB.sync.tieneToken;
        run(() => DB.setPassword(name, f.p.value),
          global ? "Contraseña cambiada en todos los dispositivos. Las sesiones abiertas con la antigua se cierran solas."
                 : "Contraseña actualizada (solo en este navegador: configura GitHub para que sea global).");
      });
  }

  function verTemasUsuario(name) {
    const u = DB.findUser(name), temas = window.TEMAS || [];
    dialog(`<form><h3>Temas de «${esc(name)}»</h3>
      <p class="legend">Activa o desactiva la visibilidad de cada tema solo para esta cuenta.</p>
      ${temas.map((t) => {
        const reason = DB.hiddenReason(u, t.id);
        const own = (DB.data.visibilidad[t.id] || {}).usuarios || [];
        const visiblePropio = !own.includes(name);
        const global = reason && reason !== "Oculto para esta cuenta";
        return `<label style="display:flex;justify-content:space-between;align-items:center;font-weight:500">
          <span>Tema ${t.numero}. ${esc(t.titulo)}${global ? `<span class="cell-state bad">${esc(reason)} (regla global)</span>` : ""}</span>
          <span class="switch"><input type="checkbox" name="t_${t.id}" ${visiblePropio ? "checked" : ""}><span></span></span></label>`;
      }).join("")}
      <div class="actions"><button class="btn" value="cancel" formnovalidate>Cerrar</button><button class="btn primary" value="ok">Guardar</button></div></form>`,
      (f) => {
        const ocultos = {};
        temas.forEach((t) => { ocultos[t.id] = !f["t_" + t.id].checked; });
        run(() => DB.setVisUsuarioVarios(name, ocultos), "Visibilidad actualizada");
      });
  }

  /* ============================ VISIBILIDAD ============================ */
  function drawVisibilidad(body) {
    const U = window.UI, users = DB.data.users, temas = window.TEMAS || [];
    const vis = (id) => DB.data.visibilidad[id] || { todos: false, noAdmin: false, usuarios: [] };
    body.innerHTML = `
      <section class="panel">
        <h3>Visibilidad de temas</h3>
        <p class="legend">Interruptor <strong style="color:var(--ok)">verde = visible</strong>, gris = oculto. Las reglas se combinan:
        un tema se ve solo si está visible en <em>Todas las cuentas</em>, en <em>No-admins</em> (si la cuenta no es admin) y en la columna de esa cuenta.</p>
        <div class="table-wrap solo-ancho"><table class="matrix">
          <thead><tr><th>Tema</th>
            <th>Todas las cuentas<small>incluye admins</small></th>
            <th>No-admins<small>todos menos admins</small></th>
            ${users.map((u) => `<th>${esc(u.username)}<small>${u.role === "admin" ? "admin" : "usuario"}${u.enabled ? "" : " · deshab."}</small></th>`).join("")}
            <th></th></tr></thead>
          <tbody>${temas.map((t) => {
            const v = vis(t.id);
            return `<tr data-t="${t.id}">
              <td><strong>Tema ${t.numero}</strong><br><small>${esc(t.titulo)}</small></td>
              <td><label class="switch"><input type="checkbox" data-scope="todos" ${!v.todos ? "checked" : ""}><span></span></label></td>
              <td><label class="switch"><input type="checkbox" data-scope="noAdmin" ${!v.noAdmin ? "checked" : ""}><span></span></label></td>
              ${users.map((u) => {
                const own = (v.usuarios || []).includes(u.username);
                const efectivo = DB.canSee(u, t.id);
                return `<td><label class="switch"><input type="checkbox" data-scope="user" data-u="${esc(u.username)}" ${!own ? "checked" : ""}><span></span></label>
                  <span class="cell-state ${efectivo ? "" : "bad"}">${efectivo ? "lo ve" : "no lo ve"}</span></td>`;
              }).join("")}
              <td><button class="btn small" data-reset>Restablecer</button></td></tr>`;
          }).join("")}</tbody></table></div>

        <div class="cards solo-movil">${temas.map((t) => {
          const v = vis(t.id);
          return `<article class="vcard" data-t="${t.id}">
            <h4>Tema ${t.numero}. ${esc(t.titulo)}</h4>
            <label class="sw-row"><span>Todas las cuentas<small>incluye admins</small></span>
              <span class="switch"><input type="checkbox" data-scope="todos" ${!v.todos ? "checked" : ""}><span></span></span></label>
            <label class="sw-row"><span>No-admins<small>todos menos admins</small></span>
              <span class="switch"><input type="checkbox" data-scope="noAdmin" ${!v.noAdmin ? "checked" : ""}><span></span></span></label>
            <hr>
            ${users.map((u) => {
              const own = (v.usuarios || []).includes(u.username);
              const efectivo = DB.canSee(u, t.id);
              return `<label class="sw-row"><span>${esc(u.username)}<small class="${efectivo ? "" : "bad"}">${u.role === "admin" ? "admin · " : ""}${efectivo ? "lo ve" : "no lo ve"}</small></span>
                <span class="switch"><input type="checkbox" data-scope="user" data-u="${esc(u.username)}" ${!own ? "checked" : ""}><span></span></span></label>`;
            }).join("")}
            <button class="btn small" data-reset>♻️ Restablecer este tema</button>
          </article>`;
        }).join("")}</div>
      </section>
      <section class="panel">
        <h3>Acciones rápidas</h3>
        <div class="form-row">
          <label>Tema<select id="q-tema">${temas.map((t) => `<option value="${t.id}">Tema ${t.numero}. ${esc(t.titulo)}</option>`).join("")}</select></label>
          <label>Ámbito<select id="q-scope">
            <option value="todos">Todas las cuentas</option>
            <option value="noAdmin">Todas las cuentas que no son admin</option>
            ${users.map((u) => `<option value="u:${esc(u.username)}">Solo la cuenta ${esc(u.username)}</option>`).join("")}
          </select></label>
          <button class="btn danger" id="q-ocultar">🙈 Ocultar</button>
          <button class="btn" id="q-mostrar">👁️ Mostrar</button>
        </div>
      </section>`;

    // Un solo manejador para la tabla y para las tarjetas
    body.addEventListener("change", (e) => {
      const cb = e.target.closest("input[data-scope]");
      if (!cb) return;
      const id = cb.closest("[data-t]").dataset.t, hide = !cb.checked;
      if (cb.dataset.scope === "todos") run(() => DB.setVisTodos(id, hide));
      else if (cb.dataset.scope === "noAdmin") run(() => DB.setVisNoAdmin(id, hide));
      else run(() => DB.setVisUsuario(id, cb.dataset.u, hide));
    });
    body.addEventListener("click", (e) => {
      const b = e.target.closest("[data-reset]");
      if (!b) return;
      run(() => DB.resetVis(b.closest("[data-t]").dataset.t), "Tema visible para todos");
    });
    const quick = (hide) => {
      const id = U.$("#q-tema", body).value, sc = U.$("#q-scope", body).value;
      run(() => {
        if (sc === "todos") DB.setVisTodos(id, hide);
        else if (sc === "noAdmin") DB.setVisNoAdmin(id, hide);
        else DB.setVisUsuario(id, sc.slice(2), hide);
      }, hide ? "Tema ocultado" : "Tema visible");
    };
    U.$("#q-ocultar", body).addEventListener("click", () => quick(true));
    U.$("#q-mostrar", body).addEventListener("click", () => quick(false));
  }

  /* ============================ SINCRONIZACIÓN Y DATOS ============================ */
  function drawDatos(body) {
    const U = window.UI, s = DB.sync, c = s.config || {};
    body.innerHTML = `
      <section class="panel">
        <h3>☁️ Sincronización con GitHub</h3>
        <p class="sync-line">${syncHtml()}</p>
        <p class="legend">Las cuentas, contraseñas y permisos se guardan en el repositorio <code>${esc(c.owner)}/${esc(c.repo)}</code>,
        archivo <code>${esc(c.path)}</code> (rama <code>${esc(c.branch)}</code>). Todos los navegadores (tu PC, GitHub Pages, el móvil…)
        leen ese archivo y comprueban cambios cada ${s.intervalo} segundos y al volver a la pestaña, así que <strong>un cambio de contraseña
        o de permisos se aplica en todos a la vez</strong>. Las sesiones abiertas se cierran solas si cambia la contraseña de la cuenta,
        si se deshabilita o si se elimina.</p>
        ${s.tieneToken
          ? `<p><span class="pill on">🔑 Token guardado en este navegador</span></p>
             <div class="actions"><button class="btn" id="s-sync">🔄 Comprobar ahora</button><button class="btn danger" id="s-forget">Olvidar token</button></div>`
          : `<form id="s-token" class="form-row" autocomplete="off">
               <label>Token de GitHub (solo se guarda en este navegador)<input name="tk" type="password" placeholder="github_pat_…" required></label>
               <button class="btn primary" type="submit">🔑 Guardar token</button>
             </form>`}
        <details class="howto" ${s.tieneToken ? "" : "open"}><summary>Cómo crear el token (solo una vez)</summary>
          <ol>
            <li>Entra en <a href="https://github.com/settings/personal-access-tokens/new" target="_blank" rel="noopener">GitHub → Settings → Developer settings → Fine-grained tokens → Generate new token</a>.</li>
            <li><strong>Token name</strong>: <em>Apuntes Kotlin</em>. <strong>Expiration</strong>: la que quieras (por ejemplo, 1 año).</li>
            <li><strong>Repository access</strong> → <em>Only select repositories</em> → <code>${esc(c.repo)}</code>.</li>
            <li><strong>Permissions → Repository permissions → Contents</strong> → <em>Read and write</em>.</li>
            <li>Pulsa <strong>Generate token</strong>, cópialo y pégalo arriba. La primera vez se publican las cuentas en GitHub.</li>
          </ol>
          <p class="legend">Solo lo necesita el administrador, una vez en cada navegador desde el que administre (PC, GitHub Pages…).
          Los usuarios normales no necesitan nada. No compartas el token con nadie.</p>
        </details>
      </section>
      <section class="panel">
        <h3>💾 Copia de seguridad</h3>
        <div class="actions"><button class="btn" id="d-exp">⬇️ Descargar copia (.json)</button>
        <label class="btn" style="display:inline-flex">⬆️ Importar copia<input type="file" id="d-imp" accept=".json,application/json" hidden></label>
        <button class="btn danger" id="d-reset">♻️ Restablecer cuentas iniciales</button></div>
      </section>
      <section class="panel"><h3>📝 Registro de cambios</h3>
        <div class="log">${(DB.data.log || []).map((l) => `<div>${fmt(l.t)} · <strong>${esc(l.by)}</strong> · ${esc(l.msg)}</div>`).join("") || "<div>Sin cambios todavía.</div>"}</div>
      </section>`;

    const ft = U.$("#s-token", body);
    if (ft) ft.addEventListener("submit", (e) => { e.preventDefault(); run(() => DB.setToken(ft.tk.value)); });
    const bs = U.$("#s-sync", body);
    if (bs) bs.addEventListener("click", () => run(() => DB.refresh(), "Comprobado: tienes los datos más recientes"));
    const bf = U.$("#s-forget", body);
    if (bf) bf.addEventListener("click", () => { DB.clearToken(); draw(); U.toast("Token eliminado de este navegador"); });

    U.$("#d-exp", body).addEventListener("click", () => {
      const blob = new Blob([DB.exportJSON()], { type: "application/json" });
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = "apuntes-kotlin-cuentas-" + new Date().toISOString().slice(0, 10) + ".json";
      a.click();
      setTimeout(() => URL.revokeObjectURL(a.href), 1000);
    });
    U.$("#d-imp", body).addEventListener("change", (e) => {
      const file = e.target.files[0];
      if (!file) return;
      file.text().then((txt) => run(() => DB.importJSON(txt), "Copia importada"));
    });
    U.$("#d-reset", body).addEventListener("click", () =>
      confirmar("¿Restablecer todo?", "Se borrarán las cuentas creadas y los permisos (en todos los dispositivos) y se volverá a las 4 cuentas iniciales. Tendrás que volver a iniciar sesión.", () =>
        run(() => DB.resetAll().then(() => { location.hash = ""; window.App.boot(); }), "Cuentas restablecidas")));
  }

  window.Admin = { render, refresh };
})();
