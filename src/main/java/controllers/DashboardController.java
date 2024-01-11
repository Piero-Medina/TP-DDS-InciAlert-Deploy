package controllers;

import io.javalin.http.Context;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;

import java.util.HashMap;
import java.util.Map;


public class DashboardController extends Controller{

    public DashboardController() {
    }

    public void index(Context context) {
        Usuario usuario = super.usuarioLogueado(context);
        Map<String, Object> model = new HashMap<>();
        TipoRol tipoRol = usuario.getRol().getTipo();


        if(tipoRol.equals(TipoRol.PROPIETARIO)){
            model.put("propietario", true);
            model.put("bienvenidaPropietario", true);
        } else if (tipoRol.equals(TipoRol.CIUDADANO)) {
            model.put("ciudadano", true);
            model.put("bienvenidaCiudadano", true);
        } else if (tipoRol.equals(TipoRol.ADMINISTRADOR)) {
            model.put("administrador", true);
        }

        context.render("dashboard.hbs",model);
    }

}
