import models.dominio.validacionContrasenia.ValidadorContrasenia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidadorTest {

    @Test
    public void contreaseniaValida() {
        ValidadorContrasenia validacion = new ValidadorContrasenia();

        String contra = "Matias1?";

        Assertions.assertTrue(validacion.validarContrasenia(contra));
    }

    @Test
    public void contreaseniaInvalida() {
        ValidadorContrasenia validacion = new ValidadorContrasenia();

        String contra = "1234";

        Assertions.assertFalse(validacion.validarContrasenia(contra));
    }

}




