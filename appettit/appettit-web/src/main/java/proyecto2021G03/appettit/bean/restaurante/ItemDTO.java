package proyecto2021G03.appettit.bean.restaurante;

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
public class ItemDTO {
	
	private int cantidad;
	private Long id;
	private String nombre;
	private Double precio;
 	
}