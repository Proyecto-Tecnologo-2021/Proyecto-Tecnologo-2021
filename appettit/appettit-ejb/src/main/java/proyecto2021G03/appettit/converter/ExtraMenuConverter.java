package proyecto2021G03.appettit.converter;


import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.entity.ExtraMenu;
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
}
