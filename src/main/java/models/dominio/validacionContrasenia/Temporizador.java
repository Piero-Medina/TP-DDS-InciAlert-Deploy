package models.dominio.validacionContrasenia;
import java.util.Scanner;

public class Temporizador {
    private static final String contra = "Matias1";
    private static final int intentos_max = 3;
    private int tiempo_espera = 30;

    public void Usar() throws InterruptedException{
        Scanner scanner = new Scanner(System.in);
        int intentos = 0;

        while (intentos < intentos_max) {
            System.out.print("Ingresa la contraseña: ");
            String contra_ingresada = scanner.nextLine();

            if (contra_ingresada.equals(contra)) {
                System.out.println("¡Contraseña correcta!");
                break;
            } else {
                intentos++;
                System.out.println("Contraseña incorrecta. Intento " + intentos + " de " + intentos_max + ".");
                if (intentos == intentos_max) {
                    System.out.println("Demasiados intentos fallidos. Espera " + tiempo_espera + " segundos.");
                    Thread.sleep(tiempo_espera * 1000);
                    tiempo_espera =  tiempo_espera* 2;
                    intentos=0;
                }
            }
        }
        scanner.close();
    }
}