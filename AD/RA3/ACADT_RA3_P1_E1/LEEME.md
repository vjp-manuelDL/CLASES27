# Plantilla ORM 01 · Primer proyecto con Hibernate y JPA

**Módulo:** 0486 Acceso a datos · **RA3** · CE a, b, c, d
**Presentación:** UT4 · 01 ORM. Hibernate y JPA

Proyecto Maven mínimo para comprobar que Hibernate funciona: guarda dos usuarios en la base de datos y recupera uno de ellos. Funciona igual en Eclipse, IntelliJ IDEA o VS Code, sin plugins ni configuración adicional del IDE.

## Requisitos

- JDK 17 o superior.
- Un IDE con soporte de Maven. En Eclipse viene de serie.
- MySQL en marcha, el mismo que en el RA2.

## Puesta en marcha

1. Importar el proyecto como proyecto Maven. En Eclipse: *File → Import → Maven → Existing Maven Projects* y seleccionar esta carpeta.
2. Abrir `src/main/resources/jpa.properties` y poner la URL, el usuario y la contraseña de vuestro MySQL. La base de datos `hibernate` se crea sola si no existe.
3. Ejecutar `App.java` como aplicación Java.

En Eclipse, si se cambia el `pom.xml` y no lo recoge: **Alt+F5** (*Maven → Update Project*).

**No convertir el proyecto en proyecto JPA ni añadirle facetas** en Eclipse.

## Estructura

```
pom.xml                                   Dependencias y versión de Java
src/main/resources/
    jpa.properties                        Conexión y propiedades de Hibernate
src/main/java/com/iesvjp/orm/
    modelo/Usuario.java                   Entidad mapeada con anotaciones
    util/JPAUtil.java                     Unidad de persistencia y EntityManagerFactory
    App.java                              Guarda dos usuarios y recupera uno
docs/
    persistence.xml.ejemplo               La misma configuración en formato persistence.xml (no se usa)
```

## Qué hace `App.java`

1. Guarda a Pepe y a Juan con `persist()` dentro de una transacción. Si algo falla, hace `rollback()`.
2. Recupera a Pepe con `find()` a partir de su id.

Al terminar, la tabla `usuarios` contiene a Pepe y a Juan.

## Añadir una entidad nueva

1. Crear la clase en el paquete `modelo`, con `@Entity` y un atributo `@Id`.
2. Registrarla en `JPAUtil`, añadiendo `.managedClass(MiEntidad.class)`.

## Avisos normales al ejecutar

- `HHH10001002: Using built-in connection pool (not for production use!)`: es un aviso, no un error.
- El SQL que aparece en consola lo genera Hibernate (`hibernate.show_sql`).

## Problemas frecuentes

- **Import de `javax.persistence`**: código copiado de un tutorial antiguo. Cambiarlo por `jakarta.persistence`.
- **`Communications link failure`**: MySQL no está arrancado o la URL de `jpa.properties` es incorrecta.
- **`Access denied for user`**: usuario o contraseña de `jpa.properties`.
- **Entidad desconocida al guardar o consultar**: la clase no está registrada con `.managedClass()`.
- **`PackageNotFoundException ... jakarta.ee/xml/ns/persistence`**: el proyecto tiene la faceta JPA de Eclipse. Quitarla o reimportar sin `.project` ni `.settings`.
- **Clave duplicada al ejecutar dos veces**: se asigna el id a mano en lugar de usar `@GeneratedValue`.
