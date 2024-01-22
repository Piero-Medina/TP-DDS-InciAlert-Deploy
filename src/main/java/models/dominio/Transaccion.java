package models.dominio;


import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.io.IOException;

import static io.javalin.apibuilder.ApiBuilder.get;

public class Transaccion implements WithSimplePersistenceUnit{

    public static void main(String[] args) throws IOException {
        new Transaccion().transaccion();
    }

    public void transaccion() {
        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        // accion

        /*
        PropietarioRepository repo = new PropietarioRepository();
        Propietario propietario = repo.findById(2L);
        propietario.setTipoPropietario(TipoPropietario.SIN_TIPO);
        repo.update(propietario);
         */

        tx.commit();
        //String currentWorkingDirectory = System.getProperty("user.dir");
        //System.out.println("Directorio de trabajo actual: " + currentWorkingDirectory);

    }

}
