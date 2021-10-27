package proyecto2021G03.appettit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRDTO {
    private TipoPago tipo;
    private Boolean pago;
    private LocalDateTime fecha;
    private Double total;
    private Long idcli;
    private Long idrest;
    private List<MenuRDTO> menus;
    private Long iddir;

}
