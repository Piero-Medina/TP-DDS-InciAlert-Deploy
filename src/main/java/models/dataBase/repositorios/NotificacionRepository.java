package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.actores.Ciudadano;
import models.dominio.notificaciones.Notificacion;

import javax.persistence.EntityTransaction;
import java.util.List;

public class NotificacionRepository implements Repository<Notificacion>, WithSimplePersistenceUnit {
    @Override
    public List<Notificacion> findAll() {
        return entityManager().createQuery("FROM " + Notificacion.class.getName()).getResultList();
    }

    @Override
    public Notificacion findById(Long id) {
        return entityManager().find(Notificacion.class, id);
    }

    @Override
    public void save(Notificacion notificacion) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (notificacion.getId() == null)
            entityManager().persist(notificacion);
        else
            entityManager().merge(notificacion);

        tx.commit();
    }

    @Override
    public void update(Notificacion notificacion) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(notificacion);
        tx.commit();
    }

    @Override
    public void delete(Notificacion notificacion) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(notificacion);
        tx.commit();
    }

}
