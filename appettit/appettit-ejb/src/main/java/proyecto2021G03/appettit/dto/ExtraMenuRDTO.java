package proyecto2021G03.appettit.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtraMenuRDTO {

    private Long id;
    private Long id_producto;
	private Long id_restaurante; 
    private String producto;
    private Double precio;
}
