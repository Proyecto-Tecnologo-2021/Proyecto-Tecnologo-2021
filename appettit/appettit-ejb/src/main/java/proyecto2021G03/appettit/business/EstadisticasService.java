package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.dao.EstadisticasDAO;
import proyecto2021G03.appettit.dao.IEstadisticasDAO;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class EstadisticasService implements IEstadisticasService {

	@EJB
	IEstadisticasDAO estadisticasDAO;
	
	@EJB
	PedidoConverter pedidoConverter;
	

	@Override
	public List<PedidoDTO> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta) throws AppettitException {
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
	public List<DashTotalDTO> listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta)
			throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta)
			throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalificacionPedidoDTO> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

}
