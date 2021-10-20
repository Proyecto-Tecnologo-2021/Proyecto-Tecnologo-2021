package proyecto2021G03.appettit.business;
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IExtraMenuService {

    public List<ExtraMenuDTO> listar() throws AppettitException;
    public ExtraMenuDTO listarPorId(Long id) throws AppettitException;
    public ExtraMenuDTO crear(ExtraMenuDTO extraMenuDTO)throws AppettitException;
    public ExtraMenuDTO editar(Long id, ExtraMenuDTO extraMenuDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
