package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.entity.Imagen;

@Local
public interface IImagenDAO {

	public Imagen crear(Imagen imagen);
	public Imagen editar (Imagen imagen);
	public void eliminar (Imagen imagen);
	public List<Imagen> listar();
	public Imagen buscarPorId(String id);
	public Imagen buscarPorIdentificador(String identificador);

}
