package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.vividsolutions.jts.io.ParseException;
import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.MenuConverter;
import proyecto2021G03.appettit.dao.IMenuDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class MenuService implements IMenuService{
	
	static Logger logger = Logger.getLogger(MenuService.class);
	
    @EJB
    IMenuDAO iMenuDao;
    
    @EJB
    MenuConverter menuConverter;

    @EJB
    IImagenService imgSrv;


    @Override
    public List<MenuDTO> listar() throws AppettitException {
        /*
    	try {
            return menuConverter.fromEntity(iMenuDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
        
        List<MenuDTO> menus = new ArrayList<MenuDTO>();
		try {
			Iterator<MenuDTO> it = menuConverter.fromEntity(iMenuDao.listar())
					.iterator();
			while (it.hasNext()) {
				MenuDTO men = it.next();
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

    @Override
    public MenuDTO listarPorId(Long id, Long id_restaurante) throws AppettitException {
        /*
    	try {
            return menuConverter.fromEntity(iMenuDao.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
    	
    	MenuDTO men = menuConverter.fromEntity(iMenuDao.listarPorId(id, id_restaurante));
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
		
		return men;
    	
    }

    @Override
    public MenuDTO crear(MenuDTO menuDTO) throws AppettitException, ParseException {
        Menu menu = menuConverter.fromDTO(menuDTO);
        
        try {
            return menuConverter.fromEntity(iMenuDao.crear(menu));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public MenuDTO editar(Long id, MenuDTO menuDTO) throws AppettitException {
        Menu menus = iMenuDao.listarPorId(menuDTO.getId(), menuDTO.getId_restaurante());
        if (menus == null)
            throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            menus.setPrecioSimple(menuDTO.getPrecioSimple());
           // menus.setExtras(menuDTO.getExtras());
            menus.setId_imagen((menuDTO.getId_imagen()));
            menus.setNombre(menus.getNombre());
            menus.setPrecioTotal(menuDTO.getPrecioTotal());
            menus.setDescripcion(menuDTO.getDescripcion());

            MenuDTO men = menuConverter.fromEntity(iMenuDao.editar(menus));
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
    		
    		return men;
            
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public void eliminar(Long id, Long id_restaurante) throws AppettitException {
        Menu menu= iMenuDao.listarPorId(id, id_restaurante);
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


    @Override
	public List<MenuDTO> listarPorRestaurante(Long Id) throws AppettitException {
		/* 
    	try {
	            return menuConverter.fromEntity(iMenuDao.listarPorRestaurate(Id));
	        } catch (Exception e) {
	            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
	        }
	    */
    	List<MenuDTO> menus = new ArrayList<MenuDTO>();
		try {
			Iterator<MenuDTO> it = menuConverter.fromEntity(iMenuDao.listarPorRestaurate(Id))
					.iterator();
			while (it.hasNext()) {
				MenuDTO men = it.next();
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
