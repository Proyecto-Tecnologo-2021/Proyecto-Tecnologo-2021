package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Pedido;
import java.util.List;

public interface IPedidoDao {
    public List<Pedido> listar();
    public Pedido listarPorId(Long id);
    public Pedido crear(Pedido pedido);
    public Pedido editar(Pedido pedido);
    public void eliminar(Pedido pedido);
}
