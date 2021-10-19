package proyecto2021G03.appettit.business;

import java.util.List;

import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.exception.AppettitException;


public interface IImagenService {
	public ImagenDTO crear(ImagenDTO imagenDTO) throws AppettitException;
	public ImagenDTO editar(ImagenDTO imagenDTO) throws AppettitException;
	public void eliminar(String id) throws AppettitException;
	public List<ImagenDTO> listar() throws AppettitException;
	public ImagenDTO buscarPorId(String id) throws AppettitException;
	public ImagenDTO buscarPorIdentificador(String id) throws AppettitException;
	
}
