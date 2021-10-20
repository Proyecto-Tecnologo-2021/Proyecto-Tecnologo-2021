package proyecto2021G03.appettit.converter;

import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.entity.Pedido;
import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class PedidoConverter extends AbstractConverter<Pedido, PedidoDTO>{

	@EJB
	DireccionConverter dirConverter;
	
	@EJB
	MenuConverter menuConverter;
	
	@EJB
	PromocionConverter promocionConverter;
	
	@EJB
	ReclamoConverter reclamoConverter;
	
	@EJB
	UsuarioConverter usrConverter;

    @Override
    public PedidoDTO fromEntity(Pedido pedido) {
        if(pedido== null) return null;
        return PedidoDTO.builder()
                .id(pedido.getId())
                .cliente(pedido.getCliente())
                .entrega(dirConverter.fromEntity(pedido.getEntrega()))
                .estado(pedido.getEstado())
                .fecha(pedido.getFecha())
                .menus(menuConverter.fromEntity(pedido.getMenus()))
                .motivo(pedido.getMotivo())
                .pago(pedido.getPago())
                .promociones(promocionConverter.fromEntity(pedido.getPromociones()))
                .reclamo(reclamoConverter.fromEntity(pedido.getReclamo()))
                .restaurante(usrConverter.fromRestaurante(pedido.getRestaurante()))
                .tiempoEstimado(pedido.getTiempoEstimado())
                .tipo(pedido.getTipo())
                .total(pedido.getTotal())

                .build();
    }

    @Override
    public Pedido fromDTO(PedidoDTO pedidoDTO) {
        if(pedidoDTO== null) return null;
        return Pedido.builder()
                .id(pedidoDTO.getId())
                .cliente(pedidoDTO.getCliente())
                .entrega(dirConverter.fromDTO(pedidoDTO.getEntrega()))
                .estado(pedidoDTO.getEstado())
                .fecha(pedidoDTO.getFecha())
                .menus(menuConverter.fromDTO(pedidoDTO.getMenus()))
                .motivo(pedidoDTO.getMotivo())
                .pago(pedidoDTO.getPago())
                .promociones(promocionConverter.fromDTO(pedidoDTO.getPromociones()))
                .reclamo(reclamoConverter.fromDTO(pedidoDTO.getReclamo()))
                .restaurante(usrConverter.fromRestauranteDTO(pedidoDTO.getRestaurante()))
                .tiempoEstimado(pedidoDTO.getTiempoEstimado())
                .tipo(pedidoDTO.getTipo())
                .total(pedidoDTO.getTotal())
                .build();
    }
}
