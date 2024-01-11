package models.dominio.rankings;

import lombok.Getter;
import lombok.Setter;
import models.dominio.entidades.Entidad;
import models.dominio.servicios.Incidente;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Tupla {
    public Entidad entidad;
    public List<Incidente> incidentes;
    public int calcularPromedioMinutos(){
        long sumaLong = incidentes.stream()
                .mapToLong(incidente -> incidente.duracionMinutos())
                .sum();

        int suma = Math.toIntExact(sumaLong);
        return suma/ incidentes.size();
    }
    public Tupla(Entidad entidad){
        this.entidad = entidad;
        this.incidentes = new ArrayList<>();
    }
    public int cantidadIncidentes(){
      return incidentes.size();
    }
    public void agregarIncidente(Incidente incidente){
        incidentes.add(incidente);
    }
}
