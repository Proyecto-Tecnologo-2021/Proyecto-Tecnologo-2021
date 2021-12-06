package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Restaurante;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionClienteDTO {

    private Long id_restaurante;
    private Long id_cliente;
    private Integer clasificacion;
    private String comentario;
    private Restaurante restaurante;
    private Cliente cliente;

}
