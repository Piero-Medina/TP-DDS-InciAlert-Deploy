package Notificador;

import config.Configuracion;
import models.dominio.actores.Ciudadano;
import models.dominio.comunidad.Comunidad;
import models.dominio.comunidad.CuandoNotificar;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.Establecimiento;
import models.dominio.entidades.OrganismoDeControl;
import models.dominio.notificaciones.Notificador;
import models.dominio.notificaciones.strategys.EstrategiaDeNotificacion;
import models.dominio.notificaciones.strategys.Mail;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.PrestacionDeServicio;
import models.dominio.servicios.Servicio;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class NotificadorTest {

    // Para Usarlo debemos poner el Notificador en Modo Test, asi solo imprime por consola los mensajes
    @Test
    public void testNotificador(){

        EstrategiaDeNotificacion mail = new Mail();

        Ciudadano juan = new Ciudadano
                ("juan", "ejemplo@gmail.com","123", CuandoNotificar.SIN_APUROS, mail, 0,0);
        Ciudadano jose = new Ciudadano
                ("jose", "ejemplo@gmail.com","123", CuandoNotificar.CUANDO_SUCEDEN, mail, 0,5);
        Ciudadano alicia = new Ciudadano
                ("alicia", "ejemplo@gmail.com","123", CuandoNotificar.SIN_APUROS, mail, 22,25);

        // obtenemos el notificador singleton ya la vez iniciamo el metodo asincronico
        Notificador unNotificador = Notificador.getInstancia();
        Comunidad unaComunidad = new Comunidad(unNotificador);

        juan.agregarComunidad(unaComunidad);
        jose.agregarComunidad(unaComunidad);
        alicia.agregarComunidad(unaComunidad);

        // propietario ORG DE CONTROL
        OrganismoDeControl organismoDeControl = new OrganismoDeControl();
        organismoDeControl.setNombre("C.N.R.T");

        // peude haber mas
        EntidadPrestadora entidadPrestadora = new EntidadPrestadora();
        entidadPrestadora.setNombre("Trenes Argentinos");

        // ENTIDAD
        Entidad entidad1 = new Entidad();
        entidad1.setNombre("Linea Mitre");

        Establecimiento establecimiento1 = new Establecimiento();
        establecimiento1.setNombre("Estacion Retiro");

        Establecimiento establecimiento2 = new Establecimiento();
        establecimiento2.setNombre("Estacion Victoria");


        Entidad entidad2 = new Entidad();
        entidad2.setNombre("Linea San Martin");

        Establecimiento establecimiento3 = new Establecimiento();
        establecimiento1.setNombre("Estacion Retiro");

        Establecimiento establecimiento4 = new Establecimiento();
        establecimiento2.setNombre("Estacion Pilar");

        Servicio servicio1 = new Servicio();
        servicio1.setNombre("Baños");

        Servicio servicio2 = new Servicio();
        servicio2.setNombre("Escalera Mecanica");

        PrestacionDeServicio prestacionDeServicio1 = new PrestacionDeServicio();
        prestacionDeServicio1.setNombreServicioPrestado("Baño Familiar exlusivo");

        PrestacionDeServicio prestacionDeServicio2 = new PrestacionDeServicio();
        prestacionDeServicio2.setNombreServicioPrestado("Escaleras para una mayor movilidad");


        prestacionDeServicio1.setServicioPrestado(servicio1);
        prestacionDeServicio2.setServicioPrestado(servicio2);

        establecimiento1.agregarServiciosPrestados(prestacionDeServicio1);
        establecimiento1.agregarServiciosPrestados(prestacionDeServicio2);

        entidad1.agregarEstablecimiento(establecimiento1);
        entidad1.agregarEstablecimiento(establecimiento2);
        entidadPrestadora.agregarEntidad(entidad1);

        entidad2.agregarEstablecimiento(establecimiento3);
        entidad2.agregarEstablecimiento(establecimiento4);
        entidadPrestadora.agregarEntidad(entidad2);

        organismoDeControl.agregarEntidadPrestadora(entidadPrestadora);

        // ANTES ME TIRABA UN ERROR PORQUE NO TENIA LA LISTA DE SERVICIOS PRESTADOS INICIALIZADA EN EL ESTABLECMIENTO

        // SOLO SE PODRA CREAR UN INCDENTE POR CADA PRESTACION DE SERVICIO
        // UNA VEZ QUE SE CIERRA ESE INCIDENTE NO SE PODRA VOLVER A ABRIR
        // SI SE QUIERE CREAR OTRO INCIDENT TENDRA QUE CREAR OTRO
        // LA LOGICA DE ESTO LO HAREMOS EN EL CONTROLER DE INCIDENTES
        // ANTES DE CREAR EL INCIDENTE HAREMOS LA VALIDACION SI PARA LA PRESTACION DE SERVICIO TAL HAY UN INCIDENTE "ACTIVO"

        // crariamos los incidentes con el controles y luego lo añadimos a los usuarios
        // los incidentes apenas se crean tienen el estado en true, osea que estan activos
        Incidente incidente1 = new Incidente(LocalDateTime.now(), null, true);
        incidente1.setNombreIncidente("se acabo el papel");
        incidente1.setPrestacionDeServicioIncidente(prestacionDeServicio1);

        Incidente incidente2 = new Incidente(LocalDateTime.now(),null, true);
        incidente2.setNombreIncidente("corte de luz");
        incidente2.setPrestacionDeServicioIncidente(prestacionDeServicio2);

        // se agregar tambien al incidente el Ciudadano que lo reporto
        juan.agregarAIncidentesReportados(incidente1);
        juan.agregarAIncidentesReportados(incidente2);

        // aviso a todas mis comunidadades
        juan.reportarAberturaDeIncidenteAComunidades(incidente1);
        juan.reportarAberturaDeIncidenteAComunidades(incidente2);

        // al cerrar un incidente este cambiara su estado a falso (CLASE Incidente - estado incidente = false)
        // TAMBIEN va a restablecer la Prestacion De Servicio la cual esta asociada a tal incidente  ( CLASE PrestacionDeServicio - estadoDeServicioPrestado = true)
        incidente1.cerrarIncidente(); // HACE DOS COSAS ESTE METODO
        juan.reportarCierreDeIncidenteAComunidades(incidente1);


        /*
        SI QUEREMOS MAS ADELANTE AL MENSAJE LE PODEMOS AGREGAR LA DESCRIPCION DEL INCIDENTE

        EJEMPLO: juan habre y cerrara incidentes (como el los habrira y cerrara no debe notificarle a el, almenos que lo halla cerrado otro
                 ciudadano, ahi si lo notificaria a juan pero no al ciudadno que lo cerro)

                 jose tiene modo de notificaion (CUANDO SUCEDEN) por eso las notificaciones son al instante, osea cuando el incedente fue
                 abierto y cuando fue cerrado

                 alicia tiene le mode de notificaciones (SIN APUROS) por eso le tiene que llegar un resumen delos incidentes que hubo en el lapso
                 de tiempo en le que ella no quiere ser notificada hasta la hora que hay que notificarla, "baño familiar exclusivo" fue abierto
                 pero tambien fue cerrado en ese lapso, por eso sele avisa que fue cerrado pero fue reestablecido, y "Escaleras para una mayor movilidad"
                 feu abierto, pero aun no fue encerrado en ese lapso de tiempo por eso se le informa que aun sigue abierto


        Hola jose, Te informamos sobre los siguientes incidentes:
        => SERVICIO PRESTADO Baño Familiar exlusivo de ESTABLECIMIENTO Estacion Retiro Con Incidentes
        ----------------------------------------

        Hola jose, Te informamos sobre los siguientes incidentes:
        => SERVICIO PRESTADO Escaleras para una mayor movilidad de ESTABLECIMIENTO Estacion Retiro Con Incidentes
        ----------------------------------------

        Hola jose, Te informamos sobre los siguientes incidentes:
        => SERVICIO PRESTADO Baño Familiar exlusivo de ESTABLECIMIENTO Estacion Retiro tuvo Incidentes pero fue Reestablecido
        ----------------------------------------

        Hola alicia, Te informamos sobre los siguientes incidentes:
        => SERVICIO PRESTADO Baño Familiar exlusivo de ESTABLECIMIENTO Estacion Retiro tuvo Incidentes pero fue Reestablecido
        ----------------------------------------
        => SERVICIO PRESTADO Escaleras para una mayor movilidad de ESTABLECIMIENTO Estacion Retiro Con Incidentes
        ----------------------------------------
        */

        // ANTES INCIABAMOS A CA EL METODO ASINCRONICO, PERO AHORA LO HACEMOS EN EL CONTRUCTOR DEL NOTIFICADOR

        // suspende la ejecuacion del hilo principal por 4 minutos
        try {
            Thread.sleep(4 * 60 * 1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }


        // para el metodo asincronico
        unNotificador.cerrarServicio();
    }


    @Test
    public void mensajeMail(){

        // obtenemos los datos de el archivo de configuracion
        String mail = Configuracion.getMailEmisor();
        String contrasenia = Configuracion.getContraseniaDeAplicacion();
        System.out.println("mail: "+mail+" contraseña: " +contrasenia);

        // Dentro de la clase Mail que implementa Estrategia de Notificaion, usamos los datos del archivo de configuracion para crear un Mail
        EstrategiaDeNotificacion estrategiamail = new Mail();

        Ciudadano ciudadano = new Ciudadano();
        ciudadano.setMail("pieromedina30@gmail.com");
        estrategiamail.enviar(ciudadano,"mesaje de prueba Usando Variables de configuracion");
    }
}
