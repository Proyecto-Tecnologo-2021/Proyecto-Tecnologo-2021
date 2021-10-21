package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.ClasificacionClienteConverter;
import proyecto2021G03.appettit.dao.IClasificacionClienteDAO;
import proyecto2021G03.appettit.dto.ClasificacionClienteDTO;
import proyecto2021G03.appettit.entity.ClasificacionCliente;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;
@Singleton

public class ClasificacionCService implements IClasificacionCService{

    @EJB
    public IClasificacionClienteDAO iClasificacionClienteDAO;

    @EJB
    public ClasificacionClienteConverter clasificacionClienteConverter;


    @Override
    public List<ClasificacionClienteDTO> listar() throws AppettitException {
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }       }

    @Override
    public ClasificacionClienteDTO listarPorId(Long id) {
        try {
            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.listarPorId(id));
        } catch (Exception e) {
            return null; //throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public ClasificacionClienteDTO crear(ClasificacionClienteDTO clasificacionClienteDTO) throws AppettitException {
        ClasificacionCliente clasificacionCliente = iClasificacionClienteDAO.listarPorId(clasificacionClienteDTO.getId_cliente());
        try {

            return clasificacionClienteConverter.fromEntity(iClasificacionClienteDAO.crear(clasificacionCliente));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }       }

    @Override
    public ClasificacionClienteDTO editar(Long id, ClasificacionClienteDTO clasificacionClienteDTO) throws AppettitException {
        ClasificacionCliente clasificacionCliente = iClasificacionClienteDAO.listarPorId(clasificacionClienteDTO.getId_cliente());
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

