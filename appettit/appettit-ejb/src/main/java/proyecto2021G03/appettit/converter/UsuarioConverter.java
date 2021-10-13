package proyecto2021G03.appettit.converter;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;

@Singleton
public class UsuarioConverter extends AbstractConverter<Usuario, UsuarioDTO> {
	
	@EJB
	private DireccionConverter direccionConverter;

	@Override
	public UsuarioDTO fromEntity(Usuario e) {
		return null;
	}

	@Override
	public Usuario fromDTO(UsuarioDTO d) {
		return null;
	}

	public AdministradorDTO fromAdministrador(Administrador e) {
		if(e == null) return null;
		return AdministradorDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.username(e.getUsername())
				.password(e.getPassword())
				.telefono(e.getTelefono())
				.correo(e.getCorreo())
				.token(e.getToken())
				.tokenFireBase(e.getTokenFireBase())
				.build();
	}

	public Administrador fromAdministradorDTO(AdministradorDTO d) {
		if(d == null) return null;
		return Administrador.builder()
				.id(d.getId())
				.nombre(d.getNombre())
				.username(d.getUsername())
				.password(d.getPassword())
				.telefono(d.getTelefono())
				.correo(d.getCorreo())
				.token(d.getToken())
				.tokenFireBase(d.getTokenFireBase())
				.build();
	}
	
	public List<AdministradorDTO> fromAdministrador(List<Administrador> entities){
		if(entities == null) return null;
		return entities.stream()
			.map(e -> fromAdministrador(e))
			.collect(Collectors.toList());
	}
	
	public List<Administrador> fromAdministradorDTO(List<AdministradorDTO> dtos){
		if(dtos == null) return null;
		return dtos.stream()
			.map(d -> fromAdministradorDTO(d))
			.collect(Collectors.toList());
	}
	

	public RestauranteDTO fromRestaurante(Restaurante e) {
		if(e == null) return null;
		return RestauranteDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.username(e.getUsername())
				.password(e.getPassword())
				.telefono(e.getTelefono())
				.correo(e.getCorreo())
				.token(e.getToken())
				.tokenFireBase(e.getTokenFireBase())
				.rut(e.getRut())
				.estado(e.getEstado())
				.bloqueado(e.getBloqueado())
				.horarioApertura(e.getHorarioApertura())
				.horarioCierre(e.getHorarioCierre())
				.abierto(e.getAbierto())
				.abiertoAutom(e.getAbiertoAutom())
				.areaentrega(e.getAreaentrega())
				.direccion(direccionConverter.fromEntity(e.getDireccion()))
				.build();
	}

	public Restaurante fromRestauranteDTO(RestauranteDTO d) {
		if(d == null) return null;
		return Restaurante.builder()
				.id(d.getId())
				.nombre(d.getNombre())
				.username(d.getUsername())
				.password(d.getPassword())
				.telefono(d.getTelefono())
				.correo(d.getCorreo())
				.token(d.getToken())
				.tokenFireBase(d.getTokenFireBase())
				.rut(d.getRut())
				.estado(d.getEstado())
				.bloqueado(d.getBloqueado())
				.horarioApertura(d.getHorarioApertura())
				.horarioCierre(d.getHorarioCierre())
				.abierto(d.getAbierto())
				.abiertoAutom(d.getAbiertoAutom())
				.areaentrega(d.getAreaentrega())
				.direccion(direccionConverter.fromDTO(d.getDireccion()))
				.build();
	}
	
	public List<RestauranteDTO> fromRestaurante(List<Restaurante> entities){
		if(entities == null) return null;
		return entities.stream()
			.map(e -> fromRestaurante(e))
			.collect(Collectors.toList());
	}
	
	public List<Restaurante> fromRestauranteDTO(List<RestauranteDTO> dtos){
		if(dtos == null) return null;
		return dtos.stream()
			.map(d -> fromRestauranteDTO(d))
			.collect(Collectors.toList());
	}

	
}
