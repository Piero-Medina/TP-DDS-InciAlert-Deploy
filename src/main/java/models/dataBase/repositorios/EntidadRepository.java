package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.entidades.Entidad;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EntidadRepository implements Repository<Entidad>, WithSimplePersistenceUnit {


    public EntidadRepository() {

    }

    @Override
    public List<Entidad> findAll() {
        return entityManager().createQuery("FROM " + Entidad.class.getName()).getResultList();
    }

    @Override
    public Entidad findById(Long id) {
        return entityManager().find(Entidad.class, id);
    }


    @Override
    public void save(Entidad entidad) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (entidad.getId() == null)
            entityManager().persist(entidad);
        else
            entityManager().merge(entidad);

        tx.commit();
    }

    @Override
    public void update(Entidad entidad){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(entidad);
        tx.commit();
    }

    @Override
    public void delete(Entidad entidad) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(entidad);
        tx.commit();
    }
}
