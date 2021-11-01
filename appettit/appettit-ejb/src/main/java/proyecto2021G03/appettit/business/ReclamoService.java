package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.entity.Reclamo;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import java.util.List;

public class ReclamoService implements IReclamoService{
    @EJB
    public IReclamoDao iReclamoDao;

    @EJB
    public ReclamoConverter reclamoConverter;

    @Override
    public List<ReclamoDTO> listar() throws AppettitException {
        try {
            return reclamoConverter.fromEntity(iReclamoDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public ReclamoDTO listarPorId(Long id) throws AppettitException {
        try {
            return reclamoConverter.fromEntity(iReclamoDao.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public ReclamoDTO crear(ReclamoDTO reclamoDTO) throws AppettitException {
        Reclamo ReclamoService = iReclamoDao.listarPorId(reclamoDTO.getId());
        try {
            return reclamoConverter.fromEntity(iReclamoDao.crear(ReclamoService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public ReclamoDTO editar(Long id, ReclamoDTO reclamoDTO) throws AppettitException {
        Reclamo reclamo = iReclamoDao.listarPorId(reclamoDTO.getId());
        if (reclamo == null)
            throw new AppettitException("El reclamo indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            reclamo.setDetalles(reclamoDTO.getDetalles());
            reclamo.setFecha((reclamoDTO.getFecha()));
            reclamo.setId(reclamoDTO.getId());
            reclamo.setMotivo(reclamoDTO.getMotivo());


            return reclamoConverter.fromEntity(iReclamoDao.editar(reclamo));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public void eliminar(Long id) throws AppettitException {
        Reclamo reclamo= iReclamoDao.listarPorId(id);
        if( reclamo== null) {
        throw new AppettitException("El reclamo indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
    } else {
        try {
            iReclamoDao.eliminar(reclamo);
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }
}
}
