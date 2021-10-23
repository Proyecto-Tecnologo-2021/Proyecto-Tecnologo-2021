package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.ImagenConverter;
import proyecto2021G03.appettit.dao.IImagenDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.entity.Imagen;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class ImagenService implements IImagenService {

	@EJB
	public IImagenDAO imagenDAO;

	@EJB
	public ImagenConverter imagenConverter;

	@Override
	public ImagenDTO crear(ImagenDTO imagenDTO) throws AppettitException {
		Imagen imagen = imagenConverter.fromDTO(imagenDTO);
		try {
			return imagenConverter.fromEntity(imagenDAO.crear(imagen));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ImagenDTO editar(ImagenDTO imagenDTO) throws AppettitException {
		Imagen imagen = imagenDAO.buscarPorId(imagenDTO.getId());
		if (imagen == null)
			throw new AppettitException("La imagen no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {
			return imagenConverter.fromEntity(imagenDAO.editar(imagen));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public void eliminar(String id) throws AppettitException {
		Imagen imagen = imagenDAO.buscarPorId(id);
		if (imagen == null)
			throw new AppettitException("La imagen no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {
			imagenDAO.eliminar(imagen);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public List<ImagenDTO> listar() throws AppettitException {
		try {
			return imagenConverter.fromEntity(imagenDAO.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ImagenDTO buscarPorId(String id) throws AppettitException {
		Imagen imagen = imagenDAO.buscarPorId(id);
		if (imagen == null)
			throw new AppettitException("La imagen no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {
			return imagenConverter.fromEntity(imagen);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ImagenDTO buscarPorIdentificador(String identificador) throws AppettitException {
		try {
			return imagenConverter.fromEntity(imagenDAO.buscarPorIdentificador(identificador));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

}
