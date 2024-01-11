package models.dominio.comunidad;


import models.dominio.actores.Ciudadano;
import models.dataBase.Persistente;
import models.dominio.entidades.Establecimiento;
import models.dominio.notificaciones.Notificador;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.Servicio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comunidad")
@Getter
@Setter
public class Comunidad extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    // unidireccional
    @ManyToMany
    @JoinTable(
            name = "comunidad_x_incidente",
            joinColumns = @JoinColumn(name = "comunidad_id"),
            inverseJoinColumns = @JoinColumn(name = "incidente_id"))
    private List<Incidente> incidentesOcurridos;

    @Transient
    public float gradoDeConfianza;

    // BIDIRECCIONAl (MIEMBROS)
    @ManyToMany(mappedBy = "comunidades")
    private List<Ciudadano> miembros;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // unidireccional (ADMINISTRADORES)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable( name = "comunidad_x_ciudadano",
            joinColumns = @JoinColumn(name = "comunidad_id"),
            inverseJoinColumns = @JoinColumn(name = "ciudadano_id")
    )
    private List<Ciudadano> administradores;

    @Column(name = "activo")
    private boolean activo;

    @Transient
    private Notificador notificador;

    // TEST - INYECTAMOS NOSOTROS MISMOS EL NOTIFICADOR
    public Comunidad(Notificador notificadorInyectado){
        this.incidentesOcurridos = new ArrayList<>();
        this.miembros = new ArrayList<>();
        this.notificador = notificadorInyectado;
    }

    // SIN ARGUMENTOS PARA HIBERNATE
    public Comunidad(){
        this.incidentesOcurridos = new ArrayList<>();
        this.miembros = new ArrayList<>();
    }

    /*
    cuando apenas Hibernate hidrate la entidad, ejecutara este metodo, con el cual haremos que
    tome la intancia del notificadort SINGLETON. (trnaquilamente podriamos haberlo intanciado
    en el contructor vacio - pero aprovechamos esta nueva anotacion)
    */
    @PostLoad
    private void agregarNotificador() {
        this.notificador = Notificador.getInstancia();  // O cualquier lógica de inicialización necesaria
    }


    // si existe no importa ya que la validacion de si Existe lo haremos antes de Crear un incidente, asi que estamos seguros que siempre sera uno nuevo
    // el contains usa el "equals" de incidente modficado
    public void reportarAperturaIncidente(Incidente nuevoIncidente, Ciudadano miembroAvisante) {
        if(!this.incidentesOcurridos.contains(nuevoIncidente)) {
            this.agregarIncidente(nuevoIncidente);
        }
        if(nuevoIncidente.incidenteActual()) {
            this.notificarMiembros(nuevoIncidente, miembroAvisante, notificador);
            System.out.println("NOTIFICANDO MIEBROS DE COMUNIDAD: "+this.getNombre()+" Excepto al Miembro: "+miembroAvisante.getNombre());
        }
    }

    /*
    cualquier Ciudadano puede cerrar un incidente, osea desde la clase CIudadano haremos el cierre,
    esto avisara del cierre a todas las comunidades las cuales el ciudadano pertenesca, pero como es un Ciudadano
    que cerro el incidente por medio de una comunidad, peude pasar que en las comunidades del Ciudadano no este el
    mismo incidente que el cerro.
    */
    public void reportarCierreIncidente(Incidente incidente, Ciudadano miembroAvisante){

        Incidente incidentEncontrado = this.buscarIncidente(incidente);
        if (incidentEncontrado != null) {
            this.notificarMiembros(incidentEncontrado, miembroAvisante, notificador);
        }
        else {
            System.out.println("No se encontro este incidente en esta Comunida");
        }
    }

    public void agregarIncidente(Incidente incidente){
        this.incidentesOcurridos.add(incidente);
    }

    public void agregarMiembros(Ciudadano ciudadano){
        this.miembros.add(ciudadano);
    }

    public void notificarMiembros(Incidente incidente, Ciudadano miembroAvisante, Notificador notificador){
        for (Ciudadano unMiembro : this.miembros){
            if( !unMiembro.equals(miembroAvisante) ){
                notificador.notificarMiembroSegunSuForma(unMiembro, incidente, "Incidentes");
            }
        }
    }

    public Incidente buscarIncidente(Incidente incidente){
        for (Incidente unIncidente : this.incidentesOcurridos) {
            if (unIncidente.equals(incidente)) {
                return unIncidente;
            }
        }
        return null;
    }

    public List<Establecimiento> obtenerEstablecimientosObservados(){
        List<Establecimiento> establecimientos = new ArrayList<>();
        this.incidentesOcurridos.forEach(incidente -> establecimientos.add(incidente.getEstablecimiento()));
        return establecimientos;
    }

    public List<Servicio> obtenerServiciosObservados(){
        List<Servicio> servicios = new ArrayList<>();
        this.incidentesOcurridos.forEach(incidente -> servicios.add(incidente.getServicio()));
        return servicios;
    }

    public void agregarAdministradores (Ciudadano ... ciudadanos){
        this.administradores.addAll(List.of(ciudadanos));
    }



}
