package controllers;

import models.dominio.actores.Usuario;
import models.dataBase.repositorios.ServicioRepository;
import models.dominio.servicios.Servicio;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import server.utils.ICrudViewsHandler;
import server.exception.AccessDeniedException;

public class ServiciosController extends Controller implements ICrudViewsHandler {
    private ServicioRepository repositorioDeServicios;

    public ServiciosController(ServicioRepository repositorioDeServicios) {
        this.repositorioDeServicios = repositorioDeServicios;
    }


    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context,"ver_lista_servicios");

        Map<String, Object> model = new HashMap<>();
        List<Servicio> servicios = this.servicioDisponibles(this.repositorioDeServicios.findAll());

        model.put("servicios", servicios);
        model.put("administrador",true);
        context.render("servicio/index-servicio.hbs", model);
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_servicio");

        Map<String, Object> model = new HashMap<>();
        Servicio servicio = this.repositorioDeServicios.findById(Long.parseLong(context.pathParam("id")));

        model.put("servicio", servicio);
        model.put("administrador",true);
        context.render("servicio/show-servicio.hbs",model);
    }

    @Override
    public void create(Context context) {
        Usuario usuarioLogueado = super.usuarioLogueado(context);

        Map<String, Object> model = new HashMap<>();
        if(usuarioLogueado == null || !usuarioLogueado.getRol().tenesPermiso("crear_servicio")) {
            throw new AccessDeniedException();
        }

        model.put("administrador",true);
        context.render("servicio/create-servicio.hbs");
    }

    @Override
    public void save(Context context) {
        super.usuarioLogueadoTienePermisos(context, "guardar_servicio");
        System.out.println("Recibida solicitud de creación de servicio");

        Servicio servicio = new Servicio();
        servicio.setActivo(true);
        this.asignarParametros(servicio, context);
        this.repositorioDeServicios.save(servicio);

        context.status(HttpStatus.CREATED);
        context.redirect("/servicios");
    }

    @Override
    public void edit(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_servicio");

        Map<String, Object> model = new HashMap<>();
        Servicio servicio = this.repositorioDeServicios.findById(Long.parseLong(context.pathParam("id")));

        model.put("servicio", servicio);
        model.put("administrador",true);
        context.render("servicio/edit-servicio.hbs", model);
    }

    @Override
    public void update(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_servicio");

        Servicio servicio = this.repositorioDeServicios.findById(Long.parseLong(context.pathParam("id")));
        this.asignarParametros(servicio, context);
        this.repositorioDeServicios.update(servicio);

        context.redirect("/servicios");
    }

    @Override
    public void delete(Context context) {
        super.usuarioLogueadoTienePermisos(context, "eliminar_servicio");

        Servicio servicio = this.repositorioDeServicios.findById(Long.parseLong(context.pathParam("id")));
        servicio.setActivo(false);
        this.repositorioDeServicios.update(servicio);

        context.redirect("/servicios");
    }

    private void asignarParametros(Servicio servicio, Context context) {
        if(context.formParam("nombre") != null && !context.formParam("nombre").isEmpty()) {
            servicio.setNombre(context.formParam("nombre"));
        }
        if(context.formParam("descripcion") != null && !context.formParam("descripcion").isEmpty()) {
            servicio.setDescripcion(context.formParam("descripcion"));
        }
    }

    private List<Servicio> servicioDisponibles(List<Servicio> servicios){
        return servicios.stream().filter(s -> s.isActivo()).collect(Collectors.toList());
    }
}