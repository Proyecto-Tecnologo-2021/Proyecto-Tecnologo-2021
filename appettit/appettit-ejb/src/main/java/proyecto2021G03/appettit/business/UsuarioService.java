package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.LoginDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;
import proyecto2021G03.appettit.util.FileManagement;


@Stateless
public class UsuarioService implements IUsuarioService {
	
	static Logger logger = Logger.getLogger(UsuarioService.class);

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
				res.setCalificacion(calificacionRestaurante(res));
				ImagenDTO img = new ImagenDTO();

				if (res.getId_imagen() == null || res.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
				} else {
					try {
						img = imgSrv.buscarPorId(res.getId_imagen());	
					} catch (Exception e) {
						FileManagement fm = new FileManagement();

						img.setIdentificador("Sin Imagen");
						img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
						logger.error(e.getMessage());
					}
					
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
				res.setCalificacion(calificacionRestaurante(res));
				restaurantes.add(res);
				ImagenDTO img = new ImagenDTO();

				if (res.getId_imagen() == null || res.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
				} else {
					try {
						img = imgSrv.buscarPorId(res.getId_imagen());	
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}
				res.setImagen(img);
			}

			return restaurantes;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public CalificacionRestauranteDTO calificacionRestaurante(RestauranteDTO restauranteDTO) throws AppettitException {
		Restaurante usuario = (Restaurante) usrDAO.buscarPorId(restauranteDTO.getId());
		if (usuario == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return usrDAO.calificacionRestaurante(restauranteDTO);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public RestauranteDTO buscarPorCorreoRestaurante(String correo) throws AppettitException {
		RestauranteDTO restaurante = null;
		restaurante = usrConverter.fromRestaurante(usrDAO.buscarPorCorreoRestaurante(correo));

		if(restaurante != null) {	
			
			restaurante.setCalificacion(calificacionRestaurante(restaurante));
			
			ImagenDTO img = new ImagenDTO();

			if (restaurante.getId_imagen() == null || restaurante.getId_imagen().equals("")) {
				FileManagement fm = new FileManagement();

				img.setIdentificador("Sin Imagen");
				img.setImagen(fm.getFileAsByteArray("META-INF/img/restaurante.png"));
			} else {
				try {
					img = imgSrv.buscarPorId(restaurante.getId_imagen());	
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			}
			restaurante.setImagen(img);
		}
		
		return restaurante;
	}

	@Override
	public RestauranteDTO editarRestaurante(RestauranteDTO restauranteDTO) throws AppettitException {
		Restaurante restaurante = usrDAO.buscarPorCorreoRestaurante(restauranteDTO.getCorreo());
		if (restaurante == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			
			restaurante = usrConverter.fromRestauranteDTO(restauranteDTO);
			
			return usrConverter.fromRestaurante(usrDAO.editarRestaurante(restaurante));
			
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}





	@Override
	public ClienteDTO crearCliente(ClienteDTO clienteData) throws AppettitException {
		Cliente usuario = usrConverter.fromClienteDTO(clienteData);

		try {
			if (usrDAO.existeCorreoTelefono(usuario.getCorreo(), usuario.getTelefono())) {
				throw new AppettitException("Teléfono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				/* Se encripta la contraseña */
				usuario.setPassword(BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray()));

				return usrConverter.fromCliente(usrDAO.crearCliente(usuario));
			}

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<ClienteDTO> listarClientes() throws AppettitException {
		List<ClienteDTO> clientes = new ArrayList<ClienteDTO>();
		try {

			Iterator<ClienteDTO> it = usrConverter.fromCliente(usrDAO.listarClientes()).iterator();
			while (it.hasNext()) {
				ClienteDTO res = it.next();
//				res.setCalificacion(calificacionRestaurante(res));

			}

			return clientes;

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
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
	public CalificacionClienteDTO calificacionCliente(ClienteDTO restauranteDTO) throws AppettitException {
		return null;
	}

	@Override
	public String login(LoginDTO loginDTO) throws AppettitException {
		/* Se valida que exista el correo electrónico o el teléfono*/
		List<Usuario> usuarios_correo = usrDAO.buscarPorCorreo(loginDTO.getUsuario());
		List<Usuario> usuarios_telefono = usrDAO.buscarPorTelefono(loginDTO.getUsuario());
		
		if ((usuarios_correo.size() == 0) && (usuarios_telefono.size() == 0)) {
			throw new AppettitException("Usuario y/o password incorrecto.", AppettitException.DATOS_INCORRECTOS);
		} else {
			/* Se verifica que la contraseña sea válida */
			
			Usuario usuario;
			
			if (usuarios_correo.size() != 0) {
				usuario = usuarios_correo.get(0);
			}else {
				usuario = usuarios_telefono.get(0);
			}
			
			BCrypt.Result resultado = null;
			resultado = BCrypt.verifyer().verify(loginDTO.getPassword().toCharArray(), usuario.getPassword());
			if(resultado.verified) {
				String token = crearJsonWebToken(usuario);
				return token;
			} else {
				throw new AppettitException("Usuario y/o password incorrecto.", AppettitException.DATOS_INCORRECTOS);
			}
		}
	}
	
	/* Función auxiliar para generar un JWT */
	public String crearJsonWebToken(Usuario usuario) {
		Date ahora = new Date();
		/* 1 horas de validez */
		Date expiracion = new Date(ahora.getTime() + (1000*60*60));
		
		String tipoUsuario;
		
		if (usuario instanceof Cliente) {
			tipoUsuario = "cliente";
		}
		else {
			if (usuario instanceof Restaurante) { 
				tipoUsuario = "restaurante";
			}
			else {
				if (usuario instanceof Administrador) {
					tipoUsuario = "administrador";
				}
				else {
					tipoUsuario = "Error";
				}
			}
		}
		
		return Jwts.builder()
				.setSubject(Long.toString(usuario.getId()))
				.setIssuedAt(ahora)
				.setExpiration(expiracion)
				.claim("tipoUsuario", tipoUsuario)
				.claim("idUsuario", usuario.getId())
				.claim("nombre", usuario.getNombre())
				.claim("correo", usuario.getCorreo())
				.claim("telefono", usuario.getTelefono())
				.signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY)
				.compact();
	}
	
}
