package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.actores.Rol;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RolRepository implements Repository<Rol>, WithSimplePersistenceUnit {


    public RolRepository() {

    }

    @Override
    public List<Rol> findAll() {
        return entityManager().createQuery("FROM " + Rol.class.getName()).getResultList();
    }

    @Override
    public Rol findById(Long id) {
        return entityManager().find(Rol.class, id);
    }

    @Override
    public void save(Rol rol) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (rol.getId() == null)
            entityManager().persist(rol);
        else
            entityManager().merge(rol);

        tx.commit();
    }

    @Override
    public void update(Rol rol){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(rol);
        tx.commit();
    }

    @Override
    public void delete(Rol rol) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(rol);
        tx.commit();
    }

}
