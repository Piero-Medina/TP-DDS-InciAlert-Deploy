package models.dominio.api.localizacion;


import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;

import javax.persistence.*;

@Entity
@Table(name = "localizacion")
@Getter
@Setter
public class Localizacion extends Persistente {

    @Column(name = "nombre",  columnDefinition = "VARCHAR(55)")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_localizacion")
    private TipoLocalizacion tipoLocalizacion;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    public Localizacion(){

    }

}
