package models.dataBase.repositorios;

import models.dominio.servicios.Incidente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.List;

public class IncidenteRepository implements Repository<Incidente>, WithSimplePersistenceUnit {


    public IncidenteRepository(){
    }

    @Override
    public List<Incidente> findAll() {
        return entityManager().createQuery("FROM " + Incidente.class.getName()).getResultList();
    }

    @Override
    public Incidente findById(Long id) {
        return entityManager().find(Incidente.class, id);
    }

    @Override
    public void save(Incidente incidente) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (incidente.getId() == null)
            entityManager().persist(incidente);
        else
            entityManager().merge(incidente);

        tx.commit();
    }

    @Override
    public void update(Incidente incidente) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(incidente);
        tx.commit();
    }

    @Override
    public void delete(Incidente incidente) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(incidente);
        tx.commit();
    }

}
