package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.entity.Departamento;

@Singleton
public class DepartamentoConverter extends AbstractConverter<Departamento, DepartamentoDTO> {

	@Override
	public DepartamentoDTO fromEntity(Departamento e) {
		if(e == null) return null;
		return DepartamentoDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.build();
	}

	@Override
	public Departamento fromDTO(DepartamentoDTO d) {
		if(d == null) return null;
		return Departamento.builder()
				.id(d.getId())
				.nombre(d.getNombre())
				.build();
	}

}
