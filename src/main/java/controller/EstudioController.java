package controller;

import model.Estudio;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Esta clase gestiona las operaciones relacionadas con la entidad Estudio en la base de datos.
 */
public class EstudioController {
    /**
     * Sirve para interactuar con la base de datos.
     */
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Constructor de la clase EstudioController.
     * @param entityManagerFactory el EntityManagerFactory para interactuar con la base de datos.
     */
    public EstudioController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    /**
     * Método para añadir una lista de estudios a la base de datos.
     * @param estudios la lista de estudios a añadir.
     */
    public void addEstudios(List<Estudio> estudios) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Estudio estudio:estudios){
            entityManager.persist(estudio);
        }
        entityManager.getTransaction().commit();
    }

}
