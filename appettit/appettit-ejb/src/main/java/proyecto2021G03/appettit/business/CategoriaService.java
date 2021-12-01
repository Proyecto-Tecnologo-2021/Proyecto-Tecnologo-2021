package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.CategoriaConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class CategoriaService implements ICategoriaService{

	@EJB
	public ICategoriaDAO cDAO;
	
	@EJB
	public CategoriaConverter cConverter;
	
	@Override
	public List<CategoriaDTO> listar() throws AppettitException {
		try {
			return cConverter.fromEntity(cDAO.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public CategoriaDTO listarPorId(Long id) {
		return cConverter.fromEntity(cDAO.listarPorId(id));
	}

	@Override
	public CategoriaDTO crear(CategoriaCrearDTO ccDTO) throws AppettitException {
		if(existeNombreCategoria(ccDTO.getNombre())) {
			throw new AppettitException("Ya existe una categoria con ese nombre.", AppettitException.EXISTE_REGISTRO);
		}else {	
			try {
				Categoria categoria = cConverter.fromCrearDTO(ccDTO);
				return cConverter.fromEntity(cDAO.crear(categoria));
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public CategoriaDTO editar(Long id, CategoriaCrearDTO ccDTO) throws AppettitException {
		if(existeNombreCategoriaExcluirId (id, ccDTO.getNombre())) {
			throw new AppettitException ("Ya existe una categoria con ese nombre.", AppettitException.EXISTE_REGISTRO);
		}else {	
			try {
				Categoria categoria = cDAO.listarPorId(id);
				categoria.setNombre(ccDTO.getNombre());
				return cConverter.fromEntity(cDAO.editar(categoria));
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public void eliminar(Long id) throws AppettitException {
		/* Se valida que exista la categoría */
		Categoria categoria= cDAO.listarPorId(id);
		if(categoria == null) {
			throw new AppettitException("La categoria indicada no existe.", AppettitException.NO_EXISTE_REGISTRO);
		} else {
			try {
				cDAO.eliminar(categoria);
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	public boolean existeNombreCategoria (String nombre) {
		List<CategoriaDTO> categorias = cConverter.fromEntity(cDAO.listar());
		for (CategoriaDTO c: categorias) {
			if (c.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existeNombreCategoriaExcluirId (Long id, String nombre) {
		List<CategoriaDTO> catogorias = cConverter.fromEntity(cDAO.listar());
		for (CategoriaDTO c: catogorias) {
			if (!c.getId().equals(id)) {
				if (c.getNombre().equals(nombre)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
