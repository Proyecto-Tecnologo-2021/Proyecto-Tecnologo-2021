package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.util.List;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IEstadisticasService {
	public List<PedidoDTO> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta ) throws AppettitException;
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id) throws AppettitException;
	public List<DashTotalDTO> listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta ) throws AppettitException;
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta ) throws AppettitException;
	public List<CalificacionPedidoDTO> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
}
