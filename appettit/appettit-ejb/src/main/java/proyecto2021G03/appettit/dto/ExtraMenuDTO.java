package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.Producto;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExtraMenuDTO {

    private Long id;
    private ProductoDTO producto;
    private Double precio;
}
