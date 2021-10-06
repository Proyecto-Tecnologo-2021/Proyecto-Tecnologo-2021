package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.exception.AppettitException;


@Local
public interface IDepartamentoService {
	public DepartamentoDTO crear(DepartamentoDTO departamentoDTO) throws AppettitException;
	public DepartamentoDTO editar(DepartamentoDTO departamentoDTO) throws AppettitException;
	public void eliminar(Long id) throws AppettitException;
	public List<DepartamentoDTO> listar() throws AppettitException;
	public DepartamentoDTO buscarPorId(Long id) throws AppettitException;
	public List<DepartamentoDTO> buscarPorNombre(String nombre) throws AppettitException;
	
}
