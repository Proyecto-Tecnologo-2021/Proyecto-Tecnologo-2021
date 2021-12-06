package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.CalificacionRConverter;
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

    @Mock
    private ImagenService mockImagenService;

    @Mock
    private CalificacionRConverter mockCalificacionRConverter;

    @Mock
    private UsuarioService mockUsuarioService;

    @Before
    public void init() {
        estadisticasServiceI = new EstadisticasService();
        estadisticasServiceI.estadisticasDAO = mockEstadisticasDAO;
        estadisticasServiceI.pedidoConverter = mockPedidoConverter;
        estadisticasServiceI.imgSrv = this.mockImagenService;
        estadisticasServiceI.calConverter = this.mockCalificacionRConverter;
        estadisticasServiceI.usrSrv = this.mockUsuarioService;
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();
        pedidosDTO.add(pedidoDTO);

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
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "id", img);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTendenciasPorRestaurante(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            Mockito.when(estadisticasServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarTendenciasPorRestaurante(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTendenciasPorRestaurante_imgNull() {
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "", null);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTendenciasPorRestaurante(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarTendenciasPorRestaurante(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarCalificacionesPorRestaurante() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
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
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L, 2L, 5, 5, 5, "comentario", pedido, cliente);
        List<ClasificacionPedido> clasificacionesPedido = new ArrayList<ClasificacionPedido>();
        clasificacionesPedido.add(clasificacionPedido);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1, 2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L, 2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);
        List<CalificacionPedidoDTO> calificacionesPedidoDTO = new ArrayList<CalificacionPedidoDTO>();
        calificacionesPedidoDTO.add(calificacionPedidoDTO);

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarCalificacionesPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(clasificacionesPedido);
        Mockito.when(estadisticasServiceI.calConverter.fromEntity(clasificacionesPedido)).thenReturn(calificacionesPedidoDTO);

        try {
            List<CalificacionPedidoDTO> obtenidos = estadisticasServiceI.listarCalificacionesPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(calificacionesPedidoDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPediosRecientesPorRestaurante() {
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "id", img);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarPediosRecientesPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            Mockito.when(estadisticasServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarPediosRecientesPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPediosRecientesPorRestaurante_imgNull() {
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "", null);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarPediosRecientesPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarPediosRecientesPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarFormaPagoPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarFormaPagoPorRestaurante(2L, fechaDesde, fechaHasta)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarFormaPagoPorRestaurante(2L, fechaDesde, fechaHasta);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarReclamosTPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarReclamosTPorRestaurante(2L, fechaDesde, fechaHasta)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarReclamosTPorRestaurante(2L, fechaDesde, fechaHasta);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarEstadoPedidosPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarEstadoPedidosPorRestaurante(2L, fechaDesde, fechaHasta)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarEstadoPedidosPorRestaurante(2L, fechaDesde, fechaHasta);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarReclamosPorRestaurante() {
        DashReclamoDTO dashReclamoDTO = new DashReclamoDTO(1L, "motivo", "detalles", LocalDateTime.now(), "cliente");
        List<DashReclamoDTO> dashReclamoDTOS = new ArrayList<DashReclamoDTO>();
        dashReclamoDTOS.add(dashReclamoDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarReclamosPorRestaurante(1L, fechaDesde, fechaHasta)).thenReturn(dashReclamoDTOS);

        try {
            List<DashReclamoDTO> obtenidos = estadisticasServiceI.listarReclamosPorRestaurante(1L, fechaDesde, fechaHasta);
            assertEquals(dashReclamoDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarClientesPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarClientesPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarClientesPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarOrdenesPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarOrdenesPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarOrdenesPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarOrdenesPromedioPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarOrdenesPromedioPorRestaurante(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarOrdenesPromedioPorRestaurante(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarCalificacionesDetPorRestaurante() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarCalificacionesDetPorRestaurante(2L, fechaDesde, fechaHasta, "5")).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarCalificacionesDetPorRestaurante(2L, fechaDesde, fechaHasta, "5");
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarGeoEntregasPorRestaurante() {
        String coord1 = "6.3206478";
        String coord2 = "-10.8061643";
        List<String> coordenadas = new ArrayList<String>();
        coordenadas.add(coord1);
        coordenadas.add(coord2);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();
        DashGeoDTO dashGeoDTO = new DashGeoDTO(coordenadas, "centro", "zona");

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarGeoEntregasPorRestaurante(2L, fechaDesde, fechaHasta)).thenReturn(dashGeoDTO);

        try {
            DashGeoDTO obtenido = estadisticasServiceI.listarGeoEntregasPorRestaurante(2L, fechaDesde, fechaHasta);
            assertEquals(dashGeoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarVentasPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarVentasPorFecha(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarVentasPorFecha(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarClientesPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarClientesPorFecha(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarClientesPorFecha(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarOrdenesPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarOrdenesPorFecha(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarOrdenesPorFecha(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarOrdenesPromedioPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarOrdenesPromedioPorFecha(2L, fechaDesde, fechaHasta, 5)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarOrdenesPromedioPorFecha(2L, fechaDesde, fechaHasta, 5);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarFormaPagoPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarFormaPagoPorFecha(2L, fechaDesde, fechaHasta)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarFormaPagoPorFecha(2L, fechaDesde, fechaHasta);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarReclamosTPorFecha() {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("a", 20.00);
        DashTotalDTO dashTotalDTO = new DashTotalDTO(map, 10.00, 15.00);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarReclamosTPorFecha(2L, fechaDesde, fechaHasta)).thenReturn(dashTotalDTO);

        try {
            DashTotalDTO obtenido = estadisticasServiceI.listarReclamosTPorFecha(2L, fechaDesde, fechaHasta);
            assertEquals(dashTotalDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRestaurantesAutorizar() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        RestauranteRDTO restauranteRDTO = new RestauranteRDTO(1L, "nombre", LocalTime.now(), LocalTime.now(), true, img, "id_imagen", "direccion", calificacion, "1234");
        List<RestauranteRDTO> restaurantesRDTO = new ArrayList<RestauranteRDTO>();
        restaurantesRDTO.add(restauranteRDTO);

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarRestaurantesAutorizar()).thenReturn(restaurantesRDTO);

        try {
            List<RestauranteRDTO> obtenidos = estadisticasServiceI.listarRestaurantesAutorizar();
            assertEquals(restaurantesRDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTendenciasPorFecha() {
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "id", img);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTendenciasPorFecha(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            Mockito.when(estadisticasServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarTendenciasPorFecha(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTendenciasPorFecha_imgNull() {
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        DashMenuDTO dashMenuDTO = new DashMenuDTO(1L, "nom_rest", "nombre", 20.00, 30.00, 8, "", null);
        List<DashMenuDTO> dashMenuDTOS = new ArrayList<DashMenuDTO>();
        dashMenuDTOS.add(dashMenuDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTendenciasPorFecha(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashMenuDTOS);

        try {
            List<DashMenuDTO> obtenidos = estadisticasServiceI.listarTendenciasPorFecha(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashMenuDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTopRestaurantesPorFecha() {
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        DashRestauranteDTO dashRestauranteDTO = new DashRestauranteDTO(1L, "nombre", LocalTime.now(), LocalTime.now(), true, img, "id", "direccion 123", calificacion, "12345", 20.00);
        List<DashRestauranteDTO> dashRestauranteDTOS = new ArrayList<DashRestauranteDTO>();
        dashRestauranteDTOS.add(dashRestauranteDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTopRestaurantesPorFecha(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashRestauranteDTOS);

        try {
            Mockito.when(estadisticasServiceI.usrSrv.calificacionRestaurante(1L)).thenReturn(calificacion);
            Mockito.when(estadisticasServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<DashRestauranteDTO> obtenidos = estadisticasServiceI.listarTopRestaurantesPorFecha(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashRestauranteDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarTopRestaurantesPorFecha_imgNull() {
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        DashRestauranteDTO dashRestauranteDTO = new DashRestauranteDTO(1L, "nombre", LocalTime.now(), LocalTime.now(), true, null, "", "direccion 123", calificacion, "12345", 20.00);
        List<DashRestauranteDTO> dashRestauranteDTOS = new ArrayList<DashRestauranteDTO>();
        dashRestauranteDTOS.add(dashRestauranteDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarTopRestaurantesPorFecha(1L, fechaDesde, fechaHasta, 5)).thenReturn(dashRestauranteDTOS);

        try {
            Mockito.when(estadisticasServiceI.usrSrv.calificacionRestaurante(1L)).thenReturn(calificacion);
            List<DashRestauranteDTO> obtenidos = estadisticasServiceI.listarTopRestaurantesPorFecha(1L, fechaDesde, fechaHasta, 5);
            assertEquals(dashRestauranteDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarInfoVentasPorFecha() {
        DashInformeDTO dashInformeDTO = new DashInformeDTO("periodo", "detalle", "detalleB", 20.00, 25.00);
        List<DashInformeDTO> dashInformeDTOS = new ArrayList<DashInformeDTO>();
        dashInformeDTOS.add(dashInformeDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarInfoVentasPorFecha(fechaDesde, fechaHasta)).thenReturn(dashInformeDTOS);

        try {
            List<DashInformeDTO> obtenidos = estadisticasServiceI.listarInfoVentasPorFecha(fechaDesde, fechaHasta);
            assertEquals(dashInformeDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarInfoVentasPorFechaRestaurante() {
        DashInformeDTO dashInformeDTO = new DashInformeDTO("periodo", "detalle", "detalleB", 20.00, 25.00);
        List<DashInformeDTO> dashInformeDTOS = new ArrayList<DashInformeDTO>();
        dashInformeDTOS.add(dashInformeDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarInfoVentasPorFechaRestaurante(fechaDesde, fechaHasta)).thenReturn(dashInformeDTOS);

        try {
            List<DashInformeDTO> obtenidos = estadisticasServiceI.listarInfoVentasPorFechaRestaurante(fechaDesde, fechaHasta);
            assertEquals(dashInformeDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarInfoVentasPorFechaBarrio() {
        DashInformeDTO dashInformeDTO = new DashInformeDTO("periodo", "detalle", "detalleB", 20.00, 25.00);
        List<DashInformeDTO> dashInformeDTOS = new ArrayList<DashInformeDTO>();
        dashInformeDTOS.add(dashInformeDTO);
        LocalDateTime fechaDesde = LocalDateTime.now();
        LocalDateTime fechaHasta = LocalDateTime.now();

        Mockito.when(estadisticasServiceI.estadisticasDAO.listarInfoVentasPorFechaBarrio(fechaDesde, fechaHasta)).thenReturn(dashInformeDTOS);

        try {
            List<DashInformeDTO> obtenidos = estadisticasServiceI.listarInfoVentasPorFechaBarrio(fechaDesde, fechaHasta);
            assertEquals(dashInformeDTOS, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}