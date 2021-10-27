package proyecto2021G03.appettit.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRDTO {

    private Long id; //ID DE MENU
    private Long id_restaurante;
    private String nom_restaurante;
    private Double descuento;
    private String nombre;
    private String descripcion;
    private Double precioSimple;
    private Double precioTotal;
    private List<ExtraMenuDTO> extras;
    private List<ProductoDTO> productos;
    private String id_imagen;
}
