package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.entity.Producto;

@Local
public interface IProductoDAO {

	public List<Producto> listar();
	public Producto listarPorId(Long id);
	public Producto crear(Producto producto);
	public Producto editar(Producto producto);
	public void eliminar(Producto producto);
	public List<Producto> listarPorRestaurante(Long id);
	
}