package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.ImagenConverter;
import proyecto2021G03.appettit.dao.IImagenDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.entity.Imagen;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ImagenServiceTest extends TestCase {

    @InjectMocks
    private ImagenService imagenServiceI;

    @Mock
    private IImagenDAO mockImagenDAO;

    @Mock
    private ImagenConverter mockImagenConverter;

    @Before
    public void init(){
        imagenServiceI = new ImagenService();
        imagenServiceI.imagenDAO = this.mockImagenDAO;
        imagenServiceI.imagenConverter = this.mockImagenConverter;
    }

    @Test
    public void testCrear() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);

        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("iamgen", "imagen", mbyteI);

        Mockito.when(imagenServiceI.imagenConverter.fromDTO(img)).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenDAO.crear(imagen)).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenConverter.fromEntity(imagen)).thenReturn(img);

        try {
            ImagenDTO obtenido = imagenServiceI.crear(img);
            assertEquals(img, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);

        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("iamgen", "imagen", mbyteI);

        Mockito.when(imagenServiceI.imagenDAO.buscarPorId(img.getId())).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenDAO.editar(imagen)).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenConverter.fromEntity(imagen)).thenReturn(img);

        try {
            ImagenDTO obtenido = imagenServiceI.editar(img);
            assertEquals(img, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEditar_imgNoExiste() throws AppettitException {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);

        Mockito.when(imagenServiceI.imagenDAO.buscarPorId(img.getId())).thenReturn(null);
        imagenServiceI.editar(img);
    }

    @Test
    public void testEliminar() {
        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("imagen", "imagen", mbyteI);

        Mockito.when(imagenServiceI.imagenDAO.buscarPorId("imagen")).thenReturn(imagen);

        try {
            imagenServiceI.eliminar("imagen");
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_imgNoExiste() throws AppettitException {
        Mockito.when(imagenServiceI.imagenDAO.buscarPorId("id")).thenReturn(null);
        imagenServiceI.eliminar("id");
    }

    @Test
    public void testListar() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);
        List<ImagenDTO> imagenesDTO = new ArrayList<ImagenDTO>();
        imagenesDTO.add(img);

        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("iamgen", "imagen", mbyteI);
        List<Imagen> imagenes = new ArrayList<Imagen>();
        imagenes.add(imagen);

        Mockito.when(imagenServiceI.imagenDAO.listar()).thenReturn(imagenes);
        Mockito.when(imagenServiceI.imagenConverter.fromEntity(imagenes)).thenReturn(imagenesDTO);

        try {
            List<ImagenDTO> obtenidos = imagenServiceI.listar();
            assertEquals(imagenesDTO, obtenidos);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorId() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);

        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("iamgen", "imagen", mbyteI);

        Mockito.when(imagenServiceI.imagenDAO.buscarPorId("imagen")).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenConverter.fromEntity(imagen)).thenReturn(img);

        try {
            ImagenDTO obtenido = imagenServiceI.buscarPorId("imagen");
            assertEquals(img, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testBuscarPorId_imgNull() throws AppettitException {
        Mockito.when(imagenServiceI.imagenDAO.buscarPorId("imagen")).thenReturn(null);

        imagenServiceI.buscarPorId("imagen");
    }

    @Test
    public void testBuscarPorIdentificador() {
        byte[] mbyte = "imagen".getBytes();
        ImagenDTO img = new ImagenDTO("iamgen", "imagen", mbyte);

        byte[] mbyteI = "imagen".getBytes();
        Imagen imagen = new Imagen("iamgen", "imagen", mbyteI);

        Mockito.when(imagenServiceI.imagenDAO.buscarPorIdentificador("imagen")).thenReturn(imagen);
        Mockito.when(imagenServiceI.imagenConverter.fromEntity(imagen)).thenReturn(img);

        try {
            ImagenDTO obtenido = imagenServiceI.buscarPorIdentificador("imagen");
            assertEquals(img, obtenido);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}