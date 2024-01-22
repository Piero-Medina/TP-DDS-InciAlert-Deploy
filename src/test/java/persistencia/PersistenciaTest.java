package persistencia;

import controllers.PrestacionController;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.dataBase.repositorios.OrganismoControlRepository;
import models.dataBase.repositorios.PrestacionRepository;
import models.dataBase.repositorios.ServicioRepository;
import models.dataBase.repositorios.UsuarioRepository;
import models.dominio.actores.Ciudadano;
import models.dominio.actores.Usuario;
import models.dominio.entidades.Entidad;
import models.dominio.entidades.EntidadPrestadora;
import models.dominio.entidades.Establecimiento;
import models.dominio.entidades.OrganismoDeControl;
import models.dominio.servicios.Incidente;
import models.dominio.servicios.PrestacionDeServicio;
import models.dominio.servicios.Servicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PersistenciaTest implements WithSimplePersistenceUnit {

    @Test
    public void testrepoUsuario(){
        UsuarioRepository repository = new UsuarioRepository();

        Usuario usuario = repository.findByNombre("DDS16");

        Assertions.assertEquals("DDS16", usuario.getNombre());
        Assertions.assertEquals("123456", usuario.getContrasenia());

    }

    @Test
    public void testJOIN (){
        String hql = "SELECT c FROM Ciudadano c JOIN c.usuario u WHERE u.id = :usuarioId";
        Ciudadano ciudadano = entityManager().createQuery(hql, Ciudadano.class)
                .setParameter("usuarioId", Long.parseLong("2"))
                .getSingleResult();

        Assertions.assertEquals("piero",ciudadano.getNombre());
    }

    @Test
    public void testGuardadoEnCascada(){
        OrganismoControlRepository repository = new OrganismoControlRepository();

        OrganismoDeControl cnrt = new OrganismoDeControl();
        EntidadPrestadora trenesArgentinos = new EntidadPrestadora();
        Entidad lineaMitre = new Entidad();
        Establecimiento estacionRetiro = new Establecimiento();

        cnrt.setNombre("CNRT");

        trenesArgentinos.setNombre("Trenes Argentinos");
        cnrt.agregarEntidadPrestadora(trenesArgentinos);

        lineaMitre.setNombre("Linea Mitre");
        trenesArgentinos.agregarEntidad(lineaMitre);

        estacionRetiro.setNombre("Estacion Retiro");
        lineaMitre.agregarEstablecimiento(estacionRetiro);

        repository.save(cnrt);

    }

    @Test
    public void testBuscadorDeIncidente(){
        PrestacionController servicio = new PrestacionController(new PrestacionRepository());
        PrestacionDeServicio prestacionDeServicio = new PrestacionRepository().findById(1L);

        System.out.println(prestacionDeServicio.getNombreServicioPrestado());

        Incidente incidente = servicio.obtenerIncidenteActualPorIdPrestacion(1L);

        System.out.println(incidente.getNombreIncidente());
    }

    @Test
    public void testRepositorio(){
        ServicioRepository repositorio = new ServicioRepository();

        /*
        Servicio ascensor = new Servicio();
        ascensor.setNombre("ascensor");
        ascensor.setDescripcion("Aparato elevador que sirve para transportar personas en un edificio");

        repositorio.save(ascensor);
        */

        List<Servicio> servicios = repositorio.findAll();

        System.out.println("Cantidad de servicios: " + servicios.size());
        for (Servicio elemento : servicios) {
            System.out.println("servicio => " + elemento.getNombre());
            System.out.println("descripcion => " + elemento.getDescripcion());
        }
    }
}
