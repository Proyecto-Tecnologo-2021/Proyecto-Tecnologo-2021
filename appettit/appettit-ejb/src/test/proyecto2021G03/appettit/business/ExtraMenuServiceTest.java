package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.ExtraMenuConverter;
import proyecto2021G03.appettit.dao.IExtraMenuDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.*;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class ExtraMenuServiceTest extends TestCase {

    @InjectMocks
    private ExtraMenuService extraMenuServiceI;

    @Mock
    private IExtraMenuDAO mockiExtraMenuDAO;

    @Mock
    private ExtraMenuConverter mockextraMenuConverter;

    @Before
    public void init() {
        extraMenuServiceI = new ExtraMenuService();
        extraMenuServiceI.iExtraMenuDAO = this.mockiExtraMenuDAO;
        extraMenuServiceI.extraMenuConverter = this.mockextraMenuConverter;
    }

    @Test
    public void testListar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extraMenu);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listar()).thenReturn(extras);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromEntity(extras)).thenReturn(extrasDTO);

        try {
            List<ExtraMenuDTO> obtenidos = extraMenuServiceI.listar();
            assertEquals(extrasDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorId(1L)).thenReturn(extraMenu);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromEntity(extraMenu)).thenReturn(extraMenuDTO);

        try {
            ExtraMenuDTO obtenido = extraMenuServiceI.listarPorId(1L);
            assertEquals(extraMenuDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.crear(extraMenu)).thenReturn(extraMenu);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromDTO(extraMenuDTO)).thenReturn(extraMenu);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromEntity(extraMenu)).thenReturn(extraMenuDTO);

        try {
            ExtraMenuDTO obtenido = extraMenuServiceI.crear(extraMenuDTO);
            assertEquals(extraMenuDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(5L, 2L, 3L, productoDTO, 10.00);
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorId(5L)).thenReturn(extraMenu);
        Mockito.when(extraMenuServiceI.iExtraMenuDAO.editar(extraMenu)).thenReturn(extraMenu);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromEntity(extraMenu)).thenReturn(extraMenuDTO);

        try {
            ExtraMenuDTO obtenido = extraMenuServiceI.editar(5L, extraMenuDTO);
            assertEquals(extraMenuDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_extraNull() throws AppettitException{
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(5L, 2L, 3L, productoDTO, 10.00);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorId(5L)).thenReturn(null);
        extraMenuServiceI.editar(5L, extraMenuDTO);
    }

    @Test
    public void testEliminar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Restaurante restaurante = new Restaurante(3L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorId(5L)).thenReturn(extraMenu);

        try {
            extraMenuServiceI.eliminar(5L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_extraNull() throws AppettitException{
        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorId(5L)).thenReturn(null);
        extraMenuServiceI.eliminar(5L);
    }

    @Test
    public void testListarPorRestaurante() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, restauranteDTO, categoriaDTO);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extrasDTO = new ArrayList<ExtraMenuDTO>();
        extrasDTO.add(extraMenuDTO);
        Restaurante restaurante = new Restaurante(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, null);
        Direccion direccion = new Direccion(4L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389");
        restaurante.setDireccion(direccion);
        Categoria categoria = new Categoria(2L, "nombreCat");
        Producto producto = new Producto(1L, 3L, "nombre", 2L, restaurante, categoria);
        ExtraMenu extraMenu = new ExtraMenu(5L, 1L, 3L, producto, 20.80);
        List<ExtraMenu> extras = new ArrayList<ExtraMenu>();
        extras.add(extraMenu);

        Mockito.when(extraMenuServiceI.iExtraMenuDAO.listarPorRestaurante(2L)).thenReturn(extras);
        Mockito.when(extraMenuServiceI.extraMenuConverter.fromEntity(extras)).thenReturn(extrasDTO);

        try {
            List<ExtraMenuDTO> obtenidos = extraMenuServiceI.listarPorRestaurante(2L);
            assertEquals(extrasDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}