package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.Menu;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRDTO {

    private Long id_restaurante;
    private String nombre;
    private String descripcion;
    private Double precioSimple;
    private Double precioTotal;
    private List<ExtraMenuDTO> extras;
    private List<ProductoDTO> productos;
    private String id_imagen;
}
