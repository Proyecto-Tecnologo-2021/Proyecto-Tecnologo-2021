package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.MenuRConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dao.IMenuRDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class MenuRServiceTest extends TestCase {

    @InjectMocks
    private MenuRService menuRServiceI;

    @Mock
    private IMenuRDAO mockIMenuRDAO;

    @Mock
    private IGeoDAO mockGeoDAO;

    @Mock
    private MenuRConverter mockMenuRConverter;

    @Mock
    private IImagenService mockImgSrv;

    @Mock
    private IUsuarioService mockUsrSrv;

    @Before
    public void init() {
        menuRServiceI = new MenuRService();
        menuRServiceI.iMenuRDAO = this.mockIMenuRDAO;
        menuRServiceI.geoDAO = this.mockGeoDAO;
        menuRServiceI.menuRConverter = this.mockMenuRConverter;
        menuRServiceI.imgSrv = this.mockImgSrv;
        menuRServiceI.usrSrv = this.mockUsrSrv;
    }

    @Test
    public void testListar() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id_img");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id_img", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listar()).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            Mockito.when(menuRServiceI.imgSrv.buscarPorId("id_img")).thenReturn(img);
            List<MenuRDTO> obtenidos = menuRServiceI.listar();
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListar_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listar()).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<MenuRDTO> obtenidos = menuRServiceI.listar();
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id_img");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id_img", img, "MENU", 5);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listarPorId(2L, 1L)).thenReturn(menu);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menu)).thenReturn(menuRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            Mockito.when(menuRServiceI.imgSrv.buscarPorId("id_img")).thenReturn(img);
            MenuRDTO obtenido = menuRServiceI.listarPorId(2L, 1L);
            assertEquals(obtenido, menuRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listarPorId(2L, 1L)).thenReturn(menu);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menu)).thenReturn(menuRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            MenuRDTO obtenido = menuRServiceI.listarPorId(2L, 1L);
            assertEquals(obtenido, menuRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurnate() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id_img");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id_img", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listarPorRestaurate(2L)).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            Mockito.when(menuRServiceI.imgSrv.buscarPorId("id_img")).thenReturn(img);
            List<MenuRDTO> obtenidos = menuRServiceI.listarPorRestaurnate(2L);
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurnate_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(menuRServiceI.iMenuRDAO.listarPorRestaurate(2L)).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<MenuRDTO> obtenidos = menuRServiceI.listarPorRestaurnate(2L);
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorPunto() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id_img");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "id_img", img, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        String punto = "punto";

        Mockito.when(menuRServiceI.geoDAO.menuRestaurantesPorPunto(punto)).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            Mockito.when(menuRServiceI.imgSrv.buscarPorId("id_img")).thenReturn(img);
            List<MenuRDTO> obtenidos = menuRServiceI.listarPorPunto(punto);
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorPunto_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        Direccion direccion = new Direccion(1L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        Categoria categoria = new Categoria(5L, "nombrecat");
        Producto producto = new Producto(1L, 2L, "nombre", 3L, restaurante, categoria);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        ExtraMenu extra = new ExtraMenu(1L, 2L, 3L, producto, 20.00);
        List<ExtraMenu> extrasMenu = new ArrayList<ExtraMenu>();
        extrasMenu.add(extra);
        MenuRDTO menuRDTO = new MenuRDTO(1L, 2L, "nom_rest", 20.09, "nombre", "descripcion", 10.09, 15.08, extras, productosDTO, "", null, "MENU", 5);
        List<MenuRDTO> menusRDTO = new ArrayList<MenuRDTO>();
        menusRDTO.add(menuRDTO);
        Menu menu = new Menu(1L, 2L, "nombre", restaurante, "dsc", 10.00, 20.00, productos, extrasMenu, "id_img");
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1, 2, 3, 4);
        String punto = "punto";

        Mockito.when(menuRServiceI.geoDAO.menuRestaurantesPorPunto(punto)).thenReturn(menus);
        Mockito.when(menuRServiceI.menuRConverter.fromEntity(menus)).thenReturn(menusRDTO);

        try {
            Mockito.when(menuRServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<MenuRDTO> obtenidos = menuRServiceI.listarPorPunto(punto);
            assertEquals(obtenidos, menusRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}