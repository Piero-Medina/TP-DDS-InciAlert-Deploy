package models.dominio.lectorCSV;

import models.dominio.actores.Propietario;

import java.io.IOException;

public interface LectorCSVAdapter {

    public void procesarArchivoCSV(Propietario propietario, String path) throws IOException;

}
