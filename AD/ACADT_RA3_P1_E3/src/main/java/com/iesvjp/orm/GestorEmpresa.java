package com.iesvjp.orm;

import java.util.List;

import com.iesvjp.orm.modelo.Departamento;
import com.iesvjp.orm.modelo.Empleado;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

// aqui va todo lo que toca el EntityManager, el menu esta en EmpresaApp
public class GestorEmpresa {

    // los 3 departamentos de ejemplo al arrancar
    public void insertarDepartamentosEjemplo() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Departamento("Contabilidad", "Sevilla"));
            em.persist(new Departamento("Investigación", "Madrid"));
            em.persist(new Departamento("Ventas", "Barcelona"));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // opcion 1, devuelvo el id que le ha dado la BD
    public Long altaDepartamento(Departamento d) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(d);
            tx.commit();
            return d.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // opcion 2, primero miro que exista el departamento
    public Long altaEmpleado(Empleado emp) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            if (em.find(Departamento.class, emp.getIdDepartamento()) == null) {
                throw new DepartamentoNoExisteException(
                        "El departamento " + emp.getIdDepartamento() + " no existe, no se guarda el empleado");
            }
            tx.begin();
            em.persist(emp);
            tx.commit();
            return emp.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // opcion 3, null si no existe
    public Departamento buscarDepartamento(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    // opcion 4
    public Empleado buscarEmpleado(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    // opcion 5, todos en una transaccion: si uno falla no se guarda ninguno
    public void altaVariosEmpleados(List<Empleado> empleados) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (int i = 0; i < empleados.size(); i++) {
                Empleado emp = empleados.get(i);
                if (em.find(Departamento.class, emp.getIdDepartamento()) == null) {
                    throw new DepartamentoNoExisteException("El empleado nº " + (i + 1) + " (" + emp.getApellido()
                            + ") tiene el departamento " + emp.getIdDepartamento() + ", que no existe");
                }
                em.persist(emp);
            }
            tx.commit();
        } catch (RuntimeException e) {
            // aqui deshago tambien los que ya se habian insertado
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
