package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ICalificacionRService {

    public List<CalificacionPedidoDTO> listar() throws AppettitException;
    public CalificacionPedidoDTO listarPorId(Long id);
    public CalificacionPedidoDTO crear(CalificacionPedidoDTO calificacionPedidoDTO)throws AppettitException;
    public CalificacionPedidoDTO editar(Long id, CalificacionPedidoDTO calificacionPedidoDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
