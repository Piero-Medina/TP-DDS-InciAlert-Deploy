package controllers;

import models.dominio.actores.Ciudadano;
import models.dominio.actores.Propietario;
import models.dominio.actores.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import server.exception.AccessDeniedException;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public abstract class Controller implements WithSimplePersistenceUnit {

    protected Usuario usuarioLogueado(Context ctx) {
        return entityManager().find(Usuario.class, Long.parseLong(("" + ctx.sessionAttribute("id_usuario"))));
    }

    protected Ciudadano CiudadanoLogueado(Context ctx){
        try {
            String hql = "SELECT c FROM Ciudadano c JOIN c.usuario u WHERE u.id = :usuarioId";
            return entityManager().createQuery(hql, Ciudadano.class)
                    .setParameter("usuarioId",  Long.parseLong(("" + ctx.sessionAttribute("id_usuario"))))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected Propietario PropietarioLogueadoSuper(Context ctx){
        try {
            String hql = "SELECT c FROM Propietario c JOIN c.usuario u WHERE u.id = :usuarioId";
            return entityManager().createQuery(hql, Propietario.class)
                    .setParameter("usuarioId",  Long.parseLong(("" + ctx.sessionAttribute("id_usuario"))))
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected void usuarioLogueadoTienePermisos(Context context, String... permisos) {
        Usuario usuarioLogueado = this.usuarioLogueado(context);

        // antes nos fijamos si el usuario en null, ya que si lo es nos tirara un error al trataer de usar un metodo de el
        if (usuarioLogueado == null) {
            throw new AccessDeniedException();
        }

        boolean tienePermiso = Arrays.stream(permisos).allMatch(p -> usuarioLogueado.getRol().tenesPermiso(p));

        if (!tienePermiso) {
            throw new AccessDeniedException();
        }
    }

}

