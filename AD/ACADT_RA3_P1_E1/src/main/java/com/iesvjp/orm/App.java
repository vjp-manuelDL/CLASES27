package com.iesvjp.orm;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Usuario;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


/**
 * Primer proyecto con Hibernate · ACADT · RA3 · CE a, b, c, d.
 *
 *  1. Guardar dos usuarios en la base de datos, dentro de una transacción.
 *  2. Recuperar uno de ellos a partir de su id.
 */
public class App {

    // Referencia estática para que el ajuste de nivel de log no se pierda
    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        // Oculta los mensajes INFO de arranque de Hibernate (el SQL se sigue mostrando)
        LOG_HIBERNATE.setLevel(Level.WARNING);

        try {
            Long idPepe = guardarUsuarios();
            recuperarUsuario(idPepe);
        } finally {
            // El EntityManagerFactory se cierra una sola vez, al terminar el programa
            JPAUtil.cerrar();
        }
    }

    // 1. Guardar dos usuarios con persist() dentro de una transacción
    private static Long guardarUsuarios() {
        System.out.println("\n===== 1. Guardar usuarios =====");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario pepe = new Usuario("Pepe", "Hola desde JPA");
            Usuario juan = new Usuario("Juan", "Hola desde JPA");
            em.persist(pepe);
            em.persist(juan);
            tx.commit();
            System.out.println("Guardados: " + pepe + " y " + juan);
            return pepe.getId();          // el id lo ha asignado la base de datos
        } catch (RuntimeException e) {
            // Si algo falla, se deshace todo lo hecho en la transacción
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // 2. Recuperar un usuario por su id con find()
    private static void recuperarUsuario(Long id) {
        System.out.println("\n===== 2. Recuperar un usuario =====");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            System.out.println("Recuperado: " + usuario);
        } finally {
            em.close();
        }
    }
}
