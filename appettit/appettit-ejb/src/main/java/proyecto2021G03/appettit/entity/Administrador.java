package proyecto2021G03.appettit.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("administrador")
public class Administrador extends Usuario {

	private static final long serialVersionUID = 1L;

	public Administrador(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase) {
		super(id, nombre, username, password, telefono, correo, token, tokenFireBase);
		// TODO Auto-generated constructor stub
	}


}
