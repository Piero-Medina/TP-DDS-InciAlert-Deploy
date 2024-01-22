package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {

    // lee el .properties que esta en resources
    private static final String CONFIG_FILE = "config\\local.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = Configuracion.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo de configuración: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMailEmisor() {
        return properties.getProperty("ADDRESS_EMAIL");
    }

    public static String getContraseniaDeAplicacion() {
        return properties.getProperty("APPLICATION_PASSWORD");
    }

    public static String getTwilioSid(){
        return properties.getProperty("TWILIO_ACCOUNT_SID");
    }

    public static String getTwilioToken(){
        return properties.getProperty("TWILIO_AUTH_TOKEN");
    }

    public static String getTwilioNumber(){
        return properties.getProperty("TWILIO_PHONE_NUMBER");
    }

}
