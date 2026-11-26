#!/usr/bin/env python3
"""
Convierte los apuntes escritos en /contenido/*.md (formato markdown simplificado)
en js/contenido.js, que es lo que carga la web.

Uso (desde la carpeta del proyecto):   python herramientas/build.py

Formato soportado en los .md:
  @id tema3                      -> identificador del tema
  @titulo Introducción a Kotlin  -> título del tema
  @numero 3
  @resumen texto corto
  ## Sección                     -> nueva sección (aparece en el menú lateral)
  ### Subtítulo
  - lista / 1. lista numerada
  | tabla | markdown |
  > nota destacada
  ```kotlin file=ruta/Fichero.kt  -> bloque de código (los consecutivos con file= se agrupan en pestañas)
  :::ejercicio Ejercicio 01 · Título
     enunciado...
  ---solucion---
     solución...
  :::
  :::tree  (árbol de carpetas)  :::
"""
import html, json, re, sys, pathlib

ROOT = pathlib.Path(__file__).resolve().parent.parent
# Datos del tema que se esta procesando (para poder numerar los ejercicios)
CURRENT = {"tema": "", "lista": []}
SRC = ROOT / "contenido"
OUT = ROOT / "js" / "contenido.js"

# ---------------------------------------------------------------- resaltado
KOTLIN_KW = set("""as break class continue do else false for fun if in interface is null object package
return super this throw true try typealias val var when while by catch constructor finally get import init
override private protected public set data enum open abstract internal lateinit companion const inner sealed
until downTo step it field value""".split())
KOTLIN_TYPES = set("""String Int Long Short Byte Float Double Boolean Char Unit Any List ArrayList MutableList
Array Map Bundle Log""".split())

TOKEN_KT = re.compile(r'''
 (?P<com>//[^\n]*|/\*.*?\*/)
|(?P<str>"""(?:.|\n)*?"""|"(?:\\.|[^"\\\n])*"|'(?:\\.|[^'\\\n])')
|(?P<ann>@[A-Za-z_]\w*)
|(?P<num>\b0x[0-9A-Fa-f]+L?\b|\b\d+(?:\.\d+)?[fFL]?\b)
|(?P<id>[A-Za-z_]\w*)
''', re.S | re.X)

def hl_kotlin(code):
    out, pos = [], 0
    for m in TOKEN_KT.finditer(code):
        out.append(html.escape(code[pos:m.start()]))
        t, k = m.group(0), m.lastgroup
        e = html.escape(t)
        if k == "com":
            out.append(f'<span class="c">{e}</span>')
        elif k == "str":
            # resalta las plantillas $var y ${...} dentro del string
            e = re.sub(r'(\$\{[^}]*\}|\$[A-Za-z_]\w*)', r'<span class="tp">\1</span>', e)
            out.append(f'<span class="s">{e}</span>')
        elif k == "ann":
            out.append(f'<span class="a">{e}</span>')
        elif k == "num":
            out.append(f'<span class="n">{e}</span>')
        else:
            if t in KOTLIN_KW:
                out.append(f'<span class="k">{e}</span>')
            elif t in KOTLIN_TYPES or (t[0].isupper() and len(t) > 1):
                out.append(f'<span class="t">{e}</span>')
            elif code[m.end():m.end()+1] == "(":
                out.append(f'<span class="f">{e}</span>')
            else:
                out.append(e)
        pos = m.end()
    out.append(html.escape(code[pos:]))
    return "".join(out)

TOKEN_XML = re.compile(r'(?P<com><!--.*?-->)|(?P<tag></?[\w:.-]+|/?>)|(?P<attr>[\w:.-]+(?==))|(?P<str>"[^"]*")', re.S)
def hl_xml(code):
    out, pos = [], 0
    for m in TOKEN_XML.finditer(code):
        out.append(html.escape(code[pos:m.start()]))
        cls = {"com": "c", "tag": "k", "attr": "a", "str": "s"}[m.lastgroup]
        out.append(f'<span class="{cls}">{html.escape(m.group(0))}</span>')
        pos = m.end()
    out.append(html.escape(code[pos:]))
    return "".join(out)

def highlight(code, lang):
    if lang in ("kotlin", "kt", "kts", "gradle"):
        return hl_kotlin(code)
    if lang in ("xml", "html"):
        return hl_xml(code)
    return html.escape(code)

# ---------------------------------------------------------------- inline
def inline(t):
    t = html.escape(t, quote=False)
    t = re.sub(r'`([^`]+)`', r'<code>\1</code>', t)
    t = re.sub(r'\*\*([^*]+)\*\*', r'<strong>\1</strong>', t)
    t = re.sub(r'(?<![\w*])\*([^*\n]+)\*(?![\w*])', r'<em>\1</em>', t)
    t = re.sub(r'\[([^\]]+)\]\((https?://[^)]+)\)', r'<a href="\2" target="_blank" rel="noopener">\1</a>', t)
    return t

# ---------------------------------------------------------------- bloques
def code_html(lang, file, code):
    label = file or {"kotlin": "Kotlin", "xml": "XML", "text": "Salida", "logcat": "Logcat"}.get(lang, lang or "código")
    cls = "logcat" if lang in ("logcat", "text") else ""
    return (f'<div class="code {cls}" data-lang="{html.escape(lang)}"><div class="code-head">'
            f'<span class="code-file">{html.escape(label)}</span><button class="copy" type="button">Copiar</button></div>'
            f'<pre><code>{highlight(code.rstrip(), lang)}</code></pre></div>')

def tabs_html(blocks):
    if len(blocks) == 1:
        return code_html(*blocks[0])
    heads, bodies = [], []
    for i, (lang, file, code) in enumerate(blocks):
        name = file.split("/")[-1] if file else lang
        act = " active" if i == 0 else ""
        heads.append(f'<button type="button" class="tab{act}" data-i="{i}" title="{html.escape(file or "")}">{html.escape(name)}</button>')
        bodies.append(f'<div class="tab-body{act}" data-i="{i}">{code_html(lang, file, code)}</div>')
    return f'<div class="tabs"><div class="tab-heads">{"".join(heads)}</div>{"".join(bodies)}</div>'

def render(lines):
    """Convierte una lista de líneas en HTML."""
    out, i, n = [], 0, len(lines)
    pending_tabs = []

    def flush_tabs():
        if pending_tabs:
            out.append(tabs_html(pending_tabs[:]))
            pending_tabs.clear()

    while i < n:
        line = lines[i]
        s = line.strip()
        # bloque de código
        m = re.match(r'^```(\w*)\s*(?:file=(\S+))?\s*$', s)
        if m:
            lang, file = m.group(1) or "text", m.group(2)
            buf = []
            i += 1
            while i < n and lines[i].strip() != "```":
                buf.append(lines[i])
                i += 1
            i += 1
            # quitar sangría común
            ind = min((len(l) - len(l.lstrip()) for l in buf if l.strip()), default=0)
            code = "\n".join(l[ind:] for l in buf)
            if file:
                pending_tabs.append((lang, file, code))
            else:
                flush_tabs()
                out.append(code_html(lang, None, code))
            continue
        if not s:
            i += 1
            continue
        flush_tabs()
        # ejercicio
        m = re.match(r'^:::ejercicio\s+(.*)$', s)
        if m:
            title = m.group(1)
            depth, buf = 1, []
            i += 1
            while i < n:
                t = lines[i].strip()
                if t.startswith(":::") and t != ":::":
                    depth += 1
                elif t == ":::":
                    depth -= 1
                    if depth == 0:
                        break
                buf.append(lines[i])
                i += 1
            i += 1
            if "---solucion---" in [b.strip() for b in buf]:
                k = [b.strip() for b in buf].index("---solucion---")
                enun, sol = buf[:k], buf[k + 1:]
            else:
                enun, sol = buf, []
            ej_id = re.sub(r'[^a-z0-9]+', '-', title.lower().split("·")[0].strip())
            # numero del ejercicio y titulo corto, para el apartado "Ejercicios"
            mnum = re.search(r'(\d+)', title.split("·")[0])
            num = int(mnum.group(1)) if mnum else len(CURRENT["lista"]) + 1
            corto = title.split("·", 1)[1].strip() if "·" in title else title
            anchor = f'ej-{CURRENT["tema"]}-{num}'
            CURRENT["lista"].append({
                "num": num, "titulo": corto, "anchor": anchor,
                "resumen": resumen_texto(enun), "resuelto": bool(sol)
            })
            out.append(
                f'<section class="ejercicio" id="{anchor}" data-ej="{html.escape(ej_id)}" data-num="{num}">'
                f'<header class="ej-head"><span class="ej-badge">Ejercicio {num:02d}</span>'
                f'<h3>{inline(corto)}</h3></header><div class="ej-enunciado">{render(enun)}</div>'
                + (f'<button class="ej-toggle" type="button">Ver solución</button><div class="ej-solucion" hidden>{render(sol)}</div>' if sol else "")
                + '</section>')
            continue
        # lista de recursos:  tipo | titulo | descripcion | url
        if s == ":::recursos":
            buf = []
            i += 1
            while i < n and lines[i].strip() != ":::":
                if lines[i].strip():
                    buf.append(lines[i].strip())
                i += 1
            i += 1
            ICONOS = {"pdf": "📄", "url": "🔗", "sol": "✅", "img": "🖼️", "cfg": "⚙️", "entrega": "📤", "doc": "📝"}
            filas = []
            for b in buf:
                partes = [x.strip() for x in b.split("|")]
                tipo = partes[0] if partes else "url"
                titulo = partes[1] if len(partes) > 1 else ""
                desc = partes[2] if len(partes) > 2 else ""
                url = partes[3] if len(partes) > 3 else "#"
                filas.append(
                    f'<a class="recurso" href="{html.escape(url)}" target="_blank" rel="noopener">'
                    f'<span class="ico">{ICONOS.get(tipo, "🔗")}</span>'
                    f'<span><b>{inline(titulo)}</b><small>{inline(desc)}</small></span>'
                    f'<span class="flecha">↗</span></a>')
            out.append('<div class="recursos">' + "".join(filas) + '</div>')
            continue
        if s == ":::tree":
            buf = []
            i += 1
            while i < n and lines[i].strip() != ":::":
                buf.append(lines[i])
                i += 1
            i += 1
            ind = min((len(l) - len(l.lstrip()) for l in buf if l.strip()), default=0)
            out.append('<div class="tree"><div class="code-head"><span class="code-file">Estructura del proyecto</span></div><pre>'
                       + html.escape("\n".join(l[ind:] for l in buf)) + '</pre></div>')
            continue
        if s.startswith("### "):
            out.append(f'<h3>{inline(s[4:])}</h3>')
            i += 1
            continue
        if s.startswith("#### "):
            out.append(f'<h4>{inline(s[5:])}</h4>')
            i += 1
            continue
        if s.startswith(">"):
            buf = []
            while i < n and lines[i].strip().startswith(">"):
                buf.append(lines[i].strip()[1:].strip())
                i += 1
            kind = "aviso" if buf and buf[0].startswith("!") else "nota"
            if kind == "aviso":
                buf[0] = buf[0][1:].strip()
            out.append(f'<div class="callout {kind}">' + "<br>".join(inline(b) for b in buf) + '</div>')
            continue
        if s.startswith("|"):
            rows = []
            while i < n and lines[i].strip().startswith("|"):
                cells = [c.strip() for c in lines[i].strip().strip("|").split("|")]
                if not all(re.fullmatch(r':?-+:?', c) for c in cells):
                    rows.append(cells)
                i += 1
            h = "".join(f"<th>{inline(c)}</th>" for c in rows[0])
            b = "".join("<tr>" + "".join(f"<td>{inline(c)}</td>" for c in r) + "</tr>" for r in rows[1:])
            out.append(f'<div class="table-wrap"><table><thead><tr>{h}</tr></thead><tbody>{b}</tbody></table></div>')
            continue
        if re.match(r'^(-|\d+\.)\s', s):
            ordered = bool(re.match(r'^\d+\.', s))
            items = []
            while i < n and lines[i].strip() and (re.match(r'^\s*(-|\d+\.)\s', lines[i]) or lines[i].startswith("    ")):
                l = lines[i]
                if re.match(r'^\s{2,}(-|\d+\.)\s', l) and items:
                    items[-1][1].append(re.sub(r'^\s*(-|\d+\.)\s', '', l))
                elif re.match(r'^\s*(-|\d+\.)\s', l):
                    items.append([re.sub(r'^\s*(-|\d+\.)\s', '', l), []])
                else:
                    items[-1][0] += " " + l.strip()
                i += 1
            tag = "ol" if ordered else "ul"
            li = "".join(
                f"<li>{inline(t)}" + (("<ul>" + "".join(f"<li>{inline(x)}</li>" for x in sub) + "</ul>") if sub else "") + "</li>"
                for t, sub in items)
            out.append(f"<{tag}>{li}</{tag}>")
            continue
        # párrafo
        buf = []
        while i < n and lines[i].strip() and not re.match(r'^(```|:::|#|>|\||-\s|\d+\.\s)', lines[i].strip()):
            buf.append(lines[i].strip())
            i += 1
        if buf:
            out.append(f"<p>{inline(' '.join(buf))}</p>")
        else:
            i += 1
    flush_tabs()
    return "".join(out)

def resumen_texto(lines):
    """Primeras palabras del enunciado, en texto plano, para el listado de ejercicios."""
    txt = []
    for l in lines:
        s = l.strip()
        if not s or s.startswith(("```", ":::", "|", ">", "#")):
            if txt:
                break
            continue
        txt.append(re.sub(r'[*`\[\]]', '', s))
        if sum(len(x) for x in txt) > 150:
            break
    t = " ".join(txt)
    return (t[:170].rsplit(" ", 1)[0] + "…") if len(t) > 170 else t


def parse_file(path):
    text = path.read_text(encoding="utf-8")
    meta, sections, cur = {}, [], None
    for line in text.splitlines():
        m = re.match(r'^@(\w+)\s+(.*)$', line)
        if m and cur is None:
            meta[m.group(1)] = m.group(2).strip()
            continue
        if line.startswith("## "):
            cur = {"titulo": line[3:].strip(), "lines": []}
            sections.append(cur)
            continue
        if cur is not None:
            cur["lines"].append(line)
    secs = []
    CURRENT["tema"] = meta["id"]
    CURRENT["lista"] = []
    ejercicios = []
    for k, s in enumerate(sections):
        CURRENT["lista"] = []
        body = render(s["lines"])
        sec_id = f'{meta["id"]}-s{k + 1}'
        for e in CURRENT["lista"]:
            e["seccion"] = sec_id
            e["seccionTitulo"] = s["titulo"]
            ejercicios.append(e)
        secs.append({
            "id": f'{meta["id"]}-s{k + 1}',
            "titulo": s["titulo"],
            "html": body,
            "ejercicios": len(re.findall(r'class="ejercicio"', body)),
        })
    ejercicios.sort(key=lambda e: e["num"])
    return {"id": meta["id"], "numero": int(meta.get("numero", 0)), "titulo": meta["titulo"],
            "resumen": meta.get("resumen", ""), "secciones": secs, "ejercicios": ejercicios}

def main():
    temas = [parse_file(p) for p in sorted(SRC.glob("tema*.md"))]
    temas.sort(key=lambda t: t["numero"])
    OUT.write_text("/* Generado automáticamente por herramientas/build.py — no editar a mano. */\n"
                   "window.TEMAS = " + json.dumps(temas, ensure_ascii=False) + ";\n", encoding="utf-8")
    total = sum(s["ejercicios"] for t in temas for s in t["secciones"])
    print(f"OK: {len(temas)} temas, {sum(len(t['secciones']) for t in temas)} secciones, {total} ejercicios -> {OUT}")

if __name__ == "__main__":
    main()
