package models.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organismo_de_control")
@Getter
@Setter
public class OrganismoDeControl extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "organismo_control_id")
    private List<EntidadPrestadora> entidadesPrestadoras;

    @Column(name = "activo")
    private boolean activo;

    public OrganismoDeControl(){
        this.entidadesPrestadoras = new ArrayList<>();
    }

    public void agregarEntidadPrestadora(EntidadPrestadora entidadPrestadora){
        this.entidadesPrestadoras.add(entidadPrestadora);
    }

}