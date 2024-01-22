package Javalin;

import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

public class JavalinTest {

    @Test
    public void testJavalin(){
        Integer port = Integer.parseInt(System.getProperty("port", "8080"));

        Javalin app = Javalin.create().start(port);

        app.get("/", ctx -> ctx.result("Hola Mundo"));


        try {
            Thread.sleep(1 * 60 * 1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
