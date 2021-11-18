package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.ReclamoCDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IReclamoService {
    public List<ReclamoDTO> listar() throws AppettitException;
    public List<ReclamoDTO> listarPorRestaurante(Long id) throws AppettitException;
    public ReclamoDTO listarPorId(Long id) throws AppettitException;
    public ReclamoDTO crear(ReclamoCDTO reclamoCDTO)throws AppettitException;
    public ReclamoDTO editar(Long id, ReclamoDTO reclamoDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
