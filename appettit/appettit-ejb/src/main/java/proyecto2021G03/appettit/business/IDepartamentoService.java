package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.exception.AppettitException;


@Local
public interface IDepartamentoService {
	public DepartamentoDTO crear(DepartamentoDTO departamentoDTO) throws AppettitException;
	public DepartamentoDTO editar(DepartamentoDTO departamentoDTO) throws AppettitException;
	public void eliminar(Long id) throws AppettitException;
	public List<DepartamentoDTO> listar() throws AppettitException;
	public DepartamentoDTO buscarPorId(Long id) throws AppettitException;
	public List<DepartamentoDTO> buscarPorNombre(String nombre) throws AppettitException;
	
	public CiudadDTO crearCiudad(CiudadDTO ciudadDTO) throws AppettitException;
	public LocalidadDTO crearLocalidad(LocalidadDTO localidadDTO) throws AppettitException;
	
	public CiudadDTO ciudadPorId(Long id, Long id_departamento) throws AppettitException;
	public LocalidadDTO localidadPorId(Long id, Long id_ciudad, Long id_departamento) throws AppettitException;

	
}
