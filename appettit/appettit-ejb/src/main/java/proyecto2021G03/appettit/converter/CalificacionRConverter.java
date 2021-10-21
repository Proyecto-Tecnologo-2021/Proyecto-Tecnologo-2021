package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;

@Singleton
public class CalificacionRConverter extends AbstractConverter<ClasificacionPedido, CalificacionPedidoDTO>{
    @Override
    public CalificacionPedidoDTO fromEntity(ClasificacionPedido clasificacionPedido) {
        if(clasificacionPedido == null) return null;
        return CalificacionPedidoDTO.builder()
                .comida(clasificacionPedido.getComida())
                .rapidez(clasificacionPedido.getRapidez())
                .servicio(clasificacionPedido.getServicio())
                .cliente(clasificacionPedido.getCliente())
                .comentario(clasificacionPedido.getComentario())
                .pedido(clasificacionPedido.getPedido())
                .build();
    }

    @Override
    public ClasificacionPedido fromDTO(CalificacionPedidoDTO calificacionPedidoDTO) {
       if(calificacionPedidoDTO == null) return null;
       return  ClasificacionPedido.builder()
               .cliente(calificacionPedidoDTO.getCliente())
               .comentario(calificacionPedidoDTO.getComentario())
               .comida(calificacionPedidoDTO.getComida())
               .rapidez(calificacionPedidoDTO.getRapidez())
               .pedido(calificacionPedidoDTO.getPedido())
               .servicio(calificacionPedidoDTO.getServicio())
               .build();
    }




}
