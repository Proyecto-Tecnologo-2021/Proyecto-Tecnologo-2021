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
import proyecto2021G03.appettit.converter.PedidoRConverter;
import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PedidoServiceTest extends TestCase {

    @InjectMocks
    private PedidoService pedidoServiceI;

    @Mock
    private IPedidoDao mockIPedidoDao;

    @Mock
    private PedidoConverter mockPedidoConverter;

    @Mock
    private NotificacionService mockNotificacionService;

    @Mock
    private PedidoRConverter mockPedidoRConverter;

    @Mock
    private IUsuarioDAO mockIUsuarioDAO;

    @Mock
    private ICalificacionRRService mockICalificacionRRService;

    @Mock
    private IImagenService mockIImagenService;

    @Mock
    private ReclamoConverter mockReclamoConverter;

    @Mock
    private IReclamoDao mockIReclamoDao;

    @Before
    public void init() {
        pedidoServiceI = new PedidoService();
        pedidoServiceI.iPedidoDao = this.mockIPedidoDao;
        pedidoServiceI.pedidoConverter = this.mockPedidoConverter;
        pedidoServiceI.notificacionSrv = this.mockNotificacionService;
        pedidoServiceI.pedidoRConverter = this.mockPedidoRConverter;
        pedidoServiceI.usrDAO = this.mockIUsuarioDAO;
        pedidoServiceI.calificacionSrv = this.mockICalificacionRRService;
        pedidoServiceI.imgSrv = this.mockIImagenService;
        pedidoServiceI.reclamoConverter = this.mockReclamoConverter;
        pedidoServiceI.iReclamoDao = this.mockIReclamoDao;
    }

    @Test
    public void testListar() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
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
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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

        Mockito.when(pedidoServiceI.iPedidoDao.listar()).thenReturn(pedidos);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedidos)).thenReturn(pedidosDTO);

        try {
            List<PedidoDTO> obtenidos = pedidoServiceI.listar();
            assertEquals(pedidosDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
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
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedido)).thenReturn(pedidoDTO);

        try {
            PedidoDTO obtenido = pedidoServiceI.listarPorId(1L);
            assertEquals(pedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedido)).thenReturn(pedidoDTO);
        Mockito.when(pedidoServiceI.iPedidoDao.crear(pedido)).thenReturn(pedido);

        try {
            PedidoDTO obtenido = pedidoServiceI.crear(pedidoDTO);
            assertEquals(pedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedido)).thenReturn(pedidoDTO);
        Mockito.when(pedidoServiceI.iPedidoDao.editar(pedido)).thenReturn(pedido);

        try {
            PedidoDTO obtenido = pedidoServiceI.editar(1L, pedidoDTO);
            assertEquals(pedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_pedidoNoExiste() throws AppettitException {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(null);

        pedidoServiceI.editar(1L, pedidoDTO);
    }

    @Test
    public void testEditarEstadoPago() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        EstadoPedido estadoPedido2 = EstadoPedido.CONFIRMADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido2, tipoPago, true, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedido)).thenReturn(pedidoDTO);
        Mockito.when(pedidoServiceI.iPedidoDao.editar(pedido)).thenReturn(pedido);

        try {
            PedidoDTO obtenido = pedidoServiceI.editarEstadoPago(pedidoDTO);
            assertEquals(pedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditarEstadoPago_pedidoNoExiste() throws AppettitException {
        EstadoPedido estadoPedido2 = EstadoPedido.CONFIRMADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido2, tipoPago, true, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(null);

        pedidoServiceI.editarEstadoPago(pedidoDTO);
    }

    @Test
    public void testEliminar() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);

        try {
            pedidoServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_noExiste() throws AppettitException {
        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(null);
        pedidoServiceI.eliminar(1L);
    }

    @Test
    public void testCrearFront() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);

        Mockito.when(pedidoServiceI.pedidoRConverter.fromDTO(pedidoRDTO)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.iPedidoDao.crear(pedido)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedido)).thenReturn(pedidoRDTO);
        Mockito.when(pedidoServiceI.usrDAO.buscarPorIdCliente(3L)).thenReturn(cliente);

        try {
            PedidoRDTO obtenido = pedidoServiceI.crearFront(pedidoRDTO);
            assertEquals(pedidoRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorClienteREST() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        List<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos.add(pedido);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);
        List<PedidoRDTO> pedidosRDTO = new ArrayList<PedidoRDTO>();
        pedidosRDTO.add(pedidoRDTO);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorCliente(3L)).thenReturn(pedidos);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedidos)).thenReturn(pedidosRDTO);

        try {
            List<PedidoRDTO> obtenidos = pedidoServiceI.listarPorClienteREST(3L);
            assertEquals(pedidosRDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurante() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
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
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorRestaurante(2L)).thenReturn(pedidos);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedidos)).thenReturn(pedidosDTO);

        try {
            List<PedidoDTO> obtenidos = pedidoServiceI.listarPorRestaurante(2L);
            assertEquals(pedidosDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUltimo() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);
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
        Cliente cliente = new Cliente(3L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 3L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);

        Mockito.when(pedidoServiceI.iPedidoDao.ultimo(3L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedido)).thenReturn(pedidoRDTO);

        try {
            Mockito.when(pedidoServiceI.calificacionSrv.listarPorId(1L, 3L)).thenReturn(calificacionRPedidoDTO);
            Mockito.when(pedidoServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            PedidoRDTO obtenido = pedidoServiceI.ultimo(3L);
            assertEquals(pedidoRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUltimo_imgNull() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);
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
        Cliente cliente = new Cliente(3L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 3L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);

        Mockito.when(pedidoServiceI.iPedidoDao.ultimo(3L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedido)).thenReturn(pedidoRDTO);

        try {
            Mockito.when(pedidoServiceI.calificacionSrv.listarPorId(1L, 3L)).thenReturn(calificacionRPedidoDTO);
            PedidoRDTO obtenido = pedidoServiceI.ultimo(3L);
            assertEquals(pedidoRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorIdREST() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);
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
        Cliente cliente = new Cliente(3L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 3L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedido)).thenReturn(pedidoRDTO);

        try {
            Mockito.when(pedidoServiceI.calificacionSrv.listarPorId(1L, 3L)).thenReturn(calificacionRPedidoDTO);
            Mockito.when(pedidoServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            PedidoRDTO obtenido = pedidoServiceI.listarPorIdREST(1L);
            assertEquals(pedidoRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorIdREST_imgNull() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CalificacionRPedidoDTO calificacionRPedidoDTO = new CalificacionRPedidoDTO(3, 3, 3, "comentario", 1L, 2L);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        PedidoRDTO pedidoRDTO = new PedidoRDTO(1L, estadoPedido, 2L, tipoPago, false, LocalDateTime.now(), 20.30, 3L, 4L, menusRDTO, extras, "idP", 15.09, calificacionRPedidoDTO);
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
        Cliente cliente = new Cliente(3L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 3L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntity(pedido)).thenReturn(pedidoRDTO);

        try {
            Mockito.when(pedidoServiceI.calificacionSrv.listarPorId(1L, 3L)).thenReturn(calificacionRPedidoDTO);
            PedidoRDTO obtenido = pedidoServiceI.listarPorIdREST(1L);
            assertEquals(pedidoRDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testObtenerReclamo() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = pedidoServiceI.obtenerReclamo(1L);
            assertEquals(reclamoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testObtenerReclamo_pedidoNoExiste() throws AppettitException {
        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(null);

        pedidoServiceI.obtenerReclamo(1L);
    }

    @Test(expected = AppettitException.class)
    public void testObtenerReclamo_reclamoNoExiste() throws AppettitException {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        cliente.setNotificationFirebase("notifF");
        cliente.setNotificationFirebaseWeb("notifFW");
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
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
        Reclamo reclamo = null;
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorId(1L)).thenReturn(pedido);
        pedidoServiceI.obtenerReclamo(1L);
    }

    @Test
    public void testListarPorClienteMREST() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        DireccionRDTO direccionRDTO = new DireccionRDTO(4L, "alias", "calle", "1234", "apto", "ref", "barrio", "8.9098289,-79.5211476");
        PedidoRMDTO pedidoRMDTO = new PedidoRMDTO(1L, estadoPedido, tipoPago, false, LocalDateTime.now(), 20.90, 2L, 3L, 10.15, direccionRDTO);
        List<PedidoRMDTO> pedidosRMDTO = new ArrayList<PedidoRMDTO>();
        pedidosRMDTO.add(pedidoRMDTO);
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

        Mockito.when(pedidoServiceI.iPedidoDao.listarPorCliente(2L)).thenReturn(pedidos);
        Mockito.when(pedidoServiceI.pedidoRConverter.fromEntityToRMDTO(pedidos)).thenReturn(pedidosRMDTO);

        try {
            List<PedidoRMDTO> obtenidos = pedidoServiceI.listarPorClienteMREST(2L);
            assertEquals(pedidosRMDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorReclamo() {
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
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
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);


        Mockito.when(pedidoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(pedidoServiceI.iPedidoDao.listarPorReclamo(1L)).thenReturn(pedido);
        Mockito.when(pedidoServiceI.pedidoConverter.fromEntity(pedido)).thenReturn(pedidoDTO);

        try {
            PedidoDTO obtenido = pedidoServiceI.listarPorReclamo(1L);
            assertEquals(pedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testListarPorReclamo_reclamoNoExiste() throws AppettitException {
        Reclamo reclamo = null;

        Mockito.when(pedidoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        pedidoServiceI.listarPorReclamo(1L);
    }

    @Test(expected = AppettitException.class)
    public void testListarPorReclamo_pedidoNoExiste() throws AppettitException {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(pedidoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(pedidoServiceI.iPedidoDao.listarPorReclamo(1L)).thenReturn(null);
        pedidoServiceI.listarPorReclamo(1L);
    }
}