package models.dominio.notificaciones;

import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;
import models.dominio.actores.Ciudadano;
import models.dominio.servicios.Incidente;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//
@Entity
@Table(name = "notificacion")
@Getter
@Setter
public class Notificacion extends Persistente {


    @ManyToOne
    @JoinColumn(name = "ciudadano_id")
    private Ciudadano destinatario;

    // tenemos una lista ya que si esta en SIN APUROS y no es su hora disponible, tenemos que hacer un resumen de todos los incidentes que hubo durante el tiempo que no estab permitido enviarle mensajes
    @ManyToMany
    @JoinTable(
            name = "notificacion_x_incidente",
            joinColumns = @JoinColumn(name = "notificacion_id"),
            inverseJoinColumns = @JoinColumn(name = "incidente_id"))
    private List<Incidente> incidentesYaNotificados;

    @Column(name = "mensaje" ,columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "enviado")
    private Boolean enviado;

    @Column(name = "fecha_de_envio", columnDefinition = "DATETIME")
    private LocalDateTime fechaYHoraDeEnvio;

    @Column(name = "titulo")
    private String titulo;

    // para HIBERNATE
    public Notificacion(){

    }

    public Notificacion (Ciudadano destinatario, Incidente incidente){
        this.destinatario = destinatario;
        this.incidentesYaNotificados = new ArrayList<>();
        this.incidentesYaNotificados.add(incidente);
        this.fechaYHoraDeEnvio = null;
        // inicializando el mensaje para que no sea null
        if (this.mensaje == null) {
            this.mensaje = "";
        }
    }

    public void agregarIncidente(Incidente incidente){
        this.incidentesYaNotificados.add(incidente);
    }

    public void agregarMensaje(String mensajeAdicional){
        mensaje += mensajeAdicional;
    }

    public Boolean incidenteYaRegistrado(Incidente incidente){
        return this.incidentesYaNotificados.contains(incidente);
        // esto era cuando buscabamos al incidente por nombre
        //return this.incidentesYaNotificados.stream().anyMatch(e -> e.getNombreIncidente().equals(incidente.getNombreIncidente()));
    }

    public void evaluarIncidenteParaNotificar(Incidente incidente){
        if(! this.incidenteYaRegistrado(incidente) && incidente.incidenteActual() ){
            agregarIncidente(incidente);
        }
    }

    // "ServicioPrestado + estado -> (con incidentes, reestablecido)"
    public String crearMensaje(){
        this.agregarMensaje("Hola " + this.destinatario.getNombre() + ", ");
        this.agregarMensaje("Te informamos sobre los siguientes "+this.getTitulo()+":\n");

        for (Incidente incidente : this.incidentesYaNotificados){
            this.agregarMensaje("=> SERVICIO PRESTADO " + incidente.getPrestacionDeServicioIncidente().informacionDeServicioPrestadoMensaje());
            this.agregarMensaje(" " + incidente.estadoIncidente());
            //this.agregarMensaje(" -- ");
            this.agregarMensaje("\n");
        }

        this.setEnviado(true);
        this.setFechaYHoraDeEnvio(LocalDateTime.now());

        return this.mensaje;
    }

    /*
    EN LA CLASE PRESTACION DE SERVICIO SE CONTRUYE ESTE MENSAJE
    informacionDeServicioPrestadoMensaje(){
     return (this.nombreServicioPrestado + " de ESTABLECIMIENTO " + this.deEstablecimiento());
    }
    */

    public String fechaDeEnvio(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.fechaYHoraDeEnvio.format(dateFormatter);
    }

    public String horaDeEnvio(){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return this.fechaYHoraDeEnvio.format(timeFormatter);
    }

    public String mensajeLimitado() {
        int limite = 130;
        if (this.getMensaje() != null && this.getMensaje().length() > limite) {
            return this.getMensaje().substring(0, limite) + "...";
        } else {
            return this.getMensaje();
        }
    }

}
