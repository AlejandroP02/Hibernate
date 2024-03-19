package controller;

import model.Serie;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Esta clase gestiona las operaciones relacionadas con la entidad Serie en la base de datos.
 */
public class SerieController {
    /**
     * Sirve para interactuar con la base de datos.
     */
    private static EntityManagerFactory entityManagerFactory;


    /**
     * Constructor de la clase SerieController.
     * @param entityManagerFactory el EntityManagerFactory para interactuar con la base de datos.
     */
    public SerieController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    /**
     * Método para añadir una lista de series a la base de datos.
     * @param series la lista de series a añadir.
     */
    public void addSeries(List<Serie> series) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Serie serie:series){
            entityManager.persist(serie);
        }
        entityManager.getTransaction().commit();
    }
}
