package models.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import models.dominio.api.localizacion.Localizacion;
import models.dataBase.Persistente;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entidad")
@Getter
@Setter
public class Entidad extends Persistente {

    @Column(name = "nombre")
    public String nombre;

    @OneToMany(mappedBy = "entidad",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<Establecimiento> establecimientos;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "localizacion_id")
    public Localizacion localizacion;

    @Column(name = "activo")
    public boolean activo;

    public Entidad(){
        this.establecimientos = new ArrayList<>();
    }

    // para evitar recursividad
    public void agregarEstablecimiento (Establecimiento establecimiento){
        this.establecimientos.add(establecimiento);
        establecimiento.setearEntidad(this);
    }

}
