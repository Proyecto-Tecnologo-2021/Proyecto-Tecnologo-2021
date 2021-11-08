package proyecto2021G03.appettit.dto;

import java.time.LocalTime;

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
public class RestauranteRDTO {
	private Long id;
	private String nombre;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	private Boolean abierto;
	private ImagenDTO imagen;
	private String id_imagen;
	private String direccion;
}
