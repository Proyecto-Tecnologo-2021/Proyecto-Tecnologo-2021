package proyecto2021G03.appettit.business;

import com.vividsolutions.jts.io.ParseException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
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

    @Mock
    private LocalidadConverter mockLocalidadConverter;

    @Mock
    private IGeoService mockiGeoSrvc;

    @Before
    public void init() {
        usuarioServiceI = Mockito.spy(new UsuarioService());
        usuarioServiceI.usrDAO = this.mockiUsuarioDAO;
        usuarioServiceI.usrConverter = this.mockusuarioConverter;
        usuarioServiceI.geoSrv = this.mockiGeoSrvc;
        usuarioServiceI.locConverter = this.mockLocalidadConverter;
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

    @Test
    public void testCalificacionRestaurante() {
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.calificacionRestaurante(1L)).thenReturn(calificacion);

        try {
            CalificacionGralRestauranteDTO obtenido = usuarioServiceI.calificacionRestaurante(1L);
            assertEquals(obtenido, calificacion);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCalificacionRestaurante_err()  throws AppettitException {
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(null);

        usuarioServiceI.calificacionRestaurante(1L);
    }

    @Test
    public void testEditarRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreoRestaurante(restauranteDTO.getCorreo())).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestauranteDTO(restauranteDTO)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.editarRestaurante(restaurante)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            RestauranteDTO obtenido = usuarioServiceI.editarRestaurante(restauranteDTO);
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditarRestaurante_err()  throws AppettitException {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreoRestaurante(restauranteDTO.getCorreo())).thenReturn(null);

        usuarioServiceI.editarRestaurante(restauranteDTO);
    }

    @Test(expected = AppettitException.class)
    public void testEditarClienteRE_err()  throws AppettitException {
        ClienteModificarDTO clienteModificarDTO = new ClienteModificarDTO("nombre", "username", "telefono");
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);
        usuarioServiceI.editarClienteRE(1L, clienteModificarDTO);
    }


    /*public void testEliminarDireccion() {
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
    }

    @Test
    public void testListarRestaurantes() {
    }

    @Test
    public void testBuscarPorNombreRestaurante() {
    }

    @Test
    public void testBuscarPorCorreoRestaurante() {
    }*/

    @Test
    public void testEditarClienteRE(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        ClienteModificarDTO clienteModificarDTO = new ClienteModificarDTO("nombre", "username", "telefono");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);
        String tokenjwt = "token";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        //Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente(cliente)).thenReturn(clienteMDTO);
        //Mockito.when(usuarioServiceI.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        //Mockito.when(usuarioServiceI.crearJsonWebToken(cliente)).thenReturn(tokenjwt);

        try {
            ClienteMDTO obtenido = usuarioServiceI.editarClienteRE(1L, clienteModificarDTO);
            assertEquals(obtenido, clienteMDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditarDireccion()  {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        DireccionCrearDTO direccionDTO = new DireccionCrearDTO(1L, "alias", "calle", "123", "2", "refs", "-4.4555651,-43.8849088");
        DireccionDTO direccionDTO2 = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO2);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        LocalidadDTO barrioDTO = new LocalidadDTO(3L, 2L, 1L, "nombre", "-34.8844477,-56.1922389");
        Localidad barrio = new Localidad(2L, 3L, 4L, "nombre", null, "-34.8844477,-56.1922389");

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
        Mockito.when(usuarioServiceI.locConverter.fromDTO(barrioDTO)).thenReturn(barrio);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

        try {
            Mockito.when(usuarioServiceI.geoSrv.localidadPorPunto(direccionDTO.getGeometry())).thenReturn(barrioDTO);
            ClienteDTO obtenido = usuarioServiceI.editarDireccion(1L, direccionDTO);
            assertEquals(obtenido, clienteDTO);
        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }
    }



    /*@Test
    public void testCrearCliente() {
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteMDTO clienteMDTO = new ClienteMDTO("jwt", false, direccionesDto, calificacionGralClienteDTO);
        DireccionCrearDTO direccionCrearDTO = new DireccionCrearDTO(1L, "alias", "calle", "1234", "apartamento", "referencias", "-34.8844477,-56.1922389");
        ClienteCrearDTO clienteCrearDTO = new ClienteCrearDTO("nombre", "username", "password", "1234", "mail@mail.com", "tokenF", direccionCrearDTO);
        LocalidadDTO barrio = new LocalidadDTO(3L, 2L, 1L, "nombre", "-34.8844477,-56.1922389");


        try {
            Mockito.when(usuarioServiceI.geoSrv.localidadPorPunto(direccionCrearDTO.getGeometry())).thenReturn(barrio);

        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }

    }*/

    @Test
    public void testEditarCliente() {
        ClienteModificarDTO clienteIn = new ClienteModificarDTO("nombre", "username", "1234");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        Cliente cliente = new Cliente(false, direcciones);
        cliente.setId(1L);
        cliente.setUsername("username");
        cliente.setNombre("nombre");
        cliente.setCorreo("mail@mail.com");
        cliente.setTelefono("1235");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        String token = "token";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        /*Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);*/
        Mockito.when(usuarioServiceI.crearJsonWebToken(cliente)).thenReturn(token);

        try {
            String obtenido = usuarioServiceI.editarCliente(1L, clienteIn);
            assertEquals(obtenido, token);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

}