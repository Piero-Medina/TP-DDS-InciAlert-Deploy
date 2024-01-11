package controllers;

import DTOs.EntidadDTO;
import DTOs.LocalizacionDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.dataBase.repositorios.EntidadPrestadoraRepository;
import models.dataBase.repositorios.EntidadRepository;
import models.dataBase.repositorios.OrganismoControlRepository;
import models.dataBase.repositorios.PropietarioRepository;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoPropietario;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntidadPController extends Controller implements ICrudViewsHandler {

    private OrganismoControlRepository repositorioOrganismoDeControl;
    private EntidadPrestadoraRepository repositorioEntidadPrestadora;
    private EntidadRepository repositorioEntidad;


    public EntidadPController(OrganismoControlRepository repositorioOrganismo, EntidadPrestadoraRepository repositorioEntidadPrestadora, EntidadRepository repositorioEntidad){
        this.repositorioOrganismoDeControl = repositorioOrganismo;
        this.repositorioEntidadPrestadora = repositorioEntidadPrestadora;
        this.repositorioEntidad = repositorioEntidad;

    }

    public void indexTest(Context context) {
        Map<String, Object> model = new HashMap<>();
        model.put("propietario","true");
        context.render("entidad/edit-entidad.hbs",model);
        //context.render("editP/edit_entidadP.hbs"); edicion
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_entidades");

        Map<String, Object> model = new HashMap<>();

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        List<EntidadDTO> entidadesDTOS = this.entidadesAEntidadesDTO(entidadPrestadora.getEntidades());

        model.put("entidadPrestadora", entidadPrestadora);
        model.put("entidad", entidadesDTOS);

        model.put("propietario",true);
        context.render("entidad/index-entidad.hbs", model);
    }

    public void indexAll(Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_entidades");

        Map<String, Object> model = new HashMap<>();
        List<Entidad> entidades = this.entidadesActivas(this.repositorioEntidad.findAll());
        List<EntidadDTO> entidadesDTOS = this.entidadesAEntidadesDTO(entidades);

        model.put("entidadesNuevas",true);
        model.put("entidad",entidadesDTOS);
        model.put("ciudadano",true);
        context.render("entidad/index-entidadC.hbs",model);
    }

    private List<Entidad> entidadesActivas(List<Entidad> entidades){
        return entidades.stream().filter(et -> et.isActivo()).collect(Collectors.toList());
    }

    public void showEntidad (Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_entidad");

        Map<String, Object> model = new HashMap<>();
        String idEntidad = context.pathParam("id");

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(idEntidad));
        EntidadDTO entidadDTO = new EntidadDTO();
        entidadDTO.conversionDTO(entidad);

        model.put("entidad",entidadDTO);
        model.put("localizacion", entidadDTO.getLocalizacionDTO());
        model.put("ciudadano",true);
        context.render("entidad/show-entidad.hbs",model);
    }


    public List<EntidadDTO> entidadesAEntidadesDTO(List<Entidad> entidades ){
        return entidades.stream().map(e -> {
            EntidadDTO entidadDTO = new EntidadDTO();
            entidadDTO.conversionDTO(e);
            return entidadDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_entidad");

        Map<String, Object> model = new HashMap<>();
        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("id")));

        model.put("entidadPrestadora", entidadPrestadora);
        model.put("entidad", entidad);

        this.checkLocalizacionDTO(model,entidad);

        model.put("propietario",true);
        context.render("entidad/show-entidad.hbs",model);
    }

    public void checkLocalizacionDTO (Map<String,Object> model, Entidad entidad){
        if(!(entidad.getLocalizacion() == null)) {
            LocalizacionDTO localizacionDTO = new LocalizacionDTO();
            localizacionDTO.conversionDTO(entidad.getLocalizacion());
            model.put("localizacion", localizacionDTO);
        }
    }

    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_entidad");

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        Map<String,Object> model = new HashMap<>();
        model.put("entidadPrestadora",entidadPrestadora);

        model.put("propietario", true);
        context.render("entidad/create-entidad.hbs",model);
    }

    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_entidad");

        Entidad entidad = new Entidad();
        this.asignarParametrosCreate(entidad,context);
        entidad.setActivo(true);

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        entidadPrestadora.agregarEntidad(entidad);
        this.repositorioEntidadPrestadora.update(entidadPrestadora);

        String idEntidadPrestadora = entidadPrestadora.getId().toString();

        context.status(HttpStatus.CREATED);
        context.redirect("/entidadesPrestadoras/"+idEntidadPrestadora+"/entidades");

    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_entidad");

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("id")));

        Map<String,Object> model = new HashMap<>();
        model.put("entidadPrestadora",entidadPrestadora);
        model.put("entidad",entidad);

        this.checkLocalizacionDTO(model, entidad);

        model.put("propietario",true);
        context.render("entidad/edit-entidad.hbs",model);
    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_entidad");

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametrosEdit(entidad, context);
        this.repositorioEntidad.update(entidad);

        EntidadPrestadora entidadPrestadora = this.repositorioEntidadPrestadora.findById(Long.parseLong(context.pathParam("idEP")));
        String idEntidadPrestadora = entidadPrestadora.getId().toString();

        context.redirect("/entidadesPrestadoras/"+idEntidadPrestadora+"/entidades");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_entidad");

        String idEntidadPrestadora = context.pathParam("idEP");
        String idEntidad = context.pathParam("id");

        EntidadPrestadora entidadPrestadora= this.repositorioEntidadPrestadora.findById(Long.parseLong(idEntidadPrestadora));
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(idEntidad));

        entidadPrestadora.getEntidades().remove(entidad);

        entidad.setActivo(false);
        this.darDeBajaEntidad(entidad);

        this.repositorioEntidad.update(entidad);
        this.repositorioEntidadPrestadora.update(entidadPrestadora);

        context.redirect("/entidadesPrestadoras/"+idEntidadPrestadora+"/entidades");
    }

    private void darDeBajaEntidad(Entidad entidad){
        entidad.getEstablecimientos().forEach(e -> e.setActivo(false));
    }

    private void asignarParametrosCreate(Entidad entidad, Context context){
        entidad.setNombre(context.formParam("nombre"));
    }

    private void asignarParametrosEdit(Entidad entidad, Context context){
        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            entidad.setNombre(context.formParam("nombre"));
        }
    }
}
