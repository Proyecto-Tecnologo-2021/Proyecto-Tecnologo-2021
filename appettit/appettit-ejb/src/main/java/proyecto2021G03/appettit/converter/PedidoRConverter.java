package proyecto2021G03.appettit.converter;

import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Restaurante;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import java.time.LocalDateTime;

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

    @Override
    public PedidoRDTO fromEntity(Pedido pedido) {
        if(pedido== null) return null;
        return PedidoRDTO.builder()
                .idcli(pedido.getId())
                .iddir(pedido.getCliente().getId())
                .menus(menuRConverter.fromEntity(pedido.getMenus()))
                .pago(pedido.getPago())
                .tipo(pedido.getTipo())
                .total(pedido.getTotal())
                .idrest(pedido.getRestaurante().getId())
                .fecha(LocalDateTime.now())
                .build();
    }

    @Override
    public Pedido fromDTO(PedidoRDTO pedidoRDTO) {
        if(pedidoRDTO== null) return null;
        return Pedido.builder()
                .id(pedidoRDTO.getIdcli())
                .pago(pedidoRDTO.getPago())
                .total(pedidoRDTO.getTotal())
                .estado(EstadoPedido.CONFIRMADO)
                .build();
    }
}
