package controllers;

import DTOs.ComunidadDTO;
import io.javalin.http.Context;
import models.dataBase.repositorios.CiudadanoRepository;
import models.dataBase.repositorios.ComunidadRepository;
import models.dominio.actores.Ciudadano;
import models.dominio.comunidad.Comunidad;
import models.dominio.servicios.Incidente;
import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComunidadController extends Controller implements ICrudViewsHandler {

    private ComunidadRepository repositorioComunidad;

    public ComunidadController (ComunidadRepository repositorioComunidad){
        this.repositorioComunidad = repositorioComunidad;
    }

    public void indexA(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_comunidades");

        Map<String,Object> model = new HashMap<>();

        List<Comunidad> comunidades = this.comunidadesActivas(this.repositorioComunidad.findAll());

        if(!comunidades.isEmpty()){
            model.put("lleno",true);
        }

        model.put("comunidades" ,comunidades);
        model.put("administrador",true);
        context.render("comunidad/index-comunidadA.hbs", model);
    }


    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_comunidades");

        Map<String,Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        // hidratamos y hacemos la conversion a DTO, de comunidades en la cual el ciudadano no pertenece
        List<ComunidadDTO> comunidadesNuevas = this.comunidadesNuevasDTO(ciudadano);

        model.put("comunidadesNuevas",true);
        model.put("comunidad" ,comunidadesNuevas);
        model.put("ciudadano",true);
        context.render("comunidad/index-comunidad.hbs", model);

    }

     // comunidades en las cuales el ciudadano no pertenece
    public List<ComunidadDTO> comunidadesNuevasDTO(Ciudadano ciudadano){
        List<Comunidad> comunidades = this.comunidadesActivas(this.repositorioComunidad.findAll());
        List<ComunidadDTO> comunidadDTOS = this.ComunidadesAComunidadesDTO(comunidades);
        return this.comunidadesNuevas(ciudadano, comunidadDTOS);
    }

    public List<ComunidadDTO> ComunidadesAComunidadesDTO(List<Comunidad> comunidads){
        return comunidads.stream().map(c -> {
            ComunidadDTO comunidadDTO = new ComunidadDTO();
            comunidadDTO.convertirADTO(c);
            return comunidadDTO;
        }).collect(Collectors.toList());
    }

    public List<ComunidadDTO> comunidadesNuevas(Ciudadano ciudadano, List<ComunidadDTO> comunidadDTOS){
        return comunidadDTOS.stream()
                .filter(c -> !c.getMiembros().contains(ciudadano))
                .collect(Collectors.toList());
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context,"ver_detalle_comunidad");

        Map<String,Object> model = new HashMap<>();

        String idEntidad = context.pathParam("id");
        Comunidad comunidad = this.repositorioComunidad.findById(Long.parseLong(idEntidad));
        ComunidadDTO comunidadDTO = new ComunidadDTO();
        comunidadDTO.convertirADTO(comunidad);

        model.put("comunidad",comunidadDTO);

        // para que no pueda ver los incidente si no pertenece a la comunidad y solo le hecha un vistazo
        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        if(comunidad.getMiembros().contains(ciudadano)) {
            model.put("ciudadanoPerteneceAComunidad",true);
            model.put("miembro", comunidadDTO.getMiembros());

            //Aunque el incidente no este activo, se debe poder visualizar en las comunidades que se añadio cuando fue reportado
            // ya que deja de estar activo solo cuando el usuario que lo reporto lo elimina de su lista.
            //model.put("incidentes", this.incidentesActivos(comunidad.getIncidentesOcurridos()));
            model.put("incidentes", comunidad.getIncidentesOcurridos());
        }

        model.put("ciudadano",true);
        context.render("comunidad/show-comunidad.hbs", model);
    }

    public void unirse (Context context){
        super.usuarioLogueadoTienePermisos(context,"unirse_a_comunidad");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        String idEntidad = context.pathParam("id");
        Comunidad comunidad = this.repositorioComunidad.findById(Long.parseLong(idEntidad));

        // por dentro tambien agregamos el miembro a la comunidad
        ciudadano.agregarComunidad(comunidad);

        // hacemos los update "ya que no pusimos el cascade, por las dudas para evitar bucles infinitos"
        new CiudadanoRepository().update(ciudadano);
        this.repositorioComunidad.update(comunidad);

        context.redirect("/misComunidades");

    }

    public void indexPropias(Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_comunidades");

        Map<String,Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        if(ciudadano.getComunidades().isEmpty()){
            model.put("ciudadano", true);
            context.render("comunidad/index-misComunidades.hbs", model);
        }
        else {
            List<Comunidad> comnuidadesActivas = this.comunidadesActivas(ciudadano.getComunidades());
            List<ComunidadDTO> comunidadesDTO = this.ComunidadesAComunidadesDTO(comnuidadesActivas);
            model.put("misComunidades", true);
            model.put("comunidad", comunidadesDTO);

            model.put("ciudadano", true);
            context.render("comunidad/index-misComunidades.hbs", model);
        }
    }

    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_comunidad");

        Map<String,Object> model = new HashMap<>();

        model.put("administrador",true);
        context.render("comunidad/create-comunidad.hbs",model);
    }

    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_comunidad");

        Comunidad comunidad = new Comunidad();
        this.asignarParametros(comunidad, context);
        comunidad.setActivo(true);
        repositorioComunidad.save(comunidad);
        context.redirect("/comunidadesA");

    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_comunidad");

        Map<String,Object> model = new HashMap<>();
        Comunidad comunidadAEditar = this.repositorioComunidad.findById(Long.parseLong(context.pathParam("id")));
        model.put("comunidad",comunidadAEditar);

        model.put("administrador",true);
        context.render("comunidad/edit-comunidad.hbs",model);
    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_comunidad");

        Comunidad comunidadAEditar = this.repositorioComunidad.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametros(comunidadAEditar, context);
        this.repositorioComunidad.update(comunidadAEditar);

        context.redirect("/comunidadesA");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_comunidad");

        Comunidad comunidad = this.repositorioComunidad.findById(Long.parseLong(context.pathParam("id")));
        comunidad.setActivo(false);
        this.repositorioComunidad.update(comunidad);

        context.redirect("/comunidadesA");
    }

    public void abandonar(Context context) {
        super.usuarioLogueadoTienePermisos(context, "abandonar_comunidad");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        String idCouminidad = context.pathParam("id");
        Comunidad comunidad = this.repositorioComunidad.findById(Long.parseLong(idCouminidad));

        ciudadano.getComunidades().remove(comunidad);
        comunidad.getMiembros().remove(ciudadano);

        this.repositorioComunidad.update(comunidad);
        new CiudadanoRepository().update(ciudadano);

        context.redirect("/comunidades");
    }


    public List<Comunidad> comunidadesActivas (List<Comunidad> comunidades){
        return comunidades.stream().filter(c -> c.isActivo()).collect(Collectors.toList());
    }

    public List<Incidente> incidentesActivos(List<Incidente> incidentes){
        return incidentes.stream().filter(i -> i.isActivo()).collect(Collectors.toList());
    }

    private void asignarParametros(Comunidad comunidad, Context context){
        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            comunidad.setNombre(context.formParam("nombre"));
        }
        if(context.formParam("descripcion") != null && !context.formParam("descripcion").isEmpty()) {
            comunidad.setDescripcion(context.formParam("descripcion"));
        }
    }

}
