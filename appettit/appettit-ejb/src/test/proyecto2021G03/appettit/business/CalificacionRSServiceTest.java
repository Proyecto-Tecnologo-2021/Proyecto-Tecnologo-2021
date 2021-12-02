package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.CalificacionRRestConverter;
import proyecto2021G03.appettit.dao.ICalificacionRDao;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class CalificacionRSServiceTest extends TestCase {

    @InjectMocks
    private CalificacionRSService calificacionRSServiceI;

    @Mock
    private ICalificacionRDao mockICalificacionRDao;
    @Mock
    private CalificacionRRestConverter mockCalificacionRRestConverter;

    @Before
    public void init() {
        calificacionRSServiceI = new CalificacionRSService();
        calificacionRSServiceI.iCalificacionRDao = this.mockICalificacionRDao;
        calificacionRSServiceI.calificacionRRestConverter = this.mockCalificacionRRestConverter;
    }

    @Test
    public void testCrear() {
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Promocion promocion = new Promocion(1L, 2L, "nombre", restaurante, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L, 2L, 3, 3, 3, "comentario", pedido, cliente);

        Mockito.when(calificacionRSServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(null);
        Mockito.when(calificacionRSServiceI.calificacionRRestConverter.fromDTO(calificacionRPedidoDTO)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.iCalificacionRDao.crear(clasificacionPedido)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.calificacionRRestConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionRPedidoDTO);

        try {
            CalificacionRPedidoDTO obtenido = calificacionRSServiceI.crear(calificacionRPedidoDTO);
            assertEquals(calificacionRPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCrear_Existe() throws AppettitException {
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Promocion promocion = new Promocion(1L, 2L, "nombre", restaurante, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L, 2L, 3, 3, 3, "comentario", pedido, cliente);
        Mockito.when(calificacionRSServiceI.iCalificacionRDao.listarPorId(calificacionRPedidoDTO.getId_pedido(), calificacionRPedidoDTO.getId_cliente())).thenReturn(clasificacionPedido);
        calificacionRSServiceI.crear(calificacionRPedidoDTO);
    }

    @Test
    public void testListarPorId() {
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Promocion promocion = new Promocion(1L, 2L, "nombre", restaurante, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L, 2L, 3, 3, 3, "comentario", pedido, cliente);

        Mockito.when(calificacionRSServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.calificacionRRestConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionRPedidoDTO);

        try {
            CalificacionRPedidoDTO obtenido = calificacionRSServiceI.listarPorId(1L, 2L);
            assertEquals(calificacionRPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        Promocion promocion = new Promocion(1L, 2L, "nombre", restaurante, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L, 2L, 3, 3, 3, "comentario", pedido, cliente);

        Mockito.when(calificacionRSServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.iCalificacionRDao.editar(clasificacionPedido)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.calificacionRRestConverter.fromDTO(calificacionRPedidoDTO)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRSServiceI.calificacionRRestConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionRPedidoDTO);

        try {
            CalificacionRPedidoDTO obtenido = calificacionRSServiceI.editar(1L, calificacionRPedidoDTO);
            assertEquals(calificacionRPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }


    @Test (expected = AppettitException.class)
    public void testEditar_noExiste() throws AppettitException {
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);

        Mockito.when(calificacionRSServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(null);
        calificacionRSServiceI.editar(1L, calificacionRPedidoDTO);
    }

    @Test
    public void testEliminar() {

    }
}