package models.dominio.actores;

import models.dataBase.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    public String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRol tipo;

    @ManyToMany
    @JoinTable(
            name = "rol_x_permiso",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    public List<Permiso> permisos;

    public Rol(){
        this.permisos = new ArrayList<>();
    }

    public void agregarPermiso(Permiso permiso){
        this.permisos.add(permiso);
    }

    public void agregarPermisos(Permiso... permisos){
        this.permisos.addAll(Arrays.asList(permisos));
    }

    public boolean tenesPermiso(Permiso permiso) {
        return this.permisos.contains(permiso);
    }

    public boolean tenesPermiso(String nombreInterno) {
        return this.permisos.stream().anyMatch(p -> p.coincideConNombreInterno(nombreInterno));
    }
}
