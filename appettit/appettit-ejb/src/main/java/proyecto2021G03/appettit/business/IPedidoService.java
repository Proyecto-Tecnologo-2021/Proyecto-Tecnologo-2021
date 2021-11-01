package proyecto2021G03.appettit.business;

import java.util.List;

import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IPedidoService {
    public List<PedidoDTO> listar() throws AppettitException;
    public PedidoDTO listarPorId(Long id);
    public PedidoDTO crear(PedidoDTO pedidoDTO)throws AppettitException;
    public PedidoDTO editar(Long id, PedidoDTO pedidoDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
    public PedidoRDTO crearFront(PedidoRDTO pedidoRDTO)throws AppettitException;
}
