package proyecto2021G03.appettit.converter;


import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.ExtraMenuRDTO;
import proyecto2021G03.appettit.entity.ExtraMenu;

@Singleton
public class ExtraMenuConverter extends AbstractConverter<ExtraMenu, ExtraMenuDTO> {

   @EJB
   ProductoConverter productoConverter;
   
    @Override
    public ExtraMenuDTO fromEntity(ExtraMenu extraMenu) {
        if(extraMenu == null) return null;
        return ExtraMenuDTO.builder()
                .id(extraMenu.getId())
                .id_producto(extraMenu.getId_producto())
                .id_restaurante(extraMenu.getId_restaurante())
                .precio(extraMenu.getPrecio())
                .producto(productoConverter.fromEntity(extraMenu.getProducto()))
                .build();

    }

    @Override
    public ExtraMenu fromDTO(ExtraMenuDTO extraMenuDTO) {
        if(extraMenuDTO == null) return null;
        return ExtraMenu.builder()
                .id(extraMenuDTO.getId())
                .id_producto(extraMenuDTO.getId_producto())
                .id_restaurante(extraMenuDTO.getId_restaurante())
                .precio(extraMenuDTO.getPrecio())
                .producto(productoConverter.fromDTO(extraMenuDTO.getProducto()))
                .build();
    }
    
    public ExtraMenuRDTO fromEntityToRDTO(ExtraMenu extraMenu) {
        if(extraMenu == null) return null;
        return ExtraMenuRDTO.builder()
                .id(extraMenu.getId())
                .id_producto(extraMenu.getId_producto())
                .id_restaurante(extraMenu.getId_restaurante())
                .precio(extraMenu.getPrecio())
                .producto(extraMenu.getProducto().getNombre())
                .build();

    }

    
    public ExtraMenu fromRDTO(ExtraMenuRDTO d) {
        if(d == null) return null;
        return ExtraMenu.builder()
                .id(d.getId())
                .id_producto(d.getId_producto())
                .id_restaurante(d.getId_restaurante())
                .precio(d.getPrecio())
                .producto(null)
                .build();
    }
    
    public List<ExtraMenuRDTO> fromEntityToRDTO(List<ExtraMenu> entities){
		if(entities == null) return null;
		return entities.stream()
			.map(e -> fromEntityToRDTO(e))
			.collect(Collectors.toList());
	}
	
	public List<ExtraMenu> fromRDTO(List<ExtraMenuRDTO> dtos){
		if(dtos == null) return null;
		return dtos.stream()
			.map(d -> fromRDTO(d))
			.collect(Collectors.toList());
	}
	
    
}
