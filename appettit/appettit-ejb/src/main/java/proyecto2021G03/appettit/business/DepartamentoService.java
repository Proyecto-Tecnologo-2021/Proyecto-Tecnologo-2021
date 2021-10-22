package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.CiudadConverter;
import proyecto2021G03.appettit.converter.DepartamentoConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.dao.IDepartamentoDAO;
import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class DepartamentoService implements IDepartamentoService {

	@EJB
	public IDepartamentoDAO departamentoDAO;

	@EJB
	public DepartamentoConverter departamentoConverter;
	
	@EJB
	public LocalidadConverter localidadConverter;
	
	@EJB
	public CiudadConverter ciudadConverter;
	

	@Override
	public DepartamentoDTO crear(DepartamentoDTO departamentoDTO) throws AppettitException {
		Departamento departamento = departamentoConverter.fromDTO(departamentoDTO);
		try {
			return departamentoConverter.fromEntity(departamentoDAO.crear(departamento));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DepartamentoDTO editar(DepartamentoDTO departamentoDTO) throws AppettitException {
		// se valida que el departamento exista
		Departamento departamento = departamentoDAO.buscarPorId(departamentoDTO.getId());
		if (departamento == null)
			throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {

			departamento.setNombre(departamentoDTO.getNombre());
			return departamentoConverter.fromEntity(departamentoDAO.editar(departamento));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public void eliminar(Long id) throws AppettitException {
		// se valida que el departamento exista
		Departamento departamento = departamentoDAO.buscarPorId(id);
		if (departamento == null)
			throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			departamentoDAO.eliminar(departamento);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public List<DepartamentoDTO> listar() throws AppettitException {
		try {
			return departamentoConverter.fromEntity(departamentoDAO.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DepartamentoDTO buscarPorId(Long id) throws AppettitException {
		// se valida que el departamento exista
		Departamento departamento = departamentoDAO.buscarPorId(id);
		if (departamento == null)
			throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return departamentoConverter.fromEntity(departamento);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DepartamentoDTO> buscarPorNombre(String nombre) throws AppettitException {
		try {
			return departamentoConverter.fromEntity(departamentoDAO.buscarPorNombre(nombre));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public CiudadDTO crearCiudad(CiudadDTO ciudadDTO) throws AppettitException {
		Ciudad ciudad = ciudadConverter.fromDTO(ciudadDTO);
		try {
			return ciudadConverter.fromEntity(departamentoDAO.crearCiudad(ciudad));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public LocalidadDTO crearLocalidad(LocalidadDTO localidadDTO) throws AppettitException {
		Localidad localidad = localidadConverter.fromDTO(localidadDTO);
		try {
			return localidadConverter.fromEntity(departamentoDAO.crearLocalidad(localidad));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public CiudadDTO ciudadPorId(Long id, Long id_departamento) throws AppettitException {
		Ciudad ciudad = departamentoDAO.ciudadPorId(id, id_departamento);
		if (ciudad == null)
			throw new AppettitException("La ciuduad indicada no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return ciudadConverter.fromEntity(ciudad);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public LocalidadDTO localidadPorId(Long id, Long id_ciudad, Long id_departamento) throws AppettitException {
		Localidad localidad = departamentoDAO.localidadPorId(id, id_ciudad, id_departamento);
		if (localidad == null)
			throw new AppettitException("La localidad indicada no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return localidadConverter.fromEntity(localidad);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

}
