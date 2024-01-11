package config;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dataBase.repositorios.CiudadanoRepository;
import models.dataBase.repositorios.PermisoRepository;
import models.dominio.actores.*;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class InicializadorDeDatos implements WithSimplePersistenceUnit {


    public InicializadorDeDatos() {

    }

    public void inicializarDatos(){

        this.inicializarPermisos();
        this.verificarExistenciaDeAdministrador();
        this.cargarComunidadesDeEjemplo();
        this.cargarServicioDeEjemplo();

    }

    public void inicializarPermisos() {
        Query consultaConteo = entityManager().createNativeQuery("SELECT COUNT(*) FROM permiso");
        long cantidadPermisos = ((Number) consultaConteo.getSingleResult()).longValue();

        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();


        if (cantidadPermisos == 0) {
            // Consultas SQL para inicializar permisos de servicio
            String[] consultasServicio = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Servicios', 'ver_lista_servicios', 'Permite al usuario ver la lista de servicios')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle del Servicio', 'ver_detalle_servicio', 'Permite al usuario ver los detalles completos de un servicio')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Servicio', 'crear_servicio', 'Permite al usuario crear un nuevo servicio')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Servicio', 'guardar_servicio', 'Permite al usuario guardar un nuevo servicio')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Servicio', 'editar_servicio', 'Permite al usuario editar la información de un servicio existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Servicio', 'actualizar_servicio', 'Permite al usuario actualizar la información de un servicio existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Servicio', 'eliminar_servicio', 'Permite al usuario eliminar un servicio existente')"
            };

            // Consultas SQL para inicializar permisos de comunidad
            String[] consultasComunidad = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Comunidades', 'ver_lista_comunidades', 'Permite al usuario ver la lista de comunidades')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Comunidad', 'ver_detalle_comunidad', 'Permite al usuario ver los detalles completos de una comunidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Comunidad', 'crear_comunidad', 'Permite al usuario crear una nueva comunidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Comunidad', 'guardar_comunidad', 'Permite al usuario guardar una nueva comunidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Comunidad', 'editar_comunidad', 'Permite al usuario editar la información de una comunidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Comunidad', 'actualizar_comunidad', 'Permite al usuario actualizar la información de una comunidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Comunidad', 'eliminar_comunidad', 'Permite al usuario eliminar una comunidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Unirse a una Comunidad', 'unirse_a_comunidad', 'Permite al usuario unirse a una comunidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Abandonar una Comunidad', 'abandonar_comunidad', 'Permite al usuario abandonar a una comunidad')"
            };

            String[] consultasOrgDeControl = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Organismos de Control', 'ver_lista_org_de_control', 'Permite al usuario ver la lista de Organismos de Control')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle del Organismo de Control', 'ver_detalle_org_de_control', 'Permite al usuario ver los detalles completos de un Organismo de Control')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Organismo de Control', 'crear_org_de_control', 'Permite al usuario crear un nuevo Organismo de Control')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Organismo de Control', 'guardar_org_de_control', 'Permite al usuario guardar un nuevo Organismo de Control')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Organismo de Control', 'editar_org_de_control', 'Permite al usuario editar la información de un Organismo de Control existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Organismo de Control', 'actualizar_org_de_control', 'Permite al usuario actualizar la información de un Organismo de Control existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Organismo de Control', 'eliminar_org_de_control', 'Permite al usuario eliminar un Organismo de Control existente')"
            };

            String[] consultasEntidadesPrestadoras = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Entidades Prestadoras', 'ver_lista_entidades_prestadoras', 'Permite al usuario ver la lista de Entidades Prestadoras')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Entidad Prestadora', 'ver_detalle_entidad_prestadora', 'Permite al usuario ver los detalles completos de una Entidad Prestadora')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Entidad Prestadora', 'crear_entidad_prestadora', 'Permite al usuario crear una nueva Entidad Prestadora')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Entidad Prestadora', 'guardar_entidad_prestadora', 'Permite al usuario guardar una nueva Entidad Prestadora')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Entidad Prestadora', 'editar_entidad_prestadora', 'Permite al usuario editar la información de una Entidad Prestadora existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Entidad Prestadora', 'actualizar_entidad_prestadora', 'Permite al usuario actualizar la información de una Entidad Prestadora existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Entidad Prestadora', 'eliminar_entidad_prestadora', 'Permite al usuario eliminar una Entidad Prestadora existente')"
            };

            // Consultas para permisos de Entidades
            String[] consultasEntidades = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Entidades', 'ver_lista_entidades', 'Permite al usuario ver la lista de Entidades')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Entidad', 'ver_detalle_entidad', 'Permite al usuario ver los detalles completos de una Entidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Entidad', 'crear_entidad', 'Permite al usuario crear una nueva Entidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Entidad', 'guardar_entidad', 'Permite al usuario guardar una nueva Entidad')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Entidad', 'editar_entidad', 'Permite al usuario editar la información de una Entidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Entidad', 'actualizar_entidad', 'Permite al usuario actualizar la información de una Entidad existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Entidad', 'eliminar_entidad', 'Permite al usuario eliminar una Entidad existente')"
            };

            String[] consultasEstablecimientos = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Establecimientos', 'ver_lista_establecimientos', 'Permite al usuario ver la lista de Establecimientos')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle del Establecimiento', 'ver_detalle_establecimiento', 'Permite al usuario ver los detalles completos de un Establecimiento')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Establecimiento', 'crear_establecimiento', 'Permite al usuario crear un nuevo Establecimiento')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Establecimiento', 'guardar_establecimiento', 'Permite al usuario guardar un nuevo Establecimiento')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Establecimiento', 'editar_establecimiento', 'Permite al usuario editar la información de un Establecimiento existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Establecimiento', 'actualizar_establecimiento', 'Permite al usuario actualizar la información de un Establecimiento existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Establecimiento', 'eliminar_establecimiento', 'Permite al usuario eliminar un Establecimiento existente')"
            };

            String[] consultasLocalizaciones = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Localizaciones', 'ver_lista_localizaciones', 'Permite al usuario ver la lista de Localizaciones')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Localización', 'ver_detalle_localizacion', 'Permite al usuario ver los detalles completos de una Localización')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Localización', 'crear_localizacion', 'Permite al usuario crear una nueva Localización')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Localización', 'guardar_localizacion', 'Permite al usuario guardar una nueva Localización')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Localización', 'editar_localizacion', 'Permite al usuario editar la información de una Localización existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Localización', 'actualizar_localizacion', 'Permite al usuario actualizar la información de una Localización existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Localización', 'eliminar_localizacion', 'Permite al usuario eliminar una Localización existente')"
            };


            String[] consultasNotificaciones = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Notificaciones', 'ver_lista_notificaciones', 'Permite al usuario ver la lista de Notificaciones')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Notificación', 'ver_detalle_notificacion', 'Permite al usuario ver los detalles completos de una Notificación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Notificación', 'crear_notificacion', 'Permite al usuario crear una nueva Notificación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Notificación', 'guardar_notificacion', 'Permite al usuario guardar una nueva Notificación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Notificación', 'editar_notificacion', 'Permite al usuario editar la información de una Notificación existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Notificación', 'actualizar_notificacion', 'Permite al usuario actualizar la información de una Notificación existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Notificación', 'eliminar_notificacion', 'Permite al usuario eliminar una Notificación existente')",

                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Configuracion de las Notificaciones', 'ver_detalle_notificacion_configuracion', 'Permite al usuario ver los detalles de la configuracion de notificaciones')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar la Configuracion de las Notificaciones', 'editar_notificacion_configuracion', 'Permite al usuario editar la configuracion de notificaciones')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar la Configuracion de las Notificaciones', 'actualizar_notificacion_configuracion', 'Permite al usuario ver los actualizar la configuracion de notificaciones')"
            };

            String[] consultasPrestaciones = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Prestaciones', 'ver_lista_prestaciones', 'Permite al usuario ver la lista de Prestaciones')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle de la Prestación', 'ver_detalle_prestacion', 'Permite al usuario ver los detalles completos de una Prestación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Prestación', 'crear_prestacion', 'Permite al usuario crear una nueva Prestación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Prestación', 'guardar_prestacion', 'Permite al usuario guardar una nueva Prestación')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Prestación', 'editar_prestacion', 'Permite al usuario editar la información de una Prestación existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Prestación', 'actualizar_prestacion', 'Permite al usuario actualizar la información de una Prestación existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Prestación', 'eliminar_prestacion', 'Permite al usuario eliminar una Prestación existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Agregar Una Prestacion A Intereses', 'agregar_intereses_prestacion', 'Permite al usuario agregar una Prestacion a sus intereses')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Remover Una Prestacion De Intereses', 'remover_intereses_prestacion', 'Permite al usuario remover una Prestacion de sus intereses')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Cambiar el estado De Una Prestacion', 'cambiar_estado_prestacion', 'Permite al usuario cambiar el estado a una Prestacion')"
            };

            String[] consultasPermisosIncidentes = {
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Lista de Incidentes', 'ver_lista_incidentes', 'Permite al usuario ver la lista de incidentes')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Ver Detalle del Incidente', 'ver_detalle_incidente', 'Permite al usuario ver los detalles completos de un incidente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Crear Incidente', 'crear_incidente', 'Permite al usuario crear un nuevo incidente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Guardar Incidente', 'guardar_incidente', 'Permite al usuario guardar un nuevo incidente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Editar Incidente', 'editar_incidente', 'Permite al usuario editar la información de un incidente existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Actualizar Incidente', 'actualizar_incidente', 'Permite al usuario actualizar la información de un incidente existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Eliminar Incidente', 'eliminar_incidente', 'Permite al usuario eliminar un incidente existente')",
                    "INSERT INTO permiso (nombre, nombreInterno, descripcion) VALUES ('Cerrar Incidente', 'cerrar_incidente', 'Permite al usuario cerrar un incidente existente')"
            };


            // Agregar permisos de servicio
            for (String consulta : consultasServicio) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Agregar permisos de comunidad
            for (String consulta : consultasComunidad) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Agregar permisos de organismo de control
            for (String consulta : consultasOrgDeControl) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // agregar permisos de Entidades Prestadoras
            for (String consulta : consultasEntidadesPrestadoras) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de Entidades
            for (String consulta : consultasEntidades) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de Establecimientos
            for (String consulta : consultasEstablecimientos) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de Localizaciones
            for (String consulta : consultasLocalizaciones) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de Notificaciones
            for (String consulta : consultasNotificaciones) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de Prestaciones
            for (String consulta : consultasPrestaciones) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }

            // Bucle for para ejecutar las consultas de permisos de incidentes
            for (String consulta : consultasPermisosIncidentes) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }
        }
        tx.commit();
    }


    public void verificarExistenciaDeAdministrador(){
        Rol rolAdministrador = entityManager().createQuery(
                        "SELECT r FROM Rol r WHERE r.tipo = :tipo", Rol.class)
                .setParameter("tipo", TipoRol.ADMINISTRADOR)
                .getResultList().stream().findFirst().orElse(null);

        if(rolAdministrador == null){

            CiudadanoRepository ciudadanoRepository = new CiudadanoRepository();
            PermisoRepository permisoRepository = new PermisoRepository();

            Permiso permisoVerListaServicios = permisoRepository.obtenerPermisoPorNombreInterno("ver_lista_servicios");
            Permiso permisoVerDetalleServicio = permisoRepository.obtenerPermisoPorNombreInterno("ver_detalle_servicio");
            Permiso permisoCrearServicio = permisoRepository.obtenerPermisoPorNombreInterno("crear_servicio");
            Permiso permisoGuardarServicio = permisoRepository.obtenerPermisoPorNombreInterno("guardar_servicio");
            Permiso permisoEditarServicio = permisoRepository.obtenerPermisoPorNombreInterno("editar_servicio");
            Permiso permisoActualizarServicio = permisoRepository.obtenerPermisoPorNombreInterno("actualizar_servicio");
            Permiso permisoEliminarServicio = permisoRepository.obtenerPermisoPorNombreInterno("eliminar_servicio");

            Permiso permisoVerListaComunidades = permisoRepository.obtenerPermisoPorNombreInterno("ver_lista_comunidades");
            Permiso permisoVerDetalleComunidad = permisoRepository.obtenerPermisoPorNombreInterno("ver_detalle_comunidad");
            Permiso permisoCrearComunidad = permisoRepository.obtenerPermisoPorNombreInterno("crear_comunidad");
            Permiso permisoGuardarComunidad = permisoRepository.obtenerPermisoPorNombreInterno("guardar_comunidad");
            Permiso permisoEditarComunidad = permisoRepository.obtenerPermisoPorNombreInterno("editar_comunidad");
            Permiso permisoActualizarComunidad = permisoRepository.obtenerPermisoPorNombreInterno("actualizar_comunidad");
            Permiso permisoEliminarComunidad = permisoRepository.obtenerPermisoPorNombreInterno("eliminar_comunidad");



            Ciudadano ciudadano = new Ciudadano();
            ciudadano.setNombre("admin");

            Usuario usuario = new Usuario();
            usuario.setNombre("admin");
            usuario.setContrasenia("admin");

            Rol rol = new Rol();
            rol.setTipo(TipoRol.ADMINISTRADOR);
            rol.agregarPermisos(
                    permisoVerListaServicios,
                    permisoVerDetalleServicio,
                    permisoCrearServicio,
                    permisoGuardarServicio,
                    permisoEditarServicio,
                    permisoActualizarServicio,
                    permisoEliminarServicio,

                    permisoVerListaComunidades,
                    permisoVerDetalleComunidad,
                    permisoCrearComunidad,
                    permisoGuardarComunidad,
                    permisoEditarComunidad,
                    permisoActualizarComunidad,
                    permisoEliminarComunidad
            );


            usuario.setRol(rol);
            ciudadano.setUsuario(usuario);

            ciudadanoRepository.save(ciudadano);

            System.out.println("ADMINISTRADOR CREADO");
        }

    }

    public void cargarComunidadesDeEjemplo() {
        // Verificar si hay alguna comunidad existente
        Query consultaConteoComunidades = entityManager().createNativeQuery("SELECT COUNT(*) FROM comunidad");
        long cantidadComunidades = ((Number) consultaConteoComunidades.getSingleResult()).longValue();

        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        if (cantidadComunidades == 0) {
            // Consulta para insertar comunidades
            String[] consultasComunidades = {
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Amantes de la Naturaleza', 'Comunidad para aquellos apasionados por la belleza natural y la conservación del medio ambiente.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Programadores Creativos', 'Un lugar para desarrolladores que buscan fusionar la programación con la creatividad y la innovación.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Viajeros Intrépidos', 'Conéctate con personas que comparten la pasión por explorar el mundo y descubrir nuevos destinos emocionantes.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Chefs en Casa', 'Comunidad dedicada a los amantes de la cocina casera, donde se comparten recetas, consejos y experiencias culinarias.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Fotógrafos en Foco', 'Únete a nosotros para compartir tus habilidades fotográficas, aprender nuevas técnicas y explorar el mundo de la fotografía.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Amigos del Libro', 'Una comunidad para lectores ávidos que desean discutir y compartir sus libros favoritos, recomendaciones y opiniones literarias.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Melómanos Unidos', 'Conecta con amantes de la música de todas las épocas y géneros para discutir, descubrir y compartir nuevas melodías.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Artistas en Ascenso', 'Un espacio dedicado a artistas emergentes que buscan inspiración, colaboración y apoyo mutuo en sus carreras creativas.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Entusiastas del Fitness', 'Únete a nosotros para hablar sobre rutinas de ejercicio, nutrición saludable y motivación para mantener un estilo de vida activo.', true)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Jugadores Virtuales', 'Comunidad para aficionados a los videojuegos, donde puedes discutir tus juegos favoritos, estrategias y últimas novedades en el mundo del gaming.', true)"
            };

            // Bucle for para ejecutar las consultas de comunidades
            for (String consulta : consultasComunidades) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }
        }
        tx.commit();
    }


    public void cargarServicioDeEjemplo(){
        // Verificar si hay alguna comunidad existente
        Query consultaConteoComunidades = entityManager().createNativeQuery("SELECT COUNT(*) FROM servicio");
        long cantidadComunidades = ((Number) consultaConteoComunidades.getSingleResult()).longValue();

        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        if (cantidadComunidades == 0) {
            // Consulta para insertar comunidades
            String[] consultasServicios = {
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Escaleras Mecánicas', 'Servicio de escaleras mecánicas para facilitar el desplazamiento', true)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Ascensores', 'Sistema de ascensores para facilitar el transporte vertical dentro de un complejo', true)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Baños', 'Servicio de instalaciones sanitarias para comodidad de los clientes', true)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Molinete', 'Servicio que proporciona el control de acceso mediante la instalación y operación de molinetes automáticos', true)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Área de Descanso', 'Servicio que proporciona áreas diseñadas para el descanso y bienestar de los usuarios', true)"
            };


            // Bucle for para ejecutar las consultas de servicios
            for (String consulta : consultasServicios) {
                entityManager().createNativeQuery(consulta).executeUpdate();
            }
        }
        tx.commit();
    }

}


/*
My SQL
String[] consultasComunidades = {
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Amantes de la Naturaleza', 'Comunidad para aquellos apasionados por la belleza natural y la conservación del medio ambiente.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Programadores Creativos', 'Un lugar para desarrolladores que buscan fusionar la programación con la creatividad y la innovación.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Viajeros Intrépidos', 'Conéctate con personas que comparten la pasión por explorar el mundo y descubrir nuevos destinos emocionantes.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Chefs en Casa', 'Comunidad dedicada a los amantes de la cocina casera, donde se comparten recetas, consejos y experiencias culinarias.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Fotógrafos en Foco', 'Únete a nosotros para compartir tus habilidades fotográficas, aprender nuevas técnicas y explorar el mundo de la fotografía.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Amigos del Libro', 'Una comunidad para lectores ávidos que desean discutir y compartir sus libros favoritos, recomendaciones y opiniones literarias.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Melómanos Unidos', 'Conecta con amantes de la música de todas las épocas y géneros para discutir, descubrir y compartir nuevas melodías.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Artistas en Ascenso', 'Un espacio dedicado a artistas emergentes que buscan inspiración, colaboración y apoyo mutuo en sus carreras creativas.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Entusiastas del Fitness', 'Únete a nosotros para hablar sobre rutinas de ejercicio, nutrición saludable y motivación para mantener un estilo de vida activo.', 1)",
                    "INSERT INTO comunidad (nombre, descripcion, activo) VALUES ('Jugadores Virtuales', 'Comunidad para aficionados a los videojuegos, donde puedes discutir tus juegos favoritos, estrategias y últimas novedades en el mundo del gaming.', 1)"
            };
*/


/*
My SQL
String[] consultasServicios = {
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Escaleras Mecánicas', 'Servicio de escaleras mecánicas para facilitar el desplazamiento', 1)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Ascensores', 'Sistema de ascensores para facilitar el transporte vertical dentro de un complejo', 1)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Baños', 'Servicio de instalaciones sanitarias para comodidad de los clientes', 1)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Molinete', 'Servicio que proporciona el control de acceso mediante la instalación y operación de molinetes automáticos', 1)",
                    "INSERT INTO servicio (nombre, descripcion, activo) VALUES ('Área de Descanso', 'Servicio que proporciona áreas diseñadas para el descanso y bienestar de los usuarios', 1)"
            };
*/