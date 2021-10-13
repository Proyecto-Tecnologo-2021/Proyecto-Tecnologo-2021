package proyecto2021G03.appettit.dto;


import com.vividsolutions.jts.geom.Point;

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
public class DireccionDTO {

	private Long id;
	private String calle;
	private Integer numero;
	private String apartamento;
	private String referencias;
	private LocalidadDTO barrio;
	private Point geometry;

}
