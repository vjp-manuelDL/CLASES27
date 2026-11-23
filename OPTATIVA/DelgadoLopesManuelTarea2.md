<!-- markdownlint-disable MD004 --> 
<!-- Esto es para que no me salgan errores por la extension de visual studio--> 
# Tarea 2 - Practica de Markdown

Autor: Manuel Delgado Lopes

## Indice

- [Ejercicio 1: Estructura jerarquica](#ejercicio-1-estructura-jerarquica)
  - [Nivel 3: Que es Markdown](#nivel-3-que-es-markdown)
    - [Nivel 4: Para que se usa](#nivel-4-para-que-se-usa)
- [Ejercicio 2: Formato de texto y enfasis](#ejercicio-2-formato-de-texto-y-enfasis)
- [Ejercicio 3: Listas anidadas](#ejercicio-3-listas-anidadas)
- [Ejercicio 4: Enlaces e imagenes](#ejercicio-4-enlaces-e-imagenes)
- [Ejercicio 5: Bloques de codigo](#ejercicio-5-bloques-de-codigo)

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

[1]: https://www.markdownguide.org
