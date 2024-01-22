package lectorCSV;

import models.dominio.actores.Propietario;
import models.dominio.lectorCSV.CSV;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class LectorCSVTest {

    @Test
    public void testLectorCSV() throws IOException {
        CSV lector = new CSV();

        Propietario propietario = new Propietario();

        // ODC
        List<String> lineas = lector.lectorDeCSV("src\\main\\java\\models\\dominio\\archivos\\archivoPropietarioODC.csv");
        // EP
        //List<String> lineas = lector.lectorDeCSV("src\\main\\java\\models\\dominio\\archivos\\archivoPropietarioEP.csv");

        lector.imprimirLineas(lineas);

        lector.mapearDatos(lineas,propietario);
        System.out.println("datos Propietario");
        System.out.println(propietario.getOrganismosDeControl().get(0).getNombre());
        System.out.println(propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras().get(0).getNombre());
        System.out.println(propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras().get(0).getEntidades().get(0).getNombre());
        System.out.println(propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras().get(0).getEntidades().get(0).getEstablecimientos().get(0).getNombre());

    }
}
