package models.dominio.validacionContrasenia;

public class ValidacionContraseña {
    private final ValidacionCaracteres validacion = new ValidacionCaracteres();
    private final PeoresContras peores_contras = new PeoresContras();
    public boolean validar(String contra_nueva) {
        return validacion.validar(contra_nueva); //&& !peores_contras.estaContenida(contra_nueva);
    }
    public String errores(String contra){
        String error="";
        error = validacion.errores(contra);
        error += "\n";
        //error += peores_contras.error(contra);
        return error;
    }
}
