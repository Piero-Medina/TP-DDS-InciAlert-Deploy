package models.dominio.servicios;

import models.dataBase.Persistente;
import models.dominio.entidades.Establecimiento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "prestacion_de_servicio")
@Getter
@Setter
public class PrestacionDeServicio extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    private String nombreServicioPrestado;

    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicioPrestado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
    private Establecimiento establecimiento;

    @Column(name = "estado", columnDefinition = "Boolean")
    private Boolean estadoDeServicioPrestado;

    // baja Logica
    @Column(name = "activo")
    private boolean activo;

    public PrestacionDeServicio(){
        // para Hibernate
    }

    // test
    public PrestacionDeServicio(String nombreServicioPrestado, Establecimiento establecimiento){
        this.nombreServicioPrestado = nombreServicioPrestado;
        this.establecimiento = establecimiento;
    }

    public PrestacionDeServicio(String nombreServicioPrestado, Servicio servicioPrestado, Establecimiento establecimiento) {
        this.nombreServicioPrestado = nombreServicioPrestado;
        this.servicioPrestado = servicioPrestado;
        this.establecimiento = establecimiento;
    }

    public void prestarServicio(){
        //todo
    }

    public String deEstablecimiento(){
        return this.establecimiento.getNombre();
    }

    // LUEGO VER SI ESTE METODO ESTA SIENDO USADO EN ALGNUA VISTA POR EL MOTOR
    public String informacionDeServicioPrestado(){
        return (this.nombreServicioPrestado + " de " + this.deEstablecimiento());
    }

    // USADO PARA CONTRUIR EL MENSAJE DE LA NOTIFICACION
    public String informacionDeServicioPrestadoMensaje(){
        return (this.nombreServicioPrestado + " de ESTABLECIMIENTO " + this.deEstablecimiento());
    }

    // es llamado por el Incidente al ser creado para avisar que el estado del servicio es inactivo
    public void servicioConIncidentes(){
        this.estadoDeServicioPrestado = false;
    }

    public void reestablecerServicio(){
        this.estadoDeServicioPrestado = true;
    }

    public String nombreDelServicioPrestado(){
        return getServicioPrestado().getNombre();
    }

    public void cambiarDeEstado(){
        this.estadoDeServicioPrestado = !this.estadoDeServicioPrestado;
    }

    // para ver los detalles de una prestacionDeServicio en Ciudadano y no cambiar de ruta
    public Long getIdEstablecimiento(){
        return this.establecimiento.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PrestacionDeServicio other = (PrestacionDeServicio) obj;
        Long id = this.getId();
        return ( id != null ) && ( id.equals(other.getId()) );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
