package proyecto2021G03.appettit.converter;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.entity.Promocion;

@Singleton
public class PromocionConverter extends AbstractConverter<Promocion, PromocionDTO> {

	@EJB
	MenuConverter menuConverter;
	
	@EJB
	MenuRConverter menuRConverter;
	
	@EJB
	UsuarioConverter usrConverter;
	
	@Override
	public PromocionDTO fromEntity(Promocion e) {
		if(e == null) return null;
		return PromocionDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.restaurante(usrConverter.fromRestaurante(e.getRestaurante()))
				.descripcion(e.getDescripcion())
				.descuento(e.getDescuento())
				.precio(e.getPrecio())
				.id_imagen(e.getId_imagen())
				.menus(menuConverter.fromEntity(e.getMenus()))
				.build();
	}

	@Override
	public Promocion fromDTO(PromocionDTO d) {
		if(d == null) return null;
		return Promocion.builder()
				.id(d.getId())
				.nombre(d.getNombre())
				.restaurante(usrConverter.fromRestauranteDTO(d.getRestaurante()))
				.descripcion(d.getDescripcion())
				.descuento(d.getDescuento())
				.precio(d.getPrecio())
				.id_imagen(d.getId_imagen())
				.menus(menuConverter.fromDTO(d.getMenus()))
				.build();	
		}

	public PromocionRDTO fromEntityToRDTO(Promocion e) {
		if(e == null) return null;
		return PromocionRDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.id_restaurante(e.getId_restaurante())
				.nom_restaurante(e.getRestaurante().getNombre())
				.descripcion(e.getDescripcion())
				.descuento(e.getDescuento())
				.precio(e.getPrecio())
				.id_imagen(e.getId_imagen())
				.menus(menuRConverter.fromEntity(e.getMenus()))
				.build();
	}

	public Promocion fromRDTO(PromocionRDTO d) {
		if(d == null) return null;
		return Promocion.builder()
				.id(d.getId())
				.nombre(d.getNombre())
				.id_restaurante(d.getId_restaurante())
				.descripcion(d.getDescripcion())
				.descuento(d.getDescuento())
				.precio(d.getPrecio())
				.id_imagen(d.getId_imagen())
				.menus(menuRConverter.fromDTO(d.getMenus()))
				.build();	
		}
	
	 public List<PromocionRDTO> fromEntityToRDTO(List<Promocion> entities){
			if(entities == null) return null;
			return entities.stream()
				.map(e -> fromEntityToRDTO(e))
				.collect(Collectors.toList());
	}
	
	public List<Promocion> fromRDTO(List<PromocionRDTO> dtos){
		if(dtos == null) return null;
		return dtos.stream()
			.map(d -> fromRDTO(d))
			.collect(Collectors.toList());
	}
}
