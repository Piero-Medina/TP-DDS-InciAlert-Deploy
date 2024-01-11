package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.entidades.OrganismoDeControl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OrganismoControlRepository implements Repository<OrganismoDeControl>, WithSimplePersistenceUnit {

    public OrganismoControlRepository() {

    }

    @Override
    public List<OrganismoDeControl> findAll() {
        return entityManager().createQuery("FROM " + OrganismoDeControl.class.getName()).getResultList();
    }

    @Override
    public OrganismoDeControl findById(Long id) {
        return entityManager().find(OrganismoDeControl.class, id);
    }

    @Override
    public void save(OrganismoDeControl organismoDeControl) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (organismoDeControl.getId() == null)
            entityManager().persist(organismoDeControl);
        else
            entityManager().merge(organismoDeControl);

        tx.commit();
    }

    @Override
    public void update(OrganismoDeControl organismoDeControl){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(organismoDeControl);
        tx.commit();
    }

    @Override
    public void delete(OrganismoDeControl organismoDeControl) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(organismoDeControl);
        tx.commit();
    }
}
