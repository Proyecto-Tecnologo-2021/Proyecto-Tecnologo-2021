package proyecto2021G03.appettit.converter;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Usuario;

@Singleton
public class UsuarioConverter extends AbstractConverter<Usuario, UsuarioDTO> {

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
}
