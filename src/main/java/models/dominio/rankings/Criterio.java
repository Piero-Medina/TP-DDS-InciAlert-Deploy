package models.dominio.rankings;

import models.dominio.comunidad.Comunidad;
import models.dominio.entidades.Entidad;

import java.time.LocalDateTime;
import java.util.List;

public interface Criterio {
    default List<Tupla> generarRanking(List<Comunidad> comunidades){return null;}
}
