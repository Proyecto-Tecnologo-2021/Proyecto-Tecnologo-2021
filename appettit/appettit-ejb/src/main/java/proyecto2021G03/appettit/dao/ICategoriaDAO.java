package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.entity.Categoria;

@Local
public interface ICategoriaDAO {

	public List<Categoria> listar();
	public Categoria listarPorId(Long id);
	public Categoria crear(Categoria categoria);
	public Categoria editar(Categoria categoria);
	public void eliminar(Categoria categoria);
	
}
