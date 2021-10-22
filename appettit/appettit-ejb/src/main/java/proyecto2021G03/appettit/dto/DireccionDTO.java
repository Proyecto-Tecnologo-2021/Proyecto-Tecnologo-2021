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
public class DireccionDTO implements Comparable<Object>{

	private Long id;
	private String alias;
	private String calle;
	private String numero;
	private String apartamento;
	private String referencias;
	private LocalidadDTO barrio;
	//private Point geometry;
	private String geometry;
	private int quantity;


	@Override
	public int compareTo(Object o) {
		return (((DireccionDTO) o).getCalle() + " " + ((DireccionDTO) o).getNumero()).compareTo(this.getCalle() + " " + this.getNumero());
	}
}
