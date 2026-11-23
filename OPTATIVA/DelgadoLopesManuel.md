[//]: # (Comentario invisible: practicas de Markdown de Manuel Delgado Lopes)

<!-- markdownlint-disable MD004 MD033 MD040 -->

# Practicas de Markdown - Tareas 2 y 3

Autor: Manuel Delgado Lopes

## Indice

- [Ejercicio 1: Estructura jerarquica](#ejercicio-1-estructura-jerarquica)
  - [Nivel 3: Que es Markdown](#nivel-3-que-es-markdown)
    - [Nivel 4: Para que se usa](#nivel-4-para-que-se-usa)
- [Ejercicio 2: Formato de texto y enfasis](#ejercicio-2-formato-de-texto-y-enfasis)
- [Ejercicio 3: Listas anidadas](#ejercicio-3-listas-anidadas)
- [Ejercicio 4: Enlaces e imagenes](#ejercicio-4-enlaces-e-imagenes)
- [Ejercicio 5: Bloques de codigo](#ejercicio-5-bloques-de-codigo)
- [Ejercicio 6: Tablas complejas](#ejercicio-6-tablas-complejas)
- [Ejercicio 7: Listas de tareas y comentarios](#ejercicio-7-listas-de-tareas-y-comentarios)
- [Ejercicio 8: Notas al pie y referencias](#ejercicio-8-notas-al-pie-y-referencias)
- [Ejercicio 9: HTML embebido y emojis](#ejercicio-9-html-embebido-y-emojis)

---

## Ejercicio 1: Estructura jerarquica

Este documento usa cuatro niveles de encabezado: el titulo principal (`#`), una seccion por ejercicio (`##`), un subapartado (`###`) y un sub-subapartado (`####`). El indice del principio enlaza a cada seccion mediante enlaces internos.

### Nivel 3: Que es Markdown

Markdown es un lenguaje de marcado ligero que permite dar formato a un texto plano usando simbolos sencillos.

#### Nivel 4: Para que se usa

Se usa en los README de GitHub, en Notion, en documentacion tecnica y en foros como Reddit o Discord.

---

## Ejercicio 2: Formato de texto y enfasis

Cuando pruebo un videojuego tengo que apuntar los fallos **muy importantes** en negrita, los detalles *menos graves* en cursiva y los ***errores criticos*** en negrita y cursiva. Los bugs que ya estan arreglados los marco como ~~solucionados~~, y los comandos los escribo como codigo en linea, por ejemplo `git status`.

> Markdown fue creado por John Gruber en 2004 para que el texto se pudiera leer facilmente incluso sin convertirlo a HTML.
>
> ---
>
> Debajo de la linea horizontal continua la misma cita.

---

## Ejercicio 3: Listas anidadas

- Aprender Markdown
- Aprender Git
    1. Comandos basicos
        * git init
        * git add
        * git commit
    2. Trabajar con GitHub
        + git push
        + git pull
- Aprender Notion

---

## Ejercicio 4: Enlaces e imagenes

Enlace externo: [Guía Markdown](https://markdownguide.org)

Enlace relativo a un archivo: [Mis notas](documentos/notas.md)

Imagen con texto alternativo y titulo:

![Logotipo de Markdown](https://markdown-here.com/img/icon256.png "Ejemplo")

Enlace de referencia: cuando tengo dudas consulto la [documentacion oficial de Markdown][1].

---

## Ejercicio 5: Bloques de codigo

Para guardar los cambios en Git se usa el comando `git commit -m "mensaje"`.

```python
def contar_vidas(vidas):
    """Muestra cuantas vidas le quedan al jugador."""
    for i in range(vidas, 0, -1):
        print(f"Te quedan {i} vidas")
    print("GAME OVER")

contar_vidas(3)
```

```
Este bloque no tiene lenguaje,
asi que se muestra como texto plano sin colores.
```

---

## Ejercicio 6: Tablas complejas

| Juego                  | Tipo            | Año de salida | Desarrolladora |
|:-----------------------|:---------------:|--------------:|----------------|
| Rainbow Six Siege      | Shooter tactico |          2015 | Ubisoft        |
| Valorant               | Shooter tactico |          2020 | Riot Games     |
| Counter-Strike 2       | Shooter tactico |          2023 | Valve          |
| **Total**              |                 |               | 3 juegos       |

## Ejercicio 7: Listas de tareas y comentarios

- [x] Subir de rango en Valorant
- [x] Aprender los callouts de Bank en Rainbow Six Siege
- [x] Practicar el spray de la AK-47 en Counter-Strike 2
- [ ] Llegar a Diamante en Rainbow Six Siege
- [ ] Ganar un torneo online con mi equipo

## Ejercicio 8: Notas al pie y referencias

Rainbow Six Siege[^1] es un shooter tactico por equipos en el que la destruccion del escenario es clave para atacar o defender.

Valorant[^2] mezcla los disparos de precision con las habilidades especiales de cada agente.

Counter-Strike 2[^3] es la ultima entrega de la saga Counter-Strike y uno de los juegos mas importantes de los esports.

## Ejercicio 9: HTML embebido y emojis

<div style="background: lightblue; padding: 10px;">Texto con fondo azul claro</div>

Emojis con notacion de dos puntos: :video_game: :gun: :trophy:

Emojis Unicode directos: ✨ 🔥 ✅

---

[1]: https://www.markdownguide.org

[^1]: Desarrollado por Ubisoft Montreal y lanzado en 2015.
[^2]: Desarrollado por Riot Games, los creadores de League of Legends, y lanzado en 2020.
[^3]: Desarrollado por Valve y lanzado en 2023 como sustituto de CS:GO.
