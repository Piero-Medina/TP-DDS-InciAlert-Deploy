package models.dominio.validacionContrasenia;

import org.passay.*;

public class ValidacionCaracteres {
    private final org.passay.PasswordValidator validator;

    public ValidacionCaracteres() {
        LengthRule lengthRule = new LengthRule(8, 30);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 1);
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 1);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 1);
        CharacterRule specialCharRule = new CharacterRule(EnglishCharacterData.Special, 1);
        validator = new org.passay.PasswordValidator(lengthRule, upperCaseRule, lowerCaseRule, digitRule, specialCharRule);
    }

    public String errores(String password) {
        String error ="";
        org.passay.RuleResult result = validator.validate(new org.passay.PasswordData(password));
        if (!result.isValid()) {
            //System.out.println("La contraseña es incorrecta, tiene los siguientes errores:");
            for (org.passay.RuleResultDetail detail : result.getDetails()) {
                if (!error.isEmpty()) {
                    error += "\n";
                }
                error += tiposErrores(detail.getErrorCode());
            }
        }
        return error;
    }
    public boolean validar(String password) {
        org.passay.RuleResult result = validator.validate(new org.passay.PasswordData(password));
        return result.isValid();
    }
    private String tiposErrores(String error){
        String conversion = "";
        switch (error){
            case "TOO_SHORT":
                conversion = "Cantidad de caracteres insuficientes.Debe tener entre 8 y 30.";
                break;
            case "INSUFFICIENT_SPECIAL" :
                conversion = "Falta un caracter especial (Ej: #,$,% etc).";
                break;
            case "INSUFFICIENT_UPPERCASE" :
                conversion= "Falta una mayuscula.";
                break;
            case "INSUFFICIENT_LOWERCASE" :
                conversion = "Falta una minuscula.";
                break;
            case "INSUFFICIENT_DIGIT":
                conversion = "Falta un numero.";
                break;
            default:
                conversion = "Error desconocido.";
                break;
        }
        return conversion;
    }
}
    /*public boolean validar(String password) {
        org.passay.RuleResult result = validator.validate(new org.passay.PasswordData(password));
        return result.isValid();
    }
}

     */
/*
AGREGAR EN EL pom.xml
<dependencies>
        <dependency>
            <groupId>org.passay</groupId>
            <artifactId>passay</artifactId>
            <version>1.6.0</version>
        </dependency>
    </dependencies>
 */



