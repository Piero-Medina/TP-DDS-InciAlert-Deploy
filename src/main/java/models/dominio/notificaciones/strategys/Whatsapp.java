package models.dominio.notificaciones.strategys;

import config.Configuracion;
import models.dominio.actores.Ciudadano;
import models.dominio.notificaciones.adapter.AdapterTwilio;
import models.dominio.notificaciones.adapter.WhatsappAdapter;

public class Whatsapp implements EstrategiaDeNotificacion{

    private WhatsappAdapter adapter;

    public Whatsapp(){
        this.adapter = new AdapterTwilio(Configuracion.getTwilioSid(), Configuracion.getTwilioToken(), Configuracion.getTwilioNumber());
    }

    @Override
    public void enviar(Ciudadano destinatario, String mensajeAEnviar) {
        this.adapter.enviar(destinatario, mensajeAEnviar);
    }
}
