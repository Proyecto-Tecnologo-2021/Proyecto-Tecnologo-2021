package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.MenuConverter;
import proyecto2021G03.appettit.dao.IMenuDao;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import java.util.List;

public class MenuService implements IMenuService{
    @EJB
    IMenuDao iMenuDao;
    @EJB
    MenuConverter menuConverter;



    @Override
    public List<MenuDTO> listar() throws AppettitException {
        try {
            return menuConverter.fromEntity(iMenuDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public MenuDTO listarPorId(Long id) throws AppettitException {
        try {
            return menuConverter.fromEntity(iMenuDao.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public MenuDTO crear(MenuDTO menuDTO) throws AppettitException {
        Menu MenuService = iMenuDao.listarPorId(menuDTO.getId());
        try {
            return menuConverter.fromEntity(iMenuDao.crear(MenuService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public MenuDTO editar(Long id, MenuDTO menuDTO) throws AppettitException {
        Menu menus = iMenuDao.listarPorId(menuDTO.getId());
        if (menus == null)
            throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            menus.setPrecioSimple(menuDTO.getPrecioSimple());
           // menus.setExtras(menuDTO.getExtras());
            menus.setId_imagen((menuDTO.getId_imagen()));
            menus.setNombre(menus.getNombre());
            menus.setPrecioTotal(menuDTO.getPrecioTotal());
            menus.setDescripcion(menuDTO.getDescripcion());

            return menuConverter.fromEntity(iMenuDao.editar(menus));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public void eliminar(Long id) throws AppettitException {
        Menu menu= iMenuDao.listarPorId(id);
        if(menu == null) {
            throw new AppettitException("El Menu indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
            try {
                iMenuDao.eliminar(menu);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }
}
