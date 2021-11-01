package proyecto2021G03.appettit.business;

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
import proyecto2021G03.appettit.converter.CiudadConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.dao.DepartamentoDAO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.exception.AppettitException;


@RunWith(MockitoJUnitRunner.class)
public class DepartamentoServiceTest {

	@InjectMocks
	private DepartamentoService departamentoServiceI;

	@Mock
	private DepartamentoDAO mockDepartamentoDAO;

	@Mock
	private DepartamentoConverter mockDdepartamentoConverter;

	@Mock
	private CiudadConverter mockCiudadConverter;

	@Mock
	private LocalidadConverter mockLocalidadConverter;

	@Before
	public void init() {
		departamentoServiceI = new DepartamentoService();
		departamentoServiceI.departamentoDAO = this.mockDepartamentoDAO;
		departamentoServiceI.departamentoConverter = this.mockDdepartamentoConverter;
		departamentoServiceI.ciudadConverter = this.mockCiudadConverter;
		departamentoServiceI.localidadConverter = this.mockLocalidadConverter;
	}


	@Test
	public void testCrear() {
		LocalidadDTO localidaddto = new LocalidadDTO(2L, 1234L, 1L, "localidadDTOPrueba", "34.2649652,-57.6199629");
		List<LocalidadDTO> localidadesDTOprueba = new ArrayList<LocalidadDTO>();
		localidadesDTOprueba.add(localidaddto);
		CiudadDTO ciudaddto = new CiudadDTO(1234L, 1L, "ciudadDTOPrueba", "34.2649652,-57.6199629", localidadesDTOprueba);
		List<CiudadDTO> ciudadesDTOprueba = new ArrayList<CiudadDTO>();
		ciudadesDTOprueba.add(ciudaddto);
		DepartamentoDTO deptoDtoCreado = new DepartamentoDTO(1L, "deptoDTOCreado", "34.2649652,-57.6199629", ciudadesDTOprueba);
		//arriba DTO abajo entidad
		Localidad localidad = new Localidad(3L, 4L, 5L, "localidadPrueba", null, "34.2649652,-57.6199629");
		List<Localidad> localidadesprueba = new ArrayList<Localidad>();
		localidadesprueba.add(localidad);
		Ciudad ciudad = new Ciudad(4L, 5L, "ciudadPrueba", null, localidadesprueba, "34.2649652,-57.6199629");
		List<Ciudad> ciudadesprueba = new ArrayList<Ciudad>();
		ciudadesprueba.add(ciudad);
		Departamento deptoCreado = new Departamento(5L, "deptoCreado", ciudadesprueba, "34.2649652,-57.6199629");
		localidad.setCiudad(ciudad);
		ciudad.setDepartamento(deptoCreado);

		Mockito.when(departamentoServiceI.departamentoConverter.fromDTO(deptoDtoCreado)).thenReturn(deptoCreado);
		Mockito.when(departamentoServiceI.departamentoDAO.crear(deptoCreado)).thenReturn(deptoCreado);
		Mockito.when(departamentoServiceI.departamentoConverter.fromEntity(deptoCreado)).thenReturn(deptoDtoCreado);

		try {
			DepartamentoDTO obtenido = departamentoServiceI.crear(deptoDtoCreado);
			assertEquals(obtenido, deptoDtoCreado);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEditar() {
		LocalidadDTO localidaddto = new LocalidadDTO(2L, 1234L, 1L, "localidadDTOPrueba", "34.2649652,-57.6199629");
		List<LocalidadDTO> localidadesDTOprueba = new ArrayList<LocalidadDTO>();
		localidadesDTOprueba.add(localidaddto);
		CiudadDTO ciudaddto = new CiudadDTO(1234L, 1L, "ciudadDTOPrueba", "34.2649652,-57.6199629", localidadesDTOprueba);
		List<CiudadDTO> ciudadesDTOprueba = new ArrayList<CiudadDTO>();
		ciudadesDTOprueba.add(ciudaddto);
		DepartamentoDTO deptoDtoCreado = new DepartamentoDTO(1L, "deptoDTOCreado", "34.2649652,-57.6199629", ciudadesDTOprueba);
		//arriba DTO abajo entidad
		Localidad localidad = new Localidad(3L, 4L, 5L, "localidadPrueba", null, "34.2649652,-57.6199629");
		List<Localidad> localidadesprueba = new ArrayList<Localidad>();
		localidadesprueba.add(localidad);
		Ciudad ciudad = new Ciudad(4L, 5L, "ciudadPrueba", null, localidadesprueba, "34.2649652,-57.6199629");
		List<Ciudad> ciudadesprueba = new ArrayList<Ciudad>();
		ciudadesprueba.add(ciudad);
		Departamento deptoCreado = new Departamento(5L, "deptoCreado", ciudadesprueba, "34.2649652,-57.6199629");
		localidad.setCiudad(ciudad);
		ciudad.setDepartamento(deptoCreado);

		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(deptoDtoCreado.getId())).thenReturn(deptoCreado);
		Mockito.when(departamentoServiceI.departamentoDAO.editar(deptoCreado)).thenReturn(deptoCreado);
		Mockito.when(departamentoServiceI.departamentoConverter.fromEntity(deptoCreado)).thenReturn(deptoDtoCreado);

		try {
			DepartamentoDTO obtenido = departamentoServiceI.editar(deptoDtoCreado);
			assertEquals(obtenido, deptoDtoCreado);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEliminar() {
		Localidad localidad = new Localidad(3L, 4L, 5L, "localidadPrueba", null, "34.2649652,-57.6199629");
		List<Localidad> localidadesprueba = new ArrayList<Localidad>();
		localidadesprueba.add(localidad);
		Ciudad ciudad = new Ciudad(4L, 5L, "ciudadPrueba", null, localidadesprueba, "34.2649652,-57.6199629");
		List<Ciudad> ciudadesprueba = new ArrayList<Ciudad>();
		ciudadesprueba.add(ciudad);
		Departamento deptoCreado = new Departamento(5L, "deptoCreado", ciudadesprueba, "34.2649652,-57.6199629");
		localidad.setCiudad(ciudad);
		ciudad.setDepartamento(deptoCreado);
		//arriba cree departamento

		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(5L)).thenReturn(deptoCreado);

		try {
			departamentoServiceI.eliminar(5L);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testListar() {
		List<Departamento> deps = new ArrayList<Departamento>();
		deps.add(new Departamento(1L, "Prueba", null, "34.2649652,-57.6199629"));
		List<DepartamentoDTO> depsDTO = new ArrayList<DepartamentoDTO>();
		depsDTO.add(new DepartamentoDTO(1L, "Prueba", "34.2649652,-57.6199629", null));
		Mockito.when(departamentoServiceI.departamentoDAO.listar()).thenReturn(deps);
		Mockito.when(departamentoServiceI.departamentoConverter.fromEntity(deps)).thenReturn(depsDTO);
		List<DepartamentoDTO> depsEsperados = null;
		try {
			depsEsperados = departamentoServiceI.listar();
			assertEquals(1, depsEsperados.size());
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBuscarPorId() {
		Departamento depto= new Departamento(1L, "Prueba", null, "34.2649652,-57.6199629");
		DepartamentoDTO deptoDTO = new DepartamentoDTO(1L, "Prueba", "34.2649652,-57.6199629", null);
		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(1L)).thenReturn(depto);
		Mockito.when(departamentoServiceI.departamentoConverter.fromEntity(depto)).thenReturn(deptoDTO);
		try {
			DepartamentoDTO dep = departamentoServiceI.buscarPorId(1L);
			assertEquals(deptoDTO, dep);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBuscarPorNombre() {
		DepartamentoDTO deptoDTO = new DepartamentoDTO(1L, "Prueba", "34.2649652,-57.6199629", null);
		List<DepartamentoDTO> deptosDTO = new ArrayList<DepartamentoDTO>();
		deptosDTO.add(deptoDTO);
		//arriba DTO abajo entidad
		Departamento depto = new Departamento(1L, "Prueba", null, "34.2649652,-57.6199629");
		List<Departamento> deptos = new ArrayList<Departamento>();
		deptos.add(depto);
		String nombre = "Prueba";
		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorNombre(nombre)).thenReturn(deptos);
		Mockito.when(departamentoServiceI.departamentoConverter.fromEntity(deptos)).thenReturn(deptosDTO);
		try {
			List<DepartamentoDTO> deptosOBT = departamentoServiceI.buscarPorNombre(nombre);
			assertEquals(deptosDTO, deptosOBT);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCrearCiudad() {
		CiudadDTO ciudadDTO = new CiudadDTO(1L, 2L, "ciudadPrueba", "34.2649652,-57.6199629", null);
		List<LocalidadDTO> localidadesDTO = new ArrayList<LocalidadDTO>();
		LocalidadDTO localidadDTO = new LocalidadDTO(3L, 1L, 2L, "localidadPrueba", "34.2649652,-57.6199629");
		localidadesDTO.add(localidadDTO);
		ciudadDTO.setLocalidades(localidadesDTO);
		//arriba DTO abajo entidad
		Ciudad ciudad = new Ciudad(1L, 2L, "ciudadPrueba", null, null, "34.2649652,-57.6199629");
		List<Localidad> localidades = new ArrayList<Localidad>();
		Localidad localidad = new Localidad(3L, 1L, 2L, "localidadPrueba", ciudad, "34.2649652,-57.6199629");
		localidades.add(localidad);
		ciudad.setLocalidades(localidades);
		Mockito.when(departamentoServiceI.ciudadConverter.fromDTO(ciudadDTO)).thenReturn(ciudad);
		Mockito.when(departamentoServiceI.departamentoDAO.crearCiudad(ciudad)).thenReturn(ciudad);
		Mockito.when(departamentoServiceI.ciudadConverter.fromEntity(ciudad)).thenReturn(ciudadDTO);

		try {
			CiudadDTO obtenido = departamentoServiceI.crearCiudad(ciudadDTO);
			assertEquals(obtenido, ciudadDTO);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCrearLocalidad() {
		LocalidadDTO localidadDTO = new LocalidadDTO(3L, 1L, 2L, "localidadPrueba", "34.2649652,-57.6199629");
		//arriba DTO abajo entidad
		Ciudad ciudad = new Ciudad(1L, 2L, "ciudadPrueba", null, null, "34.2649652,-57.6199629");
		List<Localidad> localidades = new ArrayList<Localidad>();
		Localidad localidad = new Localidad(3L, 1L, 2L, "localidadPrueba", ciudad, "34.2649652,-57.6199629");
		localidades.add(localidad);
		ciudad.setLocalidades(localidades);

		Mockito.when(departamentoServiceI.localidadConverter.fromDTO(localidadDTO)).thenReturn(localidad);
		Mockito.when(departamentoServiceI.departamentoDAO.crearLocalidad(localidad)).thenReturn(localidad);
		Mockito.when(departamentoServiceI.localidadConverter.fromEntity(localidad)).thenReturn(localidadDTO);

		try {
			LocalidadDTO obtenido = departamentoServiceI.crearLocalidad(localidadDTO);
			assertEquals(obtenido, localidadDTO);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCiudadPorId() {
		Ciudad ciudad = new Ciudad(1L, 2L, "ciudadPrueba", null, null, "34.2649652,-57.6199629");
		List<Localidad> localidades = new ArrayList<Localidad>();
		Localidad localidad = new Localidad(3L, 1L, 2L, "localidadPrueba", ciudad, "34.2649652,-57.6199629");
		localidades.add(localidad);
		ciudad.setLocalidades(localidades);
		//arriba entidad abajo DTO
		CiudadDTO ciudadDTO = new CiudadDTO(1L, 2L, "ciudadPrueba", "34.2649652,-57.6199629", null);
		List<LocalidadDTO> localidadesDTO = new ArrayList<LocalidadDTO>();
		LocalidadDTO localidadDTO = new LocalidadDTO(3L, 1L, 2L, "localidadPrueba", "34.2649652,-57.6199629");
		localidadesDTO.add(localidadDTO);
		ciudadDTO.setLocalidades(localidadesDTO);

		Mockito.when(departamentoServiceI.departamentoDAO.ciudadPorId(1L, 2L)).thenReturn(ciudad);
		Mockito.when(departamentoServiceI.ciudadConverter.fromEntity(ciudad)).thenReturn(ciudadDTO);

		try {
			CiudadDTO obtenido = departamentoServiceI.ciudadPorId(1L, 2L);
			assertEquals(obtenido, ciudadDTO);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLocalidadPorId() {
		Ciudad ciudad = new Ciudad(1L, 2L, "ciudadPrueba", null, null, "34.2649652,-57.6199629");
		List<Localidad> localidades = new ArrayList<Localidad>();
		Localidad localidad = new Localidad(3L, 1L, 2L, "localidadPrueba", ciudad, "34.2649652,-57.6199629");
		localidades.add(localidad);
		ciudad.setLocalidades(localidades);
		//arriba entidad abajo DTO
		LocalidadDTO localidadDTO = new LocalidadDTO(3L, 1L, 2L, "localidadPrueba", "34.2649652,-57.6199629");

		Mockito.when(departamentoServiceI.departamentoDAO.localidadPorId(1L, 2L, 3L)).thenReturn(localidad);
		Mockito.when(departamentoServiceI.localidadConverter.fromEntity(localidad)).thenReturn(localidadDTO);

		try {
			LocalidadDTO obtenido = departamentoServiceI.localidadPorId(1L, 2L, 3L);
			assertEquals(obtenido, localidadDTO);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = AppettitException.class)
	public void testListarPorId_null () throws AppettitException {
		Departamento departamento = null;
		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(1L)).thenReturn(departamento);
		@SuppressWarnings("unused")
		DepartamentoDTO dep = departamentoServiceI.buscarPorId(1L);
	}

	@Test(expected = AppettitException.class)
	public void testEliminar_null () throws AppettitException {
		Departamento departamento = null;
		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(1L)).thenReturn(departamento);
		departamentoServiceI.eliminar(1L);
	}

	@Test(expected = AppettitException.class)
	public void testEditar_null () throws AppettitException {
		LocalidadDTO localidaddto = new LocalidadDTO(2L, 1234L, 1L, "localidadDTOPrueba", "34.2649652,-57.6199629");
		List<LocalidadDTO> localidadesDTOprueba = new ArrayList<LocalidadDTO>();
		localidadesDTOprueba.add(localidaddto);
		CiudadDTO ciudaddto = new CiudadDTO(1234L, 1L, "ciudadDTOPrueba", "34.2649652,-57.6199629", localidadesDTOprueba);
		List<CiudadDTO> ciudadesDTOprueba = new ArrayList<CiudadDTO>();
		ciudadesDTOprueba.add(ciudaddto);
		DepartamentoDTO deptoDtoCreado = new DepartamentoDTO(1L, "deptoDTOCreado", "34.2649652,-57.6199629", ciudadesDTOprueba);
		//arriba DTO abajo entidad
		Localidad localidad = new Localidad(3L, 4L, 5L, "localidadPrueba", null, "34.2649652,-57.6199629");

		Mockito.when(departamentoServiceI.departamentoDAO.buscarPorId(deptoDtoCreado.getId())).thenReturn(null);
		departamentoServiceI.editar(deptoDtoCreado);
	}

	@Test(expected = AppettitException.class)
	public void testCiudadPorId_null() throws AppettitException {
		Mockito.when(departamentoServiceI.departamentoDAO.ciudadPorId(1L, 2L)).thenReturn(null);
		departamentoServiceI.ciudadPorId(1L, 2L);
	}

	@Test(expected = AppettitException.class)
	public void testLocalidadPorId_null() throws AppettitException {
		Mockito.when(departamentoServiceI.departamentoDAO.localidadPorId(1L, 2L, 3L)).thenReturn(null);
		departamentoServiceI.localidadPorId(1L, 2L, 3L);
	}
}


