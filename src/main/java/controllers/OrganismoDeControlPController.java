package controllers;

import io.javalin.http.Context;
import models.dataBase.repositorios.OrganismoControlRepository;
import models.dataBase.repositorios.PropietarioRepository;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.entidades.OrganismoDeControl;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.*;

public class OrganismoDeControlPController extends Controller implements ICrudViewsHandler {

    private OrganismoControlRepository repository;

    public OrganismoDeControlPController(OrganismoControlRepository repositorio){
        this.repository = repositorio;
    }

    public void indexTest(Context context){
        context.render("organismoDeControlP.hbs");
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_org_de_control");

        Map<String,Object> model = new HashMap<>();

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        model.put("propietario",true);

        if (!propietario.getOrganismosDeControl().isEmpty()) {
            model.put("organismoDeControl", propietario.getOrganismosDeControl());
            context.render("orgDeControl/index-OrgDeControl.hbs", model);
        } else {
            model.put("textoOrgDeControlEmpty", true);
            context.render("texto/texto-transparente-general.hbs", model);
        }
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_org_de_control");

        OrganismoDeControl organismoDeControl = this.repository.findById(Long.parseLong(context.pathParam("id")));
        Map<String,Object> model = new HashMap<>();

        model.put("organismo",organismoDeControl);
        model.put("propietario",true);
        context.render("orgDeControl/show-OrgDeControl.hbs",model);

    }

    // este metod estara restringida, ya que solo peude crear el propietario
    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_org_de_control");

        Map<String,Object> model = new HashMap<>();
        model.put("propietario",true);
        context.render("orgDeControl/create-OrgDeControl.hbs",model);
    }

    // este metod estara restringida, ya que solo peude crear el propietario
    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_org_de_control");

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        OrganismoDeControl organismoDeControl = new OrganismoDeControl();
        asignarParametroCreate(organismoDeControl,context);
        organismoDeControl.setActivo(true);

        propietario.agregarOrganismoDeControl(organismoDeControl);

        new PropietarioRepository().save(propietario);

    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_org_de_control");

        OrganismoDeControl organismoDeControl = this.repository.findById(Long.parseLong(context.pathParam("id")));
        Map<String, Object> model = new HashMap<>();
        model.put("organismoDeControl", organismoDeControl);
        model.put("propietario","true");
        context.render("orgDeControl/edit-OrgDeControl.hbs", model);

    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_org_de_control");

        OrganismoDeControl organismoDeControl = this.repository.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametroEdit(organismoDeControl, context);
        repository.update(organismoDeControl);
        context.redirect("/organismosDeControl");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_org_de_contro");

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        OrganismoDeControl organismoDeControl = repository.findById(Long.parseLong(context.pathParam("id")));

        propietario.getOrganismosDeControl().remove(organismoDeControl);

        organismoDeControl.setActivo(false);
        this.darDeBajaOrganismoDeControl(organismoDeControl);

        this.repository.update(organismoDeControl);
        new PropietarioRepository().update(propietario);

        context.redirect("/organismosDeControl");
    }

    private void darDeBajaOrganismoDeControl(OrganismoDeControl organismoDeControl){
        organismoDeControl.getEntidadesPrestadoras().forEach(eP -> {
            eP.setActivo(false);
            eP.getEntidades().forEach(et -> {
                et.setActivo(false);
                et.getEstablecimientos().forEach(e -> e.setActivo(false));
            });
        });
    }

    private void asignarParametroEdit(OrganismoDeControl organismoDeControl,Context context){
        if(context.formParam("nombre_edit") != null && !context.formParam("nombre_edit").isEmpty()) {
            organismoDeControl.setNombre(context.formParam("nombre_edit"));
        }
    }

    private void asignarParametroCreate(OrganismoDeControl organismoDeControl,Context context){
        if(context.formParam("nombre_edit") != null && !context.formParam("nombre_edit").isEmpty()) {
            organismoDeControl.setNombre(context.formParam("nombre_edit"));
        }
    }
}
