package proyecto2021G03.appettit.dto;

import java.time.LocalDateTime;

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
public class ReclamoDTO {

	private Long id;
	private String motivo;
	private String detalles;
	private LocalDateTime fecha;

}
