package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

@RunWith(MockitoJUnitRunner.class)
public class ReclamoServiceTest extends TestCase {

    @InjectMocks
    private ReclamoService reclamoServiceI;

    @Mock
    private IReclamoDao mockiReclamoDao;

    @Mock
    private ReclamoConverter mockreclamoConverter;

    @Mock
    private IPedidoDao mockPedidoDao;

    @Before
    public void init() {
        reclamoServiceI = new ReclamoService();
        reclamoServiceI.iReclamoDao = this.mockiReclamoDao;
        reclamoServiceI.reclamoConverter = this.mockreclamoConverter;
        reclamoServiceI.iPedidoDao = this.mockPedidoDao;
    }


    @Test
    public void testListarRec() {
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<ReclamoDTO> reclamosDTO = new ArrayList<ReclamoDTO>();
        reclamosDTO.add(reclamoDTO);

        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<Reclamo> reclamos = new ArrayList<Reclamo>();
        reclamos.add(reclamo);

        Mockito.when(reclamoServiceI.iReclamoDao.listar()).thenReturn(reclamos);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamos)).thenReturn(reclamosDTO);

        try {
            List<ReclamoDTO> obtenidos = new ArrayList<ReclamoDTO>();
            obtenidos = reclamoServiceI.listar();
            assertEquals(obtenidos, reclamosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurante() {
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<ReclamoDTO> reclamosDTO = new ArrayList<ReclamoDTO>();
        reclamosDTO.add(reclamoDTO);

        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<Reclamo> reclamos = new ArrayList<Reclamo>();
        reclamos.add(reclamo);

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorRestaurante(1L)).thenReturn(reclamos);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamos)).thenReturn(reclamosDTO);

        try {
            List<ReclamoDTO> obtenidos = reclamoServiceI.listarPorRestaurante(1L);
            assertEquals(obtenidos, reclamosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRecPorID() {
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.listarPorId(1L);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrearRec() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Pedido pedido = new Pedido(2L, estadoPedido, tipoPago, true, 20, "motivo", LocalDateTime.now(), 50.09, 3L, 4L, 5L, "paypal", restaurante, cliente, menus, promociones, direccion, null, extras, 25.00);
        ReclamoCDTO reclamoCDTO = new ReclamoCDTO(1L, "motivo prueba", "detalles");
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(reclamoServiceI.reclamoConverter.fromCDTO(reclamoCDTO)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.iReclamoDao.crear(reclamo)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.crear(reclamoCDTO);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCrearRec_yaexiste()  throws AppettitException {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Pedido pedido = new Pedido(2L, estadoPedido, tipoPago, true, 20, "motivo", LocalDateTime.now(), 50.09, 3L, 4L, 5L, "paypal", restaurante, cliente, menus, promociones, direccion, reclamo, extras, 25.00);
        ReclamoCDTO reclamoCDTO = new ReclamoCDTO(1L, "motivo prueba", "detalles");
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);

            ReclamoDTO obtenido = reclamoServiceI.crear(reclamoCDTO);
    }

    @Test(expected = AppettitException.class)
    public void testCrearRec_pedidonull()  throws AppettitException {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        List<Direccion> direcciones = new ArrayList<Direccion>();
        direcciones.add(direccion);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direcciones);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Pedido pedido = null;
        ReclamoCDTO reclamoCDTO = new ReclamoCDTO(1L, "motivo prueba", "detalles");
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
//        Mockito.when(reclamoServiceI.reclamoConverter.fromCDTO(reclamoCDTO)).thenReturn(reclamo);
//        Mockito.when(reclamoServiceI.iReclamoDao.crear(reclamo)).thenReturn(reclamo);
//        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

            ReclamoDTO obtenido = reclamoServiceI.crear(reclamoCDTO);
    }

    @Test
    public void testEditar() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.iReclamoDao.editar(reclamo)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.editar(1L, reclamoDTO);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditarNull()  throws AppettitException {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(null);

        ReclamoDTO obtenido = reclamoServiceI.editar(1L, reclamoDTO);

    }

    @Test
    public void testEliminar() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);

        try {
            reclamoServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminarNull() throws AppettitException {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(null);

        reclamoServiceI.eliminar(1L);
    }
}