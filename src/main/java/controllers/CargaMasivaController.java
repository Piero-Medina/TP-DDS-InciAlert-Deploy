package controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import models.dataBase.repositorios.PropietarioRepository;
import models.dominio.actores.Propietario;
import models.dominio.actores.TipoPropietario;
import models.dominio.actores.Usuario;
import models.dominio.lectorCSV.CSV;
import models.dominio.lectorCSV.LectorCSVAdapter;
import server.utils.ICrudViewsHandler;

import java.io.InputStream;
import io.javalin.http.UploadedFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class CargaMasivaController extends Controller{

    private PropietarioRepository propietarioRepository;

    public CargaMasivaController (PropietarioRepository propietarioRepository){
        this.propietarioRepository = propietarioRepository;
    }

    public void index(Context context) {
        super.usuarioLogueadoTienePermisos(context, "crear_org_de_control", "crear_entidad_prestadora", "crear_entidad", "crear_establecimiento");

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        Map<String,Object> model = new HashMap<>();

        model.put("propietario","true");

        if(propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL) && !propietario.getOrganismosDeControl().isEmpty()){
            model.put("cargaRealizada","true");
            context.render("cargaMasiva/index-cargaMasiva.hbs",model);
        } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA) && !propietario.getEntidadesPrestadoras().isEmpty()) {
            model.put("cargaRealizada","true");
            context.render("cargaMasiva/index-cargaMasiva.hbs",model);
        } else if (propietario.getTipoPropietario().equals(TipoPropietario.SIN_TIPO)) {
            context.render("texto/texto-tranparente-sinTipoDePropietario.hbs",model);
        } else {
            model.put("cargaPrincipal","true");
            context.render("cargaMasiva/index-cargaMasiva.hbs",model);
        }
    }

    public void save(Context context) throws IOException {
        super.usuarioLogueadoTienePermisos(context,
                "crear_org_de_control", "crear_entidad_prestadora", "crear_entidad", "crear_establecimiento"
        , "guardar_org_de_control", "guardar_entidad_prestadora", "guardar_entidad", "guardar_establecimiento");

        UploadedFile uploadedFile = context.uploadedFile("CSVfile");
        Map<String, Object> model = new HashMap<>();
        Propietario propietario = super.PropietarioLogueadoSuper(context);

        System.out.println("el Archivo Tiene el Nombre : " + uploadedFile);
        // para que se muestre esta seccion. total el navegador solo tira post con formularios
        model.put("cargaPrincipal","true");

        assert uploadedFile != null;
        if(uploadedFile.size() == 0){

            model.put("archivoVacio","true");
            model.put("propietario", "true");
            context.render("cargaMasiva/index-cargaMasiva.hbs", model);

        }else {
            this.procesarArchivo(uploadedFile, propietario, model, context);

            model.put("propietario", "true");
            context.render("cargaMasiva/index-cargaMasiva.hbs", model);
        }
    }

    public void procesarArchivo (UploadedFile uploadedFile, Propietario propietario, Map<String, Object> model ,Context context) throws IOException {
        System.out.println("ENTRE A PROCESAR EL ARCHIVO");
        LectorCSVAdapter servicio = new CSV();
        String estadoDeCarga;

        if(uploadedFile != null){
            String path = "src\\main\\java\\models\\dominio\\archivos";
            String nombreParaArchivoNuevo = propietario.getUsuario().getNombre()+"-"+uploadedFile.filename();
            this.guardarArchivo(uploadedFile, nombreParaArchivoNuevo, path);

            String pathM = path + "\\" + nombreParaArchivoNuevo;
            servicio.procesarArchivoCSV(propietario,pathM);
            propietarioRepository.update(propietario);

            estadoDeCarga = "Correcto";
            model.put("estadoCorrecto", estadoDeCarga);
        }
        else {
            estadoDeCarga = "Incorrecto";
            model.put("estadoIncorrecto", estadoDeCarga);
        }
    }

    public void edit(Context context) {
        Map<String,Object> model = new HashMap<>();
        model.put("propietario","true");
        context.render("cargaMasiva/show-cargaMasiva.hbs",model);
    }

    public void cargaManual(Context context){
        super.usuarioLogueadoTienePermisos(context, "crear_org_de_control", "crear_entidad_prestadora");

        Propietario propietario = super.PropietarioLogueadoSuper(context);
        boolean primeraVez = propietario.getEntidadesPrestadoras().isEmpty() && propietario.getOrganismosDeControl().isEmpty();

        if(primeraVez) {
            if (propietario.getTipoPropietario().equals(TipoPropietario.ORGANISMO_DE_CONTROL)) {
                context.redirect("organismosDeControl/crear");
            } else if (propietario.getTipoPropietario().equals(TipoPropietario.ENTIDAD_PRESTADORA)) {
                context.redirect("entidadesPrestadoras/crear");
            } else if (propietario.getTipoPropietario().equals(TipoPropietario.SIN_TIPO)) {
                Map<String,Object> model = new HashMap<>();
                model.put("propietario",true);
                context.render("texto/texto-tranparente-sinTipoDePropietario.hbs",model);
            }
        }
        else{
            context.redirect("/subidos");
        }
    }

    public void guardarArchivo(UploadedFile uploadedFile, String nombre, String ruta) {
        try {
            Path targetPath = Paths.get(ruta, nombre);
            Files.copy(uploadedFile.content(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("NO SE PUDO GURADR EL ARCHIVO " + nombre);
            e.printStackTrace();
        }
    }
}

