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
import proyecto2021G03.appettit.dao.ICalificacionRDao;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

import javax.ejb.EJB;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CalificacionRServiceTest extends TestCase {

    @InjectMocks
    private CalificacionRService calificacionRServiceI;

    @Mock
    private ICalificacionRDao mockICalificacionRDao;

    @Mock
    private CalificacionRConverter mockCalificacionRConverter;

    @Before
    public void init() {
        calificacionRServiceI = new CalificacionRService();
        calificacionRServiceI.iCalificacionRDao = this.mockICalificacionRDao;
        calificacionRServiceI.calificacionRConverter = this.mockCalificacionRConverter;
    }

    @Test
    public void testListar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
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
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L,2L, 5, 5, 5, "comentario", pedido, cliente);
        List<ClasificacionPedido> clasificacionesPedido = new ArrayList<ClasificacionPedido>();
        clasificacionesPedido.add(clasificacionPedido);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L,2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);
        List<CalificacionPedidoDTO> calificacionesDTO = new ArrayList<CalificacionPedidoDTO>();
        calificacionesDTO.add(calificacionPedidoDTO);

        Mockito.when(calificacionRServiceI.iCalificacionRDao.listar()).thenReturn(clasificacionesPedido);
        Mockito.when(calificacionRServiceI.calificacionRConverter.fromEntity(clasificacionesPedido)).thenReturn(calificacionesDTO);

        try {
            List<CalificacionPedidoDTO> obtenidos = calificacionRServiceI.listar();
            assertEquals(calificacionesDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
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
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L,2L, 5, 5, 5, "comentario", pedido, cliente);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L,2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);


        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRServiceI.calificacionRConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionPedidoDTO);

        try {
            CalificacionPedidoDTO obtenido = calificacionRServiceI.listarPorId(1L, 2L);
            assertEquals(calificacionPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
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
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L,2L, 5, 5, 5, "comentario", pedido, cliente);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L,2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);


        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRServiceI.iCalificacionRDao.crear(clasificacionPedido)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRServiceI.calificacionRConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionPedidoDTO);

        try {
            CalificacionPedidoDTO obtenido = calificacionRServiceI.crear(calificacionPedidoDTO);
            assertEquals(calificacionPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
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
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menusDTO, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
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
        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        Pedido pedido = new Pedido(1L, estadoPedido, tipoPago, false, 3, "motivo", LocalDateTime.now(), 20.00, 1L, 2L, 3L, "idP", restaurante, cliente, menus, promociones, direccion, reclamo, extrasMenu, 44.0);
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L,2L, 5, 5, 5, "comentario", pedido, cliente);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L,2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);


        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRServiceI.iCalificacionRDao.editar(clasificacionPedido)).thenReturn(clasificacionPedido);
        Mockito.when(calificacionRServiceI.calificacionRConverter.fromEntity(clasificacionPedido)).thenReturn(calificacionPedidoDTO);

        try {
            CalificacionPedidoDTO obtenido = calificacionRServiceI.editar(1L, calificacionPedidoDTO);
            assertEquals(calificacionPedidoDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_noexiste() throws AppettitException {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        ClienteDTO clienteDTO = new ClienteDTO(false, direccionesDto, calificacionGralClienteDTO);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
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
        FileManagement fm = new FileManagement();
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

        PedidoDTO pedidoDTO = new PedidoDTO(1L, estadoPedido, tipoPago, false, 8, "motivo", LocalDateTime.now(), 20.00, restauranteDTO, cliente, menusDTO, promocionesDTO, extras, direccionDTO, reclamoDTO, 1L, 2L, 3L, "idP", 44.00);
        CalificacionPedidoDTO calificacionPedidoDTO = new CalificacionPedidoDTO(1L,2L, 5, 5, 5, "comentario", pedidoDTO, clienteDTO);

        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(null);
        calificacionRServiceI.editar(1L, calificacionPedidoDTO);
    }

    @Test
    public void testEliminar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        CalificacionGralClienteDTO calificacionGralClienteDTO = new CalificacionGralClienteDTO(1,2);
        EstadoPedido estadoPedido = EstadoPedido.SOLICITADO;
        TipoPago tipoPago = TipoPago.EFECTIVO;
        Cliente cliente = new Cliente(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, null);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
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
        ClasificacionPedido clasificacionPedido = new ClasificacionPedido(1L,2L, 5, 5, 5, "comentario", pedido, cliente);

        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(clasificacionPedido);

        try {
            calificacionRServiceI.eliminar(1L, 2L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_noexiste() throws AppettitException {
        Mockito.when(calificacionRServiceI.iCalificacionRDao.listarPorId(1L, 2L)).thenReturn(null);
        calificacionRServiceI.eliminar(1L, 2L);
    }
}