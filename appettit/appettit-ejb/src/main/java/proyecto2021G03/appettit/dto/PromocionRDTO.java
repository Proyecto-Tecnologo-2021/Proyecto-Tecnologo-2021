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
public class PromocionRDTO {

	private Long id;
	private String nombre;
    private Long id_restaurante;
    private String nom_restaurante;
	private String descripcion;
	private Double descuento;
	private Double precio;
	private List<MenuRDTO> menus;
	private String id_imagen;
    private ImagenDTO imagen;
    
    @Builder.Default
    private String tipo = "PROM";
    
    private Integer cal_restaurante;

}
