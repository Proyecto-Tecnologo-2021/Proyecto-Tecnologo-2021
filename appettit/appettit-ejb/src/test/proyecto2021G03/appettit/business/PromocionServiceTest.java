package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.PromocionConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dao.IPromocionDAO;
import proyecto2021G03.appettit.dto.*;

@RunWith(MockitoJUnitRunner.class)
public class PromocionServiceTest extends TestCase {

    @InjectMocks
    private PromocionService promocionServiceI;

    @Mock
    private PromocionConverter mockPromocionConverter;

    @Mock
    private IPromocionDAO mockPromocionDAO;

    @Mock
    private IGeoDAO mockIGeoDAO;

    @Before
    public void init() {
        promocionServiceI = new PromocionService();
        promocionServiceI.pConverter = this.mockPromocionConverter;
        promocionServiceI.pDAO = this.mockPromocionDAO;
        promocionServiceI.geoDAO = this.mockIGeoDAO;
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
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", null);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", null);

    }

    /*public void testListarPorId() {
    }

    public void testEditar() {
    }

    public void testEliminar() {
    }

    public void testListarPorRestaurante() {
    }

    public void testCrear() {
    }

    public void testTestEditar() {
    }

    public void testExisteNombreProductoExcluirId() {
    }

    public void testListarRPromocion() {
    }

    public void testListarPorPunto() {
    }

    public void testBuscarPorId() {
    }

    public void testListarPorRestaurnateRest() {
    }
    */
}