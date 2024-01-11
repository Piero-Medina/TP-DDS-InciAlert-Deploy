package models.dominio.notificaciones.adapter;

import models.dominio.actores.Ciudadano;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AdapterJavaxMail implements MailAdapter{

    private final String username;
    private final String password;
    private final Session session;

    public AdapterJavaxMail(String usuario, String clave){
        this.username = usuario;
        this.password = clave;

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true"); //

        session = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void enviar(Ciudadano destinatario, String mensajeAEnviar) {
        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(destinatario.getMail())});
            mensaje.setSubject("Ejemplo de envío de correo electrónico"); // asunto
            mensaje.setText(mensajeAEnviar); // mensaje

            Transport.send(mensaje);

        } catch (AddressException e) {
            throw new RuntimeException("Error al procesar la dirección de correo electrónico: " + e);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico: " + e);
        }

    }
}
