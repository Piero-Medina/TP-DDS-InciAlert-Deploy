package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.entidades.Establecimiento;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EstablecimientoRepository implements Repository<Establecimiento>, WithSimplePersistenceUnit {


    public EstablecimientoRepository() {

    }

    @Override
    public List<Establecimiento> findAll() {
        return entityManager().createQuery("FROM " + Establecimiento.class.getName()).getResultList();
    }

    @Override
    public Establecimiento findById(Long id) {
        return entityManager().find(Establecimiento.class, id);
    }

    @Override
    public void save(Establecimiento establecimiento) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (establecimiento.getId() == null)
            entityManager().persist(establecimiento);
        else
            entityManager().merge(establecimiento);

        tx.commit();
    }

    @Override
    public void update(Establecimiento establecimiento){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(establecimiento);
        tx.commit();
    }

    @Override
    public void delete(Establecimiento establecimiento) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(establecimiento);
        tx.commit();
    }

}
