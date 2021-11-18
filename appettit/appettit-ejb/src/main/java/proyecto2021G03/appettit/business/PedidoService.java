package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.ExtraMenuConverter;
import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.converter.PedidoRConverter;
import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.PedidoRMDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Reclamo;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class PedidoService implements IPedidoService {

	static Logger logger = Logger.getLogger(PedidoService.class);

	@EJB
	IPedidoDao iPedidoDao;
	
	@EJB
	IReclamoDao iReclamoDao;
	
	@EJB
	PedidoConverter pedidoConverter;
	
	@EJB
	ReclamoConverter reclamoConverter;
	
	@EJB
	UsuarioConverter usuarioConverter;

	@EJB
	PedidoRConverter pedidoRConverter;

	@EJB
	ExtraMenuConverter extraMenuConverter;

	@EJB
	ICalificacionRRService calificacionSrv;

	@EJB
	IImagenService imgSrv;
	
	@EJB
	INotificacionService notificacionSrv;
	
	@EJB
	IUsuarioDAO usrDAO;

	@Override
	public List<PedidoDTO> listar() throws AppettitException {
		try {
			return pedidoConverter.fromEntity(iPedidoDao.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoDTO listarPorId(Long id) throws AppettitException {
		try {
			return pedidoConverter.fromEntity(iPedidoDao.listarPorId(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoDTO crear(PedidoDTO pedidoDTO) throws AppettitException {
		Pedido PedidoService = iPedidoDao.listarPorId(pedidoDTO.getId());
		try {

			PedidoDTO pdto = pedidoConverter.fromEntity(iPedidoDao.crear(PedidoService)); 
			
			/* Si el cliente tiene un token de firebase definido, se le envía la notificación */
			if(pdto.getCliente().getNotificationFirebase() != null) {
				
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
				
				String msg = "Fecha: " + pdto.getFecha().format(dateFormat)
				+ " Total: " + pdto.getTotal()
				+ "Forma de Pago: " + pdto.getTipo().toString()
				+ " Estado: " + pdto.getEstado().toString();
				notificacionSrv.enviarNotificacionFirebase(pdto.getCliente().getNotificationFirebase(),
						"Pedido registrado con éxito.", msg );
			}
			if(pdto.getCliente().getNotificationFirebaseWeb() != null) {

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

				String msg = "Fecha: " + pdto.getFecha().format(dateFormat)
						+ " Total: " + pdto.getTotal()
						+ "Forma de Pago: " + pdto.getTipo().toString()
						+ " Estado: " + pdto.getEstado().toString();
				notificacionSrv.enviarNotificacionFirebase(pdto.getCliente().getNotificationFirebaseWeb(),
						"Pedido registrado con éxito.", msg );
			}
			
			return pdto;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	// ESTA OPERACION TIENE ERRORES
	@Override
	public PedidoDTO editar(Long id, PedidoDTO pedidoDTO) throws AppettitException {
		Pedido pedido = iPedidoDao.listarPorId(pedidoDTO.getId());
		if (pedido == null)
			throw new AppettitException("El Pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {
			pedido.setCliente(pedido.getCliente());
			pedido.setId(pedido.getId());
			pedido.setEntrega(pedido.getEntrega());
			pedido.setEstado(pedido.getEstado());
			pedido.setFecha(pedido.getFecha());
			pedido.setMenus(pedido.getMenus());
			pedido.setMotivo(pedido.getMotivo());
			pedido.setPago(pedido.getPago());
			pedido.setPromociones(pedido.getPromociones());
			pedido.setReclamo(pedido.getReclamo());
			pedido.setTiempoEstimado(pedido.getTiempoEstimado());
			pedido.setTotal(pedido.getTotal());
			pedido.setTipo(pedido.getTipo());
			pedido.setExtraMenus(pedido.getExtraMenus());

			return pedidoConverter.fromEntity(iPedidoDao.editar(pedido));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoDTO editarEstadoPago(PedidoDTO pedidoDTO) throws AppettitException {
		Pedido pedido = iPedidoDao.listarPorId(pedidoDTO.getId());
		if (pedido == null)
			throw new AppettitException("El Pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

		try {
			
			String estado = "";
			
			if(!pedido.getEstado().toString().equalsIgnoreCase(pedidoDTO.getEstado().toString())) {
				estado = "\nEstado: " + pedidoDTO.getEstado().toString(); 
			}
			
			if(!pedido.getPago()==pedidoDTO.getPago()) {
				estado = estado +  "\nPago registrado"; 
			}
			
			pedido.setEstado(pedidoDTO.getEstado());
			pedido.setPago(pedidoDTO.getPago());
			
			/* Si el cliente tiene un token de firebase definido, se le envía la notificación */
			if(pedido.getCliente().getNotificationFirebase() != null) {
				
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
				
				String msg = "Pedido: " + pedido.getId()
				+ "\nFecha: " + pedido.getFecha().format(dateFormat)
				+ "\nTotal: " + pedido.getTotal()
				+ "\nForma de Pago: " + pedido.getTipo().toString()
				+ estado;
				notificacionSrv.enviarNotificacionFirebase(pedido.getCliente().getNotificationFirebase(),
						"Actualización de pedido.", msg );
			}
			if(pedido.getCliente().getNotificationFirebaseWeb() != null) {

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

				String msg = "Pedido: " + pedido.getId()
						+ "\nFecha: " + pedido.getFecha().format(dateFormat)
						+ "\nTotal: " + pedido.getTotal()
						+ "\nForma de Pago: " + pedido.getTipo().toString()
						+ estado;
				notificacionSrv.enviarNotificacionFirebase(pedido.getCliente().getNotificationFirebaseWeb(),
						"Actualización de pedido.", msg );
			}


			return pedidoConverter.fromEntity(iPedidoDao.editar(pedido));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public void eliminar(Long id) throws AppettitException {

		Pedido pedido = iPedidoDao.listarPorId(id);
		if (pedido == null) {
			throw new AppettitException("El Pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		} else {
			try {
				iPedidoDao.eliminar(pedido);
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public PedidoRDTO crearFront(PedidoRDTO pedidoRDTO) throws AppettitException {
		// AL CREAR UN PEDIOD EL ESTADO ES SOLICITADO
		// LA FECHA DEL PEDIDO ES LA FECHA HORA ACTUAL
		pedidoRDTO.setEstado(EstadoPedido.SOLICITADO);
		pedidoRDTO.setFecha(LocalDateTime.now());

		Pedido pedido = pedidoRConverter.fromDTO(pedidoRDTO);
		try {

			PedidoRDTO pdto = pedidoRConverter.fromEntity(iPedidoDao.crear(pedido));
			Cliente cliente = usrDAO.buscarPorIdCliente(pedidoRDTO.getIdcli());
			
			/* Si el cliente tiene un token de firebase definido, se le envía la notificación */
			if(cliente.getNotificationFirebase() != null) {
				
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
				
				String msg = "Fecha: " + pdto.getFecha().format(dateFormat)
				+ "\nTotal: " + pdto.getTotal()
				+ "\nForma de Pago: " + pdto.getTipo().toString()
				+ "\nEstado: " + pdto.getEstado().toString();
				notificacionSrv.enviarNotificacionFirebase(cliente.getNotificationFirebase(),
						"Pedido registrado con éxito.", msg );
			}
			if(cliente.getNotificationFirebaseWeb() != null) {

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

				String msg = "Fecha: " + pdto.getFecha().format(dateFormat)
						+ "\nTotal: " + pdto.getTotal()
						+ "\nForma de Pago: " + pdto.getTipo().toString()
						+ "\nEstado: " + pdto.getEstado().toString();
				notificacionSrv.enviarNotificacionFirebase(cliente.getNotificationFirebaseWeb(),
						"Pedido registrado con éxito.", msg );
			}
			
			return pdto;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<PedidoRDTO> listarPorClienteREST(Long id) throws AppettitException {
		try {
			return pedidoRConverter.fromEntity(iPedidoDao.listarPorCliente(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<PedidoDTO> listarPorRestaurante(Long id) throws AppettitException {
		try {
			return pedidoConverter.fromEntity(iPedidoDao.listarPorRestaurante(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoRDTO ultimo(Long id) throws AppettitException {
		try {
			PedidoRDTO pedido = pedidoRConverter.fromEntity(iPedidoDao.ultimo(id));
			pedido.setCalificacion(calificacionSrv.listarPorId(pedido.getId(), pedido.getIdcli()));
			
			List<MenuRDTO> menus = pedido.getMenus();
			Iterator<MenuRDTO> it = menus.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();

				if (men.getId_imagen() == null || men.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/menu.png"));
				} else {
					try {
						img = imgSrv.buscarPorId(men.getId_imagen());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}
				men.setImagen(img);
			}

			return pedido;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoRDTO listarPorIdREST(Long id) throws AppettitException {
		try {

			PedidoRDTO pedido = pedidoRConverter.fromEntity(iPedidoDao.listarPorId(id));
			pedido.setCalificacion(calificacionSrv.listarPorId(pedido.getId(), pedido.getIdcli()));

			List<MenuRDTO> menus = pedido.getMenus();
			Iterator<MenuRDTO> it = menus.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();

				if (men.getId_imagen() == null || men.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/menu.png"));
				} else {
					try {
						img = imgSrv.buscarPorId(men.getId_imagen());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}
				men.setImagen(img);
			}

			return pedido;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ReclamoDTO obtenerReclamo(Long id) throws AppettitException {
		Pedido pedido = iPedidoDao.listarPorId(id);
		if (pedido == null) {
			throw new AppettitException("El pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		} else {
			Reclamo reclamo = pedido.getReclamo();
			if (reclamo == null) {
				throw new AppettitException("El pedido indicado no tiene un reclamo.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				try {
					return reclamoConverter.fromEntity(reclamo);
				} catch (Exception e) {
					throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
				}
			}
		}
	}

	@Override
	public List<PedidoRMDTO> listarPorClienteMREST(Long id) throws AppettitException {
		try {
			return pedidoRConverter.fromEntityToRMDTO(iPedidoDao.listarPorCliente(id));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PedidoDTO listarPorReclamo(Long id) throws AppettitException {
		Reclamo reclamo = iReclamoDao.listarPorId(id);
		if (reclamo == null) {
			throw new AppettitException("El reclamo indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		} else {
			Pedido pedido = iPedidoDao.listarPorReclamo(id);
			if (pedido == null) {
				throw new AppettitException("El pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
			} else {
				try {
					return pedidoConverter.fromEntity(pedido);
				} catch (Exception e) {
					throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
				}
			}
		}
	}
	
}
