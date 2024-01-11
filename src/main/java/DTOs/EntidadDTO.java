package DTOs;

import lombok.*;
import models.dominio.entidades.Entidad;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntidadDTO {

    private Long id;
    private String nombre;
    private LocalizacionDTO localizacionDTO;

    // TODO - para la baja logica
    private Boolean visible;

    public void conversionDTO(Entidad entidad){
        setId(entidad.getId());
        setNombre(entidad.getNombre());

        // para que no tire error cuando quiere mostrar una localizacion
        LocalizacionDTO localizacionDTO1 = new LocalizacionDTO();
        if(entidad.getLocalizacion() != null) {
            localizacionDTO1.conversionDTO(entidad.getLocalizacion());
        }
        setLocalizacionDTO(localizacionDTO1);
    }

    public String getLocalizacionDTONombre(){
        if(localizacionDTO.getNombre() != null) {
            return this.getLocalizacionDTO().getNombre();
        }
        return "Sin Localizacion";
    }

}
