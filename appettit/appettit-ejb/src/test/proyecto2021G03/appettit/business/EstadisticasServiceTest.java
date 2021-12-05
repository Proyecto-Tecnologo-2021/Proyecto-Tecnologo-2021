package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.dao.EstadisticasDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class EstadisticasServiceTest extends TestCase {

    @InjectMocks
    private EstadisticasService estadisticasServiceI;

    @Mock
    private EstadisticasDAO mockEstadisticasDAO;

    @Mock
    private PedidoConverter mockPedidoConverter;

    @Before
    public void init(){
        estadisticasServiceI = new EstadisticasService();
        estadisticasServiceI.estadisticasDAO = mockEstadisticasDAO;
        estadisticasServiceI.pedidoConverter = mockPedidoConverter;
    }

    @Test
    public void testListarPedidosPendientesPorRestaurante() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();
        pedidosDTO.add(pedidoDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
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
        List<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos.add(pedido);

        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();
        
        
        Mockito.when(estadisticasServiceI.estadisticasDAO.listarPedidosPendientesPorRestaurante(2L, fechaDesde, fechaHasta)).thenReturn(pedidos);
        Mockito.when(estadisticasServiceI.pedidoConverter.fromEntity(pedidos)).thenReturn(pedidosDTO);

        try {
            List<PedidoDTO> obtenidos = estadisticasServiceI.listarPedidosPendientesPorRestaurante(2L, fechaDesde, fechaHasta);
            assertEquals(pedidosDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarCalificacionPorRestaurante() {
        List<DashCalificacionResDTO> dashCalificacionResDTOS = null;

        try {
            List<DashCalificacionResDTO> obtenido = estadisticasServiceI.listarCalificacionPorRestaurante(1L);
            assertEquals(dashCalificacionResDTOS, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarVentasPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);

        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();
        
        
        Mockito.when(estadisticasServiceI.estadisticasDAO.listarVentasPorRestaurante(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarVentasPorRestaurante(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTendenciasPorRestaurante() {

    }

    /*public void testListarCalificacionesPorRestaurante() {
    }

    public void testListarPediosRecientesPorRestaurante() {
    }

    public void testListarFormaPagoPorRestaurante() {
    }

    public void testListarReclamosTPorRestaurante() {
    }

    public void testListarEstadoPedidosPorRestaurante() {
    }

    public void testListarReclamosPorRestaurante() {
    }

    public void testListarClientesPorRestaurante() {
    }

    public void testListarOrdenesPorRestaurante() {
    }

    public void testListarOrdenesPromedioPorRestaurante() {
    }

    public void testListarCalificacionesDetPorRestaurante() {
    }

    public void testListarGeoEntregasPorRestaurante() {
    }

    public void testListarVentasPorFecha() {
    }

    public void testListarClientesPorFecha() {
    }

    public void testListarOrdenesPorFecha() {
    }

    public void testListarOrdenesPromedioPorFecha() {
    }

    public void testListarFormaPagoPorFecha() {
    }

    public void testListarReclamosTPorFecha() {
    }

    public void testListarRestaurantesAutorizar() {
    }

    public void testListarTendenciasPorFecha() {
    }

    public void testListarTopRestaurantesPorFecha() {
    }

    public void testListarInfoVentasPorFecha() {
    }

    public void testListarInfoVentasPorFechaRestaurante() {
    }

    public void testListarInfoVentasPorFechaBarrio() {
    }*/
}