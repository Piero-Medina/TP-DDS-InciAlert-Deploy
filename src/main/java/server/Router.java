package server;
import controllers.*;
import models.dominio.actores.TipoRol;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {

    public static void init() {
        Server.app().get("/",ctx -> ctx.render("home.hbs"));

        Server.app().routes(() -> {
            // Servicios
            get("servicios", ((ServiciosController) FactoryController.controller("Servicios"))::index, TipoRol.ADMINISTRADOR);
            get("servicios/crear", ((ServiciosController) FactoryController.controller("Servicios"))::create, TipoRol.ADMINISTRADOR);
            get("servicios/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::show, TipoRol.ADMINISTRADOR);
            get("servicios/{id}/editar", ((ServiciosController) FactoryController.controller("Servicios"))::edit, TipoRol.ADMINISTRADOR);
            post("servicios/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::update, TipoRol.ADMINISTRADOR);
            post("servicios", ((ServiciosController) FactoryController.controller("Servicios"))::save,TipoRol.ADMINISTRADOR);
            get("servicios/eliminar/{id}", ((ServiciosController) FactoryController.controller("Servicios"))::delete, TipoRol.ADMINISTRADOR);

            // Prestaciones De Servicio
            get("misPrestaciones", ((PrestacionController) FactoryController.controller("prestacion"))::indexPropias, TipoRol.CIUDADANO);
            get("prestaciones/{id}/agregarInteres", ((PrestacionController) FactoryController.controller("prestacion"))::agregarInteres, TipoRol.CIUDADANO);
            get("prestaciones/{id}/removerInteres", ((PrestacionController) FactoryController.controller("prestacion"))::removerInteres, TipoRol.CIUDADANO);

            get("establecimientos/{idE}/prestaciones", ((PrestacionController) FactoryController.controller("prestacion"))::index, TipoRol.PROPIETARIO,TipoRol.CIUDADANO);
            get("establecimientos/{idE}/prestaciones/crear", ((PrestacionController) FactoryController.controller("prestacion"))::create, TipoRol.PROPIETARIO);
            get("establecimientos/{idE}/prestaciones/{id}", ((PrestacionController) FactoryController.controller("prestacion"))::show, TipoRol.PROPIETARIO, TipoRol.CIUDADANO);
            get("establecimientos/{idE}/prestaciones/{id}/editar", ((PrestacionController) FactoryController.controller("prestacion"))::edit, TipoRol.PROPIETARIO);
            get("establecimientos/{idE}/prestaciones/{id}/cambiarEstado", ((PrestacionController) FactoryController.controller("prestacion"))::cambiarEstado, TipoRol.PROPIETARIO);
            post("establecimientos/{idE}/prestaciones/{id}", ((PrestacionController) FactoryController.controller("prestacion"))::update, TipoRol.PROPIETARIO);
            post("establecimientos/{idE}/prestaciones", ((PrestacionController) FactoryController.controller("prestacion"))::save, TipoRol.PROPIETARIO);
            get("establecimientos/{idE}/prestaciones/eliminar/{id}", ((PrestacionController) FactoryController.controller("prestacion"))::delete, TipoRol.PROPIETARIO);

            // Comunidades
            get("comunidadesA", ((ComunidadController) FactoryController.controller("comunidad"))::indexA, TipoRol.ADMINISTRADOR);
            get("comunidades", ((ComunidadController) FactoryController.controller("comunidad"))::index, TipoRol.CIUDADANO);
            get("misComunidades", ((ComunidadController) FactoryController.controller("comunidad"))::indexPropias, TipoRol.CIUDADANO);
            get("comunidades/unirse/{id}", ((ComunidadController) FactoryController.controller("comunidad"))::unirse, TipoRol.CIUDADANO);

            get("comunidades/abandonar/{id}", ((ComunidadController) FactoryController.controller("comunidad"))::abandonar, TipoRol.CIUDADANO); 
            get("comunidades/crear", ((ComunidadController) FactoryController.controller("comunidad"))::create, TipoRol.ADMINISTRADOR);
            get("comunidades/{id}", ((ComunidadController) FactoryController.controller("comunidad"))::show, TipoRol.CIUDADANO);
            get("comunidades/{id}/editar", ((ComunidadController) FactoryController.controller("comunidad"))::edit, TipoRol.ADMINISTRADOR);
            post("comunidades/{id}", ((ComunidadController) FactoryController.controller("comunidad"))::update, TipoRol.ADMINISTRADOR);
            post("comunidades", ((ComunidadController) FactoryController.controller("comunidad"))::save, TipoRol.ADMINISTRADOR);
            get("comunidades/eliminar/{id}", ((ComunidadController) FactoryController.controller("comunidad"))::delete, TipoRol.ADMINISTRADOR);

            // Incidentes
            get("misIncidentes", ((IncidentesController) FactoryController.controller("Incidentes"))::index, TipoRol.CIUDADANO);
            get("misIncidentes/abiertos", ((IncidentesController) FactoryController.controller("Incidentes"))::indexAbiertos, TipoRol.CIUDADANO);
            get("misIncidentes/cerrados", ((IncidentesController) FactoryController.controller("Incidentes"))::indexCerrados, TipoRol.CIUDADANO);
            get("prestaciones/{idP}/incidentes", ((IncidentesController) FactoryController.controller("Incidentes"))::indexAll, TipoRol.PROPIETARIO);
            get("incidentes/cerrar/{id}", ((IncidentesController) FactoryController.controller("Incidentes"))::cerrarIncidente, TipoRol.CIUDADANO);
            get("prestaciones/{idP}/incidentes/crear", ((IncidentesController) FactoryController.controller("Incidentes"))::create, TipoRol.CIUDADANO);
            get("prestaciones/{idP}/incidentes/{id}", ((IncidentesController) FactoryController.controller("Incidentes"))::show, TipoRol.CIUDADANO, TipoRol.PROPIETARIO);
            //get("prestaciones/{idP}/incidentes/{id}/editar", ((IncidentesController) FactoryController.controller("Incidentes"))::edit);
            //post("prestaciones/{idP}/incidentes/{id}", ((IncidentesController) FactoryController.controller("Incidentes"))::update);
            post("prestaciones/{idP}/incidentes", ((IncidentesController) FactoryController.controller("Incidentes"))::save, TipoRol.CIUDADANO);
            get("prestaciones/{idP}/incidentes/eliminar/{id}", ((IncidentesController) FactoryController.controller("Incidentes"))::delete, TipoRol.CIUDADANO);

            // Registro
            get("signUp", ((RegistroController) FactoryController.controller("registro"))::index);
            post("signUp", ((RegistroController) FactoryController.controller("registro"))::save);

            // Login
            get("login", ((LoginController) FactoryController.controller("login")):: index);
            post("login",((LoginController) FactoryController.controller("login")):: addSesion);
            get("login/detalle", ((LoginController) FactoryController.controller("login")):: show, TipoRol.CIUDADANO, TipoRol.PROPIETARIO);
            get("login/edit", ((LoginController) FactoryController.controller("login")):: edit, TipoRol.CIUDADANO, TipoRol.PROPIETARIO);
            post("login/edit", ((LoginController) FactoryController.controller("login")):: update, TipoRol.CIUDADANO, TipoRol.PROPIETARIO);
            get("logout", ((LoginController) FactoryController.controller("login")):: cerrarSesion);

            // Dashboard - Home Usuario logueado
            get("dashboard",((DashboardController) FactoryController.controller("dashboard")) :: index, TipoRol.CIUDADANO, TipoRol.PROPIETARIO, TipoRol.ADMINISTRADOR);

            //Notificaciones
            get("misNotificaciones", ((NotificacionController) FactoryController.controller("notificacion"))::index, TipoRol.CIUDADANO);
            get("notificaciones/{id}", ((NotificacionController) FactoryController.controller("notificacion"))::show, TipoRol.CIUDADANO);
            get("notificaciones/configuracion/detalle", ((NotificacionController) FactoryController.controller("notificacion"))::showDetalle, TipoRol.CIUDADANO);
            get("notificaciones/configuracion/edit", ((NotificacionController) FactoryController.controller("notificacion"))::editConfiguracion, TipoRol.CIUDADANO);
            post("notificaciones/configuracion/edit",((NotificacionController) FactoryController.controller("notificacion"))::updateConfiguracion, TipoRol.CIUDADANO);

            // Carga Masiva
            get("cargaMasiva",((CargaMasivaController) FactoryController.controller("cargaMasiva"))::index, TipoRol.PROPIETARIO);
            get("cargaMasiva/manual",((CargaMasivaController) FactoryController.controller("cargaMasiva"))::cargaManual, TipoRol.PROPIETARIO);
            get("cargaMasiva/estructura",((CargaMasivaController) FactoryController.controller("cargaMasiva"))::edit, TipoRol.PROPIETARIO);
            post("cargaMasiva",((CargaMasivaController) FactoryController.controller("cargaMasiva"))::save, TipoRol.PROPIETARIO);

            // Subidos
            get("subidos",((SubidosPController) (FactoryController.controller("subidos")))::index, TipoRol.PROPIETARIO);


            //get("organismosDeControlP/Test",((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::indexTest); // TEST
            // Organismo De Control
            get("organismosDeControl",((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::index, TipoRol.PROPIETARIO);
            get("organismosDeControl/crear", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::create, TipoRol.PROPIETARIO);
            get("organismosDeControl/{id}", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::show, TipoRol.PROPIETARIO);
            get("organismosDeControl/{id}/editar", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::edit, TipoRol.PROPIETARIO);
            post("organismosDeControl/{id}", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::update, TipoRol.PROPIETARIO);
            post("organismosDeControl", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::save, TipoRol.PROPIETARIO);
            get("organismosDeControl/eliminar/{id}", ((OrganismoDeControlPController) FactoryController.controller("organismosDeControl"))::delete, TipoRol.PROPIETARIO);


            //get("entidadesPrestadoras/Test",((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::indexTest); // TEST
            // Entidades Prestadoras
            get("entidadesPrestadoras",((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::index, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/crear", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::create, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{id}", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::show, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{id}/editar", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::edit, TipoRol.PROPIETARIO);
            post("entidadesPrestadoras/{id}", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::update, TipoRol.PROPIETARIO);
            post("entidadesPrestadoras", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::save, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/eliminar/{id}", ((EntidadPrestadoraPController) FactoryController.controller("entidadesPrestadoras"))::delete, TipoRol.PROPIETARIO);


            //get("entidades/Test",((EntidadPController) FactoryController.controller("entidades"))::indexTest); // TEST

            // Entidades
            get("entidades",((EntidadPController) FactoryController.controller("entidades"))::indexAll, TipoRol.CIUDADANO);
            get("entidades/{id}",((EntidadPController) FactoryController.controller("entidades"))::showEntidad, TipoRol.CIUDADANO);

            get("entidadesPrestadoras/{idEP}/entidades",((EntidadPController) FactoryController.controller("entidades"))::index, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{idEP}/entidades/crear", ((EntidadPController) FactoryController.controller("entidades"))::create, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{idEP}/entidades/{id}", ((EntidadPController) FactoryController.controller("entidades"))::show, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{idEP}/entidades/{id}/editar", ((EntidadPController) FactoryController.controller("entidades"))::edit, TipoRol.PROPIETARIO);
            post("entidadesPrestadoras/{idEP}/entidades/{id}", ((EntidadPController) FactoryController.controller("entidades"))::update, TipoRol.PROPIETARIO);
            post("entidadesPrestadoras/{idEP}/entidades", ((EntidadPController) FactoryController.controller("entidades"))::save, TipoRol.PROPIETARIO);
            get("entidadesPrestadoras/{idEP}/entidades/eliminar/{id}", ((EntidadPController) FactoryController.controller("entidades"))::delete, TipoRol.PROPIETARIO);


            //get("establecimientosP/Test",((EstablecimientoPController) FactoryController.controller("establecimientos"))::indexTest); // TEST

            // Establecimientos
            // vez todos los establecimientos, pero si tenes una entidad predecesora
            get("entidades/{idE}/establecimientos",((EstablecimientoPController) FactoryController.controller("establecimientos"))::index, TipoRol.PROPIETARIO, TipoRol.CIUDADANO);
            get("entidades/{idE}/establecimientos/crear", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::create, TipoRol.PROPIETARIO);
            get("entidades/{idE}/establecimientos/{id}", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::show, TipoRol.PROPIETARIO, TipoRol.CIUDADANO);
            get("entidades/{idE}/establecimientos/{id}/editar", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::edit, TipoRol.PROPIETARIO);
            post("entidades/{idE}/establecimientos/{id}", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::update, TipoRol.PROPIETARIO);
            post("entidades/{idE}/establecimientos", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::save, TipoRol.PROPIETARIO);
            get("entidades/{idE}/establecimientos/eliminar/{id}", ((EstablecimientoPController) FactoryController.controller("establecimientos"))::delete, TipoRol.PROPIETARIO);

            // localizacion
            get("{idA}/{idC}/{tipoC}/localizacion",((LocalizacionController) FactoryController.controller("localizacion"))::index, TipoRol.PROPIETARIO);
            post("{idA}/{idC}/{tipoC}/localizacion",((LocalizacionController) FactoryController.controller("localizacion")):: tipoLocalizacion, TipoRol.PROPIETARIO);
            get("{idA}/{idC}/{tipoC}/localizacion/provincia/{tipo}",((LocalizacionController) FactoryController.controller("localizacion")):: provincias, TipoRol.PROPIETARIO);
            post("{idA}/{idC}/{tipoC}/localizacion/provincia/{tipo}",((LocalizacionController) FactoryController.controller("localizacion")):: addProvincia, TipoRol.PROPIETARIO);
            get("{idA}/{idC}/{tipoC}/localizacion/departamento/{idPro}",((LocalizacionController) FactoryController.controller("localizacion")):: departamentos, TipoRol.PROPIETARIO);
            post("{idA}/{idC}/{tipoC}/localizacion/departamento/{idPro}",((LocalizacionController) FactoryController.controller("localizacion")):: addDepartamento, TipoRol.PROPIETARIO);
            get("{idA}/{idC}/{tipoC}/localizacion/municipio/{idPro}",((LocalizacionController) FactoryController.controller("localizacion")):: municipios, TipoRol.PROPIETARIO);
            post("{idA}/{idC}/{tipoC}/localizacion/municipio/{idPro}",((LocalizacionController) FactoryController.controller("localizacion")):: addMunicipio, TipoRol.PROPIETARIO);
        });
    }
}