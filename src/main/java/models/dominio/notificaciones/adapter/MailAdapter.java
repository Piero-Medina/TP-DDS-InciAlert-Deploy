package models.dominio.notificaciones.adapter;

import models.dominio.actores.Ciudadano;

public interface MailAdapter {

    void enviar(Ciudadano destinatario, String mensajeAEnviar);
}
