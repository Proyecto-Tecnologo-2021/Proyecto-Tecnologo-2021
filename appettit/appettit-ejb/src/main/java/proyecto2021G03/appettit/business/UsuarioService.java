package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import at.favre.lib.crypto.bcrypt.BCrypt;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class UsuarioService implements IUsuarioService {

	@EJB
	public IUsuarioDAO usrDAO;

	@EJB
	public UsuarioConverter usrConverter;

	@EJB
	IImagenService imgSrv;

	@Override
	public void eliminar(Long id) throws AppettitException {
		// se valida que el usuario exista
		Usuario usuario = usrDAO.buscarPorId(id);
		if (usuario == null)
			throw new AppettitException("El usuario indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			usrDAO.eliminar(usuario);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<UsuarioDTO> listar() throws AppettitException {
		try {
			return usrConverter.fromEntity(usrDAO.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public UsuarioDTO buscarPorId(Long id) throws AppettitException {
		Usuario usuario = usrDAO.buscarPorId(id);
		if (usuario == null)
			throw new AppettitException("El usuario indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return usrConverter.fromEntity(usuario);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<UsuarioDTO> buscarPorNombre(String nombre) throws AppettitException {
		try {
			return usrConverter.fromEntity(usrDAO.buscarPorNombre(nombre));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<AdministradorDTO> listarAdminsitradores() throws AppettitException {
		try {
			return usrConverter.fromAdministrador(usrDAO.listarAdministradores());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<AdministradorDTO> buscarPorNombreAdministrador(String nombre) throws AppettitException {
		try {
			return usrConverter.fromAdministrador(usrDAO.buscarPorNombreAdministrador(nombre));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public AdministradorDTO crear(AdministradorDTO administradorDTO) throws AppettitException {
		Administrador usuario = usrConverter.fromAdministradorDTO(administradorDTO);

		try {
			if (usrDAO.existeCorreoTelefono(usuario.getCorreo(), usuario.getTelefono())) {
				throw new AppettitException("Teléfono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				/* Se encripta la contraseña */
				usuario.setPassword(BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray()));

				return usrConverter.fromAdministrador(usrDAO.crearAdministrador(usuario));
			}

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public RestauranteDTO crearRestaurante(RestauranteDTO restauranteDTO) throws AppettitException {
		Restaurante usuario = usrConverter.fromRestauranteDTO(restauranteDTO);

		try {
			if (usrDAO.existeCorreoTelefono(usuario.getCorreo(), usuario.getTelefono())) {
				throw new AppettitException("Teléfono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				/* Se encripta la contraseña */
				usuario.setPassword(BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray()));

				return usrConverter.fromRestaurante(usrDAO.crearRestaurante(usuario));
			}

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<RestauranteDTO> listarRestaurantes() throws AppettitException {
		List<RestauranteDTO> restaurantes = new ArrayList<RestauranteDTO>();
		try {

			Iterator<RestauranteDTO> it = usrConverter.fromRestaurante(usrDAO.listarRestaurantes()).iterator();
			while (it.hasNext()) {
				RestauranteDTO res = it.next();
				res.setCalificacion(calificcionRestaurante(res));
				ImagenDTO img = new ImagenDTO();

				if (res.getId_imagen() == null || res.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
				} else {
					img = imgSrv.buscarPorId(res.getId_imagen());
				}
				
				res.setImagen(img);
				restaurantes.add(res);
			}

			return restaurantes;

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<RestauranteDTO> buscarPorNombreRestaurante(String nombre) throws AppettitException {
		List<RestauranteDTO> restaurantes = new ArrayList<RestauranteDTO>();
		try {
			Iterator<RestauranteDTO> it = usrConverter.fromRestaurante(usrDAO.buscarPorNombreRestaurante(nombre))
					.iterator();
			while (it.hasNext()) {
				RestauranteDTO res = it.next();
				res.setCalificacion(calificcionRestaurante(res));
				restaurantes.add(res);
				ImagenDTO img = new ImagenDTO();

				if (res.getId_imagen() == null || res.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
				} else {
					img = imgSrv.buscarPorId(res.getId_imagen());
				}
				res.setImagen(img);
			}

			return restaurantes;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public CalificacionRestauranteDTO calificcionRestaurante(RestauranteDTO restauranteDTO) throws AppettitException {
		Restaurante usuario = (Restaurante) usrDAO.buscarPorId(restauranteDTO.getId());
		if (usuario == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return usrDAO.calificcionRestaurante(restauranteDTO);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public RestauranteDTO buscarPorCorreoRestaurante(String correo) throws AppettitException {
		RestauranteDTO restaurante = null;
		restaurante = usrConverter.fromRestaurante(usrDAO.buscarPorCorreoRestaurante(correo));

		if(restaurante != null) {	
			
			restaurante.setCalificacion(calificcionRestaurante(restaurante));
			
			ImagenDTO img = new ImagenDTO();

			if (restaurante.getId_imagen() == null || restaurante.getId_imagen().equals("")) {
				FileManagement fm = new FileManagement();

				img.setIdentificador("Sin Imagen");
				img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
			} else {
				img = imgSrv.buscarPorId(restaurante.getId_imagen());
			}
			restaurante.setImagen(img);
		}
		
		return restaurante;
	}

	public ClienteDTO crearCliente(ClienteDTO clienteData) throws AppettitException {
		return null;
	}

	@Override
	public List<ClienteDTO> listarClientes() throws AppettitException {
		return null;
	}

	@Override
	public List<ClienteDTO> buscarPorNombreCliente(String nombre) throws AppettitException {
		return null;
	}

	@Override
	public List<ClienteDTO> buscarPorIdCliente(String nombre) throws AppettitException {
		return null;
	}

	@Override
	public CalificacionClienteDTO clasificacionCliente(ClienteDTO restauranteDTO) throws AppettitException {
		return null;
	}

}
