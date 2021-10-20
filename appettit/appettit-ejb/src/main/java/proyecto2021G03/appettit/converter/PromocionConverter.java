package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.entity.Promocion;

@Singleton
public class PromocionConverter extends AbstractConverter<Promocion, PromocionDTO> {

	@EJB
	MenuConverter menuConverter;
	
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
				.menus(menuConverter.fromDTO(d.getMenus()))
				.build();	
		}

}
