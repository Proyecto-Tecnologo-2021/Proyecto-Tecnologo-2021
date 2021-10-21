package proyecto2021G03.appettit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionRestauranteDTO {

	private Integer rapidez; 
	private Integer comida;
	private Integer servicio;
	private Integer general;

}
