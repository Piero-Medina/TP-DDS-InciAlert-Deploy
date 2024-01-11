package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.entidades.EntidadPrestadora;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class EntidadPrestadoraRepository implements Repository<EntidadPrestadora>, WithSimplePersistenceUnit {

    public EntidadPrestadoraRepository() {

    }

    @Override
    public List<EntidadPrestadora> findAll() {
        return entityManager().createQuery("FROM " + EntidadPrestadora.class.getName()).getResultList();
    }

    @Override
    public EntidadPrestadora findById(Long id) {
        return entityManager().find(EntidadPrestadora.class, id);
    }

    @Override
    public void save(EntidadPrestadora entidadPrestadora) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (entidadPrestadora.getId() == null)
            entityManager().persist(entidadPrestadora);
        else
            entityManager().merge(entidadPrestadora);

        tx.commit();
    }

    @Override
    public void update(EntidadPrestadora entidadPrestadora){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(entidadPrestadora);
        tx.commit();
    }

    @Override
    public void delete(EntidadPrestadora entidadPrestadora) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(entidadPrestadora);
        tx.commit();
    }

}
