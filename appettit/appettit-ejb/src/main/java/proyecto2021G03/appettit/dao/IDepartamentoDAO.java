package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.entity.Departamento;


@Local
public interface IDepartamentoDAO {
	
	public Departamento crear(Departamento departamento);
	public Departamento editar (Departamento departamento);
	public void eliminar (Departamento departamento);
	public List<Departamento> listar();
	public Departamento buscarPorId(Long id);
	public List<Departamento> buscarPorNombre(String nombre);
	

}
