package DTOs;

import lombok.Getter;
import lombok.Setter;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.PrestacionDeServicio;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
public class PrestacionConIncidenteDTO {
    private PrestacionDeServicio prestacionDeServicio;
    private Incidente incidenteActivo;


    public PrestacionConIncidenteDTO(){

    }
    public void convertirDTO(PrestacionDeServicio prestacionDeServicio, Incidente incidenteActivo){
        this.prestacionDeServicio = prestacionDeServicio;
        this.incidenteActivo = incidenteActivo;
    }

    public String getNombreServicioPrestado(){
        return prestacionDeServicio.getNombreServicioPrestado();
    }
    public String informacionDeServicioPrestado(){
        return prestacionDeServicio.informacionDeServicioPrestado();
    }

    public boolean getEstadoDeServicioPrestado(){
        return this.prestacionDeServicio.getEstadoDeServicioPrestado();
    }

    public Long getId(){
        return prestacionDeServicio.getId();
    }

    public Long getIdEstablecimiento(){
        return this.prestacionDeServicio.getIdEstablecimiento();
    }

    public Long getIdIncidenteActivo(){

        if(this.incidenteActivo != null) {
            return this.incidenteActivo.getId();
        }
        return null;

    }




}
