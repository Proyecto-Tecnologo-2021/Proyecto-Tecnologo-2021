package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.ExtraMenuConverter;
import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.converter.PedidoRConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class PedidoService implements IPedidoService {

	static Logger logger = Logger.getLogger(PedidoService.class);

	@EJB
	IPedidoDao iPedidoDao;
	@EJB
	PedidoConverter pedidoConverter;
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

			return pedidoConverter.fromEntity(iPedidoDao.crear(PedidoService));
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
			pedido.setEstado(pedidoDTO.getEstado());
			pedido.setPago(pedidoDTO.getPago());

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

			return pedidoRConverter.fromEntity(iPedidoDao.crear(pedido));
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
			return pedidoRConverter.fromEntity(iPedidoDao.ultimo(id));
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
}
