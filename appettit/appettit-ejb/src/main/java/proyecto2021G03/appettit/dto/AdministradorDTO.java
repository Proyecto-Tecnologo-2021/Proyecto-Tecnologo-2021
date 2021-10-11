package proyecto2021G03.appettit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import proyecto2021G03.appettit.entity.Usuario;

@Getter
@Setter
@AllArgsConstructor
public class AdministradorDTO extends Usuario {

	private static final long serialVersionUID = 1L;

	@Builder
	public AdministradorDTO(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase) {
		super(id, nombre, username, password, telefono, correo, token, tokenFireBase);
		// TODO Auto-generated constructor stub
	}

}
