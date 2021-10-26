package proyecto2021G03.appettit.dto;

//import com.vividsolutions.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionCrearDTO implements Comparable<Object>{

	private Long id_cliente;
	private String alias;
	private String calle;
	private String numero;
	private String apartamento;
	private String referencias;
	//private LocalidadDTO barrio;
	private String geometry;
	//private int quantity;


	@Override
	public int compareTo(Object o) {
		return (((DireccionCrearDTO) o).getCalle() + " " + ((DireccionCrearDTO) o).getNumero()).compareTo(this.getCalle() + " " + this.getNumero());
	}
	
}
