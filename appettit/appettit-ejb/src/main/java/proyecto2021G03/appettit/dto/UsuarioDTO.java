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
	private String token;
	private String tokenFireBase;

    public UsuarioDTO(Long id, String nombre, String username, String password, String telefono, String correo, String token, String tokenFireBase) {
    }
}
