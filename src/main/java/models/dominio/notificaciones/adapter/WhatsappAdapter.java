package models.dominio.notificaciones.adapter;

import models.dominio.actores.Ciudadano;

public interface WhatsappAdapter {

    void enviar(Ciudadano destinatario, String mensajeAEnviar);
}
