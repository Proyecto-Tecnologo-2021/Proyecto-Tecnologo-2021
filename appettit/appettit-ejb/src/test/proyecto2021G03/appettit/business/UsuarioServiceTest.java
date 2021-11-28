package proyecto2021G03.appettit.business;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.vividsolutions.jts.io.ParseException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import junit.framework.TestCase;
import proyecto2021G03.appettit.converter.DireccionConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.entity.Imagen;
import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;


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
    
    @Mock
    private ImagenService imagenServiceI;

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

        usuarioServiceI.eliminarDireccion(2L, ec);

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

        usuarioServiceI.eliminarDireccion(2L, ec);
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
        Mockito.when(usuarioServiceI.usrDAO.agregarDireccion(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

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

        usuarioServiceI.agregarDireccion(direccionDTO);
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

        usuarioServiceI.agregarDireccion(direccionDTO);
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
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);

        usuarioServiceI.calificacionGralCliente(1L);
    }

    @Test
    public void testLogin() {
    	Usuario usuario = new Cliente();
    	usuario.setId(1L);
    	usuario.setCorreo("prueba@appetit.com");
    	usuario.setUsername("prueba@appetit.com");
    	usuario.setTelefono("1234");
    	usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
    	usuario.setNotificationFirebase("notiF");
    	
    	
    	LoginDTO loginDTO = new LoginDTO();
    	loginDTO.setUsuario("prueba@appetit.com");
    	loginDTO.setPassword("password");
    	loginDTO.setNotificationFirebase("notiF");
    	
    	List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuarios.add(usuario);
		
		try {
			
			Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(Mockito.anyString())).thenReturn(usuarios);
			Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(Mockito.anyString())).thenReturn(usuarios);
			
			String obtenido = usuarioServiceI.login(loginDTO);
			
		} catch (AppettitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test(expected = AppettitException.class)
    public void testLogin_badPassword() throws AppettitException {
    	Usuario usuario = new Cliente();
    	usuario.setId(1L);
    	usuario.setCorreo("prueba@appetit.com");
    	usuario.setTelefono("1234");
    	usuario.setPassword("password");
    	usuario.setNotificationFirebase("notiF");

		LoginDTO loginDTO = new LoginDTO("prueba@appetit.com", "bad", "notiF");

    	List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuarios.add(usuario);
		
		Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(Mockito.anyString())).thenReturn(usuarios);
		Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(Mockito.anyString())).thenReturn(usuarios);
		
		
		String obtenido = usuarioServiceI.login(loginDTO);
	
    }

    @Test(expected = AppettitException.class)
    public void testLogin_wrongUser() throws AppettitException {
    	Usuario usuario = new Cliente();
    	usuario.setId(1L);
    	usuario.setCorreo("prueba@appetit.com");
    	usuario.setTelefono("1234");
    	usuario.setPassword("password");
    	usuario.setNotificationFirebase("notiF");
    	LoginDTO loginDTO = new LoginDTO("prueba@appetit.com", "bad", "notiF");
    		
    	List<Usuario> usuarios = new ArrayList<Usuario>();
		
		Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(Mockito.anyString())).thenReturn(usuarios);
		Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(Mockito.anyString())).thenReturn(usuarios);
		
		
		String obtenido = usuarioServiceI.login(loginDTO);
	
    }

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

        usuarioServiceI.obtenerDireccionesCliente(1L);
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

        usuarioServiceI.obtenerDireccionesCliente(1L);
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

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
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
    	byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        restauranteDTO.setImagen(img);
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

    @Test(expected = AppettitException.class)
    public void testEditarDireccion_cNoExiste()  throws AppettitException {
        List<Cliente> clientes = new ArrayList<Cliente>();
        DireccionCrearDTO direccionDTO = new DireccionCrearDTO(1L, "alias", "calle", "123", "2", "refs", "-4.4555651,-43.8849088");

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdClienteInteger(1L)).thenReturn(clientes);

        usuarioServiceI.editarDireccion(1L, direccionDTO);
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

    @Test
    public void testlistarRestaurantesAbiertos() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        RestauranteRDTO restauranteRDTO = new RestauranteRDTO(1L, "nombre", LocalTime.now(), LocalTime.now(), true, img, "id_imagen", "direccion", calificacion, "1234");
        List<RestauranteRDTO> restaurantesRDTO = new ArrayList<RestauranteRDTO>();
        restaurantesRDTO.add(restauranteRDTO);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "id_imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);

        Mockito.when(usuarioServiceI.usrDAO.listarRestaurantesAbiertos()).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrConverter.RDTOfromRestaurante(restaurantes)).thenReturn(restaurantesRDTO);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(restaurante);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(1L)).thenReturn(calificacion);
            List<RestauranteRDTO> obtenidos = usuarioServiceI.listarRestaurantesAbiertos();
            assertEquals(restaurantesRDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testlistarRestaurantesAbiertos_imgNull() {
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        RestauranteRDTO restauranteRDTO = new RestauranteRDTO(1L, "nombre", LocalTime.now(), LocalTime.now(), true, null, "", "direccion", calificacion, "1234");
        List<RestauranteRDTO> restaurantesRDTO = new ArrayList<RestauranteRDTO>();
        restaurantesRDTO.add(restauranteRDTO);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(restaurante);

        Mockito.when(usuarioServiceI.usrDAO.listarRestaurantesAbiertos()).thenReturn(restaurantes);
        Mockito.when(usuarioServiceI.usrConverter.RDTOfromRestaurante(restaurantes)).thenReturn(restaurantesRDTO);
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(restaurante);

        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(1L)).thenReturn(calificacion);
            List<RestauranteRDTO> obtenidos = usuarioServiceI.listarRestaurantesAbiertos();
            assertEquals(restaurantesRDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAbrirRestaurante(){
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.editarRestaurante(restaurante)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            RestauranteDTO obtenido = usuarioServiceI.abrirRestaurante(1L);
            assertEquals(restauranteDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = AppettitException.class)
    public void testAbrirRestaurante_restNull() throws AppettitException {
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(null);
        usuarioServiceI.abrirRestaurante(1L);
    }

    @Test
    public void testCerrarRestaurante(){
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrDAO.editarRestaurante(restaurante)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.fromRestaurante(restaurante)).thenReturn(restauranteDTO);

        try {
            RestauranteDTO obtenido = usuarioServiceI.cerrarRestaurante(1L);
            assertEquals(restauranteDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = AppettitException.class)
    public void testCerrarRestaurante_restNull() throws AppettitException {
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(1L)).thenReturn(null);
        usuarioServiceI.cerrarRestaurante(1L);
    }

    @Test
    public void testLoginGoogle(){
        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);
        String token = "token";

        Mockito.when(usuarioServiceI.existeCorreoUsuario("mail@mail.com")).thenReturn(true);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo("mail@mail.com")).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.crearJsonWebToken(usuario)).thenReturn(token);

        try {
            String obtenido = usuarioServiceI.loginGoogle("mail@mail.com", "nombre");
            assertEquals(token, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginGoogle_usuNoExiste(){
        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);
        String token = "token";
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        DireccionCrearDTO direccionCrearDTO = new DireccionCrearDTO(1L, "alias", "calle", "1234", "apartamento", "referencias", "-34.8844477,-56.1922389");
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteCrearDTO clientecrear = new ClienteCrearDTO("nombre", "username", "password", "1234", "mail@mail.com", "tokenF", direccionCrearDTO, "noti");
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);

        Mockito.when(usuarioServiceI.existeCorreoUsuario("mail@mail.com")).thenReturn(false);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo("mail@mail.com")).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.crearJsonWebToken(usuario)).thenReturn(token);
        Mockito.when(usuarioServiceI.usrConverter.fromClienteDTO(any(ClienteDTO.class))).thenReturn(cliente);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente(cliente)).thenReturn(clienteMDTO);
        Mockito.when(usuarioServiceI.usrDAO.crearCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

        try {
//            Mockito.when(usuarioServiceI.crearCliente(clientecrear)).thenReturn(clienteMDTO);
            String obtenido = usuarioServiceI.loginGoogle("mail@mail.com", "nombre");
            assertEquals(token, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginFireBase() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario("prueba@appetit.com");
        loginDTO.setPassword("password");
        loginDTO.setNotificationFirebase("notiF");

        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1, 2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);
        String token = "token";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(loginDTO.getUsuario())).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente((Cliente) usuario)).thenReturn((Cliente) usuario);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente((Cliente) usuario)).thenReturn(clienteDTO);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente((Cliente) usuario)).thenReturn(clienteMDTO);
        Mockito.when(usuarioServiceI.crearJsonWebToken((Cliente) usuario)).thenReturn(token);

        try {
            ClienteMDTO obtenido = usuarioServiceI.loginFireBase(loginDTO);
            assertEquals(clienteMDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testLoginFireBase_usuNoExiste() throws AppettitException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario("prueba@appetit.com");
        loginDTO.setPassword("password");
        loginDTO.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(loginDTO.getUsuario())).thenReturn(usuarios);
        usuarioServiceI.loginFireBase(loginDTO);
    }

    @Test
    public void testLoginMobile() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario("prueba@appetit.com");
        loginDTO.setPassword("password");
        loginDTO.setNotificationFirebase("notiF");

        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1, 2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);
        String token = "token";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(loginDTO.getUsuario())).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(loginDTO.getUsuario())).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente((Cliente) usuario)).thenReturn((Cliente) usuario);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente((Cliente) usuario)).thenReturn(clienteDTO);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente((Cliente) usuario)).thenReturn(clienteMDTO);
        Mockito.when(usuarioServiceI.crearJsonWebToken((Cliente) usuario)).thenReturn(token);

        try {
            ClienteMDTO obtenido = usuarioServiceI.loginMobile(loginDTO);
            assertEquals(clienteMDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testLoginMobile_usuNoExiste() throws AppettitException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario("prueba@appetit.com");
        loginDTO.setPassword("password");
        loginDTO.setNotificationFirebase("notiF");
        List<Usuario> usuarios = new ArrayList<Usuario>();

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(loginDTO.getUsuario())).thenReturn(usuarios);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(loginDTO.getUsuario())).thenReturn(usuarios);
        usuarioServiceI.loginMobile(loginDTO);
    }

    @Test
    public void testLoginMobile_telf() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsuario("prueba@appetit.com");
        loginDTO.setPassword("password");
        loginDTO.setNotificationFirebase("notiF");

        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        List<Usuario> usuarios_correo = new ArrayList<Usuario>();
        List<Usuario> usuarios_telf = new ArrayList<Usuario>();
        usuarios_telf.add(usuario);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1, 2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        ClienteMDTO clienteMDTO = new ClienteMDTO("token", false, direccionesDto, calificacionGralClienteDTO);
        String token = "token";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreo(loginDTO.getUsuario())).thenReturn(usuarios_correo);
        Mockito.when(usuarioServiceI.usrDAO.buscarPorTelefono(loginDTO.getUsuario())).thenReturn(usuarios_telf);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente((Cliente) usuario)).thenReturn((Cliente) usuario);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente((Cliente) usuario)).thenReturn(clienteDTO);
//        Mockito.when(usuarioServiceI.usrDAO.calificacionGralCliente(1L)).thenReturn(calificacionGralClienteDTO);
        Mockito.when(usuarioServiceI.usrConverter.ClienteMDTOfromCliente((Cliente) usuario)).thenReturn(clienteMDTO);
        Mockito.when(usuarioServiceI.crearJsonWebToken((Cliente) usuario)).thenReturn(token);

        try {
            ClienteMDTO obtenido = usuarioServiceI.loginMobile(loginDTO);
            assertEquals(clienteMDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crearJsonWebToken(){
        Usuario usuario = new Cliente();
        usuario.setId(1L);
        usuario.setCorreo("prueba@appetit.com");
        usuario.setUsername("prueba@appetit.com");
        usuario.setTelefono("1234");
        usuario.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        usuario.setNotificationFirebase("notiF");
        String obtenido = usuarioServiceI.crearJsonWebToken(usuario);
    }

    @Test
    public void crearJsonWebToken_restaurante(){
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Usuario usuario = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        String obtenido = usuarioServiceI.crearJsonWebToken(usuario);
    }

    @Test
    public void crearJsonWebToken_administrador(){
        Usuario usuario = new Administrador(1L, "nombre", "username", "pwd", "1234", "mail@mail.com", "token", "tokenF");
        String obtenido = usuarioServiceI.crearJsonWebToken(usuario);
    }

    @Test
    public void testBuscarRestaurantePorIdBasico(){
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        RestauranteRDTO restauranteRDTO = new RestauranteRDTO(2L, "nombre", LocalTime.now(), LocalTime.now(), true, img, "id_imagen", "direccion", calificacion, "1234");

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.RDTOfromRestaurante(restaurante)).thenReturn(restauranteRDTO);


        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calificacion);
            RestauranteRDTO obtenido = usuarioServiceI.buscarRestaurantePorIdBasico(2L);
            assertEquals(restauranteRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testBuscarRestaurantePorIdBasico_resnoExiste() throws AppettitException{
        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(null);
        usuarioServiceI.buscarRestaurantePorIdBasico(2L);

    }

    @Test
    public void testBuscarRestaurantePorIdBasico_imgnull(){
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "");
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        RestauranteRDTO restauranteRDTO = new RestauranteRDTO(2L, "nombre", LocalTime.now(), LocalTime.now(), true, null, "", "direccion", calificacion, "1234");

        Mockito.when(usuarioServiceI.usrDAO.buscarRestaurantePorId(2L)).thenReturn(restaurante);
        Mockito.when(usuarioServiceI.usrConverter.RDTOfromRestaurante(restaurante)).thenReturn(restauranteRDTO);


        try {
            Mockito.when(usuarioServiceI.calificacionRestaurante(2L)).thenReturn(calificacion);
            RestauranteRDTO obtenido = usuarioServiceI.buscarRestaurantePorIdBasico(2L);
            assertEquals(restauranteRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarDireccionPorId(){
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        Direccion direccion = new Direccion(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");

        Mockito.when(usuarioServiceI.usrDAO.buscarDireccionPorId(3L)).thenReturn(direccion);
        Mockito.when(usuarioServiceI.dirConverter.fromEntity(direccion)).thenReturn(direccionDTO);

        try {
            DireccionDTO obtenido = usuarioServiceI.buscarDireccionPorId(3L);
            assertEquals(direccionDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBloquearCliente(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

        try {
            usuarioServiceI.bloquearCliente(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testBloquearCliente_cNoExiste() throws AppettitException{
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);
        usuarioServiceI.bloquearCliente(1L);
    }

    @Test
    public void testDesbloquearCliente(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

        try {
            usuarioServiceI.desbloquearCliente(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testDesbloquearCliente_cNoExiste() throws AppettitException{
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);
        usuarioServiceI.desbloquearCliente(1L);
    }

    @Test
    public void testExisteCorreoUsuario(){
        String correo = "mail@mail.com";
        Boolean esperado = true;

        Mockito.when(usuarioServiceI.usrDAO.existeCorreo(correo)).thenReturn(true);
        Boolean obtenido = usuarioServiceI.existeCorreoUsuario(correo);
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testBuscarPorCorreoCliente(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        String correo = "mail@mail.com";

        Mockito.when(usuarioServiceI.usrDAO.buscarPorCorreoCliente(correo)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrConverter.fromCliente(cliente)).thenReturn(clienteDTO);

        try {
            ClienteDTO obtenido = usuarioServiceI.buscarPorCorreoCliente(correo);
            assertEquals(clienteDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCambioContraseña(){
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);

        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(cliente);
        Mockito.when(usuarioServiceI.usrDAO.editarCliente(cliente)).thenReturn(cliente);

        try {
            usuarioServiceI.cambioContraseña("pwd", 1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCambioContraseña_cNoExiste() throws AppettitException{
        Mockito.when(usuarioServiceI.usrDAO.buscarPorIdCliente(1L)).thenReturn(null);
        usuarioServiceI.cambioContraseña("pwd", 1L);
    }
}
