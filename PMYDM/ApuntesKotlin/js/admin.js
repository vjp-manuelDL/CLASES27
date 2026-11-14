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

  function run(fn, okMsg) {
    try { fn(); if (okMsg) window.UI.toast(okMsg); }
    catch (e) { window.UI.toast(e.message, true); }
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
      <div class="admin-tabs">
        <button data-t="cuentas" class="${tab === "cuentas" ? "active" : ""}">👥 Cuentas</button>
        <button data-t="visibilidad" class="${tab === "visibilidad" ? "active" : ""}">👁️ Visibilidad de temas</button>
        <button data-t="datos" class="${tab === "datos" ? "active" : ""}">💾 Datos y registro</button>
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
        <div class="table-wrap"><table>
          <thead><tr><th>Usuario</th><th>Rol</th><th>Estado</th><th>Temas visibles</th><th>Último acceso</th><th>Acciones</th></tr></thead>
          <tbody>${users.map((u) => {
            const isMe = u.username === me.username;
            const vis = temas.filter((t) => DB.canSee(u, t.id)).length;
            return `<tr data-u="${esc(u.username)}">
              <td><strong>${esc(u.username)}</strong> ${isMe ? '<span class="pill me">tú</span>' : ""}</td>
              <td><select data-act="rol" ${isMe ? "disabled" : ""}><option value="user" ${u.role === "user" ? "selected" : ""}>Usuario</option><option value="admin" ${u.role === "admin" ? "selected" : ""}>Admin</option></select></td>
              <td><span class="pill ${u.enabled ? "on" : "off"}">${u.enabled ? "Habilitada" : "Deshabilitada"}</span></td>
              <td>${vis} / ${temas.length}</td>
              <td>${fmt(u.lastLogin)}</td>
              <td><div class="actions">
                <button class="btn small" data-act="toggle" ${isMe ? "disabled" : ""}>${u.enabled ? "🚫 Deshabilitar" : "✅ Habilitar"}</button>
                <button class="btn small" data-act="pass">🔑 Contraseña</button>
                <button class="btn small" data-act="vis">👁️ Temas</button>
                <button class="btn small danger" data-act="del" ${isMe ? "disabled" : ""}>🗑️ Eliminar</button>
              </div></td></tr>`;
          }).join("")}</tbody></table></div>
        <p class="legend">No puedes deshabilitarte, eliminarte ni quitarte el rol de admin a ti mismo, y siempre tiene que quedar al menos un administrador habilitado.</p>
      </section>`;

    U.$("#f-add", body).addEventListener("submit", (e) => {
      e.preventDefault();
      const f = e.target;
      run(() => DB.addUser(f.u.value, f.p.value, f.r.value), `Cuenta «${f.u.value.trim()}» creada`);
    });
    U.$$("tr[data-u]", body).forEach((tr) => {
      const name = tr.dataset.u;
      tr.addEventListener("click", (e) => {
        const b = e.target.closest("button[data-act]");
        if (!b) return;
        const u = DB.findUser(name);
        if (b.dataset.act === "toggle") run(() => DB.setEnabled(name, !u.enabled), `Cuenta ${u.enabled ? "deshabilitada" : "habilitada"}`);
        if (b.dataset.act === "del") confirmar(`¿Eliminar la cuenta «${name}»?`, "Esta acción no se puede deshacer.", () => run(() => DB.deleteUser(name), "Cuenta eliminada"));
        if (b.dataset.act === "pass") cambiarPass(name);
        if (b.dataset.act === "vis") verTemasUsuario(name);
      });
      const sel = U.$("select[data-act=rol]", tr);
      sel.addEventListener("change", () => run(() => DB.setRole(name, sel.value), "Rol actualizado"));
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
    dialog(`<form><h3>Nueva contraseña para «${esc(name)}»</h3>
      <label>Contraseña nueva<input name="p" type="password" required minlength="4" autocomplete="new-password"></label>
      <label>Repetir contraseña<input name="p2" type="password" required minlength="4" autocomplete="new-password"></label>
      <div class="actions"><button class="btn" value="cancel" formnovalidate>Cancelar</button><button class="btn primary" value="ok">Guardar</button></div></form>`,
      (f) => {
        if (f.p.value !== f.p2.value) { window.UI.toast("Las contraseñas no coinciden", true); return false; }
        run(() => DB.setPassword(name, f.p.value), "Contraseña actualizada");
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
      (f) => run(() => temas.forEach((t) => {
        const want = f["t_" + t.id].checked;
        const own = ((DB.data.visibilidad[t.id] || {}).usuarios || []).includes(name);
        if (want === own) DB.setVisUsuario(t.id, name, !want);
      }), "Visibilidad actualizada"));
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
        <div class="table-wrap"><table class="matrix">
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

    U.$$("tr[data-t]", body).forEach((tr) => {
      const id = tr.dataset.t;
      U.$$("input[type=checkbox]", tr).forEach((cb) => cb.addEventListener("change", () => {
        const hide = !cb.checked;
        if (cb.dataset.scope === "todos") run(() => DB.setVisTodos(id, hide));
        else if (cb.dataset.scope === "noAdmin") run(() => DB.setVisNoAdmin(id, hide));
        else run(() => DB.setVisUsuario(id, cb.dataset.u, hide));
      }));
      U.$("[data-reset]", tr).addEventListener("click", () => run(() => DB.resetVis(id), "Tema visible para todos"));
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

  /* ============================ DATOS ============================ */
  function drawDatos(body) {
    const U = window.UI;
    body.innerHTML = `
      <section class="panel">
        <h3>Copia de seguridad</h3>
        <p class="legend">Las cuentas y permisos se guardan en el navegador (localStorage). Si cambias de navegador u ordenador, exporta aquí y luego importa.</p>
        <div class="actions"><button class="btn" id="d-exp">⬇️ Descargar copia (.json)</button>
        <label class="btn" style="display:inline-flex">⬆️ Importar copia<input type="file" id="d-imp" accept=".json,application/json" hidden></label>
        <button class="btn danger" id="d-reset">♻️ Restablecer valores iniciales</button></div>
      </section>
      <section class="panel"><h3>Registro de cambios</h3>
        <div class="log">${(DB.data.log || []).map((l) => `<div>${fmt(l.t)} · <strong>${esc(l.by)}</strong> · ${esc(l.msg)}</div>`).join("") || "<div>Sin cambios todavía.</div>"}</div>
      </section>`;
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
      confirmar("¿Restablecer todo?", "Se borrarán las cuentas creadas y los permisos, y se volverá a las 4 cuentas iniciales. Tendrás que volver a iniciar sesión.", () => {
        DB.resetAll(); location.hash = ""; window.App.boot();
      }));
  }

  window.Admin = { render };
})();
