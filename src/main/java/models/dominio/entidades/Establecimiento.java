package models.dominio.entidades;


import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;
import models.dominio.api.localizacion.Localizacion;
import models.dominio.servicios.PrestacionDeServicio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "establecimiento")
@Getter
@Setter
public class Establecimiento extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    public String nombre;

    @ManyToOne
    @JoinColumn(name ="entidad_id", referencedColumnName = "id")
    public Entidad entidad;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    public String descripcion;

    // BIDIRECCIONAL
    @OneToMany(mappedBy = "establecimiento", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<PrestacionDeServicio> serviciosPrestados;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "localizacion_id")
    private Localizacion localizacion;

    @Column(name = "activo")
    public boolean activo;

    @Transient
    private Double coordenadax;

    @Transient
    private Double coordenaday;


    public Establecimiento (){
        this.serviciosPrestados = new ArrayList<>();
    }


    public static double getCoordenadaX() {
        return 0;
    }

    public static double getCoordenaY() {
        return 0;
    }

    public static Establecimiento encontrarCoordenadaMasCercana(List<Establecimiento> establecimientos, double x, double y) {
        Establecimiento establecimientoMasCercano = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Establecimiento establecimiento : establecimientos) {
            double x2= Establecimiento.getCoordenadaX();
            double y2 = Establecimiento.getCoordenaY();
            double distancia = calcularDistancia(x, y,x2,y2);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                establecimientoMasCercano = establecimiento;
            }
        }
        return establecimientoMasCercano;
    }
    public static double calcularDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // para evitar recursividad BIDIRECCIONAL
    public void agregarServiciosPrestados(PrestacionDeServicio prestacionDeServicio){
        this.serviciosPrestados.add(prestacionDeServicio);
        prestacionDeServicio.setEstablecimiento(this);
    }

    public void setearEntidad(Entidad entidad){
        this.entidad = entidad;
    }

    public String deEntidad(){
        return this.entidad.getNombre();
    }

}
