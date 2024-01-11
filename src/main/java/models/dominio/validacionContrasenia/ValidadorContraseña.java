package models.dominio.validacionContrasenia;

import models.dominio.actores.Usuario;
import models.dataBase.repositorios.UsuarioRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ValidadorContraseña {
    public CambiarContraseña cambiarContraseña;
    public ValidacionContraseña validacionContraseña;
    private UsuarioRepository usuarioRepository;
    public ValidadorContraseña(EntityManager em) {
        this.usuarioRepository = new UsuarioRepository();
    }
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
        EntityManager em = emf.createEntityManager();

        ValidadorContraseña validador = new ValidadorContraseña(em);
        boolean bandera = validador.validarInicio("matias","Matias123!");
        if(bandera){
            System.out.println("INGRESOOO CORRECTOOOOOOOOOOOO");
        }else {
            System.out.println("LE ERRASTEE GILLLL");
        }

        em.close();
        emf.close();

    }

    public boolean validarContraseña(String contra){
        return validacionContraseña.validar(contra);
    }

    public boolean cambioContraseña(String contra_nueva,String contra_actual){
        return cambiarContraseña.puedeCambiar(contra_nueva,contra_actual);
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
