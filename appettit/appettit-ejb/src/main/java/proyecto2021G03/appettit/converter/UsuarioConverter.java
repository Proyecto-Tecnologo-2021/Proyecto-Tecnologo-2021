package proyecto2021G03.appettit.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.ClienteCrearDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.ClienteMDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import proyecto2021G03.appettit.exception.AppettitException;

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

	/* ADMIN */
	public AdministradorDTO fromAdministrador(Administrador e) {
		if(e == null) return null;
		return AdministradorDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.username(e.getUsername())
				.password(e.getPassword())
				.telefono(e.getTelefono())
				.correo(e.getCorreo())
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
	
	/* RESTAURANTE */
	public RestauranteDTO fromRestaurante(Restaurante e) {
		if(e == null) return null;
		return RestauranteDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.username(e.getUsername())
				.password(e.getPassword())
				.telefono(e.getTelefono())
				.correo(e.getCorreo())
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
				.id_imagen(e.getId_imagen())
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
				.id_imagen(d.getId_imagen())
				.build();
	}
	
	public List<RestauranteDTO> fromRestaurante(List<Restaurante> entities){
		if(entities == null) return null;
		return entities.stream()
			.map(e -> fromRestaurante(e))
			.collect(Collectors.toList());
	}
	
	public List<Restaurante> fromRestauranteDTO(List<RestauranteDTO> datos){
		if(datos == null) return null;
		return datos.stream()
			.map(d -> fromRestauranteDTO(d))
			.collect(Collectors.toList());
	}

	/* CLIENTE */
	public ClienteDTO fromCliente(Cliente c) {
		if(c == null) return null;
		return ClienteDTO.builder()
				.id(c.getId())
				.nombre(c.getNombre())
				.username(c.getUsername())
				.password(c.getPassword())
				.telefono(c.getTelefono())
				.correo(c.getCorreo())
				.tokenFireBase(c.getTokenFireBase())
				.bloqueado(c.getBloqueado())
				.direcciones(direccionConverter.fromEntity(c.getDirecciones()))
				.build();
	}
	
	public ClienteMDTO ClienteMDTOfromCliente(Cliente c) {
		if(c == null) return null;
		return ClienteMDTO.builder()
				.id(c.getId())
				.nombre(c.getNombre())
				.username(c.getUsername())
				.password(c.getPassword())
				.telefono(c.getTelefono())
				.correo(c.getCorreo())
				.tokenFireBase(c.getTokenFireBase())
				.bloqueado(c.getBloqueado())
				.direcciones(direccionConverter.fromEntity(c.getDirecciones()))
				.build();
	}

	public Cliente fromClienteCrearDTO(ClienteCrearDTO c) throws AppettitException, ParseException {
		if(c == null) return null;
		
		List<Direccion> direcciones = new ArrayList<Direccion>();
		Direccion direccion = direccionConverter.fromCrearDTO(c.getDireccion());
		direcciones.add(direccion);
		
		return Cliente.builder()
				.nombre(c.getNombre())
				.username(c.getUsername())
				.password(c.getPassword())
				.telefono(c.getTelefono())
				.correo(c.getCorreo())
				.tokenFireBase(c.getTokenFireBase())
				.direcciones(direcciones)
				.build();
	}
	
	public Cliente fromClienteDTO(ClienteDTO c) {
		if(c == null) return null;
		return Cliente.builder()
				.id(c.getId())
				.nombre(c.getNombre())
				.username(c.getUsername())
				.password(c.getPassword())
				.telefono(c.getTelefono())
				.correo(c.getCorreo())
				.tokenFireBase(c.getTokenFireBase())
				.bloqueado(c.getBloqueado())
				.direcciones(direccionConverter.fromDTO(c.getDirecciones()))
				.build();
	}
	
	
	public List<ClienteDTO> fromCliente(List<Cliente> entities){
		if(entities == null) return null;
		return entities.stream()
				.map(e -> fromCliente(e))
				.collect(Collectors.toList());
	}

	public List<Cliente> fromClienteDTO(List<ClienteDTO> datos){
		if(datos == null) return null;
		return datos.stream()
				.map(d -> fromClienteDTO(d))
				.collect(Collectors.toList());
	}
	
}
