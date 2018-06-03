package com.supero.gerenciador.tarefas.persistence;

import org.jinq.jpa.JinqJPAStreamProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.Serializable;
import java.util.HashMap;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import org.flywaydb.core.Flyway;

@ApplicationScoped
public class PersistenceUtil implements Serializable {

    private static final String UNIT_NAME = "desafioSuperoPU";
    private static final HashMap<Integer, EntityManager> THREAD_LOCAL_EM = new HashMap<>();
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(UNIT_NAME);
    private static final JinqJPAStreamProvider STREAMS = new JinqJPAStreamProvider(EMF);

    public static EntityManager getEntityManager() {
        EntityManager em = THREAD_LOCAL_EM.get(0);
        if (isNull(em)) {
            em = createEntityManager();
            THREAD_LOCAL_EM.put(0, em);
        }
        return em;
    }

    public static EntityManager createEntityManager() {
        return EMF.createEntityManager();
    }

    public static void closeEntityManager() {
        EntityManager entityManager = THREAD_LOCAL_EM.get(0);
        if (nonNull(entityManager) && entityManager.isOpen()) {
            entityManager.close();
        }
        THREAD_LOCAL_EM.remove(0);
    }

    public static JinqJPAStreamProvider getJinqHibernateStreamProvider() {
        return STREAMS;
    }

    public static void initializeDb() {
        try {
            Flyway flyway = new Flyway();
            flyway.setDataSource("jdbc:mysql://localhost:3306/desafio_supero", "dev", "dev");
            flyway.migrate();
        } catch (Exception e) {
            System.out.println("Erro ao realizar migração do banco!");
        }
    }

}
