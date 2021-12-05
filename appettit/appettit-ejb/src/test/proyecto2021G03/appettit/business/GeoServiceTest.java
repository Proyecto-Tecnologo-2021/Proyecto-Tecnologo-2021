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
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GeoServiceTest extends TestCase {

    @InjectMocks
    private GeoService geoServiceI;

    @Mock
    private IGeoDAO mockIGeoDAO;

    @Mock LocalidadConverter mockLocalidadConverter;

    @Before
    public void init(){
        geoServiceI = new GeoService();
        geoServiceI.geoSrv = this.mockIGeoDAO;
        geoServiceI.locConverter = this.mockLocalidadConverter;
    }

    @Test
    public void testLocalidadPorPunto() {
        LocalidadDTO localidaddto = new LocalidadDTO(2L, 1234L, 1L, "localidadDTOPrueba", "34.2649652,-57.6199629");
        String punto = "-34.8844477,-56.1922389";
        Ciudad ciudad = new Ciudad(1L, 2L, "ciudadPrueba", null, null, "34.2649652,-57.6199629");
        Localidad localidad = new Localidad(3L, 1L, 2L, "localidadPrueba", ciudad, "34.2649652,-57.6199629");

        Mockito.when(geoServiceI.geoSrv.localidadPorPunto(punto)).thenReturn(localidad);
        Mockito.when(geoServiceI.locConverter.fromEntity(localidad)).thenReturn(localidaddto);

        try {
            LocalidadDTO obtenido = geoServiceI.localidadPorPunto(punto);
            assertEquals(localidaddto, obtenido);
        } catch (AppettitException | ParseException e) {
            e.printStackTrace();
        }
    }
}