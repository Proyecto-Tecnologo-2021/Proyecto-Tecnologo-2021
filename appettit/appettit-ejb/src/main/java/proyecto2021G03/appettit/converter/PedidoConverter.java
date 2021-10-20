package proyecto2021G03.appettit.converter;

import proyecto2021G03.appettit.dao.PedidoDAO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Producto;

import javax.ejb.Singleton;

@Singleton
public class PedidoConverter extends AbstractConverter<Pedido, PedidoDTO>{


    @Override
    public PedidoDTO fromEntity(Pedido pedido) {
        if(pedido== null) return null;
        return PedidoDTO.builder()
                .id(pedido.getId())
                .cliente(pedido.getCliente())
                .entrega(pedido.getEntrega())
                .estado(pedido.getEstado())
                .fecha(pedido.getFecha())
                .menus(pedido.getMenus())
                .motivo(pedido.getMotivo())
                .pago(pedido.getPago())
                .promociones(pedido.getPromociones())
                .reclamo(pedido.getReclamo())
                .restaurante(pedido.getRestaurante())
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
                .entrega(pedidoDTO.getEntrega())
                .estado(pedidoDTO.getEstado())
                .fecha(pedidoDTO.getFecha())
                .menus(pedidoDTO.getMenus())
                .motivo(pedidoDTO.getMotivo())
                .pago(pedidoDTO.getPago())
                .promociones(pedidoDTO.getPromociones())
                .reclamo(pedidoDTO.getReclamo())
                .restaurante(pedidoDTO.getRestaurante())
                .tiempoEstimado(pedidoDTO.getTiempoEstimado())
                .tipo(pedidoDTO.getTipo())
                .total(pedidoDTO.getTotal())

                .build();
    }
}
