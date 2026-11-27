package com.iesvjp.orm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Direccion;
import com.iesvjp.orm.modelo.Envio;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class LaboratorioEstados {

    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    // ids de los 3 envios de ejemplo
    private static Long id1;
    private static Long id2;
    private static Long id3;

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING); // quito los mensajes de info

        try {
            guardarEjemplos(); // los 3 de ejemplo
            Long idNuevo = paso1Guardar(); // el id lo uso en el paso 7 para borrarlo
            paso2Contexto();
            paso3Modificar();
            Envio separado = paso4SepararAlCerrar(); // este objeto lo uso en el paso 6
            paso5Detach();
            paso6Merge(separado);
            paso7Eliminar(idNuevo);
            paso8Errores();
            paso9Rollback();
        } catch (RuntimeException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            JPAUtil.cerrar(); // la factoria se cierra una vez al final
        }
    }

    // al empezar guardo 3 envios
    private static void guardarEjemplos() {
        System.out.println("Envíos de ejemplo");
        Envio a = nuevoEnvio("ES0000000001", "Portátil", 2.5);
        Envio b = nuevoEnvio("ES0000000002", "Libros de texto", 4.0);
        Envio c = nuevoEnvio("ES0000000003", "Ropa", 1.2);

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(a); // insert
            em.persist(b); // insert
            em.persist(c); // insert
            tx.commit();
            id1 = a.getId(); // me guardo los ids para los pasos
            id2 = b.getId();
            id3 = c.getId();
            System.out.println("Guardados los envíos " + id1 + ", " + id2 + " y " + id3);
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // 1. de transient a managed con persist
    private static Long paso1Guardar() {
        System.out.println("\n1. Guardar");
        Envio nuevo = nuevoEnvio("ES0000000004", "Teclado", 0.8); // transient, id null
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            System.out.println("Antes de persist -> id = " + nuevo.getId() + ", contains = " + em.contains(nuevo)); // null y false
            tx.begin();
            em.persist(nuevo); // insert aqui mismo por ser IDENTITY
            System.out.println("Después de persist (antes del commit) -> id = " + nuevo.getId()
                    + ", contains = " + em.contains(nuevo)); // ya tiene id y true
            tx.commit();
            return nuevo.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // 2. mismo id en el mismo em -> mismo objeto y una sola select
    private static void paso2Contexto() {
        System.out.println("\n2. Contexto de persistencia");
        EntityManager em = JPAUtil.getEntityManager(); // em nuevo, contexto vacio
        try {
            System.out.println("Primer find:");
            Envio a = em.find(Envio.class, id1); // select
            System.out.println("Segundo find:");
            Envio b = em.find(Envio.class, id1); // no hay select, lo coge del contexto
            System.out.println("¿a == b? " + (a == b)); // true, es el mismo objeto
        } finally {
            em.close();
        }
    }

    // 3. el cambio dentro de la transaccion se guarda, el de fuera no
    private static void paso3Modificar() {
        System.out.println("\n3. Modificar");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio e = em.find(Envio.class, id1); // managed
            System.out.println("Antes: peso = " + e.getPeso() + ", descripción = " + e.getDescripcion());
            e.setPeso(3.0); // solo cambio el objeto, no hay update()
            tx.commit(); // aqui hibernate ve el cambio y hace el update solo
            System.out.println("Peso cambiado a 3.0 dentro de la transacción");

            e.setDescripcion("Portátil MODIFICADO"); // sin transaccion
            System.out.println("Descripción cambiada fuera de la transacción");
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close(); // al cerrar se pierde el cambio de la descripcion
        }

        Envio bd = buscar(id1); // miro que hay de verdad en la bd
        System.out.println("En la BD: peso = " + bd.getPeso() + ", descripción = " + bd.getDescripcion());
    }

    // 4. al cerrar el em el objeto pasa a detached
    private static Envio paso4SepararAlCerrar() {
        System.out.println("\n4. Separar al cerrar");
        EntityManager em = JPAUtil.getEntityManager();
        Envio e;
        try {
            e = em.find(Envio.class, id2); // managed
            System.out.println("¿Gestionado antes de cerrar? " + em.contains(e)); // true
        } finally {
            em.close(); // aqui pasa a detached
        }

        e.setDestino(new Direccion("C/ Nueva, 1", "06300", "Zafra", "Badajoz")); // este cambio no llega a la bd
        System.out.println("Destino en memoria: " + e.getDestino());
        System.out.println("Destino en la BD: " + buscar(id2).getDestino()); // sigue el de antes
        return e; // lo devuelvo para el merge del paso 6
    }

    // 5. detach con el em abierto
    private static void paso5Detach() {
        System.out.println("\n5. Separar con detach()");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio e = em.find(Envio.class, id3); // managed
            System.out.println("Peso antes: " + e.getPeso());
            em.detach(e); // lo saco del contexto
            System.out.println("Después de detach, contains = " + em.contains(e)); // false
            e.setPeso(10.0); // el em ya no lo vigila
            tx.commit(); // no hace update
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
        System.out.println("Peso en la BD: " + buscar(id3).getPeso()); // sigue igual
    }

    // 6. merge del envio detached del paso 4
    private static void paso6Merge(Envio separado) {
        System.out.println("\n6. Volver a gestionar");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio gestionado = em.merge(separado); // select y copia los datos a otro objeto managed
            System.out.println("¿devuelto == original? " + (gestionado == separado)); // false, son distintos
            System.out.println("¿contains(original)? " + em.contains(separado)
                    + "  ¿contains(devuelto)? " + em.contains(gestionado)); // false y true
            separado.setPeso(50.0); // cambio el original, no el devuelto
            tx.commit(); // update del destino, pero no del peso
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
        Envio bd = buscar(separado.getId());
        System.out.println("En la BD: destino = " + bd.getDestino() + ", peso = " + bd.getPeso());
    }

    // 7. remove
    private static void paso7Eliminar(Long id) {
        System.out.println("\n7. Eliminar");
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio e = em.find(Envio.class, id); // remove solo vale con managed, primero find
            em.remove(e); // pasa a removed
            System.out.println("Después de remove (antes del commit), contains = " + em.contains(e)); // false
            tx.commit(); // aqui se hace el delete
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
        System.out.println("¿Existe todavía el envío " + id + "? " + (buscar(id) != null)); // false
    }

    // 8. errores, cada uno en su try/catch
    private static void paso8Errores() {
        System.out.println("\n8. Errores provocados");

        // 8.1 remove de un detached
        System.out.println("8.1 remove() de un envío detached");
        Envio detached = buscar(id1); // buscar cierra su em, asi que viene detached
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(detached); // error, no es de este em
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Excepción: " + e.getMessage());
        } finally {
            em.close();
        }

        // 8.2 persist de un detached
        System.out.println("8.2 persist() de un envío detached");
        em = JPAUtil.getEntityManager();
        tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detached); // error, ya tiene id, para esto esta merge
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Excepción: " + e.getMessage());
        } finally {
            em.close();
        }

        // 8.3 find de un id que no existe
        System.out.println("8.3 find() del id 999");
        em = JPAUtil.getEntityManager();
        try {
            Envio e = em.find(Envio.class, 999L); // devuelve null
            System.out.println("find devuelve: " + e + " (no hay excepción)");
        } catch (RuntimeException e) {
            System.out.println("Excepción: " + e.getMessage());
        } finally {
            em.close();
        }

        // 8.4 getReference del 999 y luego getDescripcion
        System.out.println("8.4 getReference() del id 999 + getDescripcion()");
        em = JPAUtil.getEntityManager();
        try {
            Envio ref = em.getReference(Envio.class, 999L); // no hace select todavia
            System.out.println("getReference no lanza SELECT todavía");
            System.out.println(ref.getDescripcion()); // aqui hace la select y falla
        } catch (RuntimeException e) {
            System.out.println("Excepción: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // 9. rollback, el primero se deshace aunque tenga id
    private static void paso9Rollback() {
        System.out.println("\n9. Rollback");
        Envio bueno = nuevoEnvio("ES0000000005", "Monitor", 5.5);
        Envio repetido = nuevoEnvio("ES0000000001", "Código repetido", 1.0); // mismo codigo que el 1

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(bueno); // insert bien, coge id
            em.persist(repetido); // falla por el unique
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback(); // deshace tambien el insert del bueno
            }
            System.out.println("Error, se hace rollback: " + e.getMessage());
        } finally {
            em.close();
        }
        System.out.println("Id del primer envío en memoria: " + bueno.getId()); // conserva el id
        System.out.println("¿Existe en la BD? " + (bueno.getId() != null && buscar(bueno.getId()) != null)); // false
    }

    // auxiliares

    // para no repetir las direcciones en cada envio
    private static Envio nuevoEnvio(String codigo, String descripcion, double peso) {
        return new Envio(codigo, descripcion, peso, LocalDateTime.now(), LocalDate.now().plusDays(2),
                new Direccion("C/ Ancha, 3", "10600", "Plasencia", "Cáceres"),
                new Direccion("C/ Mayor, 5", "06800", "Mérida", "Badajoz"));
    }

    // busco en un em nuevo para ver lo que hay de verdad en la bd
    private static Envio buscar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Envio.class, id);
        } finally {
            em.close(); // lo que devuelve queda detached
        }
    }
}
