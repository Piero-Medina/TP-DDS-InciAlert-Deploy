package models.dominio.api;

import models.dominio.api.localizacion.Localizacion;
import models.dominio.api.localizacion.TipoLocalizacion;
import models.dominio.api.localizacion.Ubicacion;
import models.dominio.api.mapeo.Centroide;
import models.dominio.api.mapeo.Departamento;
import models.dominio.api.mapeo.Municipio;
import models.dominio.api.mapeo.Provincia;

public class LocalizacionMapper {

    public static Localizacion mapProvincia(Provincia provincia){
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre(provincia.getNombre());
        localizacion.setTipoLocalizacion(TipoLocalizacion.PROVINCIA);
        localizacion.setUbicacion(mapUbicacion(provincia.getCentroide()));
        return localizacion;
    }

    public static Localizacion mapMunicipio(Municipio municipio){
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre(municipio.getNombre());
        localizacion.setTipoLocalizacion(TipoLocalizacion.MUNICIPIO);
        localizacion.setUbicacion(mapUbicacion(municipio.getCentroide()));
        return localizacion;
    }

    public static Localizacion mapDepartamento(Departamento departamento){
        Localizacion localizacion = new Localizacion();
        localizacion.setNombre(departamento.getNombre());
        localizacion.setTipoLocalizacion(TipoLocalizacion.DEPARTAMENTO);
        localizacion.setUbicacion(mapUbicacion(departamento.getCentroide()));
        return localizacion;
    }

    public static Ubicacion mapUbicacion (Centroide centroide){
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(centroide.getLat());
        ubicacion.setLongitud(centroide.getLon());
        return ubicacion;
    }


}
