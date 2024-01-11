package controllers;

import DTOs.IncidenteDTO;
import models.dataBase.repositorios.*;
import models.dominio.actores.Ciudadano;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.notificaciones.AvisadorDeIncidentes;
import models.dominio.notificaciones.Notificador;
import models.dominio.servicios.Incidente;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.dominio.servicios.PrestacionDeServicio;
import server.exception.AccessDeniedException;
import server.utils.ICrudViewsHandler;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IncidentesController extends Controller implements ICrudViewsHandler {
    private IncidenteRepository repositorioDeIncidentes;
    private PrestacionRepository respositorioDePrestaciones;

    public IncidentesController(IncidenteRepository repositorioDeIncidentes, PrestacionRepository prestacionRepository) {
        this.repositorioDeIncidentes = repositorioDeIncidentes;
        this.respositorioDePrestaciones = prestacionRepository;
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_incidentes");

        Map<String, Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        if (!ciudadano.getIncidentesReportados().isEmpty()) {
            model.put("lleno", "true");
        }

        List<Incidente> incidentesActivos = this.incidentesActivos(ciudadano.getIncidentesReportados());
        model.put("incidentes", incidentesActivos);

        model.put("ciudadano", true);
        context.render("incidente/index-incidente.hbs",model);
    }

    public void indexAll(Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_incidentes");
        Map<String, Object> model = new HashMap<>();

        String idPrestacionDeServicio = context.pathParam("idP");

        List<Incidente> incidentesIndiscriminados = this.obtenerIncidentesPorIdPrestacionIndiscriminadamente(Long.parseLong(idPrestacionDeServicio));

        if(!incidentesIndiscriminados.isEmpty()){
            model.put("lleno", true);
        }

        model.put("incidentes", incidentesIndiscriminados);

        model.put("propietario", true);
        context.render("incidente/index-incidente.hbs",model);
    }

    public void indexAbiertos(Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_incidentes");

        Map<String, Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        if (!ciudadano.getIncidentesReportados().isEmpty()) {
            model.put("lleno", "true");
        }

        List<Incidente> incidentesActivos = this.incidentesActivos(ciudadano.getIncidentesReportados());
        model.put("incidentes", this.misIncidentesAbiertos(incidentesActivos));

        model.put("ciudadano", true);
        context.render("incidente/index-incidente.hbs",model);
    }

    public List<Incidente> misIncidentesAbiertos(List<Incidente> incidentes){
        return incidentes.stream().filter(i -> i.getEstadoIncidente()).collect(Collectors.toList());
    }

    public void indexCerrados(Context context){
        super.usuarioLogueadoTienePermisos(context, "ver_lista_incidentes");

        Map<String, Object> model = new HashMap<>();
        Ciudadano ciudadano = super.CiudadanoLogueado(context);

        if (!ciudadano.getIncidentesReportados().isEmpty()) {
            model.put("lleno", "true");
        }

        List<Incidente> incidentesActivos = this.incidentesActivos(ciudadano.getIncidentesReportados());
        model.put("incidentes", this.misIncidentesCerrados(incidentesActivos));

        model.put("ciudadano", true);
        context.render("incidente/index-incidente.hbs",model);
    }

    public List<Incidente> misIncidentesCerrados(List<Incidente> incidentes){
        return incidentes.stream().filter(i -> !i.getEstadoIncidente()).collect(Collectors.toList());
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context,"ver_detalle_incidente");
        Usuario usuario = super.usuarioLogueado(context);
        Map<String, Object> model = new HashMap<>();

        Incidente incidente = this.repositorioDeIncidentes.findById(Long.parseLong(context.pathParam("id")));
        model.put("incidente", incidente);
        model.put("prestacion", incidente.getPrestacionDeServicioIncidente());
        this.convertirFechaYHora(incidente, model);

        if(usuario.getRol().getTipo().equals(TipoRol.CIUDADANO)) {
            model.put("ciudadano", true);
        } else if (usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            model.put("propietario", true);
        }

        context.render("incidente/show-incidente.hbs", model);
    }

    @Override
    public void create(Context context) {
        super.usuarioLogueadoTienePermisos(context,"crear_incidente");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        PrestacionDeServicio prestacionDeServicio = this.respositorioDePrestaciones.findById(Long.parseLong(context.pathParam("idP")));

        Map<String, Object> model = new HashMap<>();

        model.put("ciudadano",true);

        if(ciudadano.getMedioDeNotificacion() == null){
            model.put("sinMedioDeNotificacionParaReportarIncidente", true);
            context.render("texto/texto-transparente-general.hbs",model);
        } else {
            model.put("prestacion", prestacionDeServicio);
            context.render("incidente/create-incidente.hbs", model);
        }
    }

    @Override
    public void save(Context context) {
        super.usuarioLogueadoTienePermisos(context, "guardar_incidente");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        PrestacionDeServicio prestacionDeServicio = this.respositorioDePrestaciones.findById(Long.parseLong(context.pathParam("idP")));

        Incidente incidente = new Incidente();
        incidente.setPrestacionDeServicioIncidente(prestacionDeServicio);
        this.asignarParametros(incidente, context);
        incidente.setActivo(true);
        incidente.abrirIncidente(); // estadoIncidente (true) y cambia el estado de la prestacion de servicios a (false)

        // se agrega el ciudaddno que reporto tambien al incidente - relacion BIDIRECCIONAL
        ciudadano.agregarAIncidentesReportados(incidente);

        this.repositorioDeIncidentes.save(incidente);

        // dentro de la comunidad se fija si el incidente es actual antes de notificar a los miembros
        ciudadano.reportarAberturaDeIncidenteAComunidades(incidente);

        // Tenemos activado el guardado en cascada, asi que tambien se guardara el incidente
        new CiudadanoRepository().update(ciudadano);

        // llamariamos al avisadorDeIncdentes
        if(incidente.incidenteActual()){
            AvisadorDeIncidentes avisadorDeIncidentes = new AvisadorDeIncidentes(Notificador.getInstancia());
            avisadorDeIncidentes.setCiudadanos(new CiudadanoRepository().findAll());
            avisadorDeIncidentes.notificarIncidentes(ciudadano, incidente);
        }

        // redirecciono a mis incidentes
        context.redirect("/misIncidentes");
    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_incidente");

        Incidente incidente = this.repositorioDeIncidentes.findById(Long.parseLong(context.pathParam("id")));
        incidente.setActivo(false);
        this.repositorioDeIncidentes.update(incidente);

        context.redirect("/misIncidentes");
    }

    public void cerrarIncidente(Context context){
        super.usuarioLogueadoTienePermisos(context, "cerrar_incidente");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        Incidente incidente = this.repositorioDeIncidentes.findById(Long.parseLong(context.pathParam("id")));
        PrestacionDeServicio prestacionDeServicio = incidente.getPrestacionDeServicioIncidente();

        String idEstablecimiento = String.valueOf(prestacionDeServicio.getEstablecimiento().getId());

        // pone el estado del incidente en false, reestablece la PrestacionDeservicio, pone fecha y hora actual de cierre al incidente
        incidente.cerrarIncidente();

        // no tenemos el cascade activa la cascada asi que hay que actualizar ambos
        this.respositorioDePrestaciones.update(prestacionDeServicio);
        this.repositorioDeIncidentes.update(incidente);

        // reportamos el cierre en las comunidades donde este el incidente (logica en la comunidad)
        ciudadano.reportarCierreDeIncidenteAComunidades(incidente);

        //hacemos un update por las dudas
        new CiudadanoRepository().update(ciudadano);

        // llamariamos al avisadorDeIncdentes
        if(incidente.incidenteActual()){
            AvisadorDeIncidentes avisadorDeIncidentes = new AvisadorDeIncidentes(Notificador.getInstancia());
            avisadorDeIncidentes.setCiudadanos(new CiudadanoRepository().findAll());
            avisadorDeIncidentes.notificarIncidentes(ciudadano, incidente);
        }

        // lo redirigo a las demas prestaciones de servicio del establecmiento donde hubo incidentes
        // recordmoe que cualquiera peude cerrar el incidente
        context.redirect("/establecimientos/"+idEstablecimiento+"/prestaciones");
    }

    private void asignarParametros(Incidente incidente, Context context) {
        String fecha = null;
        String hora = null;

        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            incidente.setNombreIncidente(context.formParam("nombre"));
        }
        if(context.formParam("observacion") != null && !context.formParam("observacion").isEmpty()) {
            incidente.setObservaciones(context.formParam("observacion"));
        }
        if(context.formParam("fecha") != null && !context.formParam("fecha").isEmpty()) {
            fecha = context.formParam("fecha");
        }
        if(context.formParam("hora") != null && !context.formParam("hora").isEmpty()) {
            hora = context.formParam("hora");
        }

        LocalDate fechaDate = (fecha == null) ? LocalDate.now() : LocalDate.parse(fecha);
        LocalTime horaTime = (hora == null) ? LocalTime.now() : LocalTime.parse(hora);
        LocalDateTime fechaYHora = LocalDateTime.of(fechaDate, horaTime);

        incidente.setFechaApertura(fechaYHora);

        System.out.print("parametros asignados");

    }

    public void convertirFechaYHora (Incidente incidente, Map<String,Object> model){
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\"yyyy-MM-dd HH:mm:ss\"");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (incidente.getFechaApertura() != null) {
            String fechaApertura = incidente.getFechaApertura().format(dateFormatter);
            String horaApertura = incidente.getFechaApertura().format(timeFormatter);
            model.put("fechaDeApertura", fechaApertura);
            model.put("horaDeApertura", horaApertura);
        }

        if (incidente.getFechaCierre() != null) {
            String fechaCierre = incidente.getFechaCierre().format(dateFormatter);
            String horaCierre = incidente.getFechaCierre().format(timeFormatter);
            model.put("fechaDeCierre", fechaCierre);
            model.put("horaDeCierre", horaCierre);
        }
    }

    private List<Incidente> incidentesActivos(List<Incidente> incidentes){
        return incidentes.stream().filter(i -> i.isActivo()).collect(Collectors.toList());
    }

    protected List<Incidente> obtenerIncidentesPorIdPrestacionYActivo(Long idPrestacion) {
        try {
            String jpql = "SELECT i FROM Incidente i " +
                    "WHERE i.prestacionDeServicioIncidente.id = :idPrestacion " +
                    "AND i.activo = true";
            return entityManager().createQuery(jpql, Incidente.class)
                    .setParameter("idPrestacion", idPrestacion)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve una lista vacía si no hay resultados
        }
    }

    public List<Incidente> obtenerIncidentesPorIdPrestacionIndiscriminadamente(Long idPrestacion) {
        try {
            String jpql = "SELECT i FROM Incidente i " +
                    "WHERE i.prestacionDeServicioIncidente.id = :idPrestacion ";
            return entityManager().createQuery(jpql, Incidente.class)
                    .setParameter("idPrestacion", idPrestacion)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve una lista vacía si no hay resultados
        }
    }


}
