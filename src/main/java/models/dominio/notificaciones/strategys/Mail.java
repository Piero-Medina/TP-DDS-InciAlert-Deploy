package models.dominio.notificaciones.strategys;

import config.Configuracion;
import models.dominio.actores.Ciudadano;
import models.dominio.notificaciones.adapter.AdapterJavaxMail;
import models.dominio.notificaciones.adapter.MailAdapter;

public class Mail implements EstrategiaDeNotificacion{

    private MailAdapter adapter;

    public Mail(){
        this.adapter = new AdapterJavaxMail(Configuracion.getMailEmisor(), Configuracion.getContraseniaDeAplicacion());
    }

    @Override
    public void enviar(Ciudadano destinatario, String mensajeAEnviar) {
        this.adapter.enviar(destinatario,mensajeAEnviar);
    }
}
