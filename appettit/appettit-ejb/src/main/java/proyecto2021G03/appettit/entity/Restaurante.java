package proyecto2021G03.appettit.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vividsolutions.jts.geom.MultiPolygon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.dto.EstadoRegistro;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("restaurante")
public class Restaurante extends Usuario {

	private static final long serialVersionUID = 1L;

	public Restaurante(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase) {
		super(id, nombre, username, password, telefono, correo, token, tokenFireBase);
		// TODO Auto-generated constructor stub
	}
	
	private String rut;
	private EstadoRegistro estado;
	private Boolean bloqueado;
	
	@Temporal (TemporalType.TIMESTAMP)
	private Date horarioApertura;
	
	private LocalDateTime horarioCierre;
	
	private Boolean abierto;
	private Boolean abiertoAutom;
	
	@Column(name = "geom", columnDefinition = "geometry(MultiPolygon, 32721)")
	private MultiPolygon areaentrega;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_direccion", referencedColumnName="id")
	private Direccion direccion;

}
