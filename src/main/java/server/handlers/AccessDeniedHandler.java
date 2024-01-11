package server.handlers;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import server.exception.AccessDeniedException;

// MANEJANDO LA EXCEPCION AccessDeniedException
public class AccessDeniedHandler implements IHandler{
    @Override
    public void setHandle(Javalin app) {
        // atrapa la excepcion personalizada
        app.exception(AccessDeniedException.class, (e, context) -> {
            // podemos redireccionar
            // 401
            context.status(HttpStatus.UNAUTHORIZED);
            context.render("exception/401.hbs");
        });
    }
}
