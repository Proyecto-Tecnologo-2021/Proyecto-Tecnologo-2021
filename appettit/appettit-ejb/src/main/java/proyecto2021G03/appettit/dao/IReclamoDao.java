package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Reclamo;

import java.util.List;

public interface IReclamoDao {

    public List<Reclamo> listar();
    public Reclamo listarPorId(Long id);
    public Reclamo crear(Reclamo reclamo);
    public Reclamo editar(Reclamo reclamo);
    public void eliminar(Reclamo reclamo);
}
