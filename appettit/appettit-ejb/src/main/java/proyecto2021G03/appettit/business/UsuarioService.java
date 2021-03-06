package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.vividsolutions.jts.io.ParseException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import proyecto2021G03.appettit.converter.DireccionConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.entity.Localidad;
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
	public IImagenService imgSrv;

	@EJB
	DireccionConverter dirConverter;

	@EJB
	LocalidadConverter locConverter;

	@EJB
	IGeoService geoSrv;

	@EJB
	ITokenService iTokenService;

	@EJB
	IMailService iMailService;
	
	@EJB
	IGeoDAO geoDAO;
	

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
				throw new AppettitException("Tel??fono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				/* Se encripta la contrase??a */
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
				throw new AppettitException("Tel??fono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				/* Se encripta la contrase??a */
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
				res.setCalificacion(calificacionRestaurante(res.getId()));
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
    public List<RestauranteRDTO> listarRestaurantesAbiertos() throws AppettitException {
        List<RestauranteRDTO> restaurantes = new ArrayList<RestauranteRDTO>();
        try {
            Iterator<RestauranteRDTO> it = usrConverter.RDTOfromRestaurante(usrDAO.listarRestaurantesAbiertos()).iterator();
            while (it.hasNext()) {
                RestauranteRDTO res = it.next();
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
                res.setCalificacion(calificacionRestaurante(res.getId()));
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
				res.setCalificacion(calificacionRestaurante(res.getId()));
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
						logger.error("Find Restaurante IMG: " + e.getMessage());
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
	public CalificacionGralRestauranteDTO calificacionRestaurante(Long id) throws AppettitException {
		Restaurante usuario = (Restaurante) usrDAO.buscarRestaurantePorId(id);
		if (usuario == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return usrDAO.calificacionRestaurante(id);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public RestauranteDTO buscarPorCorreoRestaurante(String correo) throws AppettitException {
		RestauranteDTO restaurante = null;
		restaurante = usrConverter.fromRestaurante(usrDAO.buscarPorCorreoRestaurante(correo));

		if (restaurante != null) {

			restaurante.setCalificacion(calificacionRestaurante(restaurante.getId()));

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
			return restaurante;
		} else {
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		}

	
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
	public RestauranteDTO abrirRestaurante(Long id) throws AppettitException {
		Restaurante restaurante = usrDAO.buscarRestaurantePorId(id);
		if (restaurante == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			restaurante.setAbierto(true);
			return usrConverter.fromRestaurante(usrDAO.editarRestaurante(restaurante));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}
	
	@Override
	public RestauranteDTO cerrarRestaurante(Long id) throws AppettitException {
		Restaurante restaurante = usrDAO.buscarRestaurantePorId(id);
		if (restaurante == null)
			throw new AppettitException("El restaurante indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			restaurante.setAbierto(false);
			return usrConverter.fromRestaurante(usrDAO.editarRestaurante(restaurante));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ClienteMDTO crearCliente(ClienteCrearDTO clienteData) throws AppettitException, ParseException {
		List<DireccionDTO> direcciones = null;
		if (clienteData.getDireccion() != null) {
			LocalidadDTO barrio = geoSrv.localidadPorPunto(clienteData.getDireccion().getGeometry());
			DireccionDTO dirDTO = DireccionDTO.builder().alias(clienteData.getDireccion().getAlias())
					.apartamento(clienteData.getDireccion().getApartamento())
					.calle(clienteData.getDireccion().getCalle()).numero(clienteData.getDireccion().getNumero())
					.referencias(clienteData.getDireccion().getReferencias())
					.geometry(clienteData.getDireccion().getGeometry()).barrio(barrio).quantity(0).build();
			direcciones = new ArrayList<DireccionDTO>();
			direcciones.add(dirDTO);
		}
		ClienteDTO cliente = new ClienteDTO(null, clienteData.getNombre(), clienteData.getUsername(),
				clienteData.getPassword(), clienteData.getTelefono(), clienteData.getCorreo(),
				clienteData.getTokenFireBase(), false, direcciones, null);
		Cliente usuario = usrConverter.fromClienteDTO(cliente);
		usuario.setNotificationFirebase(clienteData.getNotificationFirebase());

		try {
			if (usrDAO.existeCorreoTelefono(usuario.getCorreo(), usuario.getTelefono())) {
				throw new AppettitException("Tel??fono y/o correo ya registrado.", AppettitException.EXISTE_REGISTRO);
			} else {
				usuario.setPassword(BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray()));
				ClienteMDTO clienteMDTO = usrConverter.ClienteMDTOfromCliente(usrDAO.crearCliente(usuario));
				ClienteDTO ret = usrConverter.fromCliente(usrDAO.crearCliente(usuario));
				clienteMDTO.setCalificacion(usrDAO.calificacionGralCliente(ret.getId()));
				String token = crearJsonWebToken(usuario);
				clienteMDTO.setJwt(token);
				return clienteMDTO;
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public String editarCliente(Long id, ClienteModificarDTO clienteData) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(id);
		if (cliente == null)
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			cliente.setNombre(clienteData.getNombre());
			cliente.setTelefono(clienteData.getTelefono());
			cliente.setUsername(clienteData.getUsername());

			//ClienteDTO ret = usrConverter.fromCliente(usrDAO.editarCliente(cliente));
			String token = this.crearJsonWebToken(cliente);

			return token;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ClienteMDTO editarClienteRE(Long id, ClienteModificarDTO clienteData) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(id);
		if (cliente == null)
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {

			cliente.setNombre(clienteData.getNombre());
			cliente.setTelefono(clienteData.getTelefono());
			cliente.setUsername(clienteData.getUsername());
			ClienteDTO ret = usrConverter.fromCliente(usrDAO.editarCliente(cliente));
			ret.setCalificacion(usrDAO.calificacionGralCliente(ret.getId()));
			ClienteMDTO clienteMDTO = usrConverter.ClienteMDTOfromCliente(cliente);
			String token = crearJsonWebToken(cliente);
			clienteMDTO.setJwt(token);
			return clienteMDTO;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ClienteDTO editarDireccion(Long id, DireccionCrearDTO direccionDTO) throws AppettitException {
		List<Cliente> clientes = usrDAO.buscarPorIdClienteInteger(direccionDTO.getId_cliente());
		try {
			if (clientes.size() == 0) {
				throw new AppettitException("No existe el cliente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				Cliente cliente = clientes.get(0);
				List<Direccion> direcciones = cliente.getDirecciones();
				// obtengo la direccion
				Boolean existe_direccion = false;
				Direccion direccion = null;
				for (Direccion d : direcciones) {
					if (d.getId().compareTo(id) == 0) {
						existe_direccion = true;
						direccion = d;
					}
				}
				if (!existe_direccion) {
					throw new AppettitException("Direccion invalida para el cliente.",
							AppettitException.NO_EXISTE_REGISTRO);
				} else {
					Boolean alias_existente = false;
					for (Direccion d : direcciones) {
						if ((d.getAlias().equals(direccionDTO.getAlias())) && (d.getId().compareTo(id) != 0)) {
							alias_existente = true;
							break;
						}
					}
					if (alias_existente) {
						throw new AppettitException("Alias repetido.", AppettitException.EXISTE_REGISTRO);
					} else {
						Localidad barrio = locConverter.fromDTO(geoSrv.localidadPorPunto(direccionDTO.getGeometry()));
						direccion.setAlias(direccionDTO.getAlias());
						direccion.setApartamento(direccionDTO.getApartamento());
						direccion.setBarrio(barrio);
						direccion.setCalle(direccionDTO.getCalle());
						direccion.setGeometry(direccionDTO.getGeometry());
						direccion.setNumero(direccionDTO.getNumero());
						direccion.setReferencias(direccionDTO.getReferencias());

						ClienteDTO ret = usrConverter.fromCliente(usrDAO.editarCliente(cliente));
						ret.setCalificacion(usrDAO.calificacionGralCliente(ret.getId()));

						return ret;
					}
				}
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public ClienteDTO eliminarDireccion(Long id_direccion, EliminarDeClienteDTO ec) throws AppettitException {

		List<Cliente> clientes = usrDAO.buscarPorIdClienteInteger(ec.getId_cliente());
		try {
			if (clientes.size() == 0) {
				throw new AppettitException("No existe el cliente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				Cliente cliente = clientes.get(0);
				List<Direccion> direcciones = cliente.getDirecciones();
				// obtengo la direccion
				Boolean existe_direccion = false;
				Direccion direccion = null;
				for (Direccion d : direcciones) {
					if (d.getId().compareTo(id_direccion) == 0) {
						existe_direccion = true;
						direccion = d;
					}
				}
				if (!existe_direccion) {
					throw new AppettitException("Direccion invalida para el cliente.",
							AppettitException.NO_EXISTE_REGISTRO);
				} else {
					direcciones.remove(direccion);

					ClienteDTO ret = usrConverter.fromCliente(usrDAO.eliminarDireccion(cliente, direccion));
					ret.setCalificacion(usrDAO.calificacionGralCliente(ret.getId()));
					return ret;
				}
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public ClienteDTO agregarDireccion(DireccionCrearDTO direccion) throws AppettitException {

		List<Cliente> usuarios = usrDAO.buscarPorIdClienteInteger(direccion.getId_cliente());

		try {
			if (usuarios.size() == 0) {
				throw new AppettitException("No existe el cliente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				Cliente usuario = usuarios.get(0);

				Boolean alias_repetido = existeAlias(usuario, direccion.getAlias());

				if (alias_repetido) {
					throw new AppettitException("Alias ingresado previamente para el cliente.",
							AppettitException.EXISTE_REGISTRO);
				} else {

					List<Direccion> direcciones = usuario.getDirecciones();

					LocalidadDTO barrio = geoSrv.localidadPorPunto(direccion.getGeometry());

					DireccionDTO dirDTO = DireccionDTO.builder().alias(direccion.getAlias())
							.apartamento(direccion.getApartamento()).calle(direccion.getCalle())
							.numero(direccion.getNumero()).referencias(direccion.getReferencias())
							.geometry(direccion.getGeometry()).barrio(barrio).quantity(0).build();

					Direccion nueva = dirConverter.fromDTO(dirDTO);
					direcciones.add(nueva);

					ClienteDTO ret = usrConverter.fromCliente(usrDAO.agregarDireccion(usuario));
					ret.setCalificacion(usrDAO.calificacionGralCliente(ret.getId()));
					return ret;
				}
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
				res.setCalificacion(usrDAO.calificacionGralCliente(res.getId()));

				clientes.add(res);
			}

			return clientes;

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<ClienteDTO> buscarPorNombreCliente(String nombre) throws AppettitException {
		List<ClienteDTO> clientes = new ArrayList<ClienteDTO>();
		try {

			Iterator<ClienteDTO> it = usrConverter.fromCliente(usrDAO.buscarPorNombreCliente(nombre)).iterator();
			while (it.hasNext()) {
				ClienteDTO res = it.next();
				res.setCalificacion(usrDAO.calificacionGralCliente(res.getId()));

				clientes.add(res);
			}

			return clientes;

		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ClienteDTO buscarPorIdCliente(Long id) throws AppettitException {
		ClienteDTO cliente = null;
		cliente = usrConverter.fromCliente(usrDAO.buscarPorIdCliente(id));

		if (cliente != null) {
			cliente.setCalificacion(usrDAO.calificacionGralCliente(id));
		}

		return cliente;
	}

	@Override
	public CalificacionGralClienteDTO calificacionGralCliente(Long id) throws AppettitException {
		Cliente usuario = usrDAO.buscarPorIdCliente(id);
		if (usuario == null)
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			return usrDAO.calificacionGralCliente(id);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public String login(LoginDTO loginDTO) throws AppettitException {
		/* Se valida que exista el correo electr??nico o el tel??fono */
		List<Usuario> usuarios_correo = usrDAO.buscarPorCorreo(loginDTO.getUsuario());
		List<Usuario> usuarios_telefono = usrDAO.buscarPorTelefono(loginDTO.getUsuario());

		if ((usuarios_correo.size() == 0) && (usuarios_telefono.size() == 0)) {
			throw new AppettitException("Usuario y/o password incorrecto.", AppettitException.DATOS_INCORRECTOS);
		} else {
			/* Se verifica que la contrase??a sea v??lida */

			Usuario usuario;
			
			if (usuarios_correo.size() != 0) {
				usuario = usuarios_correo.get(0);
			} else {
				usuario = usuarios_telefono.get(0);
			}

			BCrypt.Result resultado = null;
			resultado = BCrypt.verifyer().verify(loginDTO.getPassword().toCharArray(), usuario.getPassword());
			if (resultado.verified) {
				String token = crearJsonWebToken(usuario);
				return token;
			} else {
				throw new AppettitException("Usuario y/o password incorrecto.", AppettitException.DATOS_INCORRECTOS);
			}
		}
	}

	@Override
	public ClienteMDTO loginMobile(LoginDTO loginDTO) throws AppettitException {
		/* Se valida que exista el correo electr??nico o el tel??fono */
		List<Usuario> usuarios_correo = usrDAO.buscarPorCorreo(loginDTO.getUsuario());
		List<Usuario> usuarios_telefono = usrDAO.buscarPorTelefono(loginDTO.getUsuario());
		Boolean existe = false;
		Usuario usuario;
		
		if(usuarios_correo.size() == 0) {
			if(usuarios_telefono.size() == 0) {
				throw new AppettitException("Usuario inexistente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				usuario = usuarios_telefono.get(0);
				if (usuario instanceof Cliente)
					existe = true;
			}
		} else {
			usuario = usuarios_correo.get(0);
			if (usuario instanceof Cliente)
				existe = true;
		}
		if (existe) {
			Cliente cliente = (Cliente) usuario;
			BCrypt.Result resultado = null;
			resultado = BCrypt.verifyer().verify(loginDTO.getPassword().toCharArray(), usuario.getPassword());
			if (resultado.verified) {
				usuario.setNotificationFirebase(loginDTO.getNotificationFirebase());
				usrDAO.editarCliente((Cliente) usuario);
				ClienteDTO clienteDTO = usrConverter.fromCliente(cliente);
				CalificacionGralClienteDTO califDTO = usrDAO.calificacionGralCliente(clienteDTO.getId());
				ClienteMDTO clienteMDTO = usrConverter.ClienteMDTOfromCliente(cliente);
				String token = crearJsonWebToken(usuario);
				clienteMDTO.setCalificacion(califDTO);
				clienteMDTO.setJwt(token);
				return clienteMDTO;
			} else {
				throw new AppettitException("Password incorrecto.",
						AppettitException.DATOS_INCORRECTOS);
			}
		} else {
			throw new AppettitException("Usuario y/o password incorrecto.", AppettitException.DATOS_INCORRECTOS);
		}
	}

	@Override
	public ClienteMDTO loginFireBase(LoginDTO loginDTO) throws AppettitException {
		/* Se valida que exista el correo electr??nico */
		List<Usuario> usuarios_correo = usrDAO.buscarPorCorreo(loginDTO.getUsuario());

		if ((usuarios_correo.size() == 0)) {
			throw new AppettitException("Usuario no registrado.", AppettitException.DATOS_INCORRECTOS);
		} else {
			Usuario usuario = usuarios_correo.get(0);
			if (usuario instanceof Cliente) {
				usuario.setTokenFireBase(loginDTO.getPassword());
				usuario.setNotificationFirebase(loginDTO.getNotificationFirebase());
				usrDAO.editarCliente((Cliente) usuario);
				ClienteDTO clienteDTO = usrConverter.fromCliente((Cliente) usuario);
				CalificacionGralClienteDTO califDTO = usrDAO.calificacionGralCliente(clienteDTO.getId());
				ClienteMDTO clienteMDTO = usrConverter.ClienteMDTOfromCliente((Cliente) usuario);
				clienteMDTO.setCalificacion(califDTO);
				String token = crearJsonWebToken(usuario);
				clienteMDTO.setJwt(token);
				return clienteMDTO;
			} else {
				throw new AppettitException("Usuario no habilitado para mobile.", AppettitException.DATOS_INCORRECTOS);
			}
		}
	}

	@Override
	public String loginGoogle(String correo, String nombre) throws AppettitException {
		Boolean exist = existeCorreoUsuario(correo);
		if (!exist) {
			//SI NO EXISTE LE CREO UN USUARIO
			ClienteCrearDTO nuevoCliente = new ClienteCrearDTO();
			nuevoCliente.setCorreo(correo);
			nuevoCliente.setNombre(nombre);
			nuevoCliente.setUsername(correo);
			nuevoCliente.setPassword("");
			nuevoCliente.setTelefono(correo);
			try {
				crearCliente(nuevoCliente);
			} catch (ParseException e) {
				logger.error("loginGoogle: " + e.getMessage());
				//e.printStackTrace();
			}
		}
		List<Usuario> usuarios_correo = usrDAO.buscarPorCorreo(correo);
		Usuario usuario = usuarios_correo.get(0);
		if (usuario instanceof Cliente) {
			String token = crearJsonWebToken(usuario);
			return token;
		} else {
			throw new AppettitException("Usuario no habilitado.", AppettitException.DATOS_INCORRECTOS);
		}
	}

	/* Funci??n auxiliar para generar un JWT */
	public String crearJsonWebToken(Usuario usuario) {
		Date ahora = new Date();
		/* 1 horas de validez */
		Date expiracion = new Date(ahora.getTime() + (1000 * 60 * 60));
		String tipoUsuario;

		if (usuario instanceof Cliente) {
			tipoUsuario = "cliente";
		} else {
			if (usuario instanceof Restaurante) {
				tipoUsuario = "restaurante";
			} else {
				if (usuario instanceof Administrador) {
					tipoUsuario = "administrador";
				} else {
					tipoUsuario = "Error";
				}
			}
		}

		return Jwts.builder().setSubject(Long.toString(usuario.getId())).setIssuedAt(ahora).setExpiration(expiracion)
				.claim("tipoUsuario", tipoUsuario).claim("idUsuario", usuario.getId())
				.claim("userName", usuario.getUsername()).claim("nombre", usuario.getNombre())
				.claim("correo", usuario.getCorreo()).claim("telefono", usuario.getTelefono())
				.signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY).compact();
	}

	public Boolean existeAlias(Cliente cliente, String alias) {

		List<Direccion> direcciones = cliente.getDirecciones();
		for (Direccion d : direcciones) {
			if (d.getAlias().equals(alias)) {
				return true;
			}
		}
		return false;
	}

	public Long obtenerIdDireccion(Long idUser, String alias) throws AppettitException {

		List<Cliente> clientes = usrDAO.buscarPorIdClienteInteger(idUser);
		try {
			if (clientes.size() == 0) {
				throw new AppettitException("No existe el cliente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				Cliente cliente = clientes.get(0);
				List<Direccion> direcciones = cliente.getDirecciones();
				// obtengo la direccion
				Boolean existe_direccion = false;
				Long idDireccion = null;
				for (Direccion d : direcciones) {
					if (d.getAlias().compareTo(alias) == 0) {
						existe_direccion = true;
						idDireccion = d.getId();
					}
				}
				if (!existe_direccion) {
					throw new AppettitException("La direccion con ese alias no existe para el cliente.", AppettitException.NO_EXISTE_REGISTRO);
				} else {
					return idDireccion;
				}
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	public List<DireccionDTO> obtenerDireccionesCliente(Long idUser) throws AppettitException {

		List<Cliente> clientes = usrDAO.buscarPorIdClienteInteger(idUser);
		try {
			if (clientes.size() == 0) {
				throw new AppettitException("No existe el cliente.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				Cliente cliente = clientes.get(0);
				List<Direccion> direcciones = cliente.getDirecciones();
				List<DireccionDTO> direccionesDTO = new ArrayList<>();
				Boolean existe_direccion = false;

				if (direcciones != null) {
					existe_direccion = true;
					for (Direccion d : direcciones) {
						direccionesDTO.add(dirConverter.fromEntity(d));
					}
				}
				if (!existe_direccion) {
					throw new AppettitException("El cliente no tiene direcciones.",
							AppettitException.NO_EXISTE_REGISTRO);
				} else {
					return direccionesDTO;
				}
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public AdministradorDTO buscarAdministradorPorId(Long id) throws AppettitException {
		try {
			return usrConverter.fromAdministrador(usrDAO.buscarAdministradorPorId(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}

	}

	@Override
	public RestauranteDTO buscarRestaurantePorId(Long id) throws AppettitException {
		try {
			RestauranteDTO res = usrConverter.fromRestaurante(usrDAO.buscarRestaurantePorId(id));
			
			res.setCalificacion(calificacionRestaurante(res.getId()));
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
			
			return res;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public RestauranteRDTO buscarRestaurantePorIdBasico(Long id) throws AppettitException {
		try {
			Restaurante restaurante = usrDAO.buscarRestaurantePorId(id);
			if (restaurante == null) {
				throw new AppettitException("No existe el restaurante.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				RestauranteRDTO res = usrConverter.RDTOfromRestaurante(restaurante);
				res.setCalificacion(calificacionRestaurante(res.getId()));
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
				
				return res;
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}
	
	@Override
	public DireccionDTO buscarDireccionPorId(Long id) throws AppettitException {
		try {
			return dirConverter.fromEntity(usrDAO.buscarDireccionPorId(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public void bloquearCliente(Long id) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(id);
		if (cliente == null)
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			cliente.setBloqueado(true);
			@SuppressWarnings("unused")
			ClienteDTO ret = usrConverter.fromCliente(usrDAO.editarCliente(cliente));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}
	
	@Override
	public void desbloquearCliente(Long id) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(id);
		if (cliente == null)
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		try {
			cliente.setBloqueado(false);
			@SuppressWarnings("unused")
			ClienteDTO ret = usrConverter.fromCliente(usrDAO.editarCliente(cliente));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public void solicitarCorreoVerificador(MailDTO correo) throws AppettitException {
		List<Usuario> usuarios = usrDAO.buscarPorCorreo(correo.getCorreo());
		Usuario user = null;
		if(usuarios.size() == 0) {
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		}else {
			for(Usuario usr : usuarios){
				user = usr;
			}
			try {
				//GENERADOR DE JWT
				String token = crearJsonWebToken(user);
				//GENERADOR DE LINK
				String link = Constantes.VERIFY_LINK + token;
				//AGREGO EL RECEPTOR Y EL LINK (Q SE AGREGA EN EL BODY DEL MAIL)
				iMailService.sendMail(correo.getCorreo(), link);
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public void cambioContrase??a(String password, Long clientId) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(clientId);
		if(cliente == null){
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		}else {
			try {
			cliente.setPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
			usrDAO.editarCliente(cliente);

			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public void setFirebaseTokenWeb(String webToken, Long clientId) throws AppettitException {
		Cliente cliente = usrDAO.buscarPorIdCliente(clientId);
		if(cliente == null){
			throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		}else {
			try {
				cliente.setNotificationFirebaseWeb(webToken);
				usrDAO.editarCliente(cliente);

			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public ClienteDTO buscarPorCorreoCliente(String correo) throws AppettitException {
		//ClienteDTO cliente = null;
		try {
			ClienteDTO clienteDTO = usrConverter.fromCliente(usrDAO.buscarPorCorreoCliente(correo));
			return clienteDTO;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<RestauranteRDTO> listarRestaurantesPorPunto(String punto) throws AppettitException {
		List<RestauranteRDTO> restaurantes = new ArrayList<RestauranteRDTO>();
        try {
            Iterator<RestauranteRDTO> it = usrConverter.RDTOfromRestaurante(geoDAO.repartoRestaurantesPorPunto(punto)).iterator();
            while (it.hasNext()) {
                RestauranteRDTO res = it.next();
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
                res.setCalificacion(calificacionRestaurante(res.getId()));
                restaurantes.add(res);
            }
            return restaurantes;
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
	}

	@Override
	public Boolean existeCorreoUsuario(String correo){
		return usrDAO.existeCorreo(correo);
	}
	
}
