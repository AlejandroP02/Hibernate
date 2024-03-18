package controller;

import model.Estudio;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class EstuidoController {
    private static EntityManagerFactory entityManagerFactory;

    public EstuidoController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory=entityManagerFactory;
    }

    public void addEstudios(List<Estudio> estudios) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Estudio estudio:estudios){
            entityManager.persist(estudio);
        }
        entityManager.getTransaction().commit();
    }

}
