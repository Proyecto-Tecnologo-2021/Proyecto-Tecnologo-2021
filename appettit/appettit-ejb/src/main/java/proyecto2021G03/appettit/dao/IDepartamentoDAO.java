package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.entity.Localidad;


@Local
public interface IDepartamentoDAO {
	
	public Departamento crear(Departamento departamento);
	public Departamento editar (Departamento departamento);
	public void eliminar (Departamento departamento);
	public List<Departamento> listar();
	public Departamento buscarPorId(Long id);
	public List<Departamento> buscarPorNombre(String nombre);
	
	public Ciudad crearCiudad(Ciudad ciudad);
	public Ciudad ciudadPorId(Long id, Long id_departamento);
	
	public Localidad crearLocalidad(Localidad localidad);
	public Localidad localidadPorId(Long id, Long id_ciudad, Long id_departamento);

}
