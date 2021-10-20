package proyecto2021G03.appettit.dto;


import java.util.List;


import com.vividsolutions.jts.geom.MultiPolygon;

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
public class DepartamentoDTO {

	private Long id;
	private String nombre;
	private MultiPolygon geometry;
	
	private List<CiudadDTO> ciudades;
	
}
