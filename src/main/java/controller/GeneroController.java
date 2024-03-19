package controller;

import model.Genero;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Esta clase gestiona las operaciones relacionadas con la entidad Género en la base de datos.
 */
public class GeneroController {
    /**
     * Sirve para interactuar con la base de datos.
     */
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Constructor de la clase GeneroController.
     * @param entityManagerFactory el EntityManagerFactory para interactuar con la base de datos.
     */
    public GeneroController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    /**
     * Método para añadir una lista de géneros a la base de datos.
     * @param generos la lista de géneros a añadir.
     */
    public void addGeneros(List<Genero> generos) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Genero ganero:generos){
            entityManager.persist(ganero);
        }
        entityManager.getTransaction().commit();
    }


}
