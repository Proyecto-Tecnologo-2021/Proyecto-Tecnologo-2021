package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ClasificacionPedido;

import java.util.List;

public interface ICalificacionRDao {
    public List<ClasificacionPedido> listar();
    public ClasificacionPedido listarPorId(Long id);
    public ClasificacionPedido crear(ClasificacionPedido clasificacionPedido);
    public ClasificacionPedido editar(ClasificacionPedido clasificacionPedido);
    public void eliminar(ClasificacionPedido clasificacionPedido);
}
