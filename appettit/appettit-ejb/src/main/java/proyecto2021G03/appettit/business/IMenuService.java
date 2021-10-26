package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IMenuService {
    public List<MenuDTO> listar() throws AppettitException;
    public MenuDTO listarPorId(Long id) throws AppettitException;
    public MenuDTO crear(MenuDTO menuDTO)throws AppettitException;
    public MenuDTO editar(Long id, MenuDTO menuDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
    public List<MenuDTO> listarPorRestaurante(Long Id) throws AppettitException;
}
