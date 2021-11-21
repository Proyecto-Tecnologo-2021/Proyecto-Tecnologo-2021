package proyecto2021G03.appettit.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashReclamoDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.entity.Pedido;

public interface IEstadisticasDAO {
	public List<Pedido> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta );
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id);
	public DashTotalDTO listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo);
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top);
	public List<ClasificacionPedido> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top );
	public List<DashMenuDTO> listarPediosRecientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top);
	public DashTotalDTO listarFormaPagoPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
	public DashTotalDTO listarReclamosTPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
	public DashTotalDTO listarEstadoPedidosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
	public List<DashReclamoDTO> listarReclamosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
	public DashTotalDTO listarClientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo);
	public DashTotalDTO listarOrdenesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo);
	public DashTotalDTO listarOrdenesPromedioPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo);
	public List<DashReclamoDTO> listarCalificacionesDetPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, String calificacion);
}
