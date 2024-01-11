package models.dominio.notificaciones;

import models.dominio.comunidad.CuandoNotificar;
import models.dominio.servicios.Incidente;
import models.dominio.actores.Ciudadano;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// NOTIFICADOR SINGLETON
public class Notificador {

    private static Notificador instancia = null;
    private List<Notificacion> notificacionesPorEnviar;

    // usado para generar el metodo asincronico
    private ScheduledExecutorService servicioPlanificador;


    private Notificador(){
        this.notificacionesPorEnviar = new ArrayList<>();
        this.servicioPlanificador = Executors.newScheduledThreadPool(1);
        // iniciamos el metodo asincronico dentro del notificador
        this.iniciarEnvioAsincronico();
    }

    public static Notificador getInstancia() {
        if (instancia == null){
            instancia = new Notificador();
        }
        return instancia;
    }

    public void notificar(Notificacion notificacion) {
        // para las pruevas
        //System.out.println(notificacion.crearMensaje());
        Ciudadano ciudadano = notificacion.getDestinatario();
        // dentro del metodo llamo al adapter y ademas agrego el mensaje a mi lista de notificaiones
        ciudadano.notificar(notificacion);

    }

    public void notificarAlInstante(Ciudadano persona, Incidente incidente, String titulo){
        Notificacion nuevaNotificacion = new Notificacion(persona, incidente); // se agregara mas cosas a la notificacion
        nuevaNotificacion.setTitulo(titulo);
        this.notificar(nuevaNotificacion);
    }

    public void notificarMiembroSegunSuForma(Ciudadano persona, Incidente incidente, String titulo){
        if (persona.getCuandoNotificar() == CuandoNotificar.CUANDO_SUCEDEN){
            this.notificarAlInstante(persona, incidente, titulo);
        }
         else if(persona.getCuandoNotificar() == CuandoNotificar.SIN_APUROS){
            miembroDisponibleParaNotificar(persona, incidente, titulo);
        }
         else {
             System.out.println("EL MIEBRO NO TIENE CONFIGURADA LA FORMA EN LA QUE VA A RECIBIR LOS INCIDENTES");
        }
    }

    /*
    nos fijamos si el miembro esta disponible para ser notificado
    (si ya esta en horario para ser notificado ya que podia programar para que a partir de un horario reciba notificaiones)
    o si aun no esta a horario par asi programar la notificacion.
     */
    public void miembroDisponibleParaNotificar(Ciudadano miembro, Incidente incidente, String titulo){
        if( this.horarioPasado(miembro) ){
            notificarAlInstante(miembro, incidente, titulo);
        }
        else{
            programarNotificacion(miembro, incidente, titulo);
        }
    }

    public void programarNotificacion(Ciudadano persona, Incidente incidente, String titulo){
        // si ya hay un notificaion programada para una Persona solo evaluamos si ya fue tenida en cuanta para el resumen d ela notificaion
        if( notificacionesPorEnviar.stream().anyMatch( m -> m.getDestinatario().equals(persona)) ){
            notificacionesPorEnviar.get( posicionDePersona(persona) ).evaluarIncidenteParaNotificar(incidente);
        }
        // si no hay una notifcaion programada para una persona, creamos una
        else{
            Notificacion nuevaNotificacion = new Notificacion(persona, incidente);
            this.notificacionesPorEnviar.add(nuevaNotificacion);
            nuevaNotificacion.setTitulo(titulo);
        }
    }

    // configuro el metodo asicronico para que se ejecute al intante cuando es llamado(delay = 0), para que cada 10 segundp ejecute el metodo envioAsincronico
    // scheduleAtFixedRate -> el nuevo ciclo de ejecucion no se iniciara hasta que la ejecucion anterior no se complete
    public void iniciarEnvioAsincronico(){
        servicioPlanificador.scheduleAtFixedRate( () -> envioAsincronico(), 0, 10,TimeUnit.SECONDS);
    }

    public void envioAsincronico(){
        for ( Notificacion notificacion : this.notificacionesPorEnviar){
            if (this.horarioPasado(notificacion.getDestinatario())){
                notificar(notificacion);
                this.notificacionesPorEnviar.remove(notificacion);
            }
        }
    }

    public boolean horarioPasado(Ciudadano miembro){
        return miembro.getHorarioDeNotificaion().compareTo(LocalTime.now()) < 0;
    }

    public int posicionDePersona(Ciudadano miembroBuscado){
        int posicion = 0;
        for (Notificacion notificacion : this.notificacionesPorEnviar ){
            if(notificacion.getDestinatario().equals(miembroBuscado)){
                return posicion;
            }
            posicion ++;
        }
        return -1;
    }

    // apagamos el metodo asincronico
    public void cerrarServicio(){
        this.servicioPlanificador.shutdown();
    }

}





