package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.entity.Imagen;

@Singleton
public class ImagenConverter extends AbstractConverter<Imagen, ImagenDTO> {

	@EJB
	private LocalidadConverter localidadConverter;
	
	@Override
	public ImagenDTO fromEntity(Imagen e) {
		if(e == null) return null;
		return ImagenDTO.builder()
				.id(e.getId())
				.imagen(e.getImagen())
				.build();
	}

	@Override
	public Imagen fromDTO(ImagenDTO d) {
		if(d == null) return null;
		return Imagen.builder()
				.id(d.getId())
				.imagen(d.getImagen())
				.build();
	}

}
