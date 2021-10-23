
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class DepartamentoServiceTest {

	@InjectMocks
	private DepartamentoService departamentoService;
	
	@Before
	public void init() {
		departamentoService = new DepartamentoService();
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
		deps.add(new Departamento(1L, "Prueba", null));
		List<DepartamentoDTO> depsDTO = new ArrayList<DepartamentoDTO>();
		depsDTO.add(new DepartamentoDTO(1L, "Prueba", null));
		Mockito.when(departamentoService.departamentoDAO.listar()).thenReturn(deps);
		Mockito.when(departamentoService.departamentoConverter.fromEntity(deps)).thenReturn(depsDTO);
		List<DepartamentoDTO> depsEsperados = departamentoService.listar();
		assertEquals(1, depsEsperados.size());
	}

	/*@Test
	public void testBuscarPorId() {
		fail("Not yet implemented");
	}

	@Test
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

}
