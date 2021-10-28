package proyecto2021G03.appettit.converter;

import proyecto2021G03.appettit.dao.IPromocionDAO;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.entity.Restaurante;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.ArrayList;

@Singleton
public class PromocionRConverter extends AbstractConverter<Promocion, MenuRDTO>{
    @EJB
    ProductoConverter productoConverter;
    @EJB
    ExtraMenuConverter extraMenuConverter;
    @EJB
    IPromocionDAO iPromocionDAO;
    @EJB
    IUsuarioDAO iUsuarioDAO;

    @Override
    public MenuRDTO fromEntity(Promocion promocion) {
        if (promocion == null) return null;
        return MenuRDTO.builder()
                .nombre(promocion.getNombre())
                .id_imagen("")//falta resolver
                .id_restaurante(promocion.getId_restaurante())
                .descripcion(promocion.getDescripcion())
                .precioSimple(promocion.getPrecio()+(1*(100-promocion.getDescuento())/100))
                .precioTotal(promocion.getPrecio())
                .extras(new ArrayList<>())//ver
                .productos(new ArrayList<>()) //ver
                .build();
    }
    @Override
    public Promocion fromDTO(MenuRDTO menuRDTO) {
        if (menuRDTO == null) return null;
        return Promocion.builder()
                .id(menuRDTO.getId())
                .nombre(menuRDTO.getNombre())
                //.id_imagen(menuRDTO.getId_imagen())
                .id_restaurante(menuRDTO.getId_restaurante())
                .descripcion(menuRDTO.getDescripcion())
                .precio(menuRDTO.getPrecioTotal())
                .descuento(menuRDTO.getDescuento())
              //  .extras(extraMenuConverter.fromDTO(menuRDTO.getExtras()))
              //  .productos(productoConverter.fromDTO(menuRDTO.getProductos()))
                .menus(iPromocionDAO.listarPorId(menuRDTO.getId()).getMenus())
                .restaurante((Restaurante) iUsuarioDAO.buscarPorId(menuRDTO.getId_restaurante()))
                .build();
    }
}
