package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.entity.Categoria;

@Singleton
public class CategoriaConverter extends AbstractConverter<Categoria, CategoriaDTO>{

	
	@Override
	public CategoriaDTO fromEntity(Categoria e) {
		if(e == null) return null;
		return CategoriaDTO.builder()
				.id(e.getId())
				.nombre(e.getNombre())
				.build();
	}
	
	@Override
	public Categoria fromDTO(CategoriaDTO d) {
		return null;
	}
	
	public Categoria fromCrearDTO(CategoriaCrearDTO d) {
		if(d == null) return null;
		return Categoria.builder()
			.nombre(d.getNombre())
			.build();
	}
	

}
