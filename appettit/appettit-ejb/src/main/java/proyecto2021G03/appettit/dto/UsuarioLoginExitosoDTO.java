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
public class UsuarioLoginExitosoDTO {
	
	private String tipoUsuario;
	private Long id;
	private String nombre;
	private String correo;
	private String telefono;
	private String jwt;
	
}