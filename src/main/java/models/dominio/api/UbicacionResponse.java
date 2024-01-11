package models.dominio.api;

import models.dominio.api.mapeo.Municipio;
import models.dominio.api.mapeo.Provincia;

public class UbicacionResponse {
    private Municipio municipio;
    private Provincia provincia;

    public Municipio getMunicipio() {
        return municipio;
    }
    public Provincia getProvincia() {
        return provincia;
    }
}

