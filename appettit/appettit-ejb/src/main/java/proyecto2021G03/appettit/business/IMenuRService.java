package proyecto2021G03.appettit.business;

import java.util.List;

import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IMenuRService {

    public List<MenuRDTO> listar() throws AppettitException;
    public MenuRDTO listarPorId(Long id_restaurante, Long id) throws AppettitException;
	public List<MenuRDTO> listarPorRestaurnate(Long id_restaurante) throws AppettitException;
	public List<MenuRDTO> listarPorPunto(String punto) throws AppettitException;
}
