package proyecto2021G03.appettit.dto;


//import com.vividsolutions.jts.geom.MultiPolygon;

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
public class LocalidadDTO {

	private Long id;
	private Long id_ciudad;
	private Long id_departamento;
	private String nombre;
	//private MultiPolygon geometry;
	private String geometry;
	
}
