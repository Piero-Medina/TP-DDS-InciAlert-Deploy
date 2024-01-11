package controllers;

import DTOs.EntidadDTO;
import DTOs.EstablecimientoDTO;
import DTOs.LocalizacionDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.dataBase.repositorios.EntidadPrestadoraRepository;
import models.dataBase.repositorios.EntidadRepository;
import models.dataBase.repositorios.EstablecimientoRepository;
import models.dataBase.repositorios.OrganismoControlRepository;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.Establecimiento;
import models.dominio.entidades.OrganismoDeControl;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstablecimientoPController extends Controller implements ICrudViewsHandler {

    private OrganismoControlRepository repositorioOrganismoDeControl;
    private EntidadPrestadoraRepository repositorioEntidadPrestadora;
    private EntidadRepository repositorioEntidad;
    private EstablecimientoRepository repositorioEstablecimiento;

    public EstablecimientoPController(EntidadRepository repositorioEntidad,OrganismoControlRepository repositorioOrganismo, EntidadPrestadoraRepository repositorioEntidadPrestadora, EstablecimientoRepository repositorioEstablecimiento){
        this.repositorioOrganismoDeControl = repositorioOrganismo;
        this.repositorioEntidadPrestadora = repositorioEntidadPrestadora;
        this.repositorioEntidad = repositorioEntidad;
        this.repositorioEstablecimiento = repositorioEstablecimiento;
    }

    public void indexTest(Context context) {
        context.render("establecimientosP.hbs");
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_establecimientos");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String, Object> model = new HashMap<>();
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));

        if (usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            model.put("entidad", entidad);
            List<EstablecimientoDTO> establecimientoDTOS = this.establecimientosAEstablecimientoDTOs(entidad.getEstablecimientos());
            model.put("establecimiento", establecimientoDTOS);
            model.put("propietario", true);

            context.render("/establecimiento/index-establecimiento.hbs", model);
        }
        if(usuario.getRol().getTipo().equals(TipoRol.CIUDADANO)){
            List<EstablecimientoDTO> establecimientoDTOS = this.establecimientosAEstablecimientoDTOs(entidad.getEstablecimientos());
            model.put("entidad", entidad);
            model.put("establecimientosDeEntidad","true");
            model.put("establecimiento",establecimientoDTOS);
            model.put("ciudadano",true);

            context.render("/establecimiento/index-establecimientoC.hbs",model);

        }
    }


    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_establecimiento");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String, Object> model = new HashMap<>();
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));
        Establecimiento establecimiento = this.repositorioEstablecimiento.findById(Long.parseLong(context.pathParam("id")));

        EstablecimientoDTO establecimientoDTO = new EstablecimientoDTO();
        establecimientoDTO.conversionDTO(establecimiento);

        if (usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)) {
            model.put("entidad",entidad);
            model.put("establecimiento",establecimientoDTO);
            model.put("localizacion",establecimientoDTO.getLocalizacionDTO());
            model.put("propietario", "true");
            context.render("/establecimiento/show-establecimiento.hbs", model);
        }
        if(usuario.getRol().getTipo().equals(TipoRol.CIUDADANO)){
            model.put("entidad",entidad);
            model.put("establecimiento",establecimientoDTO);
            model.put("localizacion",establecimientoDTO.getLocalizacionDTO());
            model.put("ciudadano", "true");
            context.render("/establecimiento/show-establecimiento.hbs", model);
        }
    }

    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_establecimiento");

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));
        Map<String,Object> model = new HashMap<>();
        model.put("entidad",entidad);

        model.put("propietario", true);
        context.render("/establecimiento/create-establecimiento.hbs",model);
    }

    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_establecimiento");

        Establecimiento establecimiento = new Establecimiento();
        this.asignarParametrosCreate(establecimiento, context);
        establecimiento.setActivo(true);

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));
        entidad.agregarEstablecimiento(establecimiento);
        this.repositorioEntidad.update(entidad);

        String idEntidad = entidad.getId().toString();
        context.status(HttpStatus.CREATED);
        context.redirect("/entidades/"+idEntidad+"/establecimientos");
    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_establecimiento");

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));
        Establecimiento establecimiento = this.repositorioEstablecimiento.findById(Long.parseLong(context.pathParam("id")));

        Map<String,Object> model = new HashMap<>();
        model.put("entidad",entidad);
        model.put("establecimiento", establecimiento);

        this.checkLocalizacionDTO(model, establecimiento);

        model.put("propietario",true);
        context.render("/establecimiento/edit-establecimiento.hbs",model);
    }

    public void checkLocalizacionDTO (Map<String,Object> model, Establecimiento establecimiento){
        if(!(establecimiento.getLocalizacion() == null)) {
            LocalizacionDTO localizacionDTO = new LocalizacionDTO();
            localizacionDTO.conversionDTO(establecimiento.getLocalizacion());
            model.put("localizacion", localizacionDTO);
        }
    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_establecimiento");

        Establecimiento establecimiento = this.repositorioEstablecimiento.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametrosEdit(establecimiento, context);
        this.repositorioEstablecimiento.update(establecimiento);

        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(context.pathParam("idE")));
        String idEntidad = entidad.getId().toString();

        context.redirect("/entidades/"+idEntidad+"/establecimientos");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_establecimiento");

        String idEntidad = context.pathParam("idE");
        String idEstablecimiento = context.pathParam("id");

        Establecimiento establecimiento = this.repositorioEstablecimiento.findById(Long.parseLong(idEstablecimiento));
        Entidad entidad = this.repositorioEntidad.findById(Long.parseLong(idEntidad));

        entidad.getEstablecimientos().remove(establecimiento);

        establecimiento.setActivo(false);

        this.repositorioEstablecimiento.update(establecimiento);
        this.repositorioEntidad.update(entidad);

        context.redirect("entidades/"+idEntidad+"/establecimientos");
    }

    public List<EstablecimientoDTO> establecimientosAEstablecimientoDTOs(List<Establecimiento> establecimientos ){
        return establecimientos.stream().map(e -> {
            EstablecimientoDTO establecimientoDTO = new EstablecimientoDTO();
            establecimientoDTO.conversionDTO(e);
            return establecimientoDTO;
        }).collect(Collectors.toList());
    }

    private void asignarParametrosCreate(Establecimiento establecimiento, Context context){
        establecimiento.setNombre(context.formParam("nombre"));
        establecimiento.setDescripcion(context.formParam("descripcion"));
    }

    private void asignarParametrosEdit(Establecimiento establecimiento, Context context){
        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            establecimiento.setNombre(context.formParam("nombre"));
        }
        if(context.formParam("descripcion") != null && !context.formParam("descripcion").isEmpty()) {
            establecimiento.setDescripcion(context.formParam("descripcion"));
        }
    }
}
