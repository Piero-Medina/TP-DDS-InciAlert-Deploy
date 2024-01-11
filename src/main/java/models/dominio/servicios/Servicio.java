package models.dominio.servicios;


import models.dataBase.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "servicio")
@Getter
@Setter
public class Servicio extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "activo")
    private boolean activo;

    public Servicio(){
        // para hibernate
    }

}
