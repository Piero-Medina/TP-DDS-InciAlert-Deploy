package models.dataBase.repositorios;

import models.dominio.servicios.Servicio;

import javax.persistence.EntityTransaction;
import java.util.List;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class ServicioRepository implements Repository<Servicio>, WithSimplePersistenceUnit {

    public ServicioRepository() {
    }

    @Override
    public List<Servicio> findAll() {
        return entityManager().createQuery("FROM " + Servicio.class.getName()).getResultList();
    }

    @Override
    public Servicio findById(Long id) {
        return entityManager().find(Servicio.class, id);
    }

    @Override
    public void save(Servicio servicio) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (servicio.getId() == null)
            entityManager().persist(servicio);
        else
            entityManager().merge(servicio);

        tx.commit();
    }

    @Override
    public void update(Servicio servicio) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(servicio);
        tx.commit();
    }

    @Override
    public void delete(Servicio servicio) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(servicio);
        tx.commit();
    }

}
