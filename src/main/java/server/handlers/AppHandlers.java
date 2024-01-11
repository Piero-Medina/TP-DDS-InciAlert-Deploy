package server.handlers;

import io.javalin.Javalin;

import java.util.Arrays;

// GENERALIZACION
public class AppHandlers {
    private IHandler[] handlers = new IHandler[]{
            new AccessDeniedHandler(), // excepcion personalizada de acceso denegado
    };

    // configuro cada handler
    public static void applyHandlers(Javalin app) {
        Arrays.stream(new AppHandlers().handlers).toList().forEach(handler -> handler.setHandle(app));
    }
}
