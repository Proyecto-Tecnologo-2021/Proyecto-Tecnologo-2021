package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Menu;

import java.util.List;

public interface IMenuDao {
    public List<Menu> listar();
    public Menu listarPorId(Long id);
    public Menu crear(Menu menu);
    public Menu editar(Menu menu);
    public void eliminar(Menu menu);
	public List<Menu> listarPorRestaurate(Long id);
    }
