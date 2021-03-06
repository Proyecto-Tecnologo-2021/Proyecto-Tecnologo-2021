package proyecto2021G03.appettit.dto;

import java.util.List;

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
public class PromocionDTO {

	private Long id;
	private Long id_restaurante;
	private String nombre;
	private RestauranteDTO restaurante;
	private String descripcion;
	private Double descuento;
	private Double precio;
	private List<MenuDTO> menus;
	private String id_imagen;
    private ImagenDTO imagen;

}
