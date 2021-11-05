package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;

@Singleton
public class CalificacionRConverter extends AbstractConverter<ClasificacionPedido, CalificacionPedidoDTO>{
	
	@EJB
	UsuarioConverter usrConverter;
	
	@EJB
	PedidoConverter pedConverter;
	
	
    @Override
    public CalificacionPedidoDTO fromEntity(ClasificacionPedido clasificacionPedido) {
        if(clasificacionPedido == null) return null;
        return CalificacionPedidoDTO.builder()
        		.id_pedido(clasificacionPedido.getId_pedido())
        		.id_cliente(clasificacionPedido.getId_cliente())
                .comida(clasificacionPedido.getComida())
                .rapidez(clasificacionPedido.getRapidez())
                .servicio(clasificacionPedido.getServicio())
                .cliente(usrConverter.fromCliente(clasificacionPedido.getCliente()))
                .comentario(clasificacionPedido.getComentario())
                .pedido(pedConverter.fromEntity(clasificacionPedido.getPedido()))
                .build();
    }

    @Override
    public ClasificacionPedido fromDTO(CalificacionPedidoDTO calificacionPedidoDTO) {
       if(calificacionPedidoDTO == null) return null;
       return  ClasificacionPedido.builder()
               .cliente(usrConverter.fromClienteDTO(calificacionPedidoDTO.getCliente()))
               .comentario(calificacionPedidoDTO.getComentario())
               .comida(calificacionPedidoDTO.getComida())
               .rapidez(calificacionPedidoDTO.getRapidez())
               .pedido(pedConverter.fromDTO(calificacionPedidoDTO.getPedido()))
               .servicio(calificacionPedidoDTO.getServicio())
               .build();
    }




}
