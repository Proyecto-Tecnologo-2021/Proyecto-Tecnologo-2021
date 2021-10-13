package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.entity.Direccion;

@Singleton
public class DireccionConverter extends AbstractConverter<Direccion, DireccionDTO> {

	@EJB
	private LocalidadConverter localidadConverter;
	
	@Override
	public DireccionDTO fromEntity(Direccion e) {
		if(e == null) return null;
		return DireccionDTO.builder()
				.id(e.getId())
				.calle(e.getCalle())
				.numero(e.getNumero())
				.apartamento(e.getApartamento())
				.barrio(localidadConverter.fromEntity(e.getBarrio()))
				.geometry(e.getGeometry())
				.build();
	}

	@Override
	public Direccion fromDTO(DireccionDTO d) {
		if(d == null) return null;
		return Direccion.builder()
				.id(d.getId())
				.calle(d.getCalle())
				.numero(d.getNumero())
				.apartamento(d.getApartamento())
				.barrio(localidadConverter.fromDTO(d.getBarrio()))
				.geometry(d.getGeometry())
				.build();
	}

}
