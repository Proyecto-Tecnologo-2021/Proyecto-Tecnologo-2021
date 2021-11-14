package proyecto2021G03.appettit.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import lombok.Builder;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.ExtraMenuRDTO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.ProductoRDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Promocion;

@Singleton
public class PedidoRConverter extends AbstractConverter<Pedido, PedidoRDTO>{

    @EJB
    DireccionConverter dirConverter;

    @EJB
    MenuConverter menuConverter;

    @EJB
    MenuRConverter menuRConverter;

    @EJB
    PromocionConverter promocionConverter;

    @EJB
    ReclamoConverter reclamoConverter;

    @EJB
    UsuarioConverter usrConverter;

    @EJB
    IUsuarioDAO isuarioDAO;

    @EJB
    PromocionRConverter promocionRConverter;

    @EJB
    ExtraMenuConverter extraConverter;
    
    @EJB
    DireccionConverter direccionConverter;
    
    @Override
    public PedidoRDTO fromEntity(Pedido pedido) {
        if(pedido== null) return null;
        
        PedidoRDTO pedidofinal = PedidoRDTO.builder()
        		.id(pedido.getId())
                .idcli(pedido.getId_cliente())
                .iddir(pedido.getId_entrega())
                .menus(this.getMenuPromo(pedido))
                .pago(pedido.getPago())
                .tipo(pedido.getTipo())
                .total(pedido.getTotal())
                .idrest(pedido.getRestaurante().getId())
                .fecha(LocalDateTime.now())
                .estado(pedido.getEstado())
                .extras(extraConverter.fromEntityToRDTO(pedido.getExtraMenus()))
                .id_paypal(pedido.getId_paypal())
                .cotizacion(pedido.getCotizacion())
                .build();
        
               return pedidofinal;


    }

    @Override
    public Pedido fromDTO(PedidoRDTO pedidoRDTO) {
        if(pedidoRDTO== null) return null;
        return Pedido.builder()
        		.id(null)
        		.estado(pedidoRDTO.getEstado())
        		.tipo(pedidoRDTO.getTipo())
        		.pago(pedidoRDTO.getPago())
        		.fecha(pedidoRDTO.getFecha())
        		.total(pedidoRDTO.getTotal())
        		.restaurante(isuarioDAO.buscarRestaurantePorId(pedidoRDTO.getIdrest()))
        		.id_restaurante(pedidoRDTO.getIdrest())
        		.cliente(isuarioDAO.buscarPorIdCliente(pedidoRDTO.getIdcli()))
        		.id_cliente(pedidoRDTO.getIdcli())
        		.menus(menuRConverter.fromDTO(pedidoRDTO.filtroMenu()))
                .promociones(promocionConverter.fromRDTO(pedidoRDTO.filtroPromo()))
                .extraMenus(extraConverter.fromRDTO(pedidoRDTO.getExtras()))
                .entrega(isuarioDAO.buscarDireccionPorId(pedidoRDTO.getIddir()))
                .id_entrega(pedidoRDTO.getIddir())
                .reclamo(null)
                .id_paypal(pedidoRDTO.getId_paypal())
                .cotizacion(pedidoRDTO.getCotizacion())
        		.build();
        
    }
    
    private List<MenuRDTO> getMenuPromo(Pedido pedido){
		
    	List<MenuRDTO> menus= menuRConverter.fromEntityList(pedido.getMenus());
    	for(Promocion prom: pedido.getPromociones()) {
      		
    		menus.add(new MenuRDTO(
    				prom.getId(), 
    				prom.getId_restaurante(),
    				prom.getRestaurante().getNombre(),
    				prom.getDescuento(), 
    				prom.getNombre(), 
    				prom.getDescripcion(),
    				prom.getPrecio(), 
    				prom.getPrecio(), 
    				null, 
    				null, 
    				prom.getId_imagen(), 
    				null,
    				"PROM",
    				-1));
    		
        	
        }
        return menus;
    	
    }
}



