package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionRestauranteDTO {

	private Integer rapidez; 
	private Integer comida;
	private Integer servicio;
	private Integer general;

}
