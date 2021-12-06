package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.CategoriaConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CategoriaServiceTest extends TestCase {

    @InjectMocks
    private CategoriaService categoriaServiceI;

    @Mock
    private ICategoriaDAO mockCDAO;

    @Mock
    private CategoriaConverter mockCConverter;

    @Before
    public void init() {
        categoriaServiceI = new CategoriaService();
        categoriaServiceI.cDAO = this.mockCDAO;
        categoriaServiceI.cConverter = this.mockCConverter;
    }

    @Test
    public void testListar() {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);

        try {
            List<CategoriaDTO> obtenidos = categoriaServiceI.listar();
            assertEquals(categoriasDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        Categoria categoria = new Categoria(1L, "nombre");
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");

        Mockito.when(categoriaServiceI.cDAO.listarPorId(1L)).thenReturn(categoria);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO obtenido = categoriaServiceI.listarPorId(1L);
        assertEquals(categoriaDTO, obtenido);
    }

    @Test
    public void testCrear() {
        Categoria categoria = new Categoria(1L, "nombre");
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        CategoriaCrearDTO categoriaCrearDTO = new CategoriaCrearDTO("nombre");

        Mockito.when(categoriaServiceI.cConverter.fromCrearDTO(categoriaCrearDTO)).thenReturn(categoria);
        Mockito.when(categoriaServiceI.cDAO.crear(categoria)).thenReturn(categoria);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categoria)).thenReturn(categoriaDTO);

        try {
            CategoriaDTO obtenido = categoriaServiceI.crear(categoriaCrearDTO);
            assertEquals(categoriaDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testCrear_yaExiste() throws AppettitException {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        CategoriaCrearDTO categoriaCrearDTO = new CategoriaCrearDTO("nombre");

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);
        CategoriaService categoriaServiceMock = Mockito.mock(CategoriaService.class);
        Mockito.lenient().doReturn(true).when(categoriaServiceMock).existeNombreCategoria("nombre");

        categoriaServiceI.crear(categoriaCrearDTO);
    }

    @Test
    public void testEditar() {
        Categoria categoria = new Categoria(1L, "nombre");
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        CategoriaCrearDTO categoriaCrearDTO = new CategoriaCrearDTO("nombre");

        Mockito.when(categoriaServiceI.cDAO.listarPorId(1L)).thenReturn(categoria);
        Mockito.when(categoriaServiceI.cDAO.editar(categoria)).thenReturn(categoria);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categoria)).thenReturn(categoriaDTO);

        try {
            CategoriaDTO obtenido = categoriaServiceI.editar(1L, categoriaCrearDTO);
            assertEquals(categoriaDTO, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_yaExiste() throws AppettitException {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        CategoriaCrearDTO categoriaCrearDTO = new CategoriaCrearDTO("nombre");

        //Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        //Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);
        CategoriaService categoriaServiceMock = Mockito.mock(CategoriaService.class);
        Mockito.lenient().doReturn(true).when(categoriaServiceMock).existeNombreCategoria("nombre");

        categoriaServiceI.editar(1L, categoriaCrearDTO);
    }

    @Test
    public void testEliminar() {
        Categoria categoria = new Categoria(1L, "nombre");
        Mockito.when(categoriaServiceI.cDAO.listarPorId(1L)).thenReturn(categoria);

        try {
            categoriaServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_noExiste() throws AppettitException {
        Mockito.when(categoriaServiceI.cDAO.listarPorId(1L)).thenReturn(null);

        categoriaServiceI.eliminar(1L);
    }

    @Test
    public void testExisteNombreCategoria() {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        Boolean esperado = true;

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);

        Boolean obtenido = categoriaServiceI.existeNombreCategoria("nombre");
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testExisteNombreCategoria_false() {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        Boolean esperado = false;

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);

        Boolean obtenido = categoriaServiceI.existeNombreCategoria("nombre2");
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testExisteNombreCategoriaExcluirId() {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        Boolean esperado = true;

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);

        Boolean obtenido = categoriaServiceI.existeNombreCategoriaExcluirId(2L, "nombre");
        assertEquals(esperado, obtenido);
    }

    @Test
    public void testExisteNombreCategoriaExcluirId_false() {
        Categoria categoria = new Categoria(1L, "nombre");
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoria);
        CategoriaDTO categoriaDTO = new CategoriaDTO(1L, "nombre");
        List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>();
        categoriasDTO.add(categoriaDTO);
        Boolean esperado = false;

        Mockito.when(categoriaServiceI.cDAO.listar()).thenReturn(categorias);
        Mockito.when(categoriaServiceI.cConverter.fromEntity(categorias)).thenReturn(categoriasDTO);

        Boolean obtenido = categoriaServiceI.existeNombreCategoriaExcluirId(1L, "nombre");
        assertEquals(esperado, obtenido);
    }
}