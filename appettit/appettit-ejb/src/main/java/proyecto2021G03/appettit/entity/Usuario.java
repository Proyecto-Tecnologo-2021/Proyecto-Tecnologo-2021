package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public abstract class Usuario  implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String username;
	private String password;
	
	@Column(name="telefono",unique=true, nullable = false)
	private String telefono;

	@Column(name="correo",unique=true, updatable = false, nullable = false)
	private String correo;

	@Column(name="tokenfirebase", columnDefinition="TEXT")
	private String tokenFireBase;
}
