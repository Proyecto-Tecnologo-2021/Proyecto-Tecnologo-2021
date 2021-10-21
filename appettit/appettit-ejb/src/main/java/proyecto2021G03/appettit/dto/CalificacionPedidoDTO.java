package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Pedido;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
