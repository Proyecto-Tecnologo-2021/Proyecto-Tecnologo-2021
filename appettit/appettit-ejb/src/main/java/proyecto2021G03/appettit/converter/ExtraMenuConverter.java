package proyecto2021G03.appettit.converter;


import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.entity.ExtraMenu;
import proyecto2021G03.appettit.entity.Producto;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class ExtraMenuConverter extends AbstractConverter<ExtraMenu, ExtraMenuDTO> {

   @EJB
   ProductoConverter productoConverter;
    @Override
    public ExtraMenuDTO fromEntity(ExtraMenu extraMenu) {
        if(extraMenu == null) return null;
        return ExtraMenuDTO.builder()
                .id(extraMenu.getId())
                .precio(extraMenu.getPrecio())
                .producto(productoConverter.fromEntity(extraMenu.getProducto()))
                .build();

    }

    @Override
    public ExtraMenu fromDTO(ExtraMenuDTO extraMenuDTO) {
        if(extraMenuDTO == null) return null;
        return ExtraMenu.builder()
                .id(extraMenuDTO.getId())
                .precio(extraMenuDTO.getPrecio())
                .producto(productoConverter.fromDTO(extraMenuDTO.getProducto()))
                .build();
    }
}
