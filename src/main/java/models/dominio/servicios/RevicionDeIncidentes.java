package models.dominio.servicios;

import models.dominio.actores.Ciudadano;
import models.dominio.api.localizacion.Ubicacion;
import models.dominio.notificaciones.Notificador;

import java.util.List;

public class RevicionDeIncidentes {
    public List<Incidente> incidentes;

    public void EnviarSugerencia(Ciudadano ciudadano, Notificador notificador){

    }
    public boolean CalcularProximidad(Ubicacion ubicacion1, Ubicacion ubicacion2){
        return true;
    }
}
