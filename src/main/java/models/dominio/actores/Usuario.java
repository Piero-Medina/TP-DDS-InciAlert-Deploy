package models.dominio.actores;

import models.dataBase.Persistente;
import models.dominio.validacionContrasenia.ValidadorContrasenia;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(55)")
    private String nombre;

    @Column(name = "contrasenia", columnDefinition = "VARCHAR(55)")
    private String contrasenia;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;

    public Usuario() {

    }

    public Usuario(String nombre, String contrasenia){
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    public boolean contraseniaValida(String contraseña , ValidadorContrasenia validadorContraseña){
        return validadorContraseña.validarContrasenia(contraseña);
    }

    public boolean cambiarContrasenia(String contra_nueva, ValidadorContrasenia validadorContraseña) {
       return validadorContraseña.cambioContraseña(contra_nueva, this.contrasenia);
    }



}
