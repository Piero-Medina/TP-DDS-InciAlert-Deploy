package models.dataBase.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dominio.servicios.PrestacionDeServicio;
import models.dominio.servicios.Servicio;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PrestacionRepository implements Repository<PrestacionDeServicio>, WithSimplePersistenceUnit {


    public PrestacionRepository() {

    }

    @Override
    public List<PrestacionDeServicio> findAll() {
        return entityManager().createQuery("FROM " + PrestacionDeServicio.class.getName()).getResultList();
    }

    @Override
    public PrestacionDeServicio findById(Long id) {
        return entityManager().find(PrestacionDeServicio.class,id);
    }

    @Override
    public void save(PrestacionDeServicio prestacionDeServicio) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (prestacionDeServicio.getId() == null)
            entityManager().persist(prestacionDeServicio);
        else
            entityManager().merge(prestacionDeServicio);

        tx.commit();
    }

    @Override
    public void update(PrestacionDeServicio prestacionDeServicio){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(prestacionDeServicio);
        tx.commit();
    }

    @Override
    public void delete(PrestacionDeServicio prestacionDeServicio) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(prestacionDeServicio);
        tx.commit();
    }

    public PrestacionDeServicio findByNombre(String servicio) {
         return entityManager().createQuery("SELECT p FROM PrestacionDeServicio p WHERE p.nombreServicioPrestado = :nombre", PrestacionDeServicio.class).setParameter("nombre", servicio).getSingleResult();
    }
}
