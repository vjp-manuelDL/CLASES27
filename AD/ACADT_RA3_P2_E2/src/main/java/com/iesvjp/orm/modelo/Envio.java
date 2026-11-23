package com.iesvjp.orm.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // esta clase se guarda en la bd
@Table(name = "envios") // nombre de la tabla
public class Envio {

    @Id // clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // el id lo pone mysql (auto_increment)
    private Long id; // Long y no long para que pueda ser null antes de guardarlo

    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 12) // obligatorio, unico y 12 caracteres
    private String codigoSeguimiento;

    @Column(name = "descripcion", nullable = false, length = 100) // obligatoria, max 100
    private String descripcion;

    @Column(name = "peso", nullable = false) // en kg
    private Double peso;

    @Column(name = "fecha_registro", nullable = false) // fecha y hora -> datetime
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_entrega_prevista") // solo fecha -> date, puede ir vacia
    private LocalDate fechaEntregaPrevista;

    // direccion de origen, sus columnas van dentro de envios con el prefijo origen_
    @Embedded
    @AttributeOverride(name = "via", column = @Column(name = "origen_via", nullable = false))
    @AttributeOverride(name = "codigoPostal", column = @Column(name = "origen_cp", nullable = false, length = 5))
    @AttributeOverride(name = "poblacion", column = @Column(name = "origen_poblacion", nullable = false))
    @AttributeOverride(name = "provincia", column = @Column(name = "origen_provincia", nullable = false))
    private Direccion origen;

    // direccion de destino, si no les cambio el nombre chocan con las de origen
    @Embedded
    @AttributeOverride(name = "via", column = @Column(name = "destino_via", nullable = false))
    @AttributeOverride(name = "codigoPostal", column = @Column(name = "destino_cp", nullable = false, length = 5))
    @AttributeOverride(name = "poblacion", column = @Column(name = "destino_poblacion", nullable = false))
    @AttributeOverride(name = "provincia", column = @Column(name = "destino_provincia", nullable = false))
    private Direccion destino;

    protected Envio() { // constructor vacio, lo necesita jpa para el find
    }

    public Envio(String codigoSeguimiento, String descripcion, Double peso, LocalDateTime fechaRegistro,
            LocalDate fechaEntregaPrevista, Direccion origen, Direccion destino) {
        this.codigoSeguimiento = codigoSeguimiento;
        this.descripcion = descripcion;
        this.peso = peso;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntregaPrevista = fechaEntregaPrevista;
        this.origen = origen;
        this.destino = destino;
    }

    public Long getId() { // sin setId, el id nunca se pone a mano
        return id;
    }

    public String getCodigoSeguimiento() {
        return codigoSeguimiento;
    }

    public void setCodigoSeguimiento(String codigoSeguimiento) {
        this.codigoSeguimiento = codigoSeguimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaEntregaPrevista() {
        return fechaEntregaPrevista;
    }

    public void setFechaEntregaPrevista(LocalDate fechaEntregaPrevista) {
        this.fechaEntregaPrevista = fechaEntregaPrevista;
    }

    public Direccion getOrigen() {
        return origen;
    }

    public void setOrigen(Direccion origen) {
        this.origen = origen;
    }

    public Direccion getDestino() {
        return destino;
    }

    public void setDestino(Direccion destino) {
        this.destino = destino;
    }

    @Override
    public String toString() { // para ver los datos por consola
        return "Envio[id=" + id + ", codigo=" + codigoSeguimiento + ", " + descripcion + ", " + peso + " kg"
                + ", registro=" + fechaRegistro
                + ", entrega prevista=" + (fechaEntregaPrevista != null ? fechaEntregaPrevista : "sin fecha") // si es null pongo sin fecha
                + ", origen: " + origen + ", destino: " + destino + "]";
    }
}
