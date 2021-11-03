package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.MenuRConverter;
import proyecto2021G03.appettit.dao.IMenuRDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;
@Stateless
public class MenuRService implements IMenuRService{
	
	static Logger logger = Logger.getLogger(MenuRService.class);
	
    @EJB
    IMenuRDAO iMenuRDAO;
    @EJB
    MenuRConverter menuRConverter;
    
    @EJB
    IImagenService imgSrv;

    @Override
    public List<MenuRDTO> listar() throws AppettitException {
    	/*
        try {
            return menuRConverter.fromEntity(iMenuRDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
    	
    	List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
		try {
			Iterator<MenuRDTO> it = menuRConverter.fromEntity(iMenuRDAO.listar())
					.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();

				if (men.getId_imagen() == null || men.getId_imagen().equals("")) {
					FileManagement fm = new FileManagement();

					img.setIdentificador("Sin Imagen");
					img.setImagen(fm.getFileAsByteArray("META-INF/img/menu.png"));
				} else {
					try {
						img = imgSrv.buscarPorId(men.getId_imagen());	
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}
				men.setImagen(img);

				menus.add(men);

			}

			return menus;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
    	
    	
    	
    }
}

