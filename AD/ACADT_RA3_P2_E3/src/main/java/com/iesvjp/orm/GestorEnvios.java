package com.iesvjp.orm;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iesvjp.orm.modelo.Direccion;
import com.iesvjp.orm.modelo.Envio;
import com.iesvjp.orm.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

// aqui va todo lo que usa el EntityManager, el menu esta en EnviosApp
public class GestorEnvios {

    // los 3 envios de ejemplo al arrancar
    public void insertarEjemplos() {
        LocalDate entrega = LocalDate.now().plusDays(2); // hoy + 2 dias
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Envio("ES0000000001", "Portátil", 2.5, LocalDateTime.now(), entrega,
                    new Direccion("C/ Ancha, 3", "10600", "Plasencia", "Cáceres"),
                    new Direccion("C/ Mayor, 5", "06800", "Mérida", "Badajoz"))); // id 1
            em.persist(new Envio("ES0000000002", "Libros de texto", 4.0, LocalDateTime.now(), entrega,
                    new Direccion("Avda. de Portugal, 20", "10001", "Cáceres", "Cáceres"),
                    new Direccion("C/ Gran Vía, 30", "28013", "Madrid", "Madrid"))); // id 2
            em.persist(new Envio("ES0000000003", "Ropa", 1.2, LocalDateTime.now(), null,
                    new Direccion("C/ San Pedro, 12", "06002", "Badajoz", "Badajoz"),
                    new Direccion("C/ Sierpes, 7", "41004", "Sevilla", "Sevilla"))); // id 3, sin fecha de entrega
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

    // opcion 1, persist de un envio nuevo
    public Long registrar(Envio envio) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(envio); // insert, si el codigo existe falla aqui
            tx.commit();
            return envio.getId(); // el id que ha puesto mysql
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback(); // deshago
            }
            if (esDuplicado(e)) {
                throw new CodigoDuplicadoException(envio.getCodigoSeguimiento()); // aviso con mi excepcion
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // opcion 2 (y para mostrar en la 3, 5 y 6), null si no esta
    public Envio buscar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Envio.class, id); // solo leo, sin transaccion
        } finally {
            em.close();
        }
    }

    // opcion 3, objeto managed: cambio el destino y el commit lo guarda solo (sin merge)
    public Direccion cambiarDestino(Long id, Direccion nueva) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio e = em.find(Envio.class, id); // managed
            if (e == null) {
                throw new EnvioNoEncontradoException(id);
            }
            Direccion anterior = e.getDestino(); // me guardo la vieja para mostrarla
            e.setDestino(nueva); // solo cambio el objeto
            tx.commit(); // update automatico
            return anterior;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // opcion 4, lo cargo y cierro el em, asi el envio queda detached
    public Envio cargarParaEditar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Envio e = em.find(Envio.class, id);
            if (e == null) {
                throw new EnvioNoEncontradoException(id);
            }
            return e;
        } finally {
            em.close(); // aqui pasa a detached
        }
    }

    // opcion 4, si dice S guardo los cambios del detached con merge
    public void guardarCambios(Envio detached) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detached); // select + update con los cambios
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

    // opcion 5, envio nuevo con origen y destino al reves
    public Long registrarDevolucion(Long id, String codigoNuevo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio original = em.find(Envio.class, id);
            if (original == null) {
                throw new EnvioNoEncontradoException(id);
            }
            // misma descripcion y peso, origen = destino del original y al reves
            Envio devolucion = new Envio(codigoNuevo, original.getDescripcion(), original.getPeso(),
                    LocalDateTime.now(), null, copiar(original.getDestino()), copiar(original.getOrigen()));
            em.persist(devolucion); // insert
            tx.commit();
            return devolucion.getId();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (esDuplicado(e)) {
                throw new CodigoDuplicadoException(codigoNuevo);
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // opcion 6, find y remove
    public void anular(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Envio e = em.find(Envio.class, id); // remove necesita un objeto managed
            if (e == null) {
                throw new EnvioNoEncontradoException(id);
            }
            em.remove(e);
            tx.commit(); // delete
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // copia de una direccion para no compartir el mismo objeto entre dos envios
    private Direccion copiar(Direccion d) {
        return new Direccion(d.getVia(), d.getCodigoPostal(), d.getPoblacion(), d.getProvincia());
    }

    // miro si el error viene del unique (codigo repetido), recorro las causas
    private boolean esDuplicado(Throwable e) {
        while (e != null) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }
}
