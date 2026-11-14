/* ==========================================================================
   db.js — "Base de datos" local de la web (localStorage del navegador)
   Guarda: cuentas, roles, estado (habilitada/deshabilitada) y visibilidad de temas.
   ========================================================================== */
(function () {
  "use strict";

  /* ---------- SHA-256 en JS puro (funciona también abriendo el HTML con doble clic) ---------- */
  function sha256(ascii) {
    const utf8 = unescape(encodeURIComponent(ascii));
    const K = [], H = [];
    let isComposite = {}, n = 2, c = 0;
    while (c < 64) {
      if (!isComposite[n]) {
        for (let i = 0; i < 313; i += n) isComposite[i] = n;
        if (c < 8) H[c] = (Math.pow(n, 0.5) * 0x100000000) | 0;
        K[c++] = (Math.pow(n, 1 / 3) * 0x100000000) | 0;
      }
      n++;
    }
    const words = [], bitLen = utf8.length * 8;
    let s = utf8 + "\x80";
    while (s.length % 64 - 56) s += "\x00";
    for (let i = 0; i < s.length; i++) words[i >> 2] |= s.charCodeAt(i) << ((3 - i) % 4) * 8;
    words[words.length] = (bitLen / 0x100000000) | 0;
    words[words.length] = bitLen;
    const rot = (v, a) => (v >>> a) | (v << (32 - a));
    let hash = H.slice(0);
    for (let j = 0; j < words.length;) {
      const w = words.slice(j, (j += 16));
      const old = hash.slice(0);
      for (let i = 0; i < 64; i++) {
        const w15 = w[i - 15], w2 = w[i - 2];
        const a = hash[0], e = hash[4];
        const t1 = hash[7] + (rot(e, 6) ^ rot(e, 11) ^ rot(e, 25)) + ((e & hash[5]) ^ (~e & hash[6])) + K[i] +
          (w[i] = i < 16 ? w[i] : (w[i - 16] + (rot(w15, 7) ^ rot(w15, 18) ^ (w15 >>> 3)) + w[i - 7] + (rot(w2, 17) ^ rot(w2, 19) ^ (w2 >>> 10))) | 0);
        const t2 = (rot(a, 2) ^ rot(a, 13) ^ rot(a, 22)) + ((a & hash[1]) ^ (a & hash[2]) ^ (hash[1] & hash[2]));
        hash = [(t1 + t2) | 0].concat(hash);
        hash[4] = (hash[4] + t1) | 0;
        hash.length = 8;
      }
      for (let i = 0; i < 8; i++) hash[i] = (hash[i] + old[i]) | 0;
    }
    let hex = "";
    for (let i = 0; i < 8; i++) for (let j = 3; j + 1; j--) {
      const b = (hash[i] >> (j * 8)) & 255;
      hex += (b < 16 ? "0" : "") + b.toString(16);
    }
    return hex;
  }

  const hashPass = (user, pass) => sha256("apuntes-kotlin:" + user.toLowerCase() + ":" + pass);

  /* ---------- Datos iniciales ---------- */
  const KEY = "apuntesKotlin.db.v1";
  const SESSION = "apuntesKotlin.session";

  function seed() {
    const now = new Date().toISOString();
    return {
      version: 1,
      users: [
        // Contraseñas iniciales (ver README.md). Solo se guarda el hash SHA-256.
        { username: "Manuel",  role: "admin", enabled: true, created: now, hash: "66d95f1c52acb149eaa524eb4299f972ca7240f437a55e1bb6c0eb0d725bdfb2" },
        { username: "Manuel2", role: "user",  enabled: true, created: now, hash: "716bbfa5f066375e9daabb3cacb8abcc508028caad2dec711eab2fa89cc69df8" },
        { username: "Manuel3", role: "user",  enabled: true, created: now, hash: "b8f6ce975711fd24636ca17459a243ab8e63f8bb7ef884ce462ca6ca6059bead" },
        { username: "Manuel4", role: "user",  enabled: true, created: now, hash: "47734de92f69ceda08d6f5cc6d700371e207201a74c64481b463c43cee321d34" }
      ],
      /* visibilidad[temaId] = { todos: bool, noAdmin: bool, usuarios: [nombres] }
         - todos:    oculto para TODAS las cuentas (también admins)
         - noAdmin:  oculto para todas las cuentas que no son admin
         - usuarios: oculto solo para esas cuentas concretas                 */
      visibilidad: {},
      log: []
    };
  }

  function load() {
    try {
      const raw = localStorage.getItem(KEY);
      if (raw) {
        const db = JSON.parse(raw);
        if (db && Array.isArray(db.users)) return db;
      }
    } catch (e) { /* datos corruptos -> se regeneran */ }
    const db = seed();
    save(db);
    return db;
  }
  function save(db) { localStorage.setItem(KEY, JSON.stringify(db)); }

  let db = load();
  // Si otra pestaña cambia los datos, recargamos
  window.addEventListener("storage", (e) => { if (e.key === KEY) { db = load(); window.dispatchEvent(new Event("db-change")); } });

  const findUser = (name) => db.users.find((u) => u.username.toLowerCase() === String(name).trim().toLowerCase());
  const admins = () => db.users.filter((u) => u.role === "admin");

  function addLog(msg) {
    db.log.unshift({ t: new Date().toISOString(), by: currentUserName() || "-", msg });
    db.log = db.log.slice(0, 200);
  }
  function commit(msg) { if (msg) addLog(msg); save(db); window.dispatchEvent(new Event("db-change")); }

  /* ---------- Sesión ---------- */
  function currentUserName() { return sessionStorage.getItem(SESSION); }
  function currentUser() {
    const n = currentUserName();
    if (!n) return null;
    const u = findUser(n);
    if (!u || !u.enabled) { sessionStorage.removeItem(SESSION); return null; }
    return u;
  }

  function login(username, password) {
    const u = findUser(username);
    if (!u || u.hash !== hashPass(u.username, password)) return { ok: false, error: "Usuario o contraseña incorrectos." };
    if (!u.enabled) return { ok: false, error: "Esta cuenta está deshabilitada. Contacta con el administrador." };
    sessionStorage.setItem(SESSION, u.username);
    u.lastLogin = new Date().toISOString();
    commit();
    return { ok: true, user: u };
  }
  function logout() { sessionStorage.removeItem(SESSION); }

  /* ---------- Gestión de cuentas (solo admin) ---------- */
  function requireAdmin() {
    const u = currentUser();
    if (!u || u.role !== "admin") throw new Error("Solo un administrador puede hacer esto.");
    return u;
  }

  function addUser(username, password, role) {
    requireAdmin();
    username = String(username || "").trim();
    if (!/^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ._-]{3,24}$/.test(username)) throw new Error("El usuario debe tener 3-24 caracteres (letras, números, . _ -).");
    if (findUser(username)) throw new Error("Ya existe una cuenta con ese nombre.");
    if (!password || password.length < 4) throw new Error("La contraseña debe tener al menos 4 caracteres.");
    db.users.push({ username, role: role === "admin" ? "admin" : "user", enabled: true, created: new Date().toISOString(), hash: hashPass(username, password) });
    commit(`Cuenta creada: ${username} (${role})`);
  }

  function deleteUser(username) {
    const me = requireAdmin();
    const u = findUser(username);
    if (!u) throw new Error("La cuenta no existe.");
    if (u.username === me.username) throw new Error("No puedes eliminar tu propia cuenta.");
    if (u.role === "admin" && admins().length <= 1) throw new Error("Debe quedar al menos un administrador.");
    db.users = db.users.filter((x) => x !== u);
    Object.values(db.visibilidad).forEach((v) => { v.usuarios = (v.usuarios || []).filter((n) => n !== u.username); });
    commit(`Cuenta eliminada: ${u.username}`);
  }

  function setEnabled(username, enabled) {
    const me = requireAdmin();
    const u = findUser(username);
    if (!u) throw new Error("La cuenta no existe.");
    if (u.username === me.username && !enabled) throw new Error("No puedes deshabilitar tu propia cuenta.");
    if (!enabled && u.role === "admin" && admins().filter((a) => a.enabled).length <= 1) throw new Error("Debe quedar al menos un administrador habilitado.");
    u.enabled = !!enabled;
    commit(`Cuenta ${enabled ? "habilitada" : "deshabilitada"}: ${u.username}`);
  }

  function setRole(username, role) {
    const me = requireAdmin();
    const u = findUser(username);
    if (!u) throw new Error("La cuenta no existe.");
    if (u.username === me.username && role !== "admin") throw new Error("No puedes quitarte el rol de admin a ti mismo.");
    if (u.role === "admin" && role !== "admin" && admins().length <= 1) throw new Error("Debe quedar al menos un administrador.");
    u.role = role === "admin" ? "admin" : "user";
    commit(`Rol de ${u.username} → ${u.role}`);
  }

  function setPassword(username, password) {
    const me = currentUser();
    if (!me) throw new Error("Sesión no válida.");
    if (me.role !== "admin" && me.username !== username) throw new Error("No tienes permiso.");
    if (!password || password.length < 4) throw new Error("La contraseña debe tener al menos 4 caracteres.");
    const u = findUser(username);
    if (!u) throw new Error("La cuenta no existe.");
    u.hash = hashPass(u.username, password);
    commit(`Contraseña cambiada: ${u.username}`);
  }

  function checkPassword(username, password) {
    const u = findUser(username);
    return !!u && u.hash === hashPass(u.username, password);
  }

  /* ---------- Visibilidad de temas ---------- */
  function vis(temaId) {
    if (!db.visibilidad[temaId]) db.visibilidad[temaId] = { todos: false, noAdmin: false, usuarios: [] };
    const v = db.visibilidad[temaId];
    v.usuarios = v.usuarios || [];
    return v;
  }

  /** ¿Puede el usuario ver el tema? */
  function canSee(user, temaId) {
    if (!user) return false;
    const v = db.visibilidad[temaId];
    if (!v) return true;
    if (v.todos) return false;
    if (v.noAdmin && user.role !== "admin") return false;
    if ((v.usuarios || []).includes(user.username)) return false;
    return true;
  }

  /** Motivo por el que un usuario no ve un tema (para el panel) */
  function hiddenReason(user, temaId) {
    const v = db.visibilidad[temaId];
    if (!v) return null;
    if (v.todos) return "Oculto para todas las cuentas";
    if (v.noAdmin && user.role !== "admin") return "Oculto para no-admins";
    if ((v.usuarios || []).includes(user.username)) return "Oculto para esta cuenta";
    return null;
  }

  function setVisTodos(temaId, hidden) { requireAdmin(); vis(temaId).todos = !!hidden; commit(`${temaId}: ${hidden ? "oculto" : "visible"} para todas las cuentas`); }
  function setVisNoAdmin(temaId, hidden) { requireAdmin(); vis(temaId).noAdmin = !!hidden; commit(`${temaId}: ${hidden ? "oculto" : "visible"} para no-admins`); }
  function setVisUsuario(temaId, username, hidden) {
    requireAdmin();
    const v = vis(temaId);
    v.usuarios = v.usuarios.filter((n) => n !== username);
    if (hidden) v.usuarios.push(username);
    commit(`${temaId}: ${hidden ? "oculto" : "visible"} para ${username}`);
  }
  function resetVis(temaId) { requireAdmin(); delete db.visibilidad[temaId]; commit(`${temaId}: visibilidad restablecida`); }

  /* ---------- Copia de seguridad ---------- */
  function exportJSON() { requireAdmin(); return JSON.stringify(db, null, 2); }
  function importJSON(text) {
    requireAdmin();
    const data = JSON.parse(text);
    if (!data || !Array.isArray(data.users) || !data.users.some((u) => u.role === "admin")) throw new Error("Fichero no válido (debe contener al menos un admin).");
    data.visibilidad = data.visibilidad || {};
    data.log = data.log || [];
    db = data;
    commit("Datos importados desde copia de seguridad");
  }
  function resetAll() { db = seed(); save(db); logout(); }

  window.DB = {
    get data() { return db; },
    hashPass, login, logout, currentUser, findUser,
    addUser, deleteUser, setEnabled, setRole, setPassword, checkPassword,
    canSee, hiddenReason, vis, setVisTodos, setVisNoAdmin, setVisUsuario, resetVis,
    exportJSON, importJSON, resetAll
  };
})();
