package controller;

import model.Genero;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class GeneroController {
    private static EntityManagerFactory entityManagerFactory;

    public GeneroController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    public void addGeneros(List<Genero> generos) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Genero ganero:generos){
            entityManager.persist(ganero);
        }
        entityManager.getTransaction().commit();
    }


}
