package proyecto2021G03.appettit.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExtraMenuDTO {

    private Long id;
    private Long id_producto;
	private Long id_restaurante; 
    private ProductoDTO producto;
    private Double precio;
}
