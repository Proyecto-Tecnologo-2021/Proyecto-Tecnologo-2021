package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.MenuRConverter;
import proyecto2021G03.appettit.dao.IMenuRDAO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;
@Stateless
public class MenuRService implements IMenuRService{
    @EJB
    IMenuRDAO iMenuRDAO;
    @EJB
    MenuRConverter menuRConverter;

    @Override
    public List<MenuRDTO> listar() throws AppettitException {
        try {
            return menuRConverter.fromEntity(iMenuRDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }
}

