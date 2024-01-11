package models.dataBase.converters;

import models.dominio.notificaciones.strategys.EstrategiaDeNotificacion;
import models.dominio.notificaciones.strategys.Mail;
import models.dominio.notificaciones.strategys.Whatsapp;

import javax.persistence.AttributeConverter;

public class MedioDeNotificacionConverter implements AttributeConverter<EstrategiaDeNotificacion, String> {

    private static final String mail = "Mail";
    private static final String whatsapp = "Whatsapp";

    @Override
    public String convertToDatabaseColumn(EstrategiaDeNotificacion estrategiaDeNotificacion) {

        if (estrategiaDeNotificacion == null) {
            return null;
        }

        String strategiaDeNotificacion = null;
        switch (estrategiaDeNotificacion.getClass().getSimpleName()){
            case  mail: strategiaDeNotificacion = "Mail"; break;
            case whatsapp: strategiaDeNotificacion = "Whatsapp"; break;
        }
        return strategiaDeNotificacion;
    }

    @Override
    public EstrategiaDeNotificacion convertToEntityAttribute(String s) {

        if (s == null) {
            return null;
        }

        EstrategiaDeNotificacion medioDeNotificacion = null;
        switch (s){
            case mail: medioDeNotificacion = new Mail(); break;
            case whatsapp: medioDeNotificacion = new Whatsapp(); break;
        }
        return medioDeNotificacion;
    }
}
