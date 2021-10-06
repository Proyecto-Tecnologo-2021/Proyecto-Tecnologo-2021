package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.entity.Localidad;

@Singleton
public class LocalidadConverter extends AbstractConverter<Localidad, LocalidadDTO> {

	@Override
	public LocalidadDTO fromEntity(Localidad e) {
		if(e == null) return null;
		return LocalidadDTO.builder()
				.id(e.getId())
				.id_ciudad(e.getId_ciudad())
				.id_departamento(e.getId_departamento())
				.nombre(e.getNombre())
				.geometry(e.getGeometry())
				.build();
	}

	@Override
	public Localidad fromDTO(LocalidadDTO d) {
		if(d == null) return null;
		return Localidad.builder()
				.id(d.getId())
				.id_ciudad(d.getId_ciudad())
				.id_departamento(d.getId_departamento())
				.nombre(d.getNombre())
				.geometry(d.getGeometry())
				.build();
	}

}
