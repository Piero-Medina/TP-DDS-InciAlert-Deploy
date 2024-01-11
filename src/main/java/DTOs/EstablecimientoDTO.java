package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.Establecimiento;
import models.dominio.servicios.PrestacionDeServicio;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstablecimientoDTO {

    private Long id;
    private String nombre;
    private EntidadDTO entidadDTO;
    private String descripcion;
    private List<PrestacionDeServicio> serviciosPrestados; // TODO - pronto
    private LocalizacionDTO localizacionDTO;
    private Boolean visible; // TODO -  pronto para la baja logica

    public void conversionDTO(Establecimiento establecimiento){
        setId(establecimiento.getId());
        setNombre(establecimiento.getNombre());

        if(!(establecimiento.getEntidad() == null)) {
            EntidadDTO entidadDTO1 = new EntidadDTO();
            entidadDTO1.conversionDTO(establecimiento.getEntidad());
            setEntidadDTO(entidadDTO1);
        }

        setDescripcion(establecimiento.getDescripcion());

        if(!(establecimiento.getServiciosPrestados() == null)) {
            setServiciosPrestados(establecimiento.getServiciosPrestados()); // TODO - pronto
        }

        // para que no tire error cuando quiere mostrar una localizacion
        LocalizacionDTO localizacionDTO1 = new LocalizacionDTO();
        if(!(establecimiento.getLocalizacion() == null)) {
            localizacionDTO1.conversionDTO(establecimiento.getLocalizacion());
        }
        setLocalizacionDTO(localizacionDTO1);
    }

    public String getLocalizacionDTONombre(){
        if(localizacionDTO.getNombre() != null) {
            return this.getLocalizacionDTO().getNombre();
        }
        return "Sin Localizacion";
    }

    public String deEntidad(){
        return this.entidadDTO.getNombre();
    }

}
