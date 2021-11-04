package proyecto2021G03.appettit.dao;

import java.util.List;

import proyecto2021G03.appettit.entity.Menu;

public interface IMenuRDAO {

    public List<Menu> listar();
    public Menu listarPorId(Long id_restaurante, Long id);
	public List<Menu> listarPorRestaurate(Long id_restaurante);

}
