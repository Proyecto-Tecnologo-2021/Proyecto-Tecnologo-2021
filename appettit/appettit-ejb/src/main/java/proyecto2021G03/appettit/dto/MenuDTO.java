package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.ExtraMenu;
import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.entity.Restaurante;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MenuDTO {

    private Long id;
    private Long id_restaurante;
    private String nombre;
    private Restaurante restaurante;
    private String descripcion;
    private Double precioSimple;
    private Double precioTotal;
    private List<ProductoDTO> productos;
    private List<ExtraMenuDTO> extras;
    private String id_imagen;
}
