package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Pedido;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionPedidoDTO {

    private Integer rapidez;
    private Integer comida;
    private Integer servicio;
    private String comentario;
    private Pedido pedido;
    private Cliente cliente;

}
