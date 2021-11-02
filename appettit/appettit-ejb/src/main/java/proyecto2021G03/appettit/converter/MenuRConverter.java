package proyecto2021G03.appettit.converter;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.entity.Menu;

@Singleton
public class MenuRConverter extends AbstractConverter<Menu,MenuRDTO>{

@EJB
    ProductoConverter productoConverter;
@EJB
    ExtraMenuConverter extraMenuConverter;

    @Override
    public MenuRDTO fromEntity(Menu menu) {
        if (menu == null) return null;
        return MenuRDTO.builder()
                .id(menu.getId())
                .nom_restaurante(menu.getRestaurante().getNombre())
                .descuento(0D)
                .nombre(menu.getNombre())
                .id_imagen(menu.getId_imagen())
                .id_restaurante(menu.getId_restaurante())
                .descripcion(menu.getDescripcion())
                .precioSimple(menu.getPrecioSimple())
                .precioTotal(menu.getPrecioTotal())
                .extras(extraMenuConverter.fromEntityToRDTO(menu.getExtras()))
                .productos(productoConverter.fromEntityToRDTO(menu.getProductos()))
                .build();
    }

    @Override
    public Menu fromDTO(MenuRDTO menuRDTO) {

        if (menuRDTO == null) return null;
        return Menu.builder()
                .nombre(menuRDTO.getNombre())
                .id_imagen(menuRDTO.getId_imagen())
                .id_restaurante(menuRDTO.getId_restaurante())
                .descripcion(menuRDTO.getDescripcion())
                .precioSimple(menuRDTO.getPrecioSimple())
                .precioTotal(menuRDTO.getPrecioTotal())
                .extras(extraMenuConverter.fromRDTO(menuRDTO.getExtras()))
                .productos(productoConverter.fromRDTO(menuRDTO.getProductos()))
                .build();
    }


}
