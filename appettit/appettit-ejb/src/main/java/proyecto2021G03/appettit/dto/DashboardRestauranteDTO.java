package proyecto2021G03.appettit.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRestauranteDTO {

	private Integer total_calificaciones;
	private Map<Integer, Integer> rapidez;
	private Map<Integer, Integer> comida;
	private Map<Integer, Integer> servicio;
	private Map<Integer, Integer> general;
	
	private Map<Integer, Double> ventas;
	private Map<Integer, Double> clientes;
	private Map<Integer, Double> ordenes;
	private Map<Integer, Double> promOrdenes;
	
	private Double var_ventas;
	private Double var_clientes;
	private Double var_ordenes;
	private Double var_promOrdenes;
	
	private Map<Integer, Integer> reclamos;
	private Map<Integer, Integer> formaPago;
	
}
