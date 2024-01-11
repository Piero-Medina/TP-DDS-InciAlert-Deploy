package controllers;

import models.dataBase.repositorios.*;

public class FactoryController {

    public static Object controller(String nombre) {
        Object controller = null;
        switch (nombre) {
            case "Servicios": controller = new ServiciosController(new ServicioRepository());break;
            case "Incidentes": controller = new IncidentesController(new IncidenteRepository(), new PrestacionRepository());break;
            case "registro": controller = new RegistroController(new CiudadanoRepository(), new UsuarioRepository(), new PropietarioRepository());break;
            case "login": controller = new LoginController(new UsuarioRepository(), new PropietarioRepository(), new CiudadanoRepository()); break;
            case "dashboard": controller = new DashboardController(); break;
            case "notificacion": controller = new NotificacionController(new CiudadanoRepository(), new NotificacionRepository()); break;

            case "cargaMasiva": controller = new CargaMasivaController(new PropietarioRepository()); break;
            case "subidos": controller =new SubidosPController(); break;
            case "organismosDeControl": controller = new OrganismoDeControlPController(new OrganismoControlRepository()); break;
            case "entidadesPrestadoras": controller = new EntidadPrestadoraPController(new OrganismoControlRepository(), new EntidadPrestadoraRepository()); break;
            case "entidades": controller = new EntidadPController(new OrganismoControlRepository(), new EntidadPrestadoraRepository(), new EntidadRepository()); break;
            case "establecimientos": controller = new EstablecimientoPController(new EntidadRepository(), new OrganismoControlRepository(), new EntidadPrestadoraRepository(), new EstablecimientoRepository()); break;
            case "localizacion": controller = new LocalizacionController(); break;

            case "comunidad": controller = new ComunidadController(new ComunidadRepository()); break;
            case "prestacion": controller = new PrestacionController(new PrestacionRepository()); break;

        }
        return controller;
    }
}
