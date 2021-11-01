package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.entity.Reclamo;
import proyecto2021G03.appettit.exception.AppettitException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ReclamoServiceTest extends TestCase {

    @InjectMocks
    private ReclamoService reclamoServiceI;

    @Mock
    private IReclamoDao mockiReclamoDao;

    @Mock
    private ReclamoConverter mockreclamoConverter;

    @Before
    public void init() {
        reclamoServiceI = new ReclamoService();
        reclamoServiceI.iReclamoDao = this.mockiReclamoDao;
        reclamoServiceI.reclamoConverter = this.mockreclamoConverter;
    }


    @Test
    public void testListarRec() {
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<ReclamoDTO> reclamosDTO = new ArrayList<ReclamoDTO>();
        reclamosDTO.add(reclamoDTO);

        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        List<Reclamo> reclamos = new ArrayList<Reclamo>();
        reclamos.add(reclamo);

        Mockito.when(reclamoServiceI.iReclamoDao.listar()).thenReturn(reclamos);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamos)).thenReturn(reclamosDTO);

        try {
            List<ReclamoDTO> obtenidos = new ArrayList<ReclamoDTO>();
            obtenidos = reclamoServiceI.listar();
            assertEquals(obtenidos, reclamosDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarRecPorID() {
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.listarPorId(1L);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrearRec() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.iReclamoDao.crear(reclamo)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.crear(reclamoDTO);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditar() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.iReclamoDao.editar(reclamo)).thenReturn(reclamo);
        Mockito.when(reclamoServiceI.reclamoConverter.fromEntity(reclamo)).thenReturn(reclamoDTO);

        try {
            ReclamoDTO obtenido = reclamoServiceI.editar(1L, reclamoDTO);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditarNull() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(null);

        try {
            ReclamoDTO obtenido = reclamoServiceI.editar(1L, reclamoDTO);
            assertEquals(obtenido, reclamoDTO);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminar() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(reclamo);

        try {
            reclamoServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminarNull() {
        Reclamo reclamo = new Reclamo(1L, "motivo prueba", "detalles", LocalDateTime.now());
        ReclamoDTO reclamoDTO = new ReclamoDTO(1L, "motivo prueba", "detalles", LocalDateTime.now());

        Mockito.when(reclamoServiceI.iReclamoDao.listarPorId(1L)).thenReturn(null);

        try {
            reclamoServiceI.eliminar(1L);
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }

}