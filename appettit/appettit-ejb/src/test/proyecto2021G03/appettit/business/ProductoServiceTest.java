package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.ProductoConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dao.IProductoDAO;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.ProductoRDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class ProductoServiceTest extends TestCase {

    @InjectMocks
    private ProductoService productoServiceI;

    @Mock
    private IProductoDAO mockiProdDao;

    @Mock
    private ProductoConverter mockProductoConverter;

    @Mock
    private ICategoriaDAO mockCatDao;

    @Before
    public void init() {
        productoServiceI = new ProductoService();
        productoServiceI.pDAO = this.mockiProdDao;
        productoServiceI.cDAO = this.mockCatDao;
        productoServiceI.pConverter = this.mockProductoConverter;
    }

    @Test
    public void testListar() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);

        Producto producto = new Producto(1L, 2L, "nombre", 3L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);

        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);

        try {
            List<ProductoDTO> obtenidos = productoServiceI.listar();
            assertEquals(obtenidos, productosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, null, null);
        Producto producto = new Producto(1L, 2L, "nombre", 3L, null, null);

        Mockito.when(productoServiceI.pDAO.listarPorId(1L)).thenReturn(producto);
        Mockito.when(productoServiceI.pConverter.fromEntity(producto)).thenReturn(productoDTO);

        try {
            ProductoDTO obtenido = productoServiceI.listarPorId(1L);
            assertEquals(obtenido, productoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        ProductoCrearDTO productoCDTO = new ProductoCrearDTO(1L, "nombre", 2L);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        Categoria categoria = new Categoria(2L, "nombreCat");
        ProductoDTO productoDTO = new ProductoDTO(1L, 3L, "nombre", 2L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        Mockito.when(productoServiceI.cDAO.listarPorId(productoCDTO.getId_categoria())).thenReturn(categoria);
        Mockito.when(productoServiceI.pDAO.listarPorId(1L)).thenReturn(producto);
        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);
        Mockito.when(productoServiceI.pDAO.editar(producto)).thenReturn(producto);
        Mockito.when(productoServiceI.pConverter.fromEntity(producto)).thenReturn(productoDTO);

        try {
            ProductoDTO obtenido = productoServiceI.editar(1L, productoCDTO);
            assertEquals(obtenido, productoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminar() {
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        Mockito.when(productoServiceI.pDAO.listarPorId(1L)).thenReturn(producto);
        try {
            productoServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminarNull() throws AppettitException {
        Mockito.when(productoServiceI.pDAO.listarPorId(1L)).thenReturn(null);
        productoServiceI.eliminar(1L);
    }

    @Test
    public void testExisteNombreProducto() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 3L, "nombre", 2L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);

        boolean existe = productoServiceI.existeNombreProducto("nombre");
        assertEquals(true, existe);
    }

    @Test
    public void testExisteNombreProductoFalse() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 3L, "nombre", 2L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);

        boolean existe = productoServiceI.existeNombreProducto("nombreNO");
        assertEquals(false, existe);
    }

    @Test
    public void testExisteNombreProductoExcluirId() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 3L, "nombre", 2L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);
        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);

        boolean existe = productoServiceI.existeNombreProductoExcluirId(2L, "nombre");
        assertEquals(true, existe);
    }

    @Test
    public void testListarPorRestaurante() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 3L, "nombre", 2L, null, null);
        List<ProductoDTO> productosDTO = new ArrayList<ProductoDTO>();
        productosDTO.add(productoDTO);
        Producto producto = new Producto(1L, 3L, "nombre", 2L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);

        Mockito.when(productoServiceI.pDAO.listarPorRestaurante(3L)).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntity(productos)).thenReturn(productosDTO);

        try {
            List<ProductoDTO> obtenidos = productoServiceI.listarPorRestaurante(3L);
            assertEquals(obtenidos, productosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, null, null);
        Producto producto = new Producto(1L, 2L, "nombre", 3L, null, null);

        Mockito.when(productoServiceI.pConverter.fromDTO(productoDTO)).thenReturn(producto);
        Mockito.when(productoServiceI.pConverter.fromEntity(producto)).thenReturn(productoDTO);
        Mockito.when(productoServiceI.pDAO.crear(producto)).thenReturn(producto);

        try {
            ProductoDTO obtenido = productoServiceI.crear(productoDTO);
            assertEquals(obtenido, productoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTestEditar() {
        ProductoDTO productoDTO = new ProductoDTO(1L, 2L, "nombre", 3L, null, null);
        Producto producto = new Producto(1L, 2L, "nombre", 3L, null, null);

        Mockito.when(productoServiceI.pConverter.fromDTO(productoDTO)).thenReturn(producto);
        Mockito.when(productoServiceI.pConverter.fromEntity(producto)).thenReturn(productoDTO);
        Mockito.when(productoServiceI.pDAO.editar(producto)).thenReturn(producto);

        try {
            ProductoDTO obtenido = productoServiceI.editar(productoDTO);
            assertEquals(obtenido, productoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRProducto(){
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, null);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);

        Producto producto = new Producto(1L, 2L, "nombre", 3L, null, null);
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(producto);

        Mockito.when(productoServiceI.pDAO.listar()).thenReturn(productos);
        Mockito.when(productoServiceI.pConverter.fromEntityToRDTO(productos)).thenReturn(productosDTO);

        try {
            List<ProductoRDTO> obtenidos = productoServiceI.listarRProducto();
            assertEquals(obtenidos, productosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}