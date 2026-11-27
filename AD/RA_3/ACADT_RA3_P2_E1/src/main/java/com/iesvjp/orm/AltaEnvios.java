package com.iesvjp.orm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Direccion;
import com.iesvjp.orm.modelo.Envio;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AltaEnvios {

    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING); // quito los mensajes de info

        try {
            List<Long> ids = guardarEnvios(); // paso 1, me devuelve los ids
            mostrarEnvios(ids); // paso 2, los busco con esos ids
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            JPAUtil.cerrar(); // la factoria se cierra una vez al final
        }
    }

    // 1. los tres envios en una sola transaccion
    private static List<Long> guardarEnvios() {
        System.out.println("1. Guardar tres envíos");
        LocalDate entrega = LocalDate.now().plusDays(2); // hoy + 2 dias

        // creo los envios en memoria, todavia sin id (transient)
        Envio e1 = new Envio("ES0000000001", "Portátil", 2.5, LocalDateTime.now(), entrega,
                new Direccion("C/ Ancha, 3", "10600", "Plasencia", "Cáceres"), // origen
                new Direccion("C/ Mayor, 5", "06800", "Mérida", "Badajoz")); // destino
        Envio e2 = new Envio("ES0000000002", "Libros de texto", 4.0, LocalDateTime.now(), entrega,
                new Direccion("Avda. de Portugal, 20", "10001", "Cáceres", "Cáceres"),
                new Direccion("C/ Gran Vía, 30", "28013", "Madrid", "Madrid"));
        Envio e3 = new Envio("ES0000000003", "Ropa", 1.2, LocalDateTime.now(), null, // este sin fecha de entrega
                new Direccion("C/ San Pedro, 12", "06002", "Badajoz", "Badajoz"),
                new Direccion("C/ Sierpes, 7", "41004", "Sevilla", "Sevilla"));

        EntityManager em = JPAUtil.getEntityManager(); // abro el em
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin(); // empiezo la transaccion
            em.persist(e1); // insert, ya tiene id
            em.persist(e2); // insert
            em.persist(e3); // insert
            tx.commit(); // se guardan los 3 o ninguno

            List<Long> ids = new ArrayList<>(); // guardo los ids para el paso 2
            ids.add(e1.getId());
            ids.add(e2.getId());
            ids.add(e3.getId());
            System.out.println("Guardados con ids " + ids);
            return ids;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback(); // si falla deshago todo
                System.out.println("Error al guardar, se hace rollback");
            }
            throw ex;
        } finally {
            em.close(); // cierro el em siempre
        }
    }

    // 2. recupero cada uno con find y lo muestro
    private static void mostrarEnvios(List<Long> ids) {
        System.out.println("2. Recuperar los envíos");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            for (Long id : ids) {
                Envio e = em.find(Envio.class, id); // select por id
                System.out.println(e); // toString
            }
        } finally {
            em.close(); // solo leo, no hace falta transaccion
        }
    }
}
