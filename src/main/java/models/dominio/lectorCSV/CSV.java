package models.dominio.lectorCSV;

import models.dominio.actores.Propietario;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.Establecimiento;
import models.dominio.entidades.OrganismoDeControl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSV implements LectorCSVAdapter {

    public CSV() {

    }
    public List<String> lectorDeCSV(String path) throws IOException {
        FileInputStream archivoDeEntrada = null;
        InputStreamReader lectorDeCaracteres = null;
        BufferedReader lectorDeLineas = null;
        List<String> lineas = null;
        try {
            archivoDeEntrada = new FileInputStream(path);
            lectorDeCaracteres = new InputStreamReader(archivoDeEntrada);
            lectorDeLineas = new BufferedReader(lectorDeCaracteres);
            lineas = new ArrayList<>();

            String linea;
            boolean primeraLinea = true;
            while ((linea = lectorDeLineas.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                lineas.add(linea);
            }
        } catch (Exception e) {
            System.out.println(" no se pudo leer o econtrar el archivo ");
        } finally {
            if (lectorDeLineas != null) {
                lectorDeLineas.close();
            }
            if (lectorDeCaracteres != null) {
                lectorDeCaracteres.close();
            }
            if (archivoDeEntrada != null) {
                archivoDeEntrada.close();
            }
        }
        return lineas;
    }


    // por el momento solo imprime por pantalla
    public void procesarDatos(String[] lineaCSV) {
        for (String dato : lineaCSV) {
            System.out.print(dato + " ");
        }
        System.out.println();

        // empieza en 1, tambien la consulta de listas
        System.out.println(lineaCSV.length);
    }

    public void Mapeo(Propietario propietario, String[] lineaCSV, OrganismoDeControl organismoDeControl, EntidadPrestadora entidadPrestadora) {
        String tipo = lineaCSV[0];
        String nombre = lineaCSV[1];
        String padre = lineaCSV[2];

        switch (tipo) {
            case "OrganismoDeControl":
                if (Objects.equals(padre, "Sin Padre")) {
                    System.out.println("propietario Organismo De Control");
                    organismoDeControl.setNombre(nombre);
                    organismoDeControl.setActivo(true);
                    propietario.agregarOrganismoDeControl(organismoDeControl);
                    System.out.println("-se agrego organismo " + nombre);
                }
                break;
            case "EntidadPrestadora":
                if (Objects.equals(padre, "Sin Padre")) {
                    System.out.println("propietario Entidad Prestadora");
                    entidadPrestadora.setNombre(nombre);
                    entidadPrestadora.setActivo(true);
                    propietario.agregarEntidadPrestadora(entidadPrestadora);
                    System.out.println("-se agrego entidadPrestadora " + nombre);
                } else {
                    System.out.println("propietario Organismo De Control");
                    EntidadPrestadora entidadPrestadora1 = new EntidadPrestadora();
                    entidadPrestadora1.setNombre(nombre);
                    entidadPrestadora1.setActivo(true);
                    propietario.getOrganismosDeControl().get(0).agregarEntidadPrestadora(entidadPrestadora1);
                    System.out.println("-se agrego entidadPrestadora " + nombre);
                }
                break;
            case "Entidad":
                Entidad entidad = new Entidad();
                entidad.setNombre(nombre);
                entidad.setActivo(true);

                if(!propietario.getOrganismosDeControl().isEmpty()){
                    System.out.println("propietario Organismo De control");
                    for (EntidadPrestadora entidadPrestadora1 : propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras()) {
                        if (entidadPrestadora1.getNombre().equals(padre)){
                            entidadPrestadora1.agregarEntidad(entidad);
                            System.out.println("-se agrego entidad: " + nombre + " con padre: " + padre);
                        }
                    }
                }
                else {
                    System.out.println("propietario Entidad Prestadora");
                    for (EntidadPrestadora entidadPrestadora1 : propietario.getEntidadesPrestadoras()) {
                        if (entidadPrestadora1.getNombre().equals(padre)) {
                            entidadPrestadora1.agregarEntidad(entidad);
                            System.out.println("-se agrego entidad: " + nombre + " con padre: " + padre);
                        }
                    }
                }
                break;
            case "Establecimiento":
                Establecimiento establecimiento = new Establecimiento();
                establecimiento.setNombre(nombre);
                establecimiento.setActivo(true);

                if(!propietario.getOrganismosDeControl().isEmpty()) {
                    System.out.println("propietario Organismo De Control");
                    for (EntidadPrestadora entidadPrestadora1 : propietario.getOrganismosDeControl().get(0).getEntidadesPrestadoras()) {
                        for(Entidad entidad1 : entidadPrestadora1.getEntidades()) {
                            if (entidad1.getNombre().equals(padre)) {
                                entidad1.agregarEstablecimiento(establecimiento);
                                System.out.println("-se agrego establecimiento " + nombre + " con padre: " + padre);
                            }
                        }
                    }
                }
                else {
                    System.out.println("propietario Entidad Prestadora");
                    for(Entidad entidad1 : propietario.getEntidadesPrestadoras().get(0).getEntidades()){
                        if(entidad1.getNombre().equals(padre)){
                            entidad1.agregarEstablecimiento(establecimiento);
                            System.out.println("-se agrego establecimiento " + nombre + " con padre: " + padre);
                        }
                    }
                }
                break;
            default:
                System.out.println("Tipo de entidad no reconocido: " + tipo);
                break;
        }
    }

    public void imprimirLineas(List<String> lista) {
        for (String elemento : lista) {
            System.out.println(elemento);
        }
    }

    public void mapearDatos(List<String> lista, Propietario propietario){
        OrganismoDeControl organismoDeControl = new OrganismoDeControl();
        EntidadPrestadora entidadPrestadora = new EntidadPrestadora();
        for (String linea : lista) {
            String[] datos = linea.split(",");
            this.Mapeo(propietario, datos, organismoDeControl, entidadPrestadora);
        }
    }


    @Override
    public void procesarArchivoCSV(Propietario propietario, String path) throws IOException {
        List<String> lineas = this.lectorDeCSV(path);
        this.mapearDatos(lineas,propietario);
    }


}
