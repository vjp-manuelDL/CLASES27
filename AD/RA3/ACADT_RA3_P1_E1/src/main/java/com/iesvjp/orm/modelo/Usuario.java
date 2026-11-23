package com.iesvjp.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de ejemplo · ACADT · RA3 · CE c (configuraciones de mapeo por anotaciones).
 *
 *  - @Entity: la clase es una entidad gestionada por Hibernate.
 *  - @Table: nombre de la tabla.
 *  - @Id y @GeneratedValue: clave primaria generada por la base de datos.
 *  - @Column: nombre y restricciones de cada columna.
 *
 * Los imports son de jakarta.persistence, no de javax.persistence.
 * La entidad necesita un constructor sin argumentos.
 */
@Entity
@Table(name = "usuarios")   // "user" es palabra reservada en varios SGBD: mejor evitarla
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // AUTO_INCREMENT en MySQL
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "mensaje", length = 255)
    private String mensaje;

    /** Constructor vacío obligatorio para JPA. */
    protected Usuario() {
    }

    public Usuario(String nombre, String mensaje) {
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Usuario[id=" + id + ", nombre=" + nombre + ", mensaje=" + mensaje + "]";
    }
}
