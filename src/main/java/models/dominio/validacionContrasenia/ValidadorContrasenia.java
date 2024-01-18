package models.dominio.validacionContrasenia;

import models.dominio.actores.Usuario;
import models.dataBase.repositorios.UsuarioRepository;

import javax.persistence.EntityManager;

public class ValidadorContrasenia {
    public CambiarContrasenia cambiarContrasenia;
    public ValidacionContrasenia validacionContraseña;
    private UsuarioRepository usuarioRepository;
    public ValidadorContrasenia(EntityManager em) {
        this.usuarioRepository = new UsuarioRepository();
    }
    public boolean validarContrasenia(String contra){
        return validacionContraseña.validar(contra);
    }

    public boolean cambioContraseña(String contra_nueva,String contra_actual){
        return cambiarContrasenia.puedeCambiar(contra_nueva,contra_actual);
    }
    public boolean validarInicio(String nombre ,String contra){
        Usuario usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return false;
        }
        else{
            String contra_guardada = usuario.getContrasenia();
            return contra_guardada.equals(contra);
        }

    }
}
