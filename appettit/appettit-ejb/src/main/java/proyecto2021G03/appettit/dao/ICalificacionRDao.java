package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.entity.ClasificacionPedido;

import java.util.List;

public interface ICalificacionRDao {
    public List<CalificacionRestauranteDTO> listar();
    public CalificacionRestauranteDTO listarPorId(Long id);
    public CalificacionRestauranteDTO crear(ClasificacionPedido clasificacionPedido);
    public CalificacionRestauranteDTO editar(ClasificacionPedido clasificacionPedido);
    public void eliminar(ClasificacionPedido clasificacionPedido);
}
