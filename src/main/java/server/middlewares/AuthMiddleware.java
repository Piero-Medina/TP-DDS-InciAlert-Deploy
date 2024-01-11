package server.middlewares;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;
import models.dataBase.repositorios.UsuarioRepository;
import server.exception.AccessDeniedException;

import java.util.Set;

public class AuthMiddleware {

    public static void apply(JavalinConfig config) {
        config.accessManager((handler, context, permittedRoles) -> {
            if (!isPathExempted(context.path())) {
                checkAuthentication(context);
                checkRoles(context, permittedRoles);
            }
            handler.handle(context);
        });
    }

    private static void checkAuthentication(Context context) {
        if (context.sessionAttribute("id_usuario") == null) {
            System.out.println("Acceso denegado a la ruta: " + context.path());
            throw new AccessDeniedException();
        }
    }

    private static void checkRoles(Context context, Set<? extends RouteRole> permittedRoles) {
        if (!permittedRoles.isEmpty()) {
            RouteRole userRole = getUserRoleType(context);
            if (!permittedRoles.contains(userRole)) {
                System.out.println("Acceso denegado para el rol: " + userRole);
                throw new AccessDeniedException();
            }
        }
    }

    private static RouteRole getUserRoleType(Context context) {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        return usuarioRepository.findById(context.sessionAttribute("id_usuario")).getRol().getTipo();
    }

    private static boolean isPathExempted(String path) {
        // Agrega rutas que deben estar exentas de autenticación aquí.
        return path.equals("/") || path.equals("/login") || path.equals("/signUp");
    }
}
