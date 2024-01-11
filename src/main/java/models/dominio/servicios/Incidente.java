package models.dominio.servicios;

import models.dataBase.Persistente;
import models.dominio.actores.Ciudadano;
import models.dominio.entidades.Establecimiento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "incidente")

@Getter
@Setter
public class Incidente extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    private String nombreIncidente;

    // bidireccional
    @ManyToOne
    @JoinColumn(name = "ciudadano_id")
    private Ciudadano ciudadanoQueReporto;

    @ManyToOne
    @JoinColumn(name = "prestacionDeServicio_id",referencedColumnName = "id")
    private PrestacionDeServicio prestacionDeServicioIncidente;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    //postGress
    @Column(name = "fechaApertura", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaApertura;

    @Column(name = "fechaCierre", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime fechaCierre;


    /* My SQL
    @Column(name = "fechaApertura", columnDefinition = "DATETIME")
    private LocalDateTime fechaApertura;

    @Column(name = "fechaCierre", columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime fechaCierre;
     */

    @Column(name = "estadoIncidente", columnDefinition = "BOOLEAN")
    private Boolean estadoIncidente;

    @Column(name = "activo")
    private boolean activo;

    public Incidente(LocalDateTime fechaApertura, LocalDateTime fechaCierre, boolean estadoIncidente){
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.estadoIncidente = estadoIncidente;
    }

    public Incidente(){
        this.fechaApertura = null;
        this.fechaCierre = null;
    }

    public Incidente(String nombreincidente,
                     PrestacionDeServicio prestacionDeServicioIncidente,
                     String observaciones,
                     LocalTime horarioIncidente){
        this.nombreIncidente = nombreincidente;
        this.prestacionDeServicioIncidente = prestacionDeServicioIncidente;
        this.observaciones = observaciones;
        this.fechaApertura = LocalDateTime.now();
        this.fechaCierre = null;
    }

    public void abrirIncidente(){
        this.estadoIncidente = true;
        this.prestacionDeServicioIncidente.servicioConIncidentes();
    }

    //
    public void cerrarIncidente(){
        this.estadoIncidente = false;
        this.prestacionDeServicioIncidente.reestablecerServicio();
        this.fechaCierre = LocalDateTime.now();
    }


    //devuelve true si el incidente ha estado abierto por menos de 24 horas desde su apertura y false si ha estado abierto durante 24 horas o más.
    public Boolean incidenteActual(){
        return Duration.between( fechaApertura, LocalDateTime.now() ).toHours() < 24;
    }

    public String fechaHora (){
        return fechaApertura.toLocalDate().toString() + " - "+ fechaApertura.toLocalTime().toString();
    }
    public long duracionMinutos(){
        return Duration.between(fechaApertura,fechaCierre).toMinutes();
    }
    public String estadoIncidente (){
        if (this.estadoIncidente){
            return "Con Incidentes";
        }
        else {
            return "tuvo Incidentes pero fue Reestablecido";
        }
    }

    public Servicio getServicio(){
        return this.prestacionDeServicioIncidente.getServicioPrestado();
    }
    public Establecimiento getEstablecimiento(){
        return this.prestacionDeServicioIncidente.getEstablecimiento();
    }

    // Implementación personalizada del método equals, compara por ID
    // echa por las dudas
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Incidente other = (Incidente) obj;
        Long id = this.getId();
        return ( id != null ) && ( id.equals(other.getId()) );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public Long idDeLaPrestacion(){
        return this.prestacionDeServicioIncidente.getId();
    }

    public String soloFechaDeApertura(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.getFechaApertura().format(dateFormatter);
    }

    public String soloHoraDeApertura(){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return this.getFechaApertura().format(timeFormatter);
    }


}
