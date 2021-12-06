package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ICalificacionCService {

    public List<CalificacionClienteDTO> listar() throws AppettitException;
    public CalificacionClienteDTO listarPorId(Long id);
    public CalificacionClienteDTO crear(CalificacionClienteDTO calificacionClienteDTO)throws AppettitException;
    public CalificacionClienteDTO editar(Long id, CalificacionClienteDTO calificacionClienteDTO)throws AppettitException;
    public void eliminar(Long id)throws AppettitException;
}
