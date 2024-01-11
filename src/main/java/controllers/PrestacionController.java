package controllers;

import DTOs.IncidenteDTO;
import DTOs.PrestacionConIncidenteDTO;
import io.javalin.http.Context;
import models.dataBase.repositorios.CiudadanoRepository;
import models.dataBase.repositorios.EstablecimientoRepository;
import models.dataBase.repositorios.PrestacionRepository;
import models.dataBase.repositorios.ServicioRepository;
import models.dominio.actores.Ciudadano;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.entidades.Establecimiento;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.PrestacionDeServicio;
import models.dominio.servicios.Servicio;
import server.Server;
import server.utils.ICrudViewsHandler;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrestacionController extends Controller implements ICrudViewsHandler {

    PrestacionRepository repositoryPrestacionDeServicio;

    public PrestacionController (PrestacionRepository prestacionRepository){
        this.repositoryPrestacionDeServicio = prestacionRepository;
    }


    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_prestaciones");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String, Object> model = new HashMap<>();

        String idEstablecimiento = context.pathParam("idE");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        List<PrestacionDeServicio> prestacionDeServicios = this.prestacionesDeServicioActivas(establecimiento.getServiciosPrestados());

        if (!prestacionDeServicios.isEmpty()) {
            model.put("lleno", "true");
        }

        // para relacionar las Prestacion de Servicio con los incidentes Abiertos
        List<PrestacionConIncidenteDTO> prestacionesDTO = this.prestacionesConIncidenteDTO(prestacionDeServicios);

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)) {
            model.put("prestado", prestacionesDTO);
            model.put("establecimiento",establecimiento);
            model.put("propietario","true");
            context.render("prestacionDeServicio/index-prestacionDeServicio.hbs",model);

        } else if (usuario.getRol().getTipo().equals(TipoRol.CIUDADANO)) {
            model.put("prestado", prestacionesDTO);
            model.put("establecimiento",establecimiento);
            model.put("ciudadano","true");
            context.render("prestacionDeServicio/index-prestacionDeServicio.hbs",model);
        }

    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context,"ver_detalle_prestacion");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String,Object> model = new HashMap<>();

        String idEstablecimiento = context.pathParam("idE");
        String idPrestacionDeServicioAVer = context.pathParam("id");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionDeServicioAVer));

        model.put("establecimiento",establecimiento);
        model.put("prestacion",prestacionDeServicio);

        if (usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            model.put("propietario",true);
        }else if (usuario.getRol().getTipo().equals(TipoRol.CIUDADANO)){
            model.put("ciudadano",true);
        }

        context.render("prestacionDeServicio/show-prestacionDeServicio.hbs",model);

    }

    // SOLO ENTRARA EL PROPIEATRIO-por las dudas igual metemos el IF
    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_prestacion");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String,Object> model = new HashMap<>();

        String idEstablecimiento = context.pathParam("idE");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        List<Servicio> servicios = new ServicioRepository().findAll();

        model.put("establecimiento",establecimiento);
        model.put("servicios",servicios);
        model.put("propietario","true");

        context.render("prestacionDeServicio/create-prestacionDeServicio.hbs",model);
    }

    @Override
    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "guardar_prestacion");

        EstablecimientoRepository establecimientoRepository = new EstablecimientoRepository();

        String idEstablecimiento = context.pathParam("idE");
        Establecimiento establecimiento = establecimientoRepository.findById(Long.parseLong(idEstablecimiento));

        PrestacionDeServicio prestacionDeServicio = new PrestacionDeServicio();
        this.asignarParametros(prestacionDeServicio, context);
        prestacionDeServicio.setActivo(true);
        // se encarga de guardar el estableciento en ambos lugares ya que es ua relacion bidireccional
        establecimiento.agregarServiciosPrestados(prestacionDeServicio);
        // cascada activada, por lo que tambien se peresistira la prestacion de servicio
        establecimientoRepository.update(establecimiento);

        context.redirect("/establecimientos/"+establecimiento.getId()+"/prestaciones");
    }

    // SOLO ENTRARA EL PROPIEATRIO-por las dudas igual metemos el IF
    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context,"editar_prestacion");

        Usuario usuario = super.usuarioLogueado(context);
        Map<String,Object> model = new HashMap<>();

        String idEstablecimiento = context.pathParam("idE");
        String idPrestacionDeServicioAEditar = context.pathParam("id");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        List<Servicio> servicios = this.serviciosActivos(new ServicioRepository().findAll());
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionDeServicioAEditar));

        model.put("establecimiento",establecimiento);
        model.put("prestacion",prestacionDeServicio);
        model.put("servicios",servicios);
        model.put("propietario","true");

        context.render("prestacionDeServicio/edit-prestacionDeServicio.hbs",model);


    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_prestacion");

        String idEstablecimiento = context.pathParam("idE");
        String idPrestacionAEditar = context.pathParam("id");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionAEditar));

        this.asignarParametros(prestacionDeServicio, context);

        this.repositoryPrestacionDeServicio.update(prestacionDeServicio);

        context.redirect("/establecimientos/"+establecimiento.getId()+"/prestaciones");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_prestacion");

        String idEstablecimiento = context.pathParam("idE");
        String idPrestacion = context.pathParam("id");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacion));

        establecimiento.getServiciosPrestados().remove(prestacionDeServicio);
        prestacionDeServicio.setActivo(false);

        this.repositoryPrestacionDeServicio.update(prestacionDeServicio);
        new EstablecimientoRepository().update(establecimiento);

        context.redirect("establecimientos/"+idEstablecimiento+"/prestaciones");
    }

    public void cambiarEstado(Context context){
        super.usuarioLogueadoTienePermisos(context, "cambiar_estado_prestacion");

        String idEstablecimiento = context.pathParam("idE");
        String idPrestacionDeServicioAEditar = context.pathParam("id");

        Establecimiento establecimiento = new EstablecimientoRepository().findById(Long.parseLong(idEstablecimiento));
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionDeServicioAEditar));

        prestacionDeServicio.cambiarDeEstado();

        this.repositoryPrestacionDeServicio.update(prestacionDeServicio);

        context.redirect("/establecimientos/"+idEstablecimiento+"/prestaciones");
    }

    public void indexPropias (Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_comunidades");

        Map<String,Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        List<PrestacionDeServicio> prestacionDeServicios = this.prestacionesDeServicioActivas(ciudadano.getInteres());

        List<PrestacionConIncidenteDTO> prestacionConIncidenteDTOS = this.prestacionesConIncidenteDTO(prestacionDeServicios);

        if (!prestacionDeServicios.isEmpty()) {
            model.put("lleno", "true");
            model.put("prestado",prestacionConIncidenteDTOS);
        }
        // para ver los detalles de una prestacionDeServicio en Ciudadano y no cambiar de ruta se agrego
        // un metodo que me devuelve el ID del establecimiento al cual pertenece la PrestacionDeServicio
        model.put("ciudadano",true);
        context.render("prestacionDeServicio/index-prestacionDeInteresC.hbs",model);
    }

    public void agregarInteres(Context context){
        super.usuarioLogueadoTienePermisos(context,"agregar_intereses_prestacion");

        String idPrestacionDeServicioAAgregar = context.pathParam("id");
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionDeServicioAAgregar));

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        Map<String,Object> model = new HashMap<>();

        if(ciudadano.getMedioDeNotificacion() == null){
            model.put("sinMedioDeNotificacionParaServicioDeInteres", true);
            model.put("ciudadano",true);
            context.render("texto/texto-transparente-general.hbs",model);
        }
        else if(!ciudadano.getInteres().contains(prestacionDeServicio)){
            ciudadano.agregarAServiciosDeInteres(prestacionDeServicio);

            new CiudadanoRepository().update(ciudadano);

            context.redirect("/misPrestaciones");
        }else {
            model.put("existePrestacionDeServicio",true);
            model.put("ciudadano",true);
            context.render("texto/texto-transparente-general.hbs",model);
        }
    }

    public void removerInteres (Context context){
        super.usuarioLogueadoTienePermisos(context, "remover_intereses_prestacion");

        String idPrestacionDeServicioAAgregar = context.pathParam("id");
        PrestacionDeServicio prestacionDeServicio = this.repositoryPrestacionDeServicio.findById(Long.parseLong(idPrestacionDeServicioAAgregar));

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        ciudadano.getInteres().remove(prestacionDeServicio);

        new CiudadanoRepository().update(ciudadano);

        context.redirect("/misPrestaciones");
    }

    public void asignarParametros(PrestacionDeServicio prestacionDeServicio, Context context) {
        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            prestacionDeServicio.setNombreServicioPrestado(context.formParam("nombre"));
        }
        if(context.formParam("descripcion") != null && !context.formParam("descripcion").isEmpty()) {
            prestacionDeServicio.setDescripcion(context.formParam("descripcion"));
        }
        if(context.formParam("servicioBrindado") != null && !context.formParam("servicioBrindado").isEmpty()) {
            Servicio servicio = new ServicioRepository().findById(Long.parseLong(context.formParam("servicioBrindado")));
            prestacionDeServicio.setServicioPrestado(servicio);
        }
        if(context.formParam("estado") != null && !context.formParam("estado").isEmpty()) {
            String estado = context.formParam("estado");
            if (estado.equals("activo")){
                prestacionDeServicio.setEstadoDeServicioPrestado(true);
            }else {
                prestacionDeServicio.setEstadoDeServicioPrestado(false);
            }
        }

    }

    public Incidente obtenerIncidenteActualPorIdPrestacion(Long idPrestacion) {
        try {
            String hql = "SELECT i FROM Incidente i " +
                    "WHERE i.prestacionDeServicioIncidente.id = :idPrestacion " +
                    "AND i.estadoIncidente = true";
            return entityManager().createQuery(hql, Incidente.class)
                    .setParameter("idPrestacion", idPrestacion)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Incidente> obtenerTodoslosIncidentesReportadosEnlaPrestacionPorIdPrestacion(Long idPrestacion) {
        try {
            String hql = "SELECT i FROM Incidente i " +
                    "WHERE i.prestacionDeServicioIncidente.id = :idPrestacion";
            return entityManager().createQuery(hql, Incidente.class)
                    .setParameter("idPrestacion", idPrestacion)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve una lista vacía si no se encuentra ningún resultado
        }
    }

    public List<PrestacionConIncidenteDTO> prestacionesConIncidenteDTO(List<PrestacionDeServicio> prestacionDeServicios){
        return prestacionDeServicios.stream()
                .map(p-> {
                    PrestacionConIncidenteDTO prestacionConIncidenteDTO = new PrestacionConIncidenteDTO();
                    if(p.getEstadoDeServicioPrestado()){
                        prestacionConIncidenteDTO.convertirDTO(p,null);
                    }
                    else {
                        Incidente incidenteActual = this.obtenerIncidenteActualPorIdPrestacion(p.getId());
                        prestacionConIncidenteDTO.convertirDTO(p,incidenteActual);
                    }
                    return prestacionConIncidenteDTO;
                }).toList();
    }

    public List<PrestacionDeServicio> prestacionesDeServicioActivas(List<PrestacionDeServicio> prestacionDeServicios){
        return prestacionDeServicios.stream().filter(p -> p.isActivo()).collect(Collectors.toList());
    }

    public List<Servicio> serviciosActivos(List<Servicio> servicios){
        return servicios.stream().filter(s -> s.isActivo()).collect(Collectors.toList());
    }

}
