package DTOs;

import lombok.*;
import models.dominio.actores.Ciudadano;
import models.dominio.comunidad.Comunidad;
import models.dominio.servicios.Incidente;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor // contructor con todos los argumento (NO INICIALIZA NADA - OSEA LAS LISTAS NO LAS INCIALIZA)
@NoArgsConstructor // contructor sin los argumento (NO INICIALIZA NADA - OSEA LAS LISTAS NO LAS INCIALIZA)
// @Builder - podemos usar el patron builder cuando intanciemos una entida DTO
public class ComunidadDTO {
    private Long id;
    private String nombre;
    private int cantDeMiembros;
    private List<Incidente> incidentesOcurridos;
    private List<Ciudadano> miembros;
    private String descripcion;
    private List<Ciudadano> administradores;

    public void convertirADTO (Comunidad comunidad){
        setId(comunidad.getId());
        setNombre(comunidad.getNombre());
        setCantDeMiembros(this.cantidadDeMiembros(comunidad));
        setMiembros(comunidad.getMiembros());
        setIncidentesOcurridos(comunidad.getIncidentesOcurridos());
        setDescripcion(comunidad.getDescripcion());
        setAdministradores(comunidad.getAdministradores());
    }

    // todo lombok inicializara todo, asi que una lista jamas sera null, ya que estara inicializada

    /*
    public int cantidadDeMiembros(Comunidad comunidad){
        if(!(comunidad.getMiembros() == null)){
            return comunidad.getMiembros().size();
        }
        return 0;
    }

    public String administradoresToString(){
        StringBuilder cadena = new StringBuilder();
        if(!(this.getAdministradores() == null)) {
            for (Ciudadano administrador : this.getAdministradores()) {
                cadena.append(administrador.getNombre());
            }
            return cadena.toString();
        }
        return "Sin Administradores";
    }
    */

    public int cantidadDeMiembros(Comunidad comunidad){
            return comunidad.getMiembros().size();
    }

    public String administradoresToString(){
        StringBuilder cadena = new StringBuilder();
        if(!(this.getAdministradores().isEmpty())) {
            for (Ciudadano administrador : this.getAdministradores()) {
                cadena.append(administrador.getNombre());
            }
            return cadena.toString();
        }
        return "Sin Administradores";
    }

    // usado en show-comunidad
    public String getDescripcionLimitada() {
        int limite = 130;
        if (this.getDescripcion() != null && this.getDescripcion().length() > limite) {
            return this.getDescripcion().substring(0, limite) + "...";
        } else {
            return this.getDescripcion();
        }
    }




}
