package models.dataBase.repositorios;

import models.dominio.actores.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class UsuarioRepository implements Repository<Usuario>, WithSimplePersistenceUnit {


    public UsuarioRepository() {
    }

    @Override
    public List<Usuario> findAll() {
        return entityManager().createQuery("FROM " + Usuario.class.getName()).getResultList();
    }

    @Override
    public Usuario findById(Long id) {
        return entityManager().find(Usuario.class, id);
    }

    @Override
    public void save(Usuario usuario) {
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();
        if (usuario.getId() == null)
            entityManager().persist(usuario);
        else
            entityManager().merge(usuario);

        tx.commit();
    }

    @Override
    public void update(Usuario usuario){
        EntityTransaction tx = entityManager().getTransaction();

        if(!tx.isActive())
            tx.begin();

        entityManager().merge(usuario);
        tx.commit();
    }

    @Override
    public void delete(Usuario usuario) {
        EntityTransaction tx = entityManager().getTransaction();
        if(!tx.isActive())
            tx.begin();

        entityManager().remove(usuario);
        tx.commit();

    }


    public Usuario findByNombre(String nombre) {
        try {
            return entityManager().createQuery("SELECT u FROM Usuario u WHERE u.nombre = :nombre", Usuario.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
