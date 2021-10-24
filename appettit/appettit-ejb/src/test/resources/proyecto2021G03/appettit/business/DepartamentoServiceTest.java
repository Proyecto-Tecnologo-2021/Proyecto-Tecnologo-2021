package resources.proyecto2021G03.appettit.business;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.business.DepartamentoService;
import proyecto2021G03.appettit.converter.DepartamentoConverter;
import proyecto2021G03.appettit.dao.DepartamentoDAO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.exception.AppettitException;


@RunWith(MockitoJUnitRunner.class)
public class DepartamentoServiceTest {

	@InjectMocks
	private DepartamentoService departamentoService;

	/*@Mock
	private DepartamentoDAO mockDepartamentoDAO;

	@Mock
	private DepartamentoConverter mockDdepartamentoConverter;*/


	@Before
	public void init() {
		departamentoService = new DepartamentoService();
		/*departamentoService.departamentoDAO = this.mockDepartamentoDAO;
		departamentoService.departamentoConverter = this.mockDdepartamentoConverter;*/

	}


	/*@Test
	public void testCrear() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditar() {
		fail("Not yet implemented");
	}

	@Test
	public void testEliminar() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testListar() {
		List<Departamento> deps = new ArrayList<Departamento>();
		deps.add(new Departamento(1L, "Prueba", null, null));
		List<DepartamentoDTO> depsDTO = new ArrayList<DepartamentoDTO>();
		depsDTO.add(new DepartamentoDTO(1L, "Prueba", null, null));
		Mockito.when(departamentoService.departamentoDAO.listar()).thenReturn(deps);
		Mockito.when(departamentoService.departamentoConverter.fromEntity(deps)).thenReturn(depsDTO);
		List<DepartamentoDTO> depsEsperados = null;
		try {
			depsEsperados = departamentoService.listar();
			assertEquals(1, depsEsperados.size());
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBuscarPorId() {
		Departamento depto= new Departamento(1L, "Prueba", null, null);
		DepartamentoDTO deptoDTO = new DepartamentoDTO(1L, "Prueba", null, null);
		Mockito.when(departamentoService.departamentoDAO.buscarPorId(1L)).thenReturn(depto);
		Mockito.when(departamentoService.departamentoConverter.fromEntity(depto)).thenReturn(deptoDTO);
		try {
			DepartamentoDTO dep = departamentoService.buscarPorId(1L);
			assertEquals(deptoDTO.getId(), dep.getId());
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	/*@Test
	public void testBuscarPorNombre() {
		fail("Not yet implemented");
	}

	@Test
	public void testCrearCiudad() {
		fail("Not yet implemented");
	}

	@Test
	public void testCrearLocalidad() {
		fail("Not yet implemented");
	}

	@Test
	public void testCiudadPorId() {
		fail("Not yet implemented");
	}

	@Test
	public void testLocalidadPorId() {
		fail("Not yet implemented");
	}*/

	@Test(expected = AppettitException.class)
	public void listarPorId_null () throws AppettitException {
		Departamento departamento = null;
		Mockito.when(departamentoService.departamentoDAO.buscarPorId(1L)).thenReturn(departamento);
		@SuppressWarnings("unused")
		DepartamentoDTO dep = departamentoService.buscarPorId(1L);
	}

	@Test(expected = AppettitException.class)
	public void eliminar_null () throws AppettitException {
		Departamento departamento = null;
		Mockito.when(departamentoService.departamentoDAO.buscarPorId(1L)).thenReturn(departamento);
		departamentoService.eliminar(1L);
	}
}
