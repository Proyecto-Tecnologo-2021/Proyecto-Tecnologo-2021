package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import at.favre.lib.crypto.bcrypt.BCrypt;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class UsuarioService implements IUsuarioService {

	@EJB
	public IUsuarioDAO usrDAO;

	@EJB
	public UsuarioConverter usrConverter;

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
		try {
			return usrConverter.fromRestaurante(usrDAO.listarRestaurantes());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<RestauranteDTO> buscarPorNombreRestaurante(String nombre) throws AppettitException {
		try {
			return usrConverter.fromRestaurante(usrDAO.buscarPorNombreRestaurante(nombre));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

}
