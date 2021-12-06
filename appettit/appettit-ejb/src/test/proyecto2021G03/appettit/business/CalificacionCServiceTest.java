package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.CalificacionClienteConverter;
import proyecto2021G03.appettit.dao.IClasificacionClienteDAO;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.entity.ClasificacionCliente;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CalificacionCServiceTest extends TestCase {

    @InjectMocks
    private CalificacionCService calificacionCServiceI;

    @Mock
    private IClasificacionClienteDAO mockIClasificacionClienteDAO;

    @Mock
    private CalificacionClienteConverter mockClasificacionClienteConverter;

    @Before
    public void init() {
        calificacionCServiceI = new CalificacionCService();
        calificacionCServiceI.iClasificacionClienteDAO = this.mockIClasificacionClienteDAO;
        calificacionCServiceI.clasificacionClienteConverter = this.mockClasificacionClienteConverter;
    }

    @Test
    public void testListar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionClienteDTO calificacionClienteDTO = new CalificacionClienteDTO(2L, 1L, 5, "comentario", restaurante, cliente);
        List<CalificacionClienteDTO> calificacionesDTO = new ArrayList<CalificacionClienteDTO>();
        calificacionesDTO.add(calificacionClienteDTO);
        ClasificacionCliente clasificacionCliente = new ClasificacionCliente(2L, 1L, 5, "comentario", restaurante, cliente);
        List<ClasificacionCliente> calificaciones = new ArrayList<ClasificacionCliente>();
        calificaciones.add(clasificacionCliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listar()).thenReturn(calificaciones);
        Mockito.when(calificacionCServiceI.clasificacionClienteConverter.fromEntity(calificaciones)).thenReturn(calificacionesDTO);

        try {
            List<CalificacionClienteDTO> obtenidos = calificacionCServiceI.listar();
            assertEquals(calificacionesDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionClienteDTO calificacionClienteDTO = new CalificacionClienteDTO(2L, 1L, 5, "comentario", restaurante, cliente);
        ClasificacionCliente clasificacionCliente = new ClasificacionCliente(2L, 1L, 5, "comentario", restaurante, cliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(clasificacionCliente);
        Mockito.when(calificacionCServiceI.clasificacionClienteConverter.fromEntity(clasificacionCliente)).thenReturn(calificacionClienteDTO);

        CalificacionClienteDTO obtenido = calificacionCServiceI.listarPorId(1L);
        assertEquals(calificacionClienteDTO, obtenido);
    }

    @Test
    public void testCrear() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionClienteDTO calificacionClienteDTO = new CalificacionClienteDTO(2L, 1L, 5, "comentario", restaurante, cliente);
        ClasificacionCliente clasificacionCliente = new ClasificacionCliente(2L, 1L, 5, "comentario", restaurante, cliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(clasificacionCliente);
        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.crear(clasificacionCliente)).thenReturn(clasificacionCliente);
        Mockito.when(calificacionCServiceI.clasificacionClienteConverter.fromEntity(clasificacionCliente)).thenReturn(calificacionClienteDTO);

        try {
            CalificacionClienteDTO obtenido = calificacionCServiceI.crear(calificacionClienteDTO);
            assertEquals(calificacionClienteDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionClienteDTO calificacionClienteDTO = new CalificacionClienteDTO(2L, 1L, 5, "comentario", restaurante, cliente);
        ClasificacionCliente clasificacionCliente = new ClasificacionCliente(2L, 1L, 5, "comentario", restaurante, cliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(clasificacionCliente);
        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.editar(clasificacionCliente)).thenReturn(clasificacionCliente);
        Mockito.when(calificacionCServiceI.clasificacionClienteConverter.fromEntity(clasificacionCliente)).thenReturn(calificacionClienteDTO);

        try {
            CalificacionClienteDTO obtenido = calificacionCServiceI.editar(1L, calificacionClienteDTO);
            assertEquals(calificacionClienteDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_cNoExiste() throws AppettitException {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        CalificacionClienteDTO calificacionClienteDTO = new CalificacionClienteDTO(2L, 1L, 5, "comentario", restaurante, cliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(null);
        calificacionCServiceI.editar(1L, calificacionClienteDTO);
    }

    @Test
    public void testEliminar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        ClasificacionCliente clasificacionCliente = new ClasificacionCliente(2L, 1L, 5, "comentario", restaurante, cliente);

        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(clasificacionCliente);

        try {
            calificacionCServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_cNoExiste() throws AppettitException {
        Mockito.when(calificacionCServiceI.iClasificacionClienteDAO.listarPorId(1L)).thenReturn(null);
        calificacionCServiceI.eliminar(1L);
    }
}