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
    private List<ExtraMenuRDTO> extras;
    private List<ProductoRDTO> productos;
    private String id_imagen;
    private ImagenDTO imagen;
    
    @Builder.Default
    private String tipo = "MENU";
}
