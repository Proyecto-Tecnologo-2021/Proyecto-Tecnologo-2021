package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.CalificacionClienteConverter;
import proyecto2021G03.appettit.dao.IClasificacionClienteDAO;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.entity.ClasificacionCliente;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import java.util.List;

@Stateless
public class CalificacionCService implements ICalificacionCService {

    @EJB
    public IClasificacionClienteDAO iClasificacionClienteDAO;

    @EJB
    public CalificacionClienteConverter clasificacionClienteConverter;


    @Override
    public List<CalificacionClienteDTO> listar() throws AppettitException {
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }       }

    @Override
    public CalificacionClienteDTO listarPorId(Long id) {
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.listarPorId(id));
        } catch (Exception e) {
            return null; //throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public CalificacionClienteDTO crear(CalificacionClienteDTO calificacionClienteDTO) throws AppettitException {
        ClasificacionCliente clasificacionCliente = iClasificacionClienteDAO.listarPorId(calificacionClienteDTO.getId_cliente());
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.crear(clasificacionCliente));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public CalificacionClienteDTO editar(Long id, CalificacionClienteDTO calificacionClienteDTO) throws AppettitException {
        ClasificacionCliente clasificacionCliente = iClasificacionClienteDAO.listarPorId(calificacionClienteDTO.getId_cliente());
        if (clasificacionCliente == null)
            throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.editar(clasificacionCliente));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }     }

    @Override
    public void eliminar(Long id) throws AppettitException {
        ClasificacionCliente clasificacionCliente= iClasificacionClienteDAO.listarPorId(id);
        if(clasificacionCliente == null) {
            throw new AppettitException("El cliente indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
            try {
                iClasificacionClienteDAO.eliminar(clasificacionCliente);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }
    }

