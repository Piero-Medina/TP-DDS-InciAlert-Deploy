package models.dominio.notificaciones.strategys;

import models.dominio.actores.Ciudadano;

public interface EstrategiaDeNotificacion {

    void enviar(Ciudadano destinatario, String mensajeAEnviar);
}
