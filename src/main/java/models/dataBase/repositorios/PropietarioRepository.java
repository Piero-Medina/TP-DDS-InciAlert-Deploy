package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.actores.Propietario;

import javax.persistence.EntityTransaction;
import java.util.List;

public class PropietarioRepository implements Repository<Propietario>, WithSimplePersistenceUnit {
    @Override
    public List<Propietario> findAll() {
        return entityManager().createQuery("FROM " + Propietario.class.getName()).getResultList();
    }

    @Override
    public Propietario findById(Long id) {
        return entityManager().find(Propietario.class, id);
    }

    @Override
    public void save(Propietario propietario) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (propietario.getId() == null)
            entityManager().persist(propietario);
        else
            entityManager().merge(propietario);

        tx.commit();
    }

    @Override
    public void update(Propietario propietario) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(propietario);
        tx.commit();
    }

    @Override
    public void delete(Propietario propietario) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(propietario);
        tx.commit();
    }

    public Propietario findByUsuarioID(Long usuario){
        return entityManager().createQuery("SELECT c From Ciudadano c where c.usuario= :usuario", Propietario.class).setParameter("usuario", usuario).getSingleResult();
    }
}
