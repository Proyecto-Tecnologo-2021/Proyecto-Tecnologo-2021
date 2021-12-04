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
import proyecto2021G03.appettit.converter.MenuConverter;
import proyecto2021G03.appettit.dao.IMenuDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.ExtraMenu;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

import javax.ejb.EJB;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceTest extends TestCase {

    @InjectMocks
    private MenuService menuServiceI;

    @Mock
    private IMenuDAO mockIMenuDao;

    @Mock
    private MenuConverter mockMenuConverter;

    @Mock
    private IImagenService mockImgSrv;

    @Before
    public void init() {
        menuServiceI = new MenuService();
        menuServiceI.iMenuDao = this.mockIMenuDao;
        menuServiceI.menuConverter = this.mockMenuConverter;
        menuServiceI.imgSrv = this.mockImgSrv;
    }

    @Test
    public void testListar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);

        Mockito.when(menuServiceI.iMenuDao.listar()).thenReturn(menus);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menus)).thenReturn(menusDTO);

        try {
            List<MenuDTO> obtenidos = menuServiceI.listar();
            assertEquals(obtenidos, menusDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListar_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "", null);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);

        Mockito.when(menuServiceI.iMenuDao.listar()).thenReturn(menus);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menus)).thenReturn(menusDTO);

        try {
            List<MenuDTO> obtenidos = menuServiceI.listar();
            assertEquals(obtenidos, menusDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menu)).thenReturn(menuDTO);

        try {
            MenuDTO obtenido = menuServiceI.listarPorId(6L, 5L);
            assertEquals(obtenido, menuDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "", null);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menu)).thenReturn(menuDTO);

        try {
            MenuDTO obtenido = menuServiceI.listarPorId(6L, 5L);
            assertEquals(obtenido, menuDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.crear(menu)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromDTO(menuDTO)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menu)).thenReturn(menuDTO);

        try {
            MenuDTO obtenido = menuServiceI.crear(menuDTO);
            assertEquals(obtenido, menuDTO);
        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(menu);
        Mockito.when(menuServiceI.iMenuDao.editar(menu)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menu)).thenReturn(menuDTO);

        try {
            MenuDTO obtenido = menuServiceI.editar(6L, menuDTO);
            assertEquals(obtenido, menuDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "", null);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(menu);
        Mockito.when(menuServiceI.iMenuDao.editar(menu)).thenReturn(menu);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menu)).thenReturn(menuDTO);

        try {
            MenuDTO obtenido = menuServiceI.editar(6L, menuDTO);
            assertEquals(obtenido, menuDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_noExiste() throws AppettitException {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(6L, 5L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(null);
        menuServiceI.editar(6L, menuDTO);
    }

    @Test
    public void testEliminar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");

        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(menu);

        try {
            menuServiceI.eliminar(6L, 5L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_noExiste() throws AppettitException {
        Mockito.when(menuServiceI.iMenuDao.listarPorId(6L, 5L)).thenReturn(null);
        menuServiceI.eliminar(6L, 5L);
    }

    @Test
    public void testListarPorRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "imagen", img);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);

        Mockito.when(menuServiceI.iMenuDao.listarPorRestaurate(5L)).thenReturn(menus);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menus)).thenReturn(menusDTO);

        try {
            List<MenuDTO> obtenidos = menuServiceI.listarPorRestaurante(5L);
            assertEquals(obtenidos, menusDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurante_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", direccionDTO, null);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extrasDTO, "", null);
        List<MenuDTO> menusDTO = new ArrayList<MenuDTO>();
        menusDTO.add(menuDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 5.00);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extra);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Menu menu = new Menu(6L, 5L, "nombre", restaurante, "desc", 10.00, 20.00, productos, extras, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);

        Mockito.when(menuServiceI.iMenuDao.listarPorRestaurate(5L)).thenReturn(menus);
        Mockito.when(menuServiceI.menuConverter.fromEntity(menus)).thenReturn(menusDTO);

        try {
            List<MenuDTO> obtenidos = menuServiceI.listarPorRestaurante(5L);
            assertEquals(obtenidos, menusDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}