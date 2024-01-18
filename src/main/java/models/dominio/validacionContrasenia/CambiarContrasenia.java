package models.dominio.validacionContrasenia;

public class CambiarContrasenia {
    ValidacionContrasenia validacion = new ValidacionContrasenia();

    public boolean puedeCambiar(String contra_nueva, String contra_actual) {
        return validacion.validar(contra_nueva) && !this.sonIguales(contra_nueva, contra_actual);
    }

    private boolean sonIguales(String contra_nueva, String contra_actual){
        //System.out.println("* Las contraseña nueva es igual a la actual.Tienen que ser diferentes");
        return contra_nueva.equals(contra_actual);
    }
    public String errores(String contra_nueva, String contra_actual){
        String error="";
        if(this.sonIguales(contra_nueva,contra_actual)){
            error = "Las contraseña nueva es igual a la actual.Tienen que ser diferentes";
        }
        error += "\n";
        error += validacion.errores(contra_nueva);
        return error;
    }
}