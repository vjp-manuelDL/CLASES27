package com.iesvjp.orm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Estudiante;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class GestionEstudiantes {

    // para quitar los INFO de hibernate
    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING);

        try {
            List<Long> ids = guardarEstudiantes();
            if (ids.size() >= 3) {
                buscarEstudiante(ids.get(2));
            }
            buscarEstudiante(999L);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // la factoria solo se cierra aqui, al acabar
            JPAUtil.cerrar();
        }
    }

    // a) guardo los 5 en una sola transaccion
    private static List<Long> guardarEstudiantes() {
        System.out.println("\n===== a) Guardar 5 estudiantes =====");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Estudiante> lista = new ArrayList<>();
        lista.add(new Estudiante("11111111A", "Ana", "Garcia Ruiz", "600111111", "ana@correo.com"));
        lista.add(new Estudiante("22222222B", "Luis", "Martin Sanz", "600222222", "luis@correo.com"));
        lista.add(new Estudiante("33333333C", "Marta", "Lopez Gil", "600333333", "marta@correo.com"));
        lista.add(new Estudiante("44444444D", "Pablo", "Sanchez Moreno", "600444444", "pablo@correo.com"));
        lista.add(new Estudiante("55555555E", "Lucia", "Diaz Romero", "600555555", "lucia@correo.com"));

        try {
            tx.begin();
            for (Estudiante e : lista) {
                System.out.println("Antes de persist -> id = " + e.getId());
                em.persist(e);
                // con IDENTITY aqui ya deberia tener id
                System.out.println("Despues de persist -> id = " + e.getId());
            }
            tx.commit();

            System.out.println("\nDespues del commit:");
            List<Long> ids = new ArrayList<>();
            for (Estudiante e : lista) {
                System.out.println("  " + e);
                ids.add(e.getId());
            }
            return ids;
        } catch (RuntimeException ex) {
            // si falla algo deshago todo
            if (tx.isActive()) {
                tx.rollback();
                System.out.println("Error al guardar, se hace rollback: " + ex.getMessage());
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // b) y c) busco por id, find devuelve null si no esta
    private static void buscarEstudiante(Long id) {
        System.out.println("\n===== Buscar estudiante con id " + id + " =====");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Estudiante e = em.find(Estudiante.class, id);
            if (e == null) {
                System.out.println("No se ha encontrado ningun estudiante con id " + id + " (find devuelve null)");
            } else {
                System.out.println("Encontrado: " + e);
            }
        } finally {
            em.close();
        }
    }
}
