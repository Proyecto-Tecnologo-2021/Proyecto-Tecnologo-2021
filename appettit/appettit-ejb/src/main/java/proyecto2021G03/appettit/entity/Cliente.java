package proyecto2021G03.appettit.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.ImagenDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("cliente")
public class Cliente extends Usuario {

	private static final long serialVersionUID = 1L;

	@Builder
	public Cliente(Long id, String nombre, String username, String password, String telefono, String correo,
			String tokenFireBase) {
		super(id, nombre, username, password, telefono, correo, tokenFireBase);
		// TODO Auto-generated constructor stub
	}

	private Boolean bloqueado;
	private String idImagen;

	@OneToMany
	private List<Direccion> direcciones;

}
