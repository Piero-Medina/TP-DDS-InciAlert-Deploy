package models.dominio.validacionContrasenia;

public class CambiarContraseña {
    ValidacionContraseña validacion = new ValidacionContraseña();
    public boolean puedeCambiar(String contra_nueva, String contra_actual) {

        if(validacion.validar(contra_nueva)  && !this.sonIguales(contra_nueva,contra_actual)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean sonIguales(String contra_nueva, String contra_actual){
        if(contra_nueva.equals(contra_actual)) {
            //System.out.println("* Las contraseña nueva es igual a la actual.Tienen que ser diferentes");
            return true;
        }
        else{
            return false;
        }
    }

    /*private boolean esUnaContraAntigua(String contra_nueva, ArrayList<String> contra_antiguas){
        if(contra_antiguas.contains(contra_nueva)){
            //System.out.println("* Las contraseña nueva es igual a una contraseña anterior.No se puede repetir");
            return true;
        }else {
            return false;
        }
    }*/
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