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
public class ReclamoCDTO {

	private Long id_pedido;
	private String motivo;
	private String detalles;

}
