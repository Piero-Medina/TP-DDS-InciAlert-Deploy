package models.dominio.api.localizacion;

import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ubicacion")
@Getter
@Setter
public class Ubicacion extends Persistente {

    @Column(name = "latitud")
    private double latitud ;

    @Column(name = "longitud")
    private double longitud;

}
