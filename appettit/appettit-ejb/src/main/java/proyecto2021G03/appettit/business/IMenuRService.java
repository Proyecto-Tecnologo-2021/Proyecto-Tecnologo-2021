package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IMenuRService {

    public List<MenuRDTO> listar() throws AppettitException;
}
