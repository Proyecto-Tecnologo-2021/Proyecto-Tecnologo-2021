package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ExtraMenu;

import java.util.List;

public interface IExtraMenuDAO {
    public List<ExtraMenu> listar();
    public ExtraMenu listarPorId(Long id);
    public ExtraMenu crear(ExtraMenu extraMenu);
    public ExtraMenu editar(ExtraMenu extraMenu);
    public void eliminar(ExtraMenu extraMenu);
}
