package models.dataBase.repositorios;

import models.dominio.actores.Permiso;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class PermisoRepository implements Repository<Permiso>, WithSimplePersistenceUnit {

    public PermisoRepository() {
    }

    @Override
    public void save(Permiso permiso) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (permiso.getId() == null)
            entityManager().persist(permiso);
        else
            entityManager().merge(permiso);

        tx.commit();
    }

    @Override
    public List<Permiso> findAll() {
        return entityManager().createQuery("FROM " + Permiso.class.getName()).getResultList();
    }

    @Override
    public Permiso findById(Long id) {
        return entityManager().find(Permiso.class, id);
    }

    @Override
    public void delete(Permiso permiso) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(permiso);
        tx.commit();
    }
    @Override
    public void update(Permiso permiso){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(permiso);
        tx.commit();
    }

    public Permiso obtenerPermisoPorNombreInterno(String nombreInterno) {
        try {
            String hql = "SELECT p FROM Permiso p WHERE p.nombreInterno = :nombreInterno";
            return entityManager().createQuery(hql, Permiso.class)
                    .setParameter("nombreInterno", nombreInterno)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
