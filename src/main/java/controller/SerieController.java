package controller;

import model.Serie;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SerieController {
    private static EntityManagerFactory entityManagerFactory;

    public SerieController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    public void addSeries(List<Serie> series) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Serie serie:series){
            entityManager.persist(serie);
        }
        entityManager.getTransaction().commit();
    }
}
