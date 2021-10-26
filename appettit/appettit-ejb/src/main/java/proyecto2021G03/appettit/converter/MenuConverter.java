package proyecto2021G03.appettit.converter;

import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.entity.Menu;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class MenuConverter extends AbstractConverter<Menu, MenuDTO> {

    @EJB
    ProductoConverter productoConverter;
    @EJB
    ExtraMenuConverter extraMenuConverter;
    
    @EJB
    UsuarioConverter usrConverter;

    @Override
    public  MenuDTO fromEntity(Menu menu) {
        if (menu == null) return null;
        return MenuDTO.builder()
                .id(menu.getId())
                .nombre(menu.getNombre())
                .id_imagen(menu.getId_imagen())
                .id_restaurante(menu.getId_restaurante())
                .descripcion(menu.getDescripcion())
                .precioSimple(menu.getPrecioSimple())
                .precioTotal(menu.getPrecioTotal())
                .restaurante(usrConverter.fromRestaurante(menu.getRestaurante()))
                .extras(extraMenuConverter.fromEntity(menu.getExtras()))
                .productos(productoConverter.fromEntity(menu.getProductos()))
                .build();
    }



    @Override
    public Menu fromDTO(MenuDTO menuDTO) {
        if (menuDTO == null) return null;
        return Menu.builder()
                .id(menuDTO.getId())
                .nombre(menuDTO.getNombre())
                .id_imagen(menuDTO.getId_imagen())
                .id_restaurante(menuDTO.getId_restaurante())
                .descripcion(menuDTO.getDescripcion())
                .precioSimple(menuDTO.getPrecioSimple())
                .precioTotal(menuDTO.getPrecioTotal())
                .restaurante(usrConverter.fromRestauranteDTO(menuDTO.getRestaurante()))
                .extras(extraMenuConverter.fromDTO(menuDTO.getExtras()))
                .productos(productoConverter.fromDTO(menuDTO.getProductos()))
                .build();
    }

}
