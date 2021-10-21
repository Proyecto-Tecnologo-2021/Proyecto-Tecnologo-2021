package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface ICalificacionRService {

    public List<CalificacionPedidoDTO> listar() throws AppettitException;
    public CalificacionPedidoDTO listarPorId(Long id);
    public CalificacionPedidoDTO crear(CalificacionPedidoDTO calificacionPedidoDTO)throws AppettitException;
    public CalificacionPedidoDTO editar(Long id, CalificacionPedidoDTO calificacionPedidoDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
