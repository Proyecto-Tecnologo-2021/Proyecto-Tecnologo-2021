package proyecto2021G03.appettit.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.converter.PromocionConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dao.IPromocionDAO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.FileManagement;

@Stateless
public class PromocionService implements IPromocionService {
	
	static Logger logger = Logger.getLogger(PromocionService.class);

    @EJB
    public IPromocionDAO pDAO;

    @EJB
    public ICategoriaDAO cDAO;
    
    @EJB
    public IGeoDAO geoDAO;

    @EJB
    public PromocionConverter pConverter;
    
    @EJB
    IImagenService imgSrv;

    @EJB
    IUsuarioService usrSrv;


    @Override
    public List<PromocionDTO> listar() throws AppettitException {
        /*
    	try {
            return pConverter.fromEntity(pDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
    	
    	List<PromocionDTO> promociones = new ArrayList<PromocionDTO>();
		try {
			Iterator<PromocionDTO> it = pConverter.fromEntity(pDAO.listar())
					.iterator();
			while (it.hasNext()) {
				PromocionDTO men = it.next();
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
				promociones.add(men);

			}

			return promociones;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
    }

    @Override
    public PromocionDTO listarPorId(Long id, Long id_restaurante) throws AppettitException {
    	/*
        try {
            return pConverter.fromEntity(pDAO.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
    	
    	PromocionDTO men = pConverter.fromEntity(pDAO.listarPorId(id, id_restaurante));
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
    public PromocionDTO editar(Long id, Long id_restaurante, ProductoCrearDTO pcDTO) throws AppettitException {

        // HACER EL CONTROL POR RESTAURANTE Y NO GENERAL
        if (existeNombreProductoExcluirId(id, pcDTO.getNombre())) {
            throw new AppettitException("Ya existe un producto con ese nombre.", AppettitException.EXISTE_REGISTRO);
        } else {
            try {
                Promocion promo = pDAO.listarPorId(id, id_restaurante);
                promo.setNombre(pcDTO.getNombre());
                
                PromocionDTO men = pConverter.fromEntity(pDAO.editar(promo));
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
    }

    @Override
    public void eliminar(Long id, Long id_restaurante) throws AppettitException {
        /* Se valida que exista la promocion */
        Promocion promo = pDAO.listarPorId(id, id_restaurante);
        if (promo == null) {
            throw new AppettitException("La promocion indicada no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } 
        else {
            try {
                pDAO.eliminar(promo);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }

    @Override
    public List<PromocionDTO> listarPorRestaurante(Long id) throws AppettitException {
       /*
    	try {
            return pConverter.fromEntity(pDAO.listarPorRestaurante(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
        */
    	
    	List<PromocionDTO> promociones = new ArrayList<PromocionDTO>();
		try {
			Iterator<PromocionDTO> it = pConverter.fromEntity(pDAO.listarPorRestaurante(id))
					.iterator();
			while (it.hasNext()) {
				PromocionDTO men = it.next();
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
				promociones.add(men);

			}

			return promociones;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
    	
    }

    @Override
    public PromocionDTO crear(PromocionDTO pcDTO) throws AppettitException {

        try {
            Promocion promo = pConverter.fromDTO(pcDTO);
            return pConverter.fromEntity(pDAO.crear(promo));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public PromocionDTO editar(PromocionDTO ccDTO) throws AppettitException {
        try {
            Promocion promo = pConverter.fromDTO(ccDTO);
            PromocionDTO men = pConverter.fromEntity(pDAO.editar(promo));
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


    public boolean existeNombreProductoExcluirId(Long id, String nombre) {

        List<PromocionDTO> promoData = pConverter.fromEntity(pDAO.listar());
        for (PromocionDTO p : promoData) {
            if (!p.getId().equals(id)) {
                if (p.getNombre().equals(nombre)) {
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public List<PromocionRDTO> listarRPromocion() throws AppettitException {
		List<PromocionRDTO> promociones = new ArrayList<PromocionRDTO>();
		try {
			Iterator<PromocionRDTO> it = pConverter.fromEntityToRDTO(pDAO.listar())
					.iterator();
			while (it.hasNext()) {
				PromocionRDTO men = it.next();
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
				promociones.add(men);

			}

			return promociones;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public List<PromocionRDTO> listarPorPunto(String punto) throws AppettitException {
		List<PromocionRDTO> promociones = new ArrayList<PromocionRDTO>();
		try {
			Iterator<PromocionRDTO> it = pConverter.fromEntityToRDTO(geoDAO.promocionRestaurantesPorPunto(punto))
					.iterator();
			while (it.hasNext()) {
				PromocionRDTO men = it.next();
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
				promociones.add(men);

			}

			return promociones;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public PromocionRDTO buscarPorId(Long id_restaurante, Long id) throws AppettitException {
		PromocionRDTO men = pConverter.fromEntityToRDTO(pDAO.listarPorId(id, id_restaurante));
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
		
		return men;
	}

	@Override
	public List<PromocionRDTO> listarPorRestaurnateRest(Long id_restaurante) throws AppettitException {
		List<PromocionRDTO> promociones = new ArrayList<PromocionRDTO>();
		try {
			Iterator<PromocionRDTO> it = pConverter.fromEntityToRDTO(pDAO.listarPorRestaurante(id_restaurante))
					.iterator();
			while (it.hasNext()) {
				PromocionRDTO men = it.next();
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
				promociones.add(men);

			}

			return promociones;
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}
}
