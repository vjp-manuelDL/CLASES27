package com.iesvjp.orm.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apellido", nullable = false, length = 40)
    private String apellido;

    @Column(name = "oficio", nullable = false, length = 20)
    private String oficio;

    @Column(name = "salario", nullable = false)
    private Double salario;

    // la comision puede ser null
    @Column(name = "comision")
    private Double comision;

    // de momento solo guardo el numero del departamento, sin relacion
    @Column(name = "id_departamento", nullable = false)
    private Long idDepartamento;

    protected Empleado() {
    }

    public Empleado(String apellido, String oficio, Double salario, Double comision, Long idDepartamento) {
        this.apellido = apellido;
        this.oficio = oficio;
        this.salario = salario;
        this.comision = comision;
        this.idDepartamento = idDepartamento;
    }

    public Long getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @Override
    public String toString() {
        return "Empleado[id=" + id + ", apellido=" + apellido + ", oficio=" + oficio + ", salario=" + salario
                + ", comision=" + comision + ", idDepartamento=" + idDepartamento + "]";
    }
}
