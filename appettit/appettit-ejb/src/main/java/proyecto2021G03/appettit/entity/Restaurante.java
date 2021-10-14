package proyecto2021G03.appettit.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
@Entity
@DiscriminatorValue("restaurante")
public class Restaurante extends Usuario {

	private static final long serialVersionUID = 1L;

	@Builder
	public Restaurante(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase, String rut, EstadoRegistro estado, Boolean bloqueado, 
			LocalDateTime horarioApertura, LocalDateTime horarioCierre, Boolean abierto, Boolean abiertoAutom,
			MultiPolygon areaentrega, Direccion direccion, String id_imagen) {
		super(id, nombre, username, password, telefono, correo, token, tokenFireBase);
		
		this.rut = rut;
		this.estado = estado;
		this.bloqueado = bloqueado;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.abierto = abierto;
		this.abiertoAutom = abiertoAutom;
		this.areaentrega = areaentrega;
		this.direccion = direccion;
		this.id_imagen = id_imagen;
	}
	
	private String rut;
	private EstadoRegistro estado;
	private Boolean bloqueado;
	private LocalDateTime horarioApertura;
	private LocalDateTime horarioCierre;
	private Boolean abierto;
	private Boolean abiertoAutom;
	private String id_imagen;
	
	@Column(name = "geom", columnDefinition = "geometry(MultiPolygon, 32721)")
	private MultiPolygon areaentrega;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_direccion", referencedColumnName="id")
	private Direccion direccion;

}
