package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

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

    @Mock
    private ImagenService mockImgSrv;

    @Mock
    private UsuarioService mockUsrSrv;

    @Before
    public void init() {
        promocionServiceI = new PromocionService();
        promocionServiceI.pConverter = this.mockPromocionConverter;
        promocionServiceI.pDAO = this.mockPromocionDAO;
        promocionServiceI.geoDAO = this.mockIGeoDAO;
        promocionServiceI.imgSrv = this.mockImgSrv;
        promocionServiceI.usrSrv = this.mockUsrSrv;
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);


        try {
            //Mockito.when(promocionServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<PromocionDTO> obtenidos = promocionServiceI.listar();
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListar_imgNull() {
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);


        try {
            //Mockito.when(promocionServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            List<PromocionDTO> obtenidos = promocionServiceI.listar();
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);

        try {
            //Mockito.when(promocionServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            PromocionDTO obtenido = promocionServiceI.listarPorId(promocion.getId(), promocion.getId_restaurante());
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorId_imgNull() {
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
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
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);

        try {
            //Mockito.when(promocionServiceI.imgSrv.buscarPorId("id")).thenReturn(img);
            PromocionDTO obtenido = promocionServiceI.listarPorId(promocion.getId(), promocion.getId_restaurante());
            assertEquals(obtenido, promocionDTO);
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
        ExtraMenuDTO extraMenuDTO = new ExtraMenuDTO(1L, 2L, 3L, productoDTO, 10.00);
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        ProductoCrearDTO pcDTO = new ProductoCrearDTO(2L, "nombre", 3L);

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pDAO.editar(promocion)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);

        try {
            PromocionDTO obtenido = promocionServiceI.editar(1L, 2L, pcDTO);
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar_imgNull() {
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        ProductoCrearDTO pcDTO = new ProductoCrearDTO(2L, "nombre", 3L);

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pDAO.editar(promocion)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);

        try {
            PromocionDTO obtenido = promocionServiceI.editar(1L, 2L, pcDTO);
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminar() {
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);

        try {
            promocionServiceI.eliminar(1L, 2L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AppettitException.class)
    public void testEliminar_null()  throws AppettitException {
        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(null);

         promocionServiceI.eliminar(1L, 2L);
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
        List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listarPorRestaurante(2L)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);


        try {
            List<PromocionDTO> obtenidos = promocionServiceI.listarPorRestaurante(2L);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurante_imgNull() {
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listarPorRestaurante(2L)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);

        try {
            List<PromocionDTO> obtenidos = promocionServiceI.listarPorRestaurante(2L);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrear() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);

        Mockito.when(promocionServiceI.pDAO.crear(promocion)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);
        Mockito.when(promocionServiceI.pConverter.fromDTO(promocionDTO)).thenReturn(promocion);

        try {
            PromocionDTO obtenido = promocionServiceI.crear(promocionDTO);
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTestEditar() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);

        Mockito.when(promocionServiceI.pDAO.editar(promocion)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);
        Mockito.when(promocionServiceI.pConverter.fromDTO(promocionDTO)).thenReturn(promocion);

        try {
            PromocionDTO obtenido = promocionServiceI.editar(promocionDTO);
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTestEditar_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "", null);

        Mockito.when(promocionServiceI.pDAO.editar(promocion)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promocion)).thenReturn(promocionDTO);
        Mockito.when(promocionServiceI.pConverter.fromDTO(promocionDTO)).thenReturn(promocion);

        try {
            PromocionDTO obtenido = promocionServiceI.editar(promocionDTO);
            assertEquals(obtenido, promocionDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExisteNombreProductoExcluirId_false() {
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(1L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);

        boolean obtenido = promocionServiceI.existeNombreProductoExcluirId(1L, "nombre");
        assertEquals(false, obtenido);
    }

    @Test
    public void testExisteNombreProductoExcluirId() {
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
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuDTO menuDTO = new MenuDTO(1L, 2L, "nombre", restauranteDTO, "descrip", 10.55, 30.34, productosDTO, extras, "imagen", img);
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
        menus.add(menuDTO);
        PromocionDTO promocionDTO = new PromocionDTO(3L, 2L, "nombre", restauranteDTO, "descripcion", 30.98, 20.25, menus, "imagen", img);
        List<PromocionDTO> promocionesDTO = new ArrayList<PromocionDTO>();
        promocionesDTO.add(promocionDTO);
        Promocion promocion = new Promocion(3L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntity(promociones)).thenReturn(promocionesDTO);

        boolean obtenido = promocionServiceI.existeNombreProductoExcluirId(1L, "nombre");
        assertEquals(true, obtenido);
    }

    @Test
    public void testListarRPromocion() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "imagen", img, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarRPromocion();
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRPromocion_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "", null, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listar()).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarRPromocion();
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorPunto() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "imagen", img, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        String punto = "punto";

        Mockito.when(promocionServiceI.geoDAO.promocionRestaurantesPorPunto(punto)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarPorPunto(punto);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorPunto_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "", null, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);
        String punto = "punto";

        Mockito.when(promocionServiceI.geoDAO.promocionRestaurantesPorPunto(punto)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarPorPunto(punto);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorId() {
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "imagen", img, "PROM", 2);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promocion)).thenReturn(promocionRDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            PromocionRDTO obtenido = promocionServiceI.buscarPorId(2L, 1L);
            assertEquals(obtenido, promocionRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuscarPorId_imgNull() {
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "", null, "PROM", 2);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listarPorId(1L, 2L)).thenReturn(promocion);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promocion)).thenReturn(promocionRDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            PromocionRDTO obtenido = promocionServiceI.buscarPorId(2L, 1L);
            assertEquals(obtenido, promocionRDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurnateRest() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "imagen", img, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listarPorRestaurante(2L)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarPorRestaurnateRest(2L);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarPorRestaurnateRest_imgNull() {
        EstadoRegistro estadoRegistro = EstadoRegistro.APROBADO;
        RestauranteDTO restauranteDTO = new RestauranteDTO(2L, "nombre", "username", "pwd", "1234", "mail@mail.com", "tokenF", "123445356", estadoRegistro, false, LocalTime.now(), LocalTime.now(), true, true, "-34.8299416,-56.1407427", null, "imagen");
        DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        restauranteDTO.setDireccion(direccionDTO);
        CategoriaDTO categoriaDTO = new CategoriaDTO(3L, "nombre");
        ProductoRDTO productoDTO = new ProductoRDTO(1L, 2L, "nombre", 3L, categoriaDTO);
        List<ProductoRDTO> productosDTO = new ArrayList<ProductoRDTO>();
        productosDTO.add(productoDTO);
        ExtraMenuRDTO extraMenuDTO = new ExtraMenuRDTO(1L, 2L, 3L, "PRODUCTO", 10.00);
        List<ExtraMenuRDTO> extras = new ArrayList<ExtraMenuRDTO>();
        extras.add(extraMenuDTO);
        ImagenDTO img = new ImagenDTO();
        FileManagement fm = new FileManagement();
        img.setId("id");
        img.setIdentificador("Sin Imagen");
        img.setImagen(null);
        MenuRDTO menuDTO = new MenuRDTO(1L, 2L, "nombreRes", 10.25, "nombre", "desc", 30.34, 15.00, extras, productosDTO, "imagen", img, "MENU", 2);
        List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
        menus.add(menuDTO);
        PromocionRDTO promocionRDTO = new PromocionRDTO(1L, "nombre", 2L, "nomrestaurante", "descripcion", 30.98, 20.25, menus, "imagen", img, "PROM", 2);
        List<PromocionRDTO> promocionesDTO = new ArrayList<PromocionRDTO>();
        promocionesDTO.add(promocionRDTO);
        Promocion promocion = new Promocion(1L, 2L, "nombre", null, "desc", 10.00, 20.00, null, "id");
        List<Promocion> promociones = new ArrayList<Promocion>();
        promociones.add(promocion);
        CalificacionGralRestauranteDTO calificacion = new CalificacionGralRestauranteDTO(1,2,3,4);

        Mockito.when(promocionServiceI.pDAO.listarPorRestaurante(2L)).thenReturn(promociones);
        Mockito.when(promocionServiceI.pConverter.fromEntityToRDTO(promociones)).thenReturn(promocionesDTO);

        try {
            Mockito.when(promocionServiceI.usrSrv.calificacionRestaurante(2L)).thenReturn(calificacion);
            List<PromocionRDTO> obtenidos = promocionServiceI.listarPorRestaurnateRest(2L);
            assertEquals(obtenidos, promocionesDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}