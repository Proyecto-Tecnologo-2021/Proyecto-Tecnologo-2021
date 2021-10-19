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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(String id) throws AppettitException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ImagenDTO> listar() throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImagenDTO buscarPorId(String id) throws AppettitException {
		// TODO Auto-generated method stub
		return null;
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
