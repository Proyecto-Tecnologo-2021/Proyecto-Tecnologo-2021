package proyecto2021G03.appettit.business;

import com.vividsolutions.jts.io.ParseException;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IMenuService {
    public List<MenuDTO> listar() throws AppettitException;
    public MenuDTO listarPorId(Long id, Long id_restaurante) throws AppettitException;
    public MenuDTO crear(MenuDTO menuDTO)throws AppettitException, ParseException;
    public MenuDTO editar(Long id, MenuDTO menuDTO)throws AppettitException;
    public void eliminar(Long id, Long id_restaurante)throws AppettitException;
    public List<MenuDTO> listarPorRestaurante(Long Id) throws AppettitException;
}
