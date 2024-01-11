package models.dominio.actores;


import models.dataBase.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// desarrollodor
@Entity
@Table(name = "permiso")
@Getter
@Setter
public class Permiso extends Persistente {

    // este rol lo uso para mostrar en pantalla
    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    public String nombre;

    // este nombre nunca Varia - este nombre lo uso a nivel codigo
    @Column(name = "nombreInterno",columnDefinition = "VARCHAR(55)")
    private String nombreInterno;

    @Column(name = "descripcion", columnDefinition = "text")
    public String descripcion;

    public Permiso(){

    }
    public boolean coincideConNombreInterno(String nombre) {
        return this.nombreInterno.equals(nombre);
    }
}
