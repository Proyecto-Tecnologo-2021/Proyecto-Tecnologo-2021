package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ClasificacionCliente;
import java.util.List;

public interface IClasificacionClienteDAO {

    public List<ClasificacionCliente> listar();
    public ClasificacionCliente listarPorId(Long id);
    public ClasificacionCliente crear(ClasificacionCliente clasificacionCliente);
    public ClasificacionCliente editar(ClasificacionCliente clasificacionCliente);
    public void eliminar(ClasificacionCliente clasificacionCliente);
}
