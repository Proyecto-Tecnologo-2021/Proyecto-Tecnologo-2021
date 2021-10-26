package proyecto2021G03.appettit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromocionCrearDTO {

    private Long id_restaurante;
    private String nombre;
    private Long id_categoria;
}
