package proyecto2021G03.appettit.business;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;


@Local
public interface ICalificacionRRService {

    public CalificacionRPedidoDTO crear(CalificacionRPedidoDTO calificacionRPedidoDTO) throws AppettitException;
    public CalificacionRPedidoDTO listarPorId(Long id_pedido, Long id_cliente) throws AppettitException;;
    public CalificacionRPedidoDTO editar(Long id, CalificacionRPedidoDTO calificacionPedidoDTO)throws AppettitException;
    public void eliminar(Long id_pedido, Long id_cliente)throws AppettitException;
}
