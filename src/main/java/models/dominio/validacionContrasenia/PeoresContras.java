package models.dominio.validacionContrasenia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PeoresContras {
    private static final String archivo_contras = "C:\\Users\\Usuario\\Documents\\peores_contras.txt";

    private String[] leerPalabras() {
        String[] contras = new String[10000];
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo_contras));
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) {
                contras[i] = linea;
                i++;
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error de lectura del archivo: " + e.getMessage());
        }
        return contras;
    }

    public boolean estaContenida(String contra) {
        String[] lista = this.leerPalabras();
        boolean bandera = false;
        for (String principio : lista) {
            if (principio.contains(contra)) {
                //System.out.println("* La contraseña insegura.Esta en una lista de las peores contraseñas");
                bandera = true;
            }
        }
        return bandera;
    }
    public String error(String contra){
        String error = "";
        if(estaContenida(contra)){
            error = "La contraseña insegura.Esta en una lista de las peores contraseñas";
        }
        return error;
    }
}