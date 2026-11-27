package com.iesvjp.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Tipo embebido · ACADT · RA3 · CE c.
 *
 * Una clase @Embeddable no tiene tabla propia ni id: sus atributos se guardan como
 * columnas de la tabla de la entidad que la contiene.
 */
@Embeddable
public class Direccion {

    @Column(nullable = false)
    private String via;

    @Column(nullable = false, length = 5)
    private String codigoPostal;

    @Column(nullable = false)
    private String poblacion;

    @Column(nullable = false)
    private String provincia;

    /** Constructor vacío obligatorio para JPA. */
    protected Direccion() {
    }

    public Direccion(String via, String codigoPostal, String poblacion, String provincia) {
        this.via = via;
        this.codigoPostal = codigoPostal;
        this.poblacion = poblacion;
        this.provincia = provincia;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return via + ", " + codigoPostal + " " + poblacion + " (" + provincia + ")";
    }
}
