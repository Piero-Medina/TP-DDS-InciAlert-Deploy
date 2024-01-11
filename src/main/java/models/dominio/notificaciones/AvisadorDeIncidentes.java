package models.dominio.notificaciones;

import lombok.Getter;
import lombok.Setter;
import models.dominio.actores.Ciudadano;
import models.dominio.servicios.Incidente;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class AvisadorDeIncidentes {
    public List<Ciudadano> ciudadanos;
    public Notificador notificador;

    public AvisadorDeIncidentes(Notificador notificador) {
        this.ciudadanos = new ArrayList<>();
        this.notificador = notificador;
    }

    public void notificarIncidentes(Ciudadano ciudadanoQueReporto,Incidente incidente){
        for (Ciudadano ciudadano : this.ciudadanos) {
            if(ciudadano != ciudadanoQueReporto && tieneLaPrestacionDeServicioAfectadaComoInteres(ciudadano, incidente)){
                notificador.notificarMiembroSegunSuForma(ciudadano, incidente, "Incidentes en tus Servicios De Interes");
            }
        }
    }

    public boolean tieneLaPrestacionDeServicioAfectadaComoInteres(Ciudadano ciudadano,Incidente incidente){
        return ciudadano.getInteres().contains(incidente.getPrestacionDeServicioIncidente());
    }

    public boolean tieneLaPrestacionDeServicioAfectadaComoInteres2(Ciudadano ciudadano,Incidente incidente){
        return ciudadano.getInteres().stream().anyMatch(p -> p.getId().equals(incidente.getPrestacionDeServicioIncidente().getId()));
    }

}
