package controllers;

import models.dataBase.repositorios.*;
import models.dominio.actores.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.dominio.comunidad.CuandoNotificar;
import models.dominio.validacionContrasenia.ValidacionContrasenia;

import java.util.HashMap;
import java.util.Map;

public class RegistroController extends Controller{
    private final ValidacionContrasenia validador  = new ValidacionContrasenia();;
    CiudadanoRepository repositorioCiudadano;
    PropietarioRepository repositoryPropietario;
    UsuarioRepository repositorioUsuario;

    public RegistroController(CiudadanoRepository repositorioCiudadano, UsuarioRepository repositorioUsuario, PropietarioRepository repositoryPropietario){
        this.repositorioCiudadano = repositorioCiudadano;
        this.repositoryPropietario = repositoryPropietario;
        this.repositorioUsuario = repositorioUsuario;
    }


    public void index(Context context) {
        context.render("signUp.hbs");
    }

    public void save(Context context) {
        boolean usuarioValido = this.usuarioValido(context.formParam("usuario"));
        String contrasenia = context.formParam("contrasenia");
        // comentar y poner en true para no analizar la contraseña
        boolean esValida = validador.validar(contrasenia);

        if(esValida && usuarioValido) {
            registroUsuario(context);
            context.status(HttpStatus.CREATED);
            context.render("texto/registro-exitoso.hbs");
       }else{
            Map<String, Object> model = new HashMap<>();
            String errores = validador.errores(contrasenia);
            String mensajeUsuario = mensajeDeUsaurio(usuarioValido);
            model.put("errorContrasenia", errores);
            model.put("errorUsuario",mensajeUsuario);
            context.render("signUp.hbs", model);
        }
    }

    private void asignarParametros(Ciudadano ciudadano, Context context) {
        ciudadano.setNombre(context.formParam("nombre"));
        ciudadano.setApellido(context.formParam("apellido"));
        ciudadano.setMail(context.formParam("email"));

        // por defecto
        ciudadano.setCuandoNotificar(CuandoNotificar.CUANDO_SUCEDEN);
        ciudadano.setMedioDeNotificacion(null);

        Usuario usuario = new Usuario();
        usuario.setNombre(context.formParam("usuario"));
        usuario.setContrasenia(context.formParam("contrasenia"));

        Rol rol = new Rol();
        rol.setTipo(TipoRol.CIUDADANO);
        this.asignarPermisosCiudadano(rol);
        usuario.setRol(rol);
        ciudadano.setUsuario(usuario);
    }

    private void asignarParametros(Propietario propietario, Context context) {
        propietario.setNombre(context.formParam("nombre"));
        propietario.setApellido(context.formParam("apellido"));
        propietario.setMail(context.formParam("email"));
        // por defecto se asigna este al recien registrar un propietario
        propietario.setTipoPropietario(TipoPropietario.SIN_TIPO);

        Usuario usuario = new Usuario();
        usuario.setNombre(context.formParam("usuario"));
        usuario.setContrasenia(context.formParam("contrasenia"));

        Rol rol = new Rol();
        rol.setTipo(TipoRol.PROPIETARIO);
        this.asignarPermisosPropietario(rol);
        usuario.setRol(rol);
        propietario.setUsuario(usuario);
    }

    private void registroUsuario(Context context){
        String categoria = context.formParam("category");

        assert categoria != null;

        if(categoria.equals("Ciudadano")) {
            Ciudadano ciudadano = new Ciudadano();
            this.asignarParametros(ciudadano, context);
            this.repositorioCiudadano.save(ciudadano);
        }else if(categoria.equals("Propietario")) {
            Propietario propietario = new Propietario();
            this.asignarParametros(propietario, context);
            this.repositoryPropietario.save(propietario);
        }
    }

    private boolean usuarioValido (String nombreUsuario){
        Usuario usuario = repositorioUsuario.findByNombre(nombreUsuario);
        return (usuario == null) ? true : false;
    }

    private String mensajeDeUsaurio (boolean bool) {
        return bool ? "" : "El nombre de usuario ya existe";
    }

    private void asignarPermisosCiudadano(Rol rolCiudadano){

        PermisoRepository permisoRepository = new PermisoRepository();

        String[] nombresPermisos = {

                "ver_lista_comunidades",
                "ver_detalle_comunidad",
                "unirse_a_comunidad",
                "abandonar_comunidad",

                "ver_lista_prestaciones",
                "ver_detalle_prestacion",
                "agregar_intereses_prestacion",
                "remover_intereses_prestacion",
                "cambiar_estado_prestacion",

                "ver_lista_incidentes",
                "ver_detalle_incidente",
                "crear_incidente",
                "guardar_incidente",
                "editar_incidente",
                "actualizar_incidente",
                "eliminar_incidente",
                "cerrar_incidente",

                "ver_lista_notificaciones",
                "ver_detalle_notificacion",
                "ver_detalle_notificacion_configuracion",
                "editar_notificacion_configuracion",
                "actualizar_notificacion_configuracion",

                "ver_lista_entidades",
                "ver_detalle_entidad",

                "ver_lista_establecimientos",
                "ver_detalle_establecimiento",

                "ver_lista_localizaciones",
                "ver_detalle_localizacion"

        };

        for (String nombrePermiso : nombresPermisos) {
            Permiso permiso = permisoRepository.obtenerPermisoPorNombreInterno(nombrePermiso);
            if (permiso != null) {
                rolCiudadano.agregarPermiso(permiso);
            }
        }

    }


    private void asignarPermisosPropietario(Rol rolPropietario){

        PermisoRepository permisoRepository = new PermisoRepository();

        String[] nombresPermisos = {

                "ver_lista_prestaciones",
                "ver_detalle_prestacion",
                "crear_prestacion",
                "guardar_prestacion",
                "editar_prestacion",
                "actualizar_prestacion",
                "eliminar_prestacion",
                "cambiar_estado_prestacion",

                "ver_lista_incidentes",
                "ver_detalle_incidente",

                "ver_lista_org_de_control",
                "ver_detalle_org_de_control",
                "crear_org_de_control",
                "guardar_org_de_control",
                "editar_org_de_control",
                "actualizar_org_de_control",
                "eliminar_org_de_control",

                "ver_lista_entidades_prestadoras",
                "ver_detalle_entidad_prestadora",
                "crear_entidad_prestadora",
                "guardar_entidad_prestadora",
                "editar_entidad_prestadora",
                "actualizar_entidad_prestadora",
                "eliminar_entidad_prestadora",

                "ver_lista_entidades",
                "ver_detalle_entidad",
                "crear_entidad",
                "guardar_entidad",
                "editar_entidad",
                "actualizar_entidad",
                "eliminar_entidad",

                "ver_lista_establecimientos",
                "ver_detalle_establecimiento",
                "crear_establecimiento",
                "guardar_establecimiento",
                "editar_establecimiento",
                "actualizar_establecimiento",
                "eliminar_establecimiento",

                "ver_lista_localizaciones",
                "ver_detalle_localizacion",
                "crear_localizacion",
                "guardar_localizacion",
                "editar_localizacion",
                "actualizar_localizacion",
                "eliminar_localizacion"
        };

        for (String nombrePermiso : nombresPermisos) {
            Permiso permiso = permisoRepository.obtenerPermisoPorNombreInterno(nombrePermiso);
            if (permiso != null) {
                rolPropietario.agregarPermiso(permiso);
            }
        }

    }
}
