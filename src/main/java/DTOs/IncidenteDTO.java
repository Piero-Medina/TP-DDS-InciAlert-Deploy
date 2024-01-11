package DTOs;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IncidenteDTO {

    private Long id;
    private String nombre;

    private String servicio;

    private String observaciones;

    private String fechaHora;

    private String establecimiento;


    public IncidenteDTO(Long id,String servicio, String observaciones, String fechaHora, String establecimiento) {
        this.id = id;
        this.servicio = servicio;
        this.observaciones = observaciones;
        this.fechaHora = fechaHora;
        this.establecimiento = establecimiento;
    }
}
