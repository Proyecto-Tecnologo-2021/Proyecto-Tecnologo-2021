package proyecto2021G03.appettit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CalificacionRPedidoDTO {

    private Long id;
    private Integer rapidez;
    private Integer comida;
    private Integer servicio;
    private String comentario;
    private Long id_pedido;
    private Long id_cliente;
}
