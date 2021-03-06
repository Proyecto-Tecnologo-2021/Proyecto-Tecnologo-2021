package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.MenuRConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dao.IMenuRDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;
@Stateless
public class MenuRService implements IMenuRService{
	
	static Logger logger = Logger.getLogger(MenuRService.class);
	
    @EJB
    IMenuRDAO iMenuRDAO;
    
    @EJB
    public IGeoDAO geoDAO;
    
    @EJB
    MenuRConverter menuRConverter;
    
    @EJB
    IImagenService imgSrv;

    @EJB
    IUsuarioService usrSrv;

    
    @Override
    public List<MenuRDTO> listar() throws AppettitException {
    	List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
		try {
			Iterator<MenuRDTO> it = menuRConverter.fromEntity(iMenuRDAO.listar())
					.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();
				men.setCal_restaurante(usrSrv.calificacionRestaurante(men.getId_restaurante()).getGeneral());
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

	@Override
	public MenuRDTO listarPorId(Long id_restaurante, Long id) throws AppettitException {
		try {
			MenuRDTO men = menuRConverter.fromEntity(iMenuRDAO.listarPorId(id_restaurante, id));
			ImagenDTO img = new ImagenDTO();
			men.setCal_restaurante(usrSrv.calificacionRestaurante(id_restaurante).getGeneral());
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
            return men;
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
	}

	@Override
	public List<MenuRDTO> listarPorRestaurnate(Long id_restaurante) throws AppettitException {
		List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
		try {
			Iterator<MenuRDTO> it = menuRConverter.fromEntity(iMenuRDAO.listarPorRestaurate(id_restaurante))
					.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();
				men.setCal_restaurante(usrSrv.calificacionRestaurante(id_restaurante).getGeneral());
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

	@Override
	public List<MenuRDTO> listarPorPunto(String punto) throws AppettitException {
		List<MenuRDTO> menus = new ArrayList<MenuRDTO>();
		try {
			Iterator<MenuRDTO> it = menuRConverter.fromEntity(geoDAO.menuRestaurantesPorPunto(punto))
					.iterator();
			while (it.hasNext()) {
				MenuRDTO men = it.next();
				ImagenDTO img = new ImagenDTO();
				men.setCal_restaurante(usrSrv.calificacionRestaurante(men.getId_restaurante()).getGeneral());
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

