package com.iesvjp.orm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

import com.iesvjp.orm.modelo.Direccion;
import com.iesvjp.orm.modelo.Envio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.PersistenceUnitTransactionType;

/**
 * Punto único de acceso a la unidad de persistencia · ACADT · RA3 · CE b.
 *
 * La unidad de persistencia se define en código con PersistenceConfiguration (JPA 3.2),
 * que es el equivalente programático del clásico META-INF/persistence.xml.
 * Así el proyecto no depende de las herramientas JPA de ningún IDE.
 * Los datos de conexión se leen del fichero jpa.properties.
 *
 * El EntityManagerFactory es costoso de crear (analiza las entidades y abre el pool de
 * conexiones), así que se crea UNA vez por aplicación. Los EntityManager son ligeros:
 * se crea uno por unidad de trabajo y se cierra al terminar.
 */
public final class JPAUtil {

    private static final String UNIDAD_PERSISTENCIA = "ra3p2e2";
    private static final String FICHERO_CONFIGURACION = "/jpa.properties";

    private static final EntityManagerFactory EMF = crearEntityManagerFactory();

    private JPAUtil() {
    }

    private static EntityManagerFactory crearEntityManagerFactory() {
        PersistenceConfiguration config = new PersistenceConfiguration(UNIDAD_PERSISTENCIA)
                .provider("org.hibernate.jpa.HibernatePersistenceProvider")
                .transactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
                // Entidades gestionadas: añadir aquí cada clase @Entity nueva
                .managedClass(Envio.class)
                // Clases embebibles (@Embeddable) que usan las entidades
                .managedClass(Direccion.class);

        // Propiedades de conexión y de Hibernate leídas de jpa.properties
        cargarPropiedades().forEach((clave, valor) -> config.property((String) clave, valor));

        return config.createEntityManagerFactory();
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream in = JPAUtil.class.getResourceAsStream(FICHERO_CONFIGURACION)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra " + FICHERO_CONFIGURACION + " en src/main/resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static void cerrar() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
