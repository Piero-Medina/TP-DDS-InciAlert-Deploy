package DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.dominio.api.localizacion.Localizacion;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LocalizacionDTO {

    private String nombre;
    private String tipoDeLocalizacion;
    private Double latitud;
    private Double longitud;


    public void conversionDTO(Localizacion localizacion){
        setNombre(localizacion.getNombre());
        setTipoDeLocalizacion(localizacion.getTipoLocalizacion().name());
        setLatitud(localizacion.getUbicacion().getLatitud());
        setLongitud(localizacion.getUbicacion().getLongitud());
    }
}
