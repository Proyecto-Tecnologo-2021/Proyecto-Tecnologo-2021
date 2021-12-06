package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UsuarioDTO {

	private Long id;
	private String nombre;
	private String username;
	private String password;
	private String telefono;
	private String correo;
	private String tokenFireBase;

}
