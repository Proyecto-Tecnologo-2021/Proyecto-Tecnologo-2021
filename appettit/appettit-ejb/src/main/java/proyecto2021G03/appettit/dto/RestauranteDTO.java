package proyecto2021G03.appettit.dto;

import java.time.LocalDateTime;


import com.vividsolutions.jts.geom.MultiPolygon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteDTO extends UsuarioDTO  {

	@Builder
	public RestauranteDTO(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase, String rut, EstadoRegistro estado, Boolean bloqueado, 
			LocalDateTime horarioApertura, LocalDateTime horarioCierre, Boolean abierto, Boolean abiertoAutom,
			MultiPolygon areaentrega, DireccionDTO direccion) {
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
	}
	
	private String rut;
	private EstadoRegistro estado;
	private Boolean bloqueado;
	private LocalDateTime horarioApertura;
	private LocalDateTime horarioCierre;
	private Boolean abierto;
	private Boolean abiertoAutom;
	private MultiPolygon areaentrega;
	private DireccionDTO direccion;
	private CalificacionRestauranteDTO calificacion;
	
}
