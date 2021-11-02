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
public class ProductoRDTO {
	
	private Long id;
	private Long id_restaurante;
	private String nombre;
	private Long id_categoria;
	private CategoriaDTO categoria;
	
}