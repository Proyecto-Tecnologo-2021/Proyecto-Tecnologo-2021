package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionPedidoDTO {
    private Long id_pedido;
    private Long id_cliente;
    private Integer rapidez;
    private Integer comida;
    private Integer servicio;
    private String comentario;
    private PedidoDTO pedido;
    private ClienteDTO cliente;
    
    
    public Integer getGeneral() {
    	return (this.rapidez + this.comida + this.servicio)/3;
    }

}
