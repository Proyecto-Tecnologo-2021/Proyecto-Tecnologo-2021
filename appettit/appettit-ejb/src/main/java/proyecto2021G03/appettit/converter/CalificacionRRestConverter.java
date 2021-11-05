package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;

@Singleton
public class CalificacionRRestConverter {



    @EJB
    IUsuarioDAO iUsuarioDAO;
    @EJB
    IPedidoDao iPedidoDao;

    //@Override
    public CalificacionRPedidoDTO fromEntity(ClasificacionPedido clasificacionPedido) {
        if (clasificacionPedido == null) return null;
        return CalificacionRPedidoDTO.builder()
                .id_cliente(clasificacionPedido.getId_cliente())
                .id_pedido(clasificacionPedido.getId_pedido())
                .comentario(clasificacionPedido.getComentario())
                .comida(clasificacionPedido.getComida())
                .rapidez(clasificacionPedido.getRapidez())
                .servicio(clasificacionPedido.getServicio())
                .build();
    }

  //  @Override
    public ClasificacionPedido fromDTO(CalificacionRPedidoDTO calificacionRPedidoDTO) {

        if (calificacionRPedidoDTO == null) return null;
        return ClasificacionPedido.builder()
                .cliente(iUsuarioDAO.buscarPorIdCliente(calificacionRPedidoDTO.getId_cliente()))
                .pedido(iPedidoDao.listarPorId(calificacionRPedidoDTO.getId_cliente()))
                .servicio(calificacionRPedidoDTO.getServicio())
                .rapidez(calificacionRPedidoDTO.getRapidez())
                .comida(calificacionRPedidoDTO.getComida())
                .comentario(calificacionRPedidoDTO.getComentario())
                .id_cliente(calificacionRPedidoDTO.getId_cliente())
                .id_pedido(calificacionRPedidoDTO.getId_pedido())
                .build();
    }
}
