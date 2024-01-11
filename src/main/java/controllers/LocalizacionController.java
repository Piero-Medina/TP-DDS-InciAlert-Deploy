package controllers;

import io.javalin.http.Context;
import models.dataBase.repositorios.EntidadRepository;
import models.dataBase.repositorios.EstablecimientoRepository;
import models.dominio.actores.TipoRol;
import models.dominio.actores.Usuario;
import models.dominio.api.GeorefApi;
import models.dominio.api.LocalizacionMapper;
import models.dominio.api.localizacion.Localizacion;
import models.dominio.api.mapeo.*;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.Establecimiento;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocalizacionController extends Controller {

    public LocalizacionController(){

    }

    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);
        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)) {
            this.asignarTipoYIdDeEntidad(model, idEntidadA, idEntidad, tipoEntidad);

            model.put("propietario", "true");
            context.render("localizacion/create-localizacion.hbs", model);
        }
    }


    public void tipoLocalizacion(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String tipoLocalizacion = context.formParam("tipoLocalizacion");

        context.redirect("/"+idEntidadA+"/"+idEntidad+"/"+tipoEntidad+"/localizacion/provincia/"+tipoLocalizacion);
        System.out.println("PASE PASE PASE");
    }

    public void provincias(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String tipoLocalizacion = context.pathParam("tipo");

        GeorefApi servicio = GeorefApi.instancia();
        ListaProvincias listaDeProvincias = servicio.listadoDeProvincias();
        model.put("provincia", listaDeProvincias.getProvincias());

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            this.asignarTipoYIdDeEntidad(model, idEntidadA, idEntidad, tipoEntidad);
            model.put("tipoLocalizacion",tipoLocalizacion);

            model.put("propietario","true");
            context.render("localizacion/index1-localizacion.hbs",model);
        }
    }

    public void addProvincia(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String tipoLocalizacion = context.pathParam("tipo");

        String idProvincia = context.formParam("varProvincia");

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)) {
            switch (Objects.requireNonNull(tipoLocalizacion)) {
                case "Provincia":

                    Localizacion localizacion = new Localizacion();
                    assert idProvincia != null;
                    Provincia provincia = this.buscarProvinciaPorId(Integer.parseInt(idProvincia));
                    localizacion = LocalizacionMapper.mapProvincia(provincia);

                    this.guardarLocalizacion(localizacion, idEntidad, tipoEntidad);
                    // son redirigidos a la vista de edicion
                    this.redirigirSegunElTipoDeEntidad(tipoEntidad, idEntidadA, idEntidad, context);

                    break;
                case "Municipio":
                    context.redirect("/"+idEntidadA+"/"+idEntidad+"/"+tipoEntidad+"/localizacion/municipio/"+idProvincia);
                    break;
                case "Departamento":
                    context.redirect("/"+idEntidadA+"/"+idEntidad+"/"+tipoEntidad+"/localizacion/departamento/"+idProvincia);
                    break;
                default:
                    System.out.println("no se eligio ninguna opcion");
            }
        }
    }

    public void departamentos(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String idProvincia = context.pathParam("idPro");

        GeorefApi servicio = GeorefApi.instancia();
        ListaDepartamentos listaDepartamentos = servicio.listadoDeDepartamentosPorIDProvincia(Integer.parseInt(idProvincia));
        model.put("departamento", listaDepartamentos.getDepartamentos());

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            this.asignarTipoYIdDeEntidad(model, idEntidadA, idEntidad, tipoEntidad);
            model.put("idProvincia",idProvincia);

            // para entrar al IF de la vista Especifica, ya que index2 tiene dos vistas
            model.put("departamentoOk","true");
            model.put("propietario","true");
            context.render("localizacion/index2-localizacion.hbs",model);
        }
    }

    public void addDepartamento(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String idProvincia = context.pathParam("idPro");

        String idDepartamentoElegido = context.formParam("varDepartamento");

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            Localizacion localizacion = new Localizacion();
            assert idDepartamentoElegido != null;
            Departamento departamento = this.buscarDepartamentoPorId(Integer.parseInt(idDepartamentoElegido), Integer.parseInt(idProvincia));
            localizacion = LocalizacionMapper.mapDepartamento(departamento);

            this.guardarLocalizacion(localizacion, idEntidad, tipoEntidad);
            this.redirigirSegunElTipoDeEntidad(tipoEntidad, idEntidadA, idEntidad, context);
        }
    }

    public void municipios(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");

        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String idProvincia = context.pathParam("idPro");

        GeorefApi servicio = GeorefApi.instancia();
        ListaMunicipios listaMunicipios = servicio.listadoDeMunicipiosPorIDProvincia(Integer.parseInt(idProvincia));
        model.put("municipio", listaMunicipios.getMunicipios());

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            this.asignarTipoYIdDeEntidad(model, idEntidadA, idEntidad, tipoEntidad);
            model.put("idProvincia",idProvincia);

            // para entrar al IF de la vista Especifica, ya que index2 tiene dos vistas
            model.put("municipioOk","true");
            model.put("propietario","true");
            context.render("localizacion/index2-localizacion.hbs",model);
        }
    }

    public void addMunicipio(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context, "ver_lista_localizaciones", "crear_localizacion", "guardar_localizacion", "editar_localizacion", "actualizar_localizacion");
        Map<String,Object> model = new HashMap<>();
        Usuario usuario = super.usuarioLogueado(context);

        String idEntidadA = context.pathParam("idA");
        String idEntidad = context.pathParam("idC");
        String tipoEntidad =context.pathParam("tipoC");
        String idProvincia = context.pathParam("idPro");

        String idMunicipioElegido = context.formParam("varMunicipio");

        if(usuario.getRol().getTipo().equals(TipoRol.PROPIETARIO)){
            Localizacion localizacion = new Localizacion();
            assert idMunicipioElegido != null;
            Municipio municipio = this.buscarMunicipioPorId(Integer.parseInt(idMunicipioElegido), Integer.parseInt(idProvincia));
            localizacion = LocalizacionMapper.mapMunicipio(municipio);

            this.guardarLocalizacion(localizacion, idEntidad, tipoEntidad);
            this.redirigirSegunElTipoDeEntidad(tipoEntidad, idEntidadA, idEntidad, context);
        }

    }

    public void asignarTipoYIdDeEntidad(Map<String,Object> model, String idA, String id, String tipo){
        model.put("idEntidadA",idA);
        model.put("idEntidad", id);
        model.put("tipoEntidad", tipo);
    }

    public Provincia buscarProvinciaPorId(int idProvincia) throws IOException {
        GeorefApi servicio = GeorefApi.instancia();
        ListaProvincias listaDeProvincias = servicio.listadoDeProvincias();
        for (Provincia provincia: listaDeProvincias.getProvincias()) {
            if (provincia.getId() == idProvincia )
                return provincia;
        }
        return null;
    }

    public Departamento buscarDepartamentoPorId(int idDepartamento, int idProvincia) throws IOException {
        GeorefApi servicio = GeorefApi.instancia();
        ListaDepartamentos listaDepartamentos = servicio.listadoDeDepartamentosPorIDProvincia(idProvincia);
        for (Departamento departamento: listaDepartamentos.getDepartamentos() ) {
            if (departamento.getId() == idDepartamento )
                return departamento;
        }
        return null;
    }

    public Municipio buscarMunicipioPorId(int idMunicipio, int idProvincia) throws IOException {
        GeorefApi servicio = GeorefApi.instancia();
        ListaMunicipios listaMunicipios = servicio.listadoDeMunicipiosPorIDProvincia(idProvincia);
        for (Municipio municipio: listaMunicipios.getMunicipios() ) {
            if (municipio.getId() == idMunicipio )
                return municipio;
        }
        return null;
    }

    // metodo para el propietario
    public void guardarLocalizacion(Localizacion localizacion, String idEntidad, String tipoEntidad){
        switch (tipoEntidad){
            case "Establecimiento":
                EstablecimientoRepository establecimientoRepository = new EstablecimientoRepository();
                Establecimiento establecimiento = establecimientoRepository.findById(Long.parseLong(idEntidad));
                establecimiento.setLocalizacion(localizacion);
                establecimientoRepository.update(establecimiento);

                break;
            case "Entidad":
                EntidadRepository entidadRepository = new EntidadRepository();
                Entidad entidad = entidadRepository.findById(Long.parseLong(idEntidad));
                entidad.setLocalizacion(localizacion);
                entidadRepository.update(entidad);
                break;
            default:
                System.out.println("no se eligio ninguna opcion");
        }
    }

    // metodo para el propietario
    public void redirigirSegunElTipoDeEntidad(String tipoEntidad, String idEntidadA, String idEntidad, Context context) {
        switch (tipoEntidad) {
            case "Establecimiento":
                context.redirect("/entidades/"+idEntidadA+"/establecimientos/"+idEntidad+"/editar");
                break;
            case "Entidad":
                context.redirect("/entidadesPrestadoras/"+idEntidadA+"/entidades/"+idEntidad+"/editar");
                break;
            default:
                System.out.println("no se eligio ninguna opcion");
        }
    }

}
