package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.comunidad.Comunidad;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ComunidadRepository implements Repository<Comunidad>, WithSimplePersistenceUnit {

    public ComunidadRepository() {

    }

    @Override
    public List<Comunidad> findAll() {
        return entityManager().createQuery("FROM " + Comunidad.class.getName()).getResultList();
    }

    @Override
    public Comunidad findById(Long id) {
        return entityManager().find(Comunidad.class, id);
    }

    @Override
    public void save(Comunidad comunidad) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (comunidad.getId() == null)
            entityManager().persist(comunidad);
        else
            entityManager().merge(comunidad);

        tx.commit();
    }

    @Override
    public void update(Comunidad comunidad){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(comunidad);
        tx.commit();
    }

    @Override
    public void delete(Comunidad comunidad) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(comunidad);
        tx.commit();
    }


}
