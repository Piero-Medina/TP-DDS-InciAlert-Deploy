package controllers;

import io.javalin.http.Context;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoPropietario;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.OrganismoDeControl;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubidosPController extends Controller {

    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_org_de_control", "ver_lista_entidades_prestadoras");

        Propietario propietario = super.PropietarioLogueadoSuper(context);

        if(propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL)){
            context.redirect("/organismosDeControl");
        } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA)) {
            context.redirect("/entidadesPrestadoras");
        } else if(propietario.getTipoPropietario().equals(TipoPropietario.SIN_TIPO)) {
            Map<String,Object> model = new HashMap<>();
            model.put("propietario","true");
            context.render("texto/texto-tranparente-sinTipoDePropietario.hbs",model);
        }

    }
}
