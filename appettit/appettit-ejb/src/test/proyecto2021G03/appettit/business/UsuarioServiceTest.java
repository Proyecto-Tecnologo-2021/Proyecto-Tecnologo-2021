package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest extends TestCase {

    @InjectMocks
    private UsuarioService usuarioServiceI;

    @Mock
    private IUsuarioDAO mockiUsuarioDAO;

    @Mock
    private UsuarioConverter mockusuarioConverter;

    @Before
    public void init() {
        usuarioServiceI = new UsuarioService();
        usuarioServiceI.usrDAO = this.mockiUsuarioDAO;
        usuarioServiceI.usrConverter = this.mockusuarioConverter;
    }

    @Test
    public void testListarAdminsitradores() {
        AdministradorDTO administradorDTO = new AdministradorDTO(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF");
        List<AdministradorDTO> adminsDTO = new ArrayList<AdministradorDTO>();
        adminsDTO.add(administradorDTO);
        Administrador administrador = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");
        List<Administrador> admins = new ArrayList<Administrador>();
        admins.add(administrador);

        Mockito.when(usuarioServiceI.usrDAO.listarAdministradores()).thenReturn(admins);
        Mockito.when(usuarioServiceI.usrConverter.fromAdministrador(admins)).thenReturn(adminsDTO);

        try {
            List<AdministradorDTO> obtenidos = usuarioServiceI.listarAdminsitradores();
            assertEquals(obtenidos, adminsDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorNombreAdministrador() {
        AdministradorDTO administradorDTO = new AdministradorDTO(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF");
        List<AdministradorDTO> adminsDTO = new ArrayList<AdministradorDTO>();
        adminsDTO.add(administradorDTO);
        Administrador administrador = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");
        List<Administrador> admins = new ArrayList<Administrador>();
        admins.add(administrador);
        String nombre = "nombre";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorNombreAdministrador(nombre)).thenReturn(admins);
        Mockito.when(usuarioServiceI.usrConverter.fromAdministrador(admins)).thenReturn(adminsDTO);

        try {
            List<AdministradorDTO> obtenidos = usuarioServiceI.buscarPorNombreAdministrador(nombre);
            assertEquals(obtenidos, adminsDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCrear_yaexiste() throws AppettitException {
        AdministradorDTO administradorDTO = new AdministradorDTO(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF");
        Administrador administrador = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");

        Mockito.when(usuarioServiceI.usrConverter.fromAdministradorDTO(administradorDTO)).thenReturn(administrador);
        Mockito.when(usuarioServiceI.usrDAO.existeCorreoTelefono(administrador.getCorreo(), administrador.getTelefono())).thenReturn(true);

        usuarioServiceI.crear(administradorDTO);
    }

    @Test
    public void testCrear() {
        AdministradorDTO administradorDTO = new AdministradorDTO(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF");
        Administrador administrador = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");

        Mockito.when(usuarioServiceI.usrConverter.fromAdministradorDTO(administradorDTO)).thenReturn(administrador);
        Mockito.when(usuarioServiceI.usrDAO.existeCorreoTelefono(administrador.getCorreo(), administrador.getTelefono())).thenReturn(false);
        Mockito.when(usuarioServiceI.usrDAO.crearAdministrador(administrador)).thenReturn(administrador);
        Mockito.when(usuarioServiceI.usrConverter.fromAdministrador(administrador)).thenReturn(administradorDTO);

        try {
            AdministradorDTO obtenido = usuarioServiceI.crear(administradorDTO);
            assertEquals(obtenido, administradorDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrearRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);

        Mockito.when(usuarioServiceI.usrConverter.fromRestauranteDTO(restauranteDTO)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.existeCorreoTelefono(restaurante.getCorreo(), restaurante.getTelefono())).thenReturn(false);
        Mockito.when(usuarioServiceI.usrDAO.crearRestaurante(restaurante)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            RestauranteDTO obtenido = usuarioServiceI.crearRestaurante(restauranteDTO);
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCrearRestaurante_yaexiste() throws AppettitException {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);

        Mockito.when(usuarioServiceI.usrConverter.fromRestauranteDTO(restauranteDTO)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.existeCorreoTelefono(restaurante.getCorreo(), restaurante.getTelefono())).thenReturn(true);

        usuarioServiceI.crearRestaurante(restauranteDTO);
    }

    /*public void testListarRestaurantes() {
    }

    public void testBuscarPorNombreRestaurante() {
    }

    public void testCalificacionRestaurante() {
    }

    public void testBuscarPorCorreoRestaurante() {
    }

    public void testEditarRestaurante() {
    }

    public void testCrearCliente() {
    }

    public void testEditarCliente() {
    }

    public void testEditarClienteRE() {
    }

    public void testEditarDireccion() {
    }

    public void testEliminarDireccion() {
    }

    public void testAgregarDireccion() {
    }

    public void testListarClientes() {
    }

    public void testBuscarPorNombreCliente() {
    }

    public void testBuscarPorIdCliente() {
    }

    public void testCalificacionGralCliente() {
    }

    public void testLogin() {
    }

    public void testLoginMobile() {
    }

    public void testLoginFireBase() {
    }

    public void testCrearJsonWebToken() {
    }

    public void testExisteAlias() {
    }

    public void testObtenerIdDireccion() {
    }

    public void testObtenerDireccionesCliente() {
    }

    public void testBuscarAdministradorPorId() {
    }

    public void testBuscarRestaurantePorId() {
    }*/
}