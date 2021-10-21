package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.ClasificacionClienteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IClasificacionCService {

    public List<ClasificacionClienteDTO> listar() throws AppettitException;
    public ClasificacionClienteDTO listarPorId(Long id);
    public ClasificacionClienteDTO crear(ClasificacionClienteDTO clasificacionClienteDTO)throws AppettitException;
    public ClasificacionClienteDTO editar(Long id, ClasificacionClienteDTO clasificacionClienteDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
