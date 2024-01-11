package models.dominio.rankings;

import models.dominio.comunidad.Comunidad;
import models.dominio.entidades.Entidad;
import models.dominio.servicios.Incidente;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntidadesConMayorPromedioDeCierre implements Criterio{
    @Override
    public List <Tupla> generarRanking(List<Comunidad> comunidades ){
        List<Incidente> incidentes = new ArrayList<>();
        List<Tupla> tuplas = new ArrayList<>();

        comunidades.forEach(comunidad -> incidentes.addAll(comunidad.getIncidentesOcurridos().stream().filter(incidente -> Duration.between(incidente.getFechaApertura(),LocalDateTime.now()).toDays() < 7).toList()));
        for(Incidente incidente:incidentes){
            Entidad entidad = incidente.getEstablecimiento().getEntidad();

            if (tuplas.isEmpty()){
                Tupla tupla = new Tupla(entidad);
                tupla.agregarIncidente(incidente);
                tuplas.add(tupla);
            }
            else if (tuplas.stream().anyMatch(tupla -> entidad.getId().equals(tupla.getEntidad().getId()))){
                for (Tupla tupla : tuplas) {
                    if(tupla.entidad.getId().equals(entidad.getId())){
                        tupla.agregarIncidente(incidente);
                    }
                }
            }
            else
            {
                Tupla tupla = new Tupla(entidad);
                tupla.agregarIncidente(incidente);
                tuplas.add(tupla);
            }
        }
         List<Tupla> tuplasOrdenadas = tuplas.stream()
                .sorted(Comparator.comparingInt(Tupla::calcularPromedioMinutos))
                .collect(Collectors.toList());
            return tuplasOrdenadas;


    }



}
