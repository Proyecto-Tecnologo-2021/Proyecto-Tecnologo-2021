package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface ICategoriaService {

	public List<CategoriaDTO> listar() throws AppettitException;
	public CategoriaDTO listarPorId(Long id);
	public CategoriaDTO crear(CategoriaCrearDTO ccDTO)throws AppettitException;
	public CategoriaDTO editar(Long id, CategoriaCrearDTO ccDTO)throws AppettitException;
	public void eliminar(Long id)throws AppettitException;
}