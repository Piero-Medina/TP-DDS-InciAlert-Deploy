package models.dominio.api;
import models.dominio.api.mapeo.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //new Main().testMatias();
        //new Main().testCentroide();
        //new Main().testMunicipiosPorNombreDeProvincia();
        //new Main().testDepartamentosPorNombreDeProvincia();
        //new Main().testDepartamentosPorIDDeProvincia();
        new Main().testMunicipiosPorIDDeProvincia();

    }
    private void testCentroide() throws IOException {
        GeorefApi servicio = GeorefApi.instancia();

        ListaProvincias listaDeProvincias = servicio.listadoDeProvincias();

        for(Provincia provincia : listaDeProvincias.getProvincias()){
            System.out.println( "ID: " + provincia.getId() + " PROVINCIA -> " + provincia.getNombre());
            System.out.println("UBICACION -> LAT: " + provincia.getCentroide().getLat() + " LON: " + provincia.getCentroide().getLon() );
        }
    }

    private void testDepartamentosPorNombreDeProvincia() throws IOException {
        GeorefApi servicio = GeorefApi.instancia();

        ListaDepartamentos listaDepartamentos = servicio.listadoDeDepartamentosDeProvincia("jujuy");

        for(Departamento departamento : listaDepartamentos.getDepartamentos()){
            System.out.println( "ID: " + departamento.getId() + " DEPARTAMENTO -> " + departamento.getNombre());
            System.out.println("UBICACION -> LAT: " + departamento.getCentroide().getLat() + " LON: " + departamento.getCentroide().getLon() );
        }
    }

    private void testDepartamentosPorIDDeProvincia() throws IOException{
        GeorefApi servicio = GeorefApi.instancia();

        ListaDepartamentos listaDepartamentos = servicio.listadoDeDepartamentosPorIDProvincia(2);

        for(Departamento departamento : listaDepartamentos.getDepartamentos()){
            System.out.println( "ID: " + departamento.getId() + " DEPARTAMENTO -> " + departamento.getNombre());
            System.out.println("UBICACION -> LAT: " + departamento.getCentroide().getLat() + " LON: " + departamento.getCentroide().getLon() );
        }
    }

    private void testMunicipiosPorNombreDeProvincia() throws IOException {
        GeorefApi servicio = GeorefApi.instancia();

        ListaMunicipios listaMunicipios = servicio.listadoDeMunicipiosDeProvincia("jujuy");

        for(Municipio municipio : listaMunicipios.getMunicipios()){
            System.out.println( "ID: " + municipio.getId() + " MUNICIPIO -> " + municipio.getNombre());
            System.out.println("UBICACION -> LAT: " + municipio.getCentroide().getLat() + " LON: " + municipio.getCentroide().getLon() );
        }
    }

    private void testMunicipiosPorIDDeProvincia() throws IOException {
        GeorefApi servicio = GeorefApi.instancia();

        ListaMunicipios listaMunicipios = servicio.listadoDeMunicipiosPorIDProvincia(22);

        for(Municipio municipio : listaMunicipios.getMunicipios()){
            System.out.println( "ID: " + municipio.getId() + " MUNICIPIO -> " + municipio.getNombre());
            System.out.println("UBICACION -> LAT: " + municipio.getCentroide().getLat() + " LON: " + municipio.getCentroide().getLon() );
        }
    }


    private void testMatias() throws IOException {
        GeorefApi georefApi = GeorefApi.instancia();

        ListaProvincias listaProvincias = georefApi.listadoDeProvincias();

        for(Provincia provincia : listaProvincias.getProvincias()){
            System.out.println("id:"+ provincia.getId() + ", nombre: " + provincia.getNombre());
        }

        System.out.println("Ingrese el id de la provincia para ver sus municipios ");
        Scanner scanner = new Scanner(System.in);

        int id = Integer.parseInt(scanner.next());

        ListaMunicipios listaMunicipios = georefApi.listadoDeMunicipiosDeProvincia(id);

        int a =1;

        for(Municipio municipio : listaMunicipios.getMunicipios()){
            System.out.println(a + "-" + municipio.getNombre());
            a++;
        }
    }
}

