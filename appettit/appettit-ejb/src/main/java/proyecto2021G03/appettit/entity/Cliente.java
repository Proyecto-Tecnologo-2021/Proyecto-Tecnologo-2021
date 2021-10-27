package proyecto2021G03.appettit.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
			String tokenFireBase, Boolean bloqueado, List<Direccion> direcciones) {
		super(id, nombre, username, password, telefono, correo, tokenFireBase);
		
		this.bloqueado = bloqueado;
		this.direcciones = direcciones;		
	}

	private Boolean bloqueado;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="id_direccion", referencedColumnName="id")
	@Builder.Default
	private List<Direccion> direcciones = new ArrayList<Direccion>();

}
