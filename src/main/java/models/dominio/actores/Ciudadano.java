package models.dominio.actores;


import models.dataBase.converters.MedioDeNotificacionConverter;
import models.dataBase.repositorios.CiudadanoRepository;
import models.dominio.api.localizacion.Localizacion;
import models.dominio.comunidad.Comunidad;
import models.dominio.comunidad.CuandoNotificar;
import models.dataBase.Persistente;
import lombok.Getter;
import lombok.Setter;
import models.dominio.notificaciones.Notificacion;
import models.dominio.notificaciones.strategys.EstrategiaDeNotificacion;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.PrestacionDeServicio;
import models.dominio.api.localizacion.Ubicacion;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "ciudadano")
@Getter
@Setter
public class Ciudadano extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "numeroDeTelefono")
    private String numeroDeTelefono;

    @Column(name = "mail")
    private String mail;

    // unidireccional
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    // BIDIRECCIONAL
    @ManyToMany
    @JoinTable(name = "ciudadano_x_comunidad",
               joinColumns = @JoinColumn(name = "ciudadano_id"),
               inverseJoinColumns = @JoinColumn(name = "comunidad_id"))
    private List<Comunidad> comunidades;

    @Enumerated(EnumType.STRING)
    @Column(name = "cuandoNotificar")
    private CuandoNotificar cuandoNotificar;

    @Convert(converter = MedioDeNotificacionConverter.class)
    @Column(name = "medioDeNotificacion")
    private EstrategiaDeNotificacion medioDeNotificacion;


    @Column(name = "horarioDeNotificacion", columnDefinition = "TIME")
    private LocalTime horarioDeNotificaion;

    // unidireccional
    @ManyToMany
    @JoinTable(
            name = "ciudadano_x_prestacion",
            joinColumns = @JoinColumn(name = "ciudadano_id"),
            inverseJoinColumns = @JoinColumn(name = "prestacion_de_servicio_id"))
    private List<PrestacionDeServicio> interes;

    // BIDIRECCIONAl
    @OneToMany(mappedBy = "ciudadanoQueReporto", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Incidente> incidentesReportados;

    // UIDIRECCIONAL
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "localizacion_id")
    private Localizacion localizacionDeInteres;

    // creo que no hace falta persistirlo ya que se activa temporalmente, va nose
    // UNIDIRECCIONAL
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    @OneToMany(mappedBy = "destinatario",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Notificacion> notificaciones;


    public Ciudadano (){
        this.comunidades = new ArrayList<>();
        this.interes = new ArrayList<>();
        this.incidentesReportados = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    // test
    public Ciudadano(String nombre, String mail, String numero, CuandoNotificar forma, EstrategiaDeNotificacion medio, int hora, int minuto){
        this.nombre = nombre;
        this.mail = mail;
        this.numeroDeTelefono = numero;
        this.cuandoNotificar = forma;
        this.medioDeNotificacion = medio;
        this.horarioDeNotificaion = LocalTime.of(hora,minuto);
        this.interes = new ArrayList<>();
        this.comunidades = new ArrayList<>();
        this.incidentesReportados = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public void reportarAberturaDeIncidenteAComunidades(Incidente incidente){
        this.comunidades.forEach(c -> c.reportarAperturaIncidente(incidente,this));
    }

    public void reportarCierreDeIncidenteAComunidades(Incidente incidente){
        this.comunidades.forEach(c -> c.reportarCierreIncidente(incidente, this));
    }

    // POR SER BIDIRECCIONAL
    public void agregarComunidad(Comunidad unacomunidad){
        this.comunidades.add(unacomunidad);
        unacomunidad.agregarMiembros(this); //
    }

    public void agregarAServiciosDeInteres(PrestacionDeServicio prestacionDeServicio){
        this.interes.add(prestacionDeServicio);
    }

    // POR SER BIDIRECCIONAL
    public void agregarAIncidentesReportados(Incidente incidente){
        this.incidentesReportados.add(incidente);
        incidente.setCiudadanoQueReporto(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Ciudadano other = (Ciudadano) obj;
        Long id = this.getId();
        return ( id != null ) && ( id.equals(other.getId()) );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public void agregarNotificacion(Notificacion notificacion){
        this.notificaciones.add(notificacion);
    }

    // CAMBIAR ESTRATEGIA (lo hara el ciudadano)
    public void cambiarMedioDeNotificacion(EstrategiaDeNotificacion estrategiaDeNotificacion){
        this.medioDeNotificacion = estrategiaDeNotificacion;
    }


    //@Transactional
    public void notificar(Notificacion notificacion){
        this.agregarNotificacion(notificacion);
        this.medioDeNotificacion.enviar(this, notificacion.crearMensaje());
        new CiudadanoRepository().update(this);

    }

    public String medioDeNotificacionText(){
        if(this.medioDeNotificacion == null){
            return "Sin Medio De Notificacion";
        } else {
            return this.medioDeNotificacion.getClass().getSimpleName();
        }
    }

    public String cuandoNotificar(){
        return this.cuandoNotificar.toString();
    }

    public String horarioDeNotificaciones(){
        if(this.horarioDeNotificaion != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return this.horarioDeNotificaion.format(formatter);
        }
        return "Sin Horario De Notificacion";
    }

    public String numeroDeTelefonoText(){
        return this.numeroDeTelefono == null ? "sin Numero De Telono" : this.numeroDeTelefono;
    }


}
