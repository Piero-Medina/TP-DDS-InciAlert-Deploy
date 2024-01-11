package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.dataBase.repositorios.EntidadPrestadoraRepository;
import models.dataBase.repositorios.OrganismoControlRepository;
import models.dataBase.repositorios.PropietarioRepository;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoPropietario;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.OrganismoDeControl;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadPrestadoraPController extends Controller implements ICrudViewsHandler {

    private OrganismoControlRepository repositorioOrganismo;
    private EntidadPrestadoraRepository repositorioEntidadPrestadora;

    public EntidadPrestadoraPController(OrganismoControlRepository repositorioOrganismo, EntidadPrestadoraRepository repositorioEntidadPrestadora){
        this.repositorioOrganismo = repositorioOrganismo;
        this.repositorioEntidadPrestadora = repositorioEntidadPrestadora;
    }

    public void indexTest(Context context) {
        Map<String,Object> model = new HashMap<>();
        model.put("propietario", "true");
        context.render("/entidadPrestadora/edit-entidadPrestadora.hbs",model);
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_entidades_prestadoras");

        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        model.put("propietario",true);

        if (propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL)) {
            OrganismoDeControl organismo = propietario.getOrganismosDeControl().get(0);
            model.put("puedeAgregarMasEntidades",true);
            model.put("entidadPrestadora", organismo.getEntidadesPrestadoras());
            context.render("/entidadPrestadora/index-entidadPrestadora.hbs", model);

        } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA)) {

            if (!propietario.getEntidadesPrestadoras().isEmpty()) {
                EntidadPrestadora entidadPrestadora = propietario.getEntidadesPrestadoras().get(0);
                model.put("entidadPrestadora", entidadPrestadora);
                context.render("/entidadPrestadora/index-entidadPrestadora.hbs", model);
            } else {
                model.put("textoEntidadPrestadoraEmpty",true);
                context.render("/texto/texto-transparente-general.hbs", model);
            }
        }
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_entidad_prestadora");

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("id")));
        Map<String,Object> model = new HashMap<>();

        model.put("entidadPrestadora",entidadPrestadora);
        model.put("propietario",true);
        context.render("/entidadPrestadora/show-entidadPrestadora.hbs",model);
    }

    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_entidad_prestadora");

        Map<String,Object> model = new HashMap<>();
        model.put("propietario","true");
        context.render("/entidadPrestadora/create-entidadPrestadora.hbs",model);
    }

    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_entidad_prestadora");

        EntidadPrestadora entidadPrestadora = new EntidadPrestadora();
        Propietario propietario = super.PropietarioLogueadoSuper(context);
        this.asignarParametrosCreate(entidadPrestadora, context);
        entidadPrestadora.setActivo(true);

        if(propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL)){

            OrganismoDeControl organismo = propietario.getOrganismosDeControl().get(0);
            organismo.agregarEntidadPrestadora(entidadPrestadora);
            this.repositorioOrganismo.update(organismo);

        } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA)) {

            propietario.agregarEntidadPrestadora(entidadPrestadora);
            PropietarioRepository repositoryPropietario = new PropietarioRepository();
            repositoryPropietario.save(propietario);
        }

        context.status(HttpStatus.CREATED);
        context.redirect("/entidadesPrestadoras");
    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_entidad_prestadora");

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("id")));
        Map<String,Object> model = new HashMap<>();

        model.put("entidadPrestadora",entidadPrestadora);
        model.put("propietario", true);
        context.render("/entidadPrestadora/edit-entidadPrestadora.hbs",model);

    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_entidad_prestadora");

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametrosEdit(entidadPrestadora,context);
        this.repositorioEntidadPrestadora.update(entidadPrestadora);

        context.redirect("/entidadesPrestadoras");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_entidad_prestadora");

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        EntidadPrestadora entidadPrestadora= this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("id")));

        if(propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL)){
            propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras().remove(entidadPrestadora);

        } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA)){
            propietario.getEntidadesPrestadoras().remove(entidadPrestadora);
        }

        entidadPrestadora.setActivo(false);
        this.darDeBajaEntidadPrestadora(entidadPrestadora);

        this.repositorioEntidadPrestadora.update(entidadPrestadora);
        new PropietarioRepository().update(propietario);

        context.redirect("/entidadesPrestadoras");
    }

    private void darDeBajaEntidadPrestadora(EntidadPrestadora entidadPrestadora){
        entidadPrestadora.getEntidades().forEach(et -> {
            et.setActivo(false);
            et.getEstablecimientos().forEach(e -> e.setActivo(false));
        });
    }

    private void asignarParametrosEdit(EntidadPrestadora entidadPrestadora, Context context){
        if(context.formParam("nombre_edit") != null && !context.formParam("nombre_edit").isEmpty()) {
            entidadPrestadora.setNombre(context.formParam("nombre_edit"));
        }
    }

    private void asignarParametrosCreate(EntidadPrestadora entidadPrestadora, Context context){
            entidadPrestadora.setNombre(context.formParam("nombre"));
    }
}
