package proyecto2021G03.appettit.business;

import java.util.List;

import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.PedidoRMDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IPedidoService {
    public List<PedidoDTO> listar() throws AppettitException;
    public PedidoDTO listarPorId(Long id) throws AppettitException;;
    public PedidoDTO crear(PedidoDTO pedidoDTO)throws AppettitException;
    public PedidoDTO editar(Long id, PedidoDTO pedidoDTO)throws AppettitException;
    public PedidoDTO editarEstadoPago(PedidoDTO pedidoDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
    public PedidoRDTO crearFront(PedidoRDTO pedidoRDTO)throws AppettitException;
    public List<PedidoRDTO> listarPorClienteREST(Long id) throws AppettitException;
    public List<PedidoDTO> listarPorRestaurante(Long id) throws AppettitException;
    public PedidoRDTO ultimo(Long id)throws AppettitException;
    public PedidoDTO listarPorReclamo(Long id)throws AppettitException;
    public PedidoRDTO listarPorIdREST(Long id) throws AppettitException;
    public ReclamoDTO obtenerReclamo(Long id) throws AppettitException;
    public List<PedidoRMDTO> listarPorClienteMREST(Long id) throws AppettitException;

}
