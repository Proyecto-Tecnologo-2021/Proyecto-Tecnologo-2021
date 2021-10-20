package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.entity.Reclamo;

@Singleton
public class ReclamoConverter extends AbstractConverter<Reclamo, ReclamoDTO> {

	@Override
	public ReclamoDTO fromEntity(Reclamo e) {
		if(e == null) return null;
		return ReclamoDTO.builder()
				.id(e.getId())
				.motivo(e.getMotivo())
				.detalles(e.getDetalles())
				.fecha(e.getFecha())
				.build();
	}

	@Override
	public Reclamo fromDTO(ReclamoDTO d) {
		if(d == null) return null;
		return Reclamo.builder()
				.id(d.getId())
				.motivo(d.getMotivo())
				.detalles(d.getDetalles())
				.fecha(d.getFecha())
				.build();
	}

}
