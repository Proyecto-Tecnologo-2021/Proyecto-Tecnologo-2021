package proyecto2021G03.appettit.dao;

import java.time.LocalDateTime;
import java.util.List;

import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.entity.Pedido;

public interface IEstadisticasDAO {
	public List<Pedido> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta );
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id);
	public List<DashTotalDTO> listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta );
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top);
	public List<ClasificacionPedido> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer top );

}
