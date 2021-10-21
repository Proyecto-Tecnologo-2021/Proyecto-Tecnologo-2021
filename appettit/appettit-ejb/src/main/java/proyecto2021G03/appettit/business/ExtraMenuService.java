package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.ExtraMenuConverter;
import proyecto2021G03.appettit.dao.IExtraMenuDAO;
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.entity.ExtraMenu;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import java.util.List;

@Stateless
public class ExtraMenuService implements IExtraMenuService{
    @EJB
    IExtraMenuDAO iExtraMenuDAO;
    @EJB
    ExtraMenuConverter extraMenuConverter;


    @Override
    public List<ExtraMenuDTO> listar() throws AppettitException {
        try {
            return extraMenuConverter.fromEntity(iExtraMenuDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public ExtraMenuDTO listarPorId(Long id) throws AppettitException {
            try {
                return extraMenuConverter.fromEntity(iExtraMenuDAO.listarPorId(id));
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }

    }

    @Override
    public ExtraMenuDTO crear(ExtraMenuDTO extraMenuDTO) throws AppettitException {
        ExtraMenu extraMenuService = iExtraMenuDAO.listarPorId(extraMenuDTO.getId());
        try {
            return extraMenuConverter.fromEntity(iExtraMenuDAO.crear(extraMenuService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public ExtraMenuDTO editar(Long id, ExtraMenuDTO extraMenuDTO) throws AppettitException {
        ExtraMenu extraMenuService = iExtraMenuDAO.listarPorId(extraMenuDTO.getId());
        if (extraMenuService == null)
            throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            extraMenuService.setPrecio(extraMenuDTO.getPrecio());
            return extraMenuConverter.fromEntity(iExtraMenuDAO.editar(extraMenuService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }



    @Override
    public void eliminar(Long id) throws AppettitException {
        ExtraMenu extraMenuService = iExtraMenuDAO.listarPorId(id);
        if (extraMenuService == null)
            throw new AppettitException("El extra menu no esta.", AppettitException.NO_EXISTE_REGISTRO);
        try {
            iExtraMenuDAO.eliminar(extraMenuService);
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }

    }
}
