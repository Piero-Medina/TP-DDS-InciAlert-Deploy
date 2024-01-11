package models.dominio.actores;

import lombok.Getter;
import lombok.Setter;
import models.dataBase.Persistente;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.OrganismoDeControl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propietario")
@Getter
@Setter
public class Propietario extends Persistente {

    @Column(name = "nombre",  columnDefinition = "VARCHAR(55)")
    private String nombre;

    @Column(name = "apellido",  columnDefinition = "VARCHAR(55)")
    private String apellido;

    @Column(name = "mail",  columnDefinition = "VARCHAR(55)")
    private String mail;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoDePropietario")
    private TipoPropietario tipoPropietario;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "propietario_id")
    private List<EntidadPrestadora> entidadesPrestadoras;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "propietario_id")
    private List<OrganismoDeControl> organismosDeControl;


    public Propietario(){
        this.organismosDeControl = new ArrayList<>();
        this.entidadesPrestadoras = new ArrayList<>();
    }

    public void agregarEntidadPrestadora(EntidadPrestadora entidadPrestadora){
        this.entidadesPrestadoras.add(entidadPrestadora);
    }

    public void agregarOrganismoDeControl(OrganismoDeControl organismoDeControl){
        this.organismosDeControl.add(organismoDeControl);
    }


}
