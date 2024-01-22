package server;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import config.InicializadorDeDatos;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import models.dominio.Transaccion;
import server.handlers.AppHandlers;
import server.middlewares.AuthMiddleware;

import java.io.IOException;
import java.util.function.Consumer;

public class Server {

    private static Javalin app = null;

    public static Javalin app() {
        if(app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    // levantando servidor embebido
    public static void init() {
        if(app == null){
            Integer port = Integer.parseInt(System.getProperty("port", "8080"));
            app = Javalin.create(config()).start(port);
            initTemplateEngine();
            AppHandlers.applyHandlers(app); // 2
            new Transaccion().transaccion();
            new InicializadorDeDatos().inicializarDatos();
            Router.init();
        }
    }

    private static Consumer<JavalinConfig> config() {
        return config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
            });
            AuthMiddleware.apply(config); // 1
        };
    }

    // configuracion de handlebars
    public static void initTemplateEngine(){
        JavalinRenderer.register(
                (path, model, context) -> {
                    Handlebars handlebars = new Handlebars();
                    Template template = null;
                    try{
                        template = handlebars.compile( "templates/" + path.replace(".hbs", ""));
                        return template.apply(model);
                    }catch(IOException e){
                        e.printStackTrace();
                        context.status(HttpStatus.NOT_FOUND);
                        return "No se encuentra la página indicada...";
                    }
                }, ".hbs"
        );
    }

}
