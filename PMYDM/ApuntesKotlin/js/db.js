/* ==========================================================================
   db.js — Cuentas, sesión y visibilidad de temas
   Los datos se guardan en un archivo JSON del repositorio de GitHub (ver
   config.js) y se sincronizan cada pocos segundos, así un cambio hecho en un
   sitio (p. ej. una contraseña) se aplica en todos los navegadores a la vez.
   Si GitHub no está configurado, funciona en modo local (localStorage).
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
  function seed() {
    const now = new Date().toISOString();
    return {
      version: 2,
      users: [
        // Cuentas iniciales. Solo se guarda el hash SHA-256 de la contraseña.
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


  /* ======================================================================
     ALMACENAMIENTO
     - Copia local en localStorage (para arrancar al instante y sin conexión)
     - Fuente de verdad: archivo JSON en el repositorio de GitHub (config.js)
     ====================================================================== */
  const KEY = "apuntesKotlin.db.v1";          // datos (copia local)
  const META = "apuntesKotlin.db.meta";       // { sha, etag } del archivo remoto
  const TOKEN = "apuntesKotlin.githubToken";  // token del admin (solo en su navegador)
  const SESSION = "apuntesKotlin.session";
  const CFG = (window.APUNTES_CONFIG && window.APUNTES_CONFIG.github) || null;
  const INTERVALO = Math.max(5, ((window.APUNTES_CONFIG || {}).intervaloSegundos || 10)) * 1000;

  const clone = (o) => JSON.parse(JSON.stringify(o));
  const emit = () => window.dispatchEvent(new Event("db-change"));

  function normalize(d) {
    d = d && typeof d === "object" ? d : {};
    d.version = 2;
    d.users = Array.isArray(d.users) ? d.users : [];
    d.visibilidad = d.visibilidad && typeof d.visibilidad === "object" ? d.visibilidad : {};
    d.log = Array.isArray(d.log) ? d.log : [];
    d.users.forEach((u) => { delete u.lastLogin; });
    return d;
  }

  function readLocal() {
    try {
      const d = JSON.parse(localStorage.getItem(KEY) || "null");
      if (d && Array.isArray(d.users) && d.users.length) return normalize(d);
    } catch (e) { /* corrupto -> semilla */ }
    return seed();
  }
  function readMeta() { try { return JSON.parse(localStorage.getItem(META) || "{}") || {}; } catch (e) { return {}; } }
  function saveLocal() {
    localStorage.setItem(KEY, JSON.stringify(db));
    localStorage.setItem(META, JSON.stringify(meta));
  }

  let db = readLocal();
  let meta = readMeta();
  const estado = { modo: "local", ok: null, mensaje: "Comprobando GitHub…", ultima: null, pendiente: null };

  // Otra pestaña del mismo navegador cambió los datos
  window.addEventListener("storage", (e) => {
    if (e.key === KEY || e.key === META) { db = readLocal(); meta = readMeta(); emit(); }
  });

  /* ======================================================================
     GITHUB (API REST "contents")
     ====================================================================== */
  const token = () => localStorage.getItem(TOKEN) || "";
  const apiUrl = (withRef) => `https://api.github.com/repos/${encodeURIComponent(CFG.owner)}/${encodeURIComponent(CFG.repo)}/contents/` +
    CFG.path.split("/").map(encodeURIComponent).join("/") + (withRef ? `?ref=${encodeURIComponent(CFG.branch)}` : "");
  const b64encode = (str) => btoa(unescape(encodeURIComponent(str)));
  const b64decode = (b64) => decodeURIComponent(escape(atob(String(b64).replace(/\s/g, ""))));

  function headers(extra) {
    const h = Object.assign({ Accept: "application/vnd.github+json" }, extra || {});
    if (token()) h.Authorization = "Bearer " + token();
    return h;
  }

  function errorGitHub(res) {
    if (res.status === 401) return "El token de GitHub no es válido o ha caducado.";
    if (res.status === 403 || res.status === 429) return "GitHub ha rechazado la petición (permisos del token o límite de peticiones). Prueba en un minuto.";
    if (res.status === 404) return "No se encuentra el repositorio o la ruta (¿es privado o está mal escrito en js/config.js?).";
    return "GitHub respondió con el error " + res.status + ".";
  }

  /** Descarga el archivo remoto. Solo descarga si ha cambiado (ETag). Devuelve true si cambian los datos. */
  async function pull() {
    if (!CFG) { estado.modo = "local"; estado.mensaje = "Sin configuración de GitHub (js/config.js)."; return false; }
    if (estado.pendiente) return estado.pendiente;
    estado.pendiente = (async () => {
      try {
        const h = headers(meta.etag ? { "If-None-Match": meta.etag } : null);
        const ctrl = new AbortController();
        const timer = setTimeout(() => ctrl.abort(), 8000);
        const res = await fetch(apiUrl(true), { headers: h, cache: "no-store", signal: ctrl.signal }).finally(() => clearTimeout(timer));
        estado.ultima = new Date();
        if (res.status === 304) { estado.modo = "github"; estado.ok = true; estado.mensaje = "Sincronizado con GitHub."; return false; }
        if (res.status === 404) {
          // El archivo aún no existe (o el repo es privado y no hay token)
          meta = { sha: null, etag: null };
          saveLocal();
          estado.modo = "local"; estado.ok = true;
          estado.mensaje = token() ? "El archivo de cuentas aún no existe en GitHub: se creará con el primer cambio."
                                   : "Las cuentas todavía no están publicadas en GitHub (modo local).";
          return false;
        }
        if (!res.ok) throw new Error(errorGitHub(res));
        const json = await res.json();
        const data = normalize(JSON.parse(b64decode(json.content)));
        const cambia = JSON.stringify(data) !== JSON.stringify(db);
        db = data;
        meta = { sha: json.sha, etag: res.headers.get("ETag") };
        saveLocal();
        estado.modo = "github"; estado.ok = true; estado.mensaje = "Sincronizado con GitHub.";
        if (cambia) emit();
        return cambia;
      } catch (e) {
        estado.ok = false;
        estado.mensaje = e.message && !/fetch|network/i.test(e.message) ? e.message : "No se puede conectar con GitHub (¿sin Internet?). Se usan los últimos datos guardados.";
        throw e;
      } finally {
        estado.pendiente = null;
        window.dispatchEvent(new Event("db-sync"));
      }
    })();
    return estado.pendiente;
  }

  async function put(data, mensaje) {
    const body = { message: "Apuntes Kotlin: " + mensaje, content: b64encode(JSON.stringify(data, null, 2) + "\n"), branch: CFG.branch };
    if (meta.sha) body.sha = meta.sha;
    const res = await fetch(apiUrl(false), {
      method: "PUT",
      headers: headers({ "Content-Type": "application/json" }),
      body: JSON.stringify(body)
    });
    if (res.status === 409 || res.status === 422) return { conflicto: true };
    if (!res.ok) throw new Error(errorGitHub(res));
    const json = await res.json();
    return { sha: json.content.sha };
  }

  /** ¿Las cuentas viven en GitHub? (si es así, los cambios hay que guardarlos allí) */
  const remotoActivo = () => !!token() || !!meta.sha;

  /**
   * Aplica un cambio de forma segura:
   *  1) descarga la última versión de GitHub
   *  2) aplica el cambio sobre una copia (las validaciones pueden lanzar error)
   *  3) lo sube a GitHub (si otro admin guardó a la vez, se reintenta)
   */
  async function mutate(fn, mensaje, alGuardar) {
    requireAdmin();
    if (CFG && remotoActivo()) {
      if (!token()) throw new Error("Las cuentas están en GitHub: para cambiarlas añade tu token en Panel admin → Sincronización.");
      for (let intento = 0; intento < 3; intento++) {
        await pull();
        requireAdmin();
        let draft = clone(db);
        draft = fn(draft) || draft;
        addLog(draft, mensaje);
        const r = await put(draft, mensaje);
        if (r.conflicto) { meta.etag = null; continue; }
        db = normalize(draft);
        meta = { sha: r.sha, etag: null };
        saveLocal();
        if (alGuardar) alGuardar();
        estado.modo = "github"; estado.ok = true; estado.ultima = new Date(); estado.mensaje = "Sincronizado con GitHub.";
        emit();
        window.dispatchEvent(new Event("db-sync"));
        return;
      }
      throw new Error("Otro cambio se guardó a la vez. Vuelve a intentarlo.");
    }
    // Modo local (sin GitHub)
    let draft = clone(db);
    draft = fn(draft) || draft;
    addLog(draft, mensaje);
    db = normalize(draft);
    saveLocal();
    if (alGuardar) alGuardar();
    emit();
  }

  function addLog(d, msg) {
    d.log = d.log || [];
    d.log.unshift({ t: new Date().toISOString(), by: currentUserName() || "-", msg });
    d.log = d.log.slice(0, 200);
  }

  /* ---------- Comprobación periódica (para que los cambios lleguen "al instante") ---------- */
  function tick() { if (document.visibilityState === "visible") pull().catch(() => {}); }
  setInterval(tick, INTERVALO);
  document.addEventListener("visibilitychange", tick);
  window.addEventListener("focus", tick);
  window.addEventListener("online", tick);
  pull().catch(() => {});

  /* ======================================================================
     SESIÓN
     Se guarda el usuario y el hash de su contraseña: si la contraseña cambia
     (o la cuenta se deshabilita / elimina) la sesión se cierra sola.
     ====================================================================== */
  const find = (d, name) => d.users.find((u) => u.username.toLowerCase() === String(name).trim().toLowerCase());
  const findUser = (name) => find(db, name);
  const adminsOf = (d) => d.users.filter((u) => u.role === "admin");
  let motivoCierre = "";

  function readSession() { try { return JSON.parse(sessionStorage.getItem(SESSION) || "null"); } catch (e) { return null; } }
  function currentUserName() { const s = readSession(); return s ? s.u : null; }
  function currentUser() {
    const s = readSession();
    if (!s) return null;
    const u = findUser(s.u);
    let motivo = "";
    if (!u) motivo = "Tu cuenta ha sido eliminada.";
    else if (!u.enabled) motivo = "Tu cuenta ha sido deshabilitada por el administrador.";
    else if (u.hash !== s.h) motivo = "La contraseña de tu cuenta ha cambiado. Vuelve a iniciar sesión con la nueva.";
    if (motivo) { sessionStorage.removeItem(SESSION); motivoCierre = motivo; return null; }
    return u;
  }

  async function login(username, password) {
    // Antes de comprobar la contraseña traemos la versión más reciente de GitHub
    // (máximo 5 s de espera; sin conexión se usan los últimos datos guardados)
    try { await Promise.race([pull(), new Promise((r) => setTimeout(r, 5000))]); } catch (e) { /* sin conexión */ }
    const u = findUser(username);
    if (!u || u.hash !== hashPass(u.username, password)) return { ok: false, error: "Usuario o contraseña incorrectos." };
    if (!u.enabled) return { ok: false, error: "Esta cuenta está deshabilitada. Contacta con el administrador." };
    sessionStorage.setItem(SESSION, JSON.stringify({ u: u.username, h: u.hash }));
    motivoCierre = "";
    return { ok: true, user: u };
  }
  function logout() { sessionStorage.removeItem(SESSION); }
  function takeMotivoCierre() { const m = motivoCierre; motivoCierre = ""; return m; }

  function requireAdmin() {
    const u = currentUser();
    if (!u || u.role !== "admin") throw new Error("Solo un administrador puede hacer esto.");
    return u;
  }

  /* ======================================================================
     GESTIÓN DE CUENTAS (solo admin)
     ====================================================================== */
  function addUser(username, password, role) {
    username = String(username || "").trim();
    if (!/^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ._-]{3,24}$/.test(username)) return Promise.reject(new Error("El usuario debe tener 3-24 caracteres (letras, números, . _ -)."));
    if (!password || password.length < 4) return Promise.reject(new Error("La contraseña debe tener al menos 4 caracteres."));
    return mutate((d) => {
      if (find(d, username)) throw new Error("Ya existe una cuenta con ese nombre.");
      d.users.push({ username, role: role === "admin" ? "admin" : "user", enabled: true, created: new Date().toISOString(), hash: hashPass(username, password) });
    }, `cuenta creada: ${username} (${role === "admin" ? "admin" : "usuario"})`);
  }

  function deleteUser(username) {
    const me = currentUserName();
    return mutate((d) => {
      const u = find(d, username);
      if (!u) throw new Error("La cuenta no existe.");
      if (u.username === me) throw new Error("No puedes eliminar tu propia cuenta.");
      if (u.role === "admin" && adminsOf(d).length <= 1) throw new Error("Debe quedar al menos un administrador.");
      d.users = d.users.filter((x) => x !== u);
      Object.values(d.visibilidad).forEach((v) => { v.usuarios = (v.usuarios || []).filter((n) => n !== u.username); });
    }, `cuenta eliminada: ${username}`);
  }

  function setEnabled(username, enabled) {
    const me = currentUserName();
    return mutate((d) => {
      const u = find(d, username);
      if (!u) throw new Error("La cuenta no existe.");
      if (u.username === me && !enabled) throw new Error("No puedes deshabilitar tu propia cuenta.");
      if (!enabled && u.role === "admin" && adminsOf(d).filter((a) => a.enabled).length <= 1) throw new Error("Debe quedar al menos un administrador habilitado.");
      u.enabled = !!enabled;
    }, `cuenta ${enabled ? "habilitada" : "deshabilitada"}: ${username}`);
  }

  function setRole(username, role) {
    const me = currentUserName();
    return mutate((d) => {
      const u = find(d, username);
      if (!u) throw new Error("La cuenta no existe.");
      if (u.username === me && role !== "admin") throw new Error("No puedes quitarte el rol de admin a ti mismo.");
      if (u.role === "admin" && role !== "admin" && adminsOf(d).length <= 1) throw new Error("Debe quedar al menos un administrador.");
      u.role = role === "admin" ? "admin" : "user";
    }, `rol de ${username} → ${role}`);
  }

  async function setPassword(username, password) {
    if (!password || password.length < 4) throw new Error("La contraseña debe tener al menos 4 caracteres.");
    const me = currentUserName();
    let nuevoHash = null;
    await mutate((d) => {
      const u = find(d, username);
      if (!u) throw new Error("La cuenta no existe.");
      nuevoHash = hashPass(u.username, password);
      u.hash = nuevoHash;
    }, `contraseña cambiada: ${username}`, () => {
      // Si es tu propia contraseña, tu sesión sigue abierta con la nueva
      if (me && me.toLowerCase() === username.toLowerCase()) sessionStorage.setItem(SESSION, JSON.stringify({ u: me, h: nuevoHash }));
    });
  }

  /* ======================================================================
     VISIBILIDAD DE TEMAS
     visibilidad[temaId] = { todos, noAdmin, usuarios: [] }
     ====================================================================== */
  function vis(d, temaId) {
    if (!d.visibilidad[temaId]) d.visibilidad[temaId] = { todos: false, noAdmin: false, usuarios: [] };
    d.visibilidad[temaId].usuarios = d.visibilidad[temaId].usuarios || [];
    return d.visibilidad[temaId];
  }

  function canSee(user, temaId) {
    if (!user) return false;
    const v = db.visibilidad[temaId];
    if (!v) return true;
    if (v.todos) return false;
    if (v.noAdmin && user.role !== "admin") return false;
    if ((v.usuarios || []).includes(user.username)) return false;
    return true;
  }

  function hiddenReason(user, temaId) {
    const v = db.visibilidad[temaId];
    if (!v) return null;
    if (v.todos) return "Oculto para todas las cuentas";
    if (v.noAdmin && user.role !== "admin") return "Oculto para no-admins";
    if ((v.usuarios || []).includes(user.username)) return "Oculto para esta cuenta";
    return null;
  }

  const setVisTodos = (temaId, hidden) =>
    mutate((d) => { vis(d, temaId).todos = !!hidden; }, `${temaId}: ${hidden ? "oculto" : "visible"} para todas las cuentas`);
  const setVisNoAdmin = (temaId, hidden) =>
    mutate((d) => { vis(d, temaId).noAdmin = !!hidden; }, `${temaId}: ${hidden ? "oculto" : "visible"} para no-admins`);
  const setVisUsuario = (temaId, username, hidden) =>
    mutate((d) => {
      const v = vis(d, temaId);
      v.usuarios = v.usuarios.filter((n) => n !== username);
      if (hidden) v.usuarios.push(username);
    }, `${temaId}: ${hidden ? "oculto" : "visible"} para ${username}`);
  /** ocultos = { temaId: true/false } para una cuenta (un solo guardado) */
  const setVisUsuarioVarios = (username, ocultos) =>
    mutate((d) => {
      Object.entries(ocultos).forEach(([temaId, hidden]) => {
        const v = vis(d, temaId);
        v.usuarios = v.usuarios.filter((n) => n !== username);
        if (hidden) v.usuarios.push(username);
      });
    }, `temas visibles actualizados para ${username}`);
  const resetVis = (temaId) => mutate((d) => { delete d.visibilidad[temaId]; }, `${temaId}: visibilidad restablecida`);

  /* ======================================================================
     COPIA DE SEGURIDAD Y TOKEN
     ====================================================================== */
  function exportJSON() { requireAdmin(); return JSON.stringify(db, null, 2); }
  function importJSON(text) {
    let data;
    try { data = JSON.parse(text); } catch (e) { return Promise.reject(new Error("El archivo no es un JSON válido.")); }
    if (!data || !Array.isArray(data.users) || !data.users.some((u) => u.role === "admin")) return Promise.reject(new Error("Fichero no válido (debe contener al menos un admin)."));
    return mutate(() => normalize(data), "datos importados desde copia de seguridad");
  }
  async function resetAll() {
    await mutate(() => seed(), "restablecidas las cuentas iniciales");
    logout();
  }

  /** Comprueba el token contra GitHub y lo guarda. Si el archivo de cuentas no existe, lo crea. */
  async function setToken(tk) {
    requireAdmin();
    tk = String(tk || "").trim();
    if (!tk) throw new Error("Pega el token.");
    if (!CFG) throw new Error("Falta la configuración en js/config.js.");
    const res = await fetch(`https://api.github.com/repos/${encodeURIComponent(CFG.owner)}/${encodeURIComponent(CFG.repo)}`, {
      headers: { Accept: "application/vnd.github+json", Authorization: "Bearer " + tk }, cache: "no-store"
    });
    if (res.status === 401) throw new Error("El token no es válido o ha caducado.");
    if (res.status === 404) throw new Error(`El token no tiene acceso a ${CFG.owner}/${CFG.repo}.`);
    if (!res.ok) throw new Error("GitHub respondió con el error " + res.status + ".");
    const repo = await res.json();
    if (repo.permissions && repo.permissions.push === false) throw new Error("El token solo tiene permiso de lectura: necesita Contents → Read and write.");
    localStorage.setItem(TOKEN, tk);
    meta.etag = null;
    await pull();
    if (!meta.sha) {
      // Primera vez: publicamos las cuentas actuales en GitHub
      await mutate((d) => d, "cuentas publicadas en GitHub");
      return "Token guardado. Las cuentas se han publicado en GitHub.";
    }
    return "Token guardado. Las cuentas se sincronizan con GitHub.";
  }
  function clearToken() { localStorage.removeItem(TOKEN); estado.mensaje = "Token eliminado de este navegador."; window.dispatchEvent(new Event("db-sync")); }

  window.DB = {
    get data() { return db; },
    get sync() { return Object.assign({}, estado, { config: CFG, tieneToken: !!token(), publicado: !!meta.sha, intervalo: INTERVALO / 1000 }); },
    hashPass, login, logout, currentUser, findUser, takeMotivoCierre, refresh: pull,
    addUser, deleteUser, setEnabled, setRole, setPassword,
    canSee, hiddenReason, setVisTodos, setVisNoAdmin, setVisUsuario, setVisUsuarioVarios, resetVis,
    exportJSON, importJSON, resetAll, setToken, clearToken
  };
})();
