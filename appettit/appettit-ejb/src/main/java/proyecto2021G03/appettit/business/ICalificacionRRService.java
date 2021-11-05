package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.Local;


@Local
public interface ICalificacionRRService {

    public CalificacionRPedidoDTO crear(CalificacionRPedidoDTO calificacionRPedidoDTO)throws AppettitException;
}
