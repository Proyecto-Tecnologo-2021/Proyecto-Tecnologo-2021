package proyecto2021G03.appettit.dto;

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
public class DashMenuDTO {

	private Long id;
	private String nom_restaurante;
	private String nombre;
	private Double precio;
	private Double total;
	private Integer ordenes;
	private String id_imagen;
    private ImagenDTO imagen;
}
