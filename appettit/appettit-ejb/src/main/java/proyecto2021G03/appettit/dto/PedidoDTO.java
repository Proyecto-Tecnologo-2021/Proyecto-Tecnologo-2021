package proyecto2021G03.appettit.dto;

import lombok.*;
import proyecto2021G03.appettit.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private EstadoPedido estado;
    private TipoPago tipo;
    private Boolean pago;
    private Integer tiempoEstimado;
    private String motivo;
    private LocalDateTime fecha;
    private Double total;
    private Restaurante restaurante;
    private Cliente cliente;
    private List<Menu> menus;
    private List<Promocion> promociones;
    private Direccion entrega;
    private Reclamo reclamo;

}
