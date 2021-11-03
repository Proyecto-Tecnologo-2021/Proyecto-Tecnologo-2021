package proyecto2021G03.appettit.converter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.business.UsuarioService;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.exception.AppettitException;

@Singleton
public class MenuRConverter extends AbstractConverter<Menu,MenuRDTO>{

@EJB
    ProductoConverter productoConverter;
@EJB
    ExtraMenuConverter extraMenuConverter;

@EJB
    IUsuarioService iUsuarioService;

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
                .id(menuRDTO.getId())
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

  // @Override
    public List<MenuRDTO> fromEntityList(List<Menu> menues) {
    if (menues == null) return null;
    else {
        List<MenuRDTO> listafinal = new ArrayList<>();
        for (Menu m : menues) {
            try {
                listafinal.add(MenuRDTO.builder()
                        .id(m.getId())
                        .nom_restaurante(iUsuarioService.buscarRestaurantePorId(m.getId_restaurante()).getNombre())
                        .descuento(0D)
                        .nombre(m.getNombre())
                        .id_imagen(m.getId_imagen())
                        .id_restaurante(m.getId_restaurante())
                        .descripcion(m.getDescripcion())
                        .precioSimple(m.getPrecioSimple())
                        .precioTotal(m.getPrecioTotal())
                        .extras(extraMenuConverter.fromEntityToRDTO(m.getExtras()))
                        .productos(productoConverter.fromEntityToRDTO(m.getProductos()))
                        .build());
            } catch (AppettitException e) {
                e.printStackTrace();
            }

        }
        return listafinal;
    }
}
}
