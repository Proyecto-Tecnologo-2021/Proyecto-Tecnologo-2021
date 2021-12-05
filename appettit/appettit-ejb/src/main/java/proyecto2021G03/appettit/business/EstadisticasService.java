package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.CalificacionRConverter;
import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.dao.IEstadisticasDAO;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashGeoDTO;
import proyecto2021G03.appettit.dto.DashInformeDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashReclamoDTO;
import proyecto2021G03.appettit.dto.DashRestauranteDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.RestauranteRDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class EstadisticasService implements IEstadisticasService {

	static Logger logger = Logger.getLogger(EstadisticasService.class);

	@EJB
	IEstadisticasDAO estadisticasDAO;

	@EJB
	PedidoConverter pedidoConverter;

	@EJB
	CalificacionRConverter calConverter;

	@EJB
	IImagenService imgSrv;
	
	@EJB
	IUsuarioService usrSrv;

	@Override
	public List<PedidoDTO> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return pedidoConverter.fromEntity(estadisticasDAO.listarPedidosPendientesPorRestaurante(id, fechaDesde, fechaHasta));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id) throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DashTotalDTO listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo)
			throws AppettitException {
		try {
			return estadisticasDAO.listarVentasPorRestaurante(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top) throws AppettitException {
		List<DashMenuDTO> tendencias = new ArrayList<DashMenuDTO>();
		try {
			Iterator<DashMenuDTO> it = estadisticasDAO.listarTendenciasPorRestaurante(id, fechaDesde, fechaHasta, top)
					.iterator();
			while (it.hasNext()) {
				DashMenuDTO men = it.next();
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
				tendencias.add(men);
			}
			return tendencias;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<CalificacionPedidoDTO> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top) throws AppettitException {
		// TODO Auto-generated method stub
		try {
			return calConverter.fromEntity(estadisticasDAO.listarCalificacionesPorRestaurante(id, fechaDesde, fechaHasta, top));
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashMenuDTO> listarPediosRecientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top) throws AppettitException {
		List<DashMenuDTO> recientes = new ArrayList<DashMenuDTO>();
		try {
			Iterator<DashMenuDTO> it = estadisticasDAO.listarPediosRecientesPorRestaurante(id, fechaDesde, fechaHasta, top).iterator();
			while (it.hasNext()) {
				DashMenuDTO men = it.next();
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
				recientes.add(men);
			}
			return recientes;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarFormaPagoPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarFormaPagoPorRestaurante(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarReclamosTPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarReclamosTPorRestaurante(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarEstadoPedidosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta)	throws AppettitException {
		try {
			return estadisticasDAO.listarEstadoPedidosPorRestaurante(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashReclamoDTO> listarReclamosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarReclamosPorRestaurante(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarClientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarClientesPorRestaurante(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarOrdenesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarOrdenesPorRestaurante(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarOrdenesPromedioPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarOrdenesPromedioPorRestaurante(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarCalificacionesDetPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, String calificacion) throws AppettitException {
		try {
			return estadisticasDAO.listarCalificacionesDetPorRestaurante(id, fechaDesde, fechaHasta, calificacion);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashGeoDTO listarGeoEntregasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarGeoEntregasPorRestaurante(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarVentasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarVentasPorFecha(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarClientesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarClientesPorFecha(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarOrdenesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarOrdenesPorFecha(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarOrdenesPromedioPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException {
		try {
			return estadisticasDAO.listarOrdenesPromedioPorFecha(id, fechaDesde, fechaHasta, periodo);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarFormaPagoPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarFormaPagoPorFecha(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public DashTotalDTO listarReclamosTPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta)
			throws AppettitException {
		try {
			return estadisticasDAO.listarReclamosTPorFecha(id, fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<RestauranteRDTO> listarRestaurantesAutorizar() throws AppettitException {
		try {
			return estadisticasDAO.listarRestaurantesAutorizar();
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashMenuDTO> listarTendenciasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top) throws AppettitException {
		List<DashMenuDTO> tendencias = new ArrayList<DashMenuDTO>();
		try {
			Iterator<DashMenuDTO> it = estadisticasDAO.listarTendenciasPorFecha(id, fechaDesde, fechaHasta, top)
					.iterator();
			while (it.hasNext()) {
				DashMenuDTO men = it.next();
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
				tendencias.add(men);
			}
			return tendencias;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashRestauranteDTO> listarTopRestaurantesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top) throws AppettitException {
		List<DashRestauranteDTO> restaurantes = new ArrayList<DashRestauranteDTO>();
		try {
			Iterator<DashRestauranteDTO> it = estadisticasDAO.listarTopRestaurantesPorFecha(id, fechaDesde, fechaHasta, top).iterator();
			while (it.hasNext()) {
				DashRestauranteDTO res = it.next();
				res.setCalificacion(usrSrv.calificacionRestaurante(res.getId()));
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
	public List<DashInformeDTO> listarInfoVentasPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarInfoVentasPorFecha(fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashInformeDTO> listarInfoVentasPorFechaRestaurante(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarInfoVentasPorFechaRestaurante(fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<DashInformeDTO> listarInfoVentasPorFechaBarrio(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException {
		try {
			return estadisticasDAO.listarInfoVentasPorFechaBarrio(fechaDesde, fechaHasta);
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

}
