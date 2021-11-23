package proyecto2021G03.appettit.business;

import com.vividsolutions.jts.io.ParseException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.DireccionConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

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

    @Mock
    private DireccionConverter mockDirConverter;

    @Before
    public void init() {
        usuarioServiceI = Mockito.spy(new UsuarioService());
        usuarioServiceI.usrDAO = this.mockiUsuarioDAO;
        usuarioServiceI.usrConverter = this.mockusuarioConverter;
        usuarioServiceI.geoSrv = this.mockiGeoSrvc;
        usuarioServiceI.locConverter = this.mockLocalidadConverter;
        usuarioServiceI.dirConverter = this.mockDirConverter;
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

    @Test
    public void testEliminarDireccion() {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        DireccionDTO direccionDTO = new DireccionDTO(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EliminarDeClienteDTO ec = new EliminarDeClienteDTO(1L);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
        Mockito.when(usuarioServiceI.usrDAO.eliminarDireccion(cliente, direccion)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            ClienteDTO obtenido = usuarioServiceI.eliminarDireccion(2L, ec);
            assertEquals(obtenido, clienteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminarDireccion_cNoEx()   throws AppettitException {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        DireccionDTO direccionDTO = new DireccionDTO(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EliminarDeClienteDTO ec = new EliminarDeClienteDTO(1L);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
/*        Mockito.when(usuarioServiceI.usrDAO.eliminarDireccion(cliente, direccion)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
*/

        ClienteDTO obtenido = usuarioServiceI.eliminarDireccion(2L, ec);

    }

    @Test(expected = AppettitException.class)
    public void testEliminarDireccion_dNoEx() throws AppettitException {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        DireccionDTO direccionDTO = new DireccionDTO(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EliminarDeClienteDTO ec = new EliminarDeClienteDTO(1L);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
/*        Mockito.when(usuarioServiceI.usrDAO.eliminarDireccion(cliente, direccion)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
*/
        ClienteDTO obtenido = usuarioServiceI.eliminarDireccion(2L, ec);
    }

    @Test
    public void testAgregarDireccion() {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Direccion direccion2 = new Direccion(1L, "alias2", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion2);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        LocalidadDTO barrioDTO = new LocalidadDTO(3L, 2L, 1L, "nombre", "-34.8844477,-56.1922389");
        DireccionCrearDTO direccionDTO = new DireccionCrearDTO(1L, "alias", "calle", "123", "2", "refs", "-4.4555651,-43.8849088");
        DireccionDTO direccionDTO2 = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO2);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
        //Mockito.when(usuarioServiceI.dirConverter.fromDTO(direccionDTO2)).thenReturn(direccion);
        Mockito.when(usuarioServiceI.usrDAO.agregarDireccion(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        //Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            Mockito.when(usuarioServiceI.geoSrv.localidadPorPunto(direccionDTO.getGeometry())).thenReturn(barrioDTO);
            ClienteDTO obtenido = usuarioServiceI.agregarDireccion(direccionDTO);
            assertEquals(obtenido, clienteDTO);
        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testAgregarDireccion_aliasRep() throws AppettitException {
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

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
        //Mockito.when(usuarioServiceI.dirConverter.fromDTO(direccionDTO2)).thenReturn(direccion);
        //Mockito.when(usuarioServiceI.usrDAO.agregarDireccion(cliente)).thenReturn(cliente);
        //Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        //Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

            ClienteDTO obtenido = usuarioServiceI.agregarDireccion(direccionDTO);
    }

    @Test(expected = AppettitException.class)
    public void testAgregarDireccion_cNoExiste() throws AppettitException {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        DireccionCrearDTO direccionDTO = new DireccionCrearDTO(1L, "alias", "calle", "123", "2", "refs", "-4.4555651,-43.8849088");
        DireccionDTO direccionDTO2 = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO2);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
//        Mockito.when(usuarioServiceI.dirConverter.fromDTO(direccionDTO2)).thenReturn(direccion);
//        Mockito.when(usuarioServiceI.usrDAO.agregarDireccion(cliente)).thenReturn(cliente);
//        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        ClienteDTO obtenido = usuarioServiceI.agregarDireccion(direccionDTO);
    }

    @Test
    public void testListarClientes() {
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);
        List<ClienteDTO> clientesDTO = new ArrayList<ClienteDTO>();
        clientesDTO.add(clienteDTO);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);

        Mockito.when(usuarioServiceI.usrDAO.listarClientes()).thenReturn(clientes);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(clientes)).thenReturn(clientesDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            List<ClienteDTO> obtenidos = usuarioServiceI.listarClientes();
            assertEquals(obtenidos, clientesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorNombreCliente() {
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);
        List<ClienteDTO> clientesDTO = new ArrayList<ClienteDTO>();
        clientesDTO.add(clienteDTO);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorNombreCliente("nombre")).thenReturn(clientes);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(clientes)).thenReturn(clientesDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            List<ClienteDTO> obtenidos = usuarioServiceI.buscarPorNombreCliente("nombre");
            assertEquals(obtenidos, clientesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorIdCliente() {
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            ClienteDTO obtenido = usuarioServiceI.buscarPorIdCliente(1L);
            assertEquals(obtenido, clienteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCalificacionGralCliente() {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            CalificacionGralClienteDTO obtenido = usuarioServiceI.calificacionGralCliente(1L);
            assertEquals(obtenido, calificacionGralClienteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCalificacionGralCliente_noExiste() throws AppettitException {
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);
        //Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        CalificacionGralClienteDTO obtenido = usuarioServiceI.calificacionGralCliente(1L);
    }

    /*@Test
    public void testLogin() {
        Usuario usuario = new Usuario(1L, "nombre", "username", "password", "telefono", "correo", "tokenF", "notiF", "notiFWeb");

    }

    public void testLoginMobile() {
    }

    public void testLoginFireBase() {
    }

    public void testCrearJsonWebToken() {
    }*/

    @Test
    public void testExisteAlias() {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        Boolean esperado = true;

        Boolean obtenido = usuarioServiceI.existeAlias(cliente, "alias");
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testExisteAlias_false() {
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        Boolean esperado = false;

        Boolean obtenido = usuarioServiceI.existeAlias(cliente, "alias2");
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testObtenerIdDireccion() {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        Long esperado = 2L;
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);

        try {
            Long obtenido = usuarioServiceI.obtenerIdDireccion(1L, "alias");
            assertEquals(obtenido, esperado);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testObtenerIdDireccion_cNoExiste() throws AppettitException {
        usuarioServiceI.obtenerIdDireccion(1L, "alias");
    }

    @Test(expected = AppettitException.class)
    public void testObtenerIdDireccion_aliasNoEx() throws AppettitException {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);

        usuarioServiceI.obtenerIdDireccion(1L, "alias2");
    }

    @Test
    public void testObtenerDireccionesCliente() {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
        Mockito.when(usuarioServiceI.dirConverter.fromEntity(direccion)).thenReturn(direccionDTO);

        try {
            List<DireccionDTO> obtenidos = usuarioServiceI.obtenerDireccionesCliente(1L);
            assertEquals(obtenidos, direccionesDto);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testObtenerDireccionesCliente_cNoEx() throws AppettitException {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        List<Cliente> clientes = new ArrayList<Cliente>();
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
//        Mockito.when(usuarioServiceI.dirConverter.fromEntity(direccion)).thenReturn(direccionDTO);

        List<DireccionDTO> obtenidos = usuarioServiceI.obtenerDireccionesCliente(1L);
    }

    @Test(expected = AppettitException.class)
    public void testObtenerDireccionesCliente_noDir() throws AppettitException {
        Direccion direccion = new Direccion(2L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        List<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(cliente);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);
//        Mockito.when(usuarioServiceI.dirConverter.fromEntity(direccion)).thenReturn(direccionDTO);

        List<DireccionDTO> obtenidos = usuarioServiceI.obtenerDireccionesCliente(1L);
    }

    @Test
    public void testBuscarAdministradorPorId() {
        AdministradorDTO administradorDTO = new AdministradorDTO(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF");
        Administrador administrador = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");

        Mockito.when(usuarioServiceI.usrDAO.buscarAdministradorPorId(1L)).thenReturn(administrador);
        Mockito.when(usuarioServiceI.usrConverter.fromAdministrador(administrador)).thenReturn(administradorDTO);

        try {
            AdministradorDTO obtenido = usuarioServiceI.buscarAdministradorPorId(1L);
            assertEquals(obtenido, administradorDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarRestaurantePorId() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        /*ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("imagen");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);*/

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            //Mockito.when(usuarioServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            RestauranteDTO obtenido = usuarioServiceI.buscarRestaurantePorId(2L);
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarRestaurantePorId_null() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("imagen");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            //Mockito.when(usuarioServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            RestauranteDTO obtenido = usuarioServiceI.buscarRestaurantePorId(2L);
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRestaurantes() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        List<RestauranteDTO> restaurantesDTO = new ArrayList<RestauranteDTO>();
        restaurantesDTO.add(restauranteDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.listarRestaurantes()).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurantes)).thenReturn(restaurantesDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            List<RestauranteDTO> obtenidos = usuarioServiceI.listarRestaurantes();
            assertEquals(obtenidos, restaurantesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRestaurantes_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        List<RestauranteDTO> restaurantesDTO = new ArrayList<RestauranteDTO>();
        restaurantesDTO.add(restauranteDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.listarRestaurantes()).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurantes)).thenReturn(restaurantesDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            List<RestauranteDTO> obtenidos = usuarioServiceI.listarRestaurantes();
            assertEquals(obtenidos, restaurantesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorNombreRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        List<RestauranteDTO> restaurantesDTO = new ArrayList<RestauranteDTO>();
        restaurantesDTO.add(restauranteDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorNombreRestaurante("nombre")).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurantes)).thenReturn(restaurantesDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            List<RestauranteDTO> obtenidos = usuarioServiceI.buscarPorNombreRestaurante("nombre");
            assertEquals(obtenidos, restaurantesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorNombreRestaurante_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        List<RestauranteDTO> restaurantesDTO = new ArrayList<RestauranteDTO>();
        restaurantesDTO.add(restauranteDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorNombreRestaurante("nombre")).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurantes)).thenReturn(restaurantesDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            List<RestauranteDTO> obtenidos = usuarioServiceI.buscarPorNombreRestaurante("nombre");
            assertEquals(obtenidos, restaurantesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorCorreoRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreoRestaurante("mail@mail.com")).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            RestauranteDTO obtenido = usuarioServiceI.buscarPorCorreoRestaurante("mail@mail.com");
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorCorreoRestaurante_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        CalificacionGralRestauranteDTO calif = new CalificacionGralRestauranteDTO(1, 2, 3, 4);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreoRestaurante("mail@mail.com")).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calif);
            RestauranteDTO obtenido = usuarioServiceI.buscarPorCorreoRestaurante("mail@mail.com");
            assertEquals(obtenido, restauranteDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditarClienteRE(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        ClienteModificarDTO clienteModificarDTO = new ClienteModificarDTO("nombre", "username", "telefono");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente(cliente)).thenReturn(clienteMDTO);

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

    @Test
    public void testCrearCliente() {
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteMDTO clienteMDTO = new ClienteMDTO("jwt", false, direccionesDto, calificacionGralClienteDTO);
        DireccionCrearDTO direccionCrearDTO = new DireccionCrearDTO(1L, "alias", "calle", "1234", "apartamento", "referencias", "-34.8844477,-56.1922389");
        ClienteCrearDTO clienteCrearDTO = new ClienteCrearDTO("nombre", "username", "password", "1234", "mail@mail.com", "tokenF", direccionCrearDTO, "noti");
        LocalidadDTO barrio = new LocalidadDTO(4L, 2L, 1L, "nombre", "-34.8844477,-56.1922389");
        Direccion direccion = new Direccion(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);

        Mockito.when(usuarioServiceI.usrConverter.fromClienteDTO(any(ClienteDTO.class))).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.existeCorreoTelefono("mail@mail.com", "1234")).thenReturn(false);
        Mockito.when(usuarioServiceI.usrDAO.crearCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente(cliente)).thenReturn(clienteMDTO);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);
        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);

        try {
            Mockito.when(usuarioServiceI.geoSrv.localidadPorPunto(direccionCrearDTO.getGeometry())).thenReturn(barrio);
            ClienteMDTO obtenido = usuarioServiceI.crearCliente(clienteCrearDTO);
            assertEquals(obtenido, clienteMDTO);
        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEditarCliente() {
        ClienteModificarDTO clienteIn = new ClienteModificarDTO("nombre", "username", "1234");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
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
        Mockito.when(usuarioServiceI.crearJsonWebToken(cliente)).thenReturn(token);

        try {
            String obtenido = usuarioServiceI.editarCliente(1L, clienteIn);
            assertEquals(obtenido, token);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

}