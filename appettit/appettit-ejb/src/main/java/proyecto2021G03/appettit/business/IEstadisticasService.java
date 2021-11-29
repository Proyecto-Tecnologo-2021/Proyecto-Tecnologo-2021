package proyecto2021G03.appettit.business;

import java.time.LocalDateTime;
import java.util.List;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashGeoDTO;
import proyecto2021G03.appettit.dto.DashInformeDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashReclamoDTO;
import proyecto2021G03.appettit.dto.DashRestauranteDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.RestauranteRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IEstadisticasService {
	public List<PedidoDTO> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta ) throws AppettitException;
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id) throws AppettitException;
	public DashTotalDTO listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarClientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarOrdenesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarOrdenesPromedioPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
	public List<CalificacionPedidoDTO> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
	public List<DashMenuDTO> listarPediosRecientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
	public DashTotalDTO listarFormaPagoPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public DashTotalDTO listarReclamosTPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public DashTotalDTO listarEstadoPedidosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public List<DashReclamoDTO> listarReclamosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public DashTotalDTO listarCalificacionesDetPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, String calificacion) throws AppettitException;
	public DashGeoDTO listarGeoEntregasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	
	//ADMIN
	public DashTotalDTO listarVentasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarClientesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarOrdenesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarOrdenesPromedioPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) throws AppettitException;
	public DashTotalDTO listarFormaPagoPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public DashTotalDTO listarReclamosTPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public List<RestauranteRDTO> listarRestaurantesAutorizar() throws AppettitException;
	public List<DashMenuDTO> listarTendenciasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
	public List<DashRestauranteDTO> listarTopRestaurantesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top ) throws AppettitException;
	
	//INFORMES
	public List<DashInformeDTO> listarInfoVentasPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public List<DashInformeDTO> listarInfoVentasPorFechaRestaurante(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
	public List<DashInformeDTO> listarInfoVentasPorFechaBarrio(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws AppettitException;
}
