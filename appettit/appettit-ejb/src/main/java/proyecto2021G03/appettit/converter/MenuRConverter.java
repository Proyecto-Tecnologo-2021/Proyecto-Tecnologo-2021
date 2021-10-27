package proyecto2021G03.appettit.converter;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.entity.Menu;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class MenuRConverter extends AbstractConverter<Menu,MenuRDTO>{

@EJB
    ProductoConverter productoConverter;
@EJB
    ExtraMenuConverter extraMenuConverter;

    @Override
    public MenuRDTO fromEntity(Menu menu){
        if (menu == null) {
            return null;
        }else {

            return MenuRDTO.builder()
                    .id(menu.getId())
                    .nombre(menu.getNombre())
                    .id_imagen(menu.getId_imagen())
                    .id_restaurante(menu.getId_restaurante())
                    .nom_restaurante(menu.getRestaurante().getNombre())
                    .descripcion(menu.getDescripcion())
                    .precioSimple(menu.getPrecioSimple())
                    .precioTotal(menu.getPrecioTotal())
                    .descuento(0D)
                    .extras(extraMenuConverter.fromEntity(menu.getExtras()))
                    .productos(productoConverter.fromEntity(menu.getProductos()))
                    .build();
        }
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
                .extras(extraMenuConverter.fromDTO(menuRDTO.getExtras()))
                .productos(productoConverter.fromDTO(menuRDTO.getProductos()))
                .build();
    }

}
