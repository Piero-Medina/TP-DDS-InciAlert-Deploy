package controllers;

import io.javalin.http.Context;
import models.dataBase.repositorios.CiudadanoRepository;
import models.dataBase.repositorios.NotificacionRepository;
import models.dominio.actores.Ciudadano;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoPropietario;
import models.dominio.comunidad.CuandoNotificar;
import models.dominio.comunidad.MedioDeNotificaion;
import models.dominio.notificaciones.Notificacion;
import models.dominio.notificaciones.strategys.EstrategiaDeNotificacion;
import models.dominio.notificaciones.strategys.Mail;
import models.dominio.notificaciones.strategys.Whatsapp;
import server.utils.ICrudViewsHandler;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NotificacionController extends Controller implements ICrudViewsHandler {

    private CiudadanoRepository ciudadanoRepository;

    private NotificacionRepository repositorioNotificaciones;

    public NotificacionController(CiudadanoRepository ciudadanoRepository, NotificacionRepository notificacionRepository){
        this.ciudadanoRepository = ciudadanoRepository;
        this.repositorioNotificaciones = notificacionRepository;
    }

    @Override
    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_notificaciones");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        Map<String,Object> model = new HashMap<>();

        Collections.reverse(ciudadano.getNotificaciones());

        model.put("notificaciones",ciudadano.getNotificaciones());

        model.put("ciudadano",true);
        context.render("notificacion/index-notificacion.hbs",model);
    }

    @Override
    public void show(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_notificacion");

        Map<String,Object> model = new HashMap<>();
        Notificacion notificacion = repositorioNotificaciones.findById(Long.parseLong(context.pathParam("id")));

        model.put("notificacion",notificacion);
        model.put("ciudadano",true);
        context.render("notificacion/show-notificacion.hbs",model);
    }

    public void showDetalle(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_detalle_notificacion_configuracion");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        Map<String,Object> model = new HashMap<>();

        model.put("detalleNotificaiones","true");
        model.put("persona",ciudadano);
        model.put("ciudadano",true);
        context.render("login/show-login.hbs",model);
    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    public void editConfiguracion(Context context) {
        super.usuarioLogueadoTienePermisos(context, "editar_notificacion_configuracion");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        Map<String, Object> model = new HashMap<>();

        model.put("editNotificaciones",true);
        model.put("persona",ciudadano);
        model.put("ciudadano", true);
        context.render("login/edit-login.hbs",model);
    }

    @Override
    public void update(Context context) {

    }

    public void updateConfiguracion(Context context) {
        super.usuarioLogueadoTienePermisos(context, "actualizar_notificacion_configuracion");

        Ciudadano ciudadano = super.CiudadanoLogueado(context);
        this.asignarParametrosCiudadanoEdit(ciudadano, context);
        this.ciudadanoRepository.update(ciudadano);
        context.redirect("/notificaciones/configuracion/detalle");
    }

    @Override
    public void delete(Context context) {

    }


    public void asignarParametrosCiudadanoEdit(Ciudadano ciudadano, Context context){
        if(context.formParam("cuandoNotificar") != null && !context.formParam("cuandoNotificar").isEmpty()) {
            if(context.formParam("cuandoNotificar").equals("cuandoSuceden")){
                ciudadano.setCuandoNotificar(CuandoNotificar.CUANDO_SUCEDEN);
            }
            else if (context.formParam("cuandoNotificar").equals("sinApuros")){
                ciudadano.setCuandoNotificar(CuandoNotificar.SIN_APUROS);
            }
        }

        if(context.formParam("hora") != null && !context.formParam("hora").isEmpty()) {
            String hora = context.formParam("hora");
            LocalTime horaTime = (hora == null) ? LocalTime.now() : LocalTime.parse(hora);
            ciudadano.setHorarioDeNotificaion(horaTime);
        }

        if(context.formParam("medioDeNotificacion") != null && !context.formParam("medioDeNotificacion").isEmpty()) {
            if(context.formParam("medioDeNotificacion").equals("whatsapp")){
                EstrategiaDeNotificacion whatsapp = new Whatsapp();
                ciudadano.setMedioDeNotificacion(whatsapp);
            }
            else if (context.formParam("medioDeNotificacion").equals("mail")){
                EstrategiaDeNotificacion mail = new Mail();
                ciudadano.setMedioDeNotificacion(mail);
            }
        }

        if(context.formParam("telefono") != null && !context.formParam("telefono").isEmpty()) {
            ciudadano.setNumeroDeTelefono(context.formParam("telefono"));
        }

        if(context.formParam("mail") != null && !context.formParam("mail").isEmpty()) {
            ciudadano.setMail(context.formParam("mail"));
        }

    }
}
