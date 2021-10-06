package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.entity.Ciudad;

@Singleton
public class CiudadConverter extends AbstractConverter<Ciudad, CiudadDTO> {

	@EJB
	private LocalidadConverter localidadConverter;
	
	@Override
	public CiudadDTO fromEntity(Ciudad e) {
		if(e == null) return null;
		return CiudadDTO.builder()
				.id(e.getId())
				.id_departamento(e.getId_departamento())
				.nombre(e.getNombre())
				.localidades(localidadConverter.fromEntity(e.getLocalidades()))
				.geometry(e.getGeometry())
				.build();
	}

	@Override
	public Ciudad fromDTO(CiudadDTO d) {
		if(d == null) return null;
		return Ciudad.builder()
				.id(d.getId())
				.id_departamento(d.getId_departamento())
				.localidades(localidadConverter.fromDTO(d.getLocalidades()))
				.nombre(d.getNombre())
				.geometry(d.getGeometry())
				.build();
	}

}
