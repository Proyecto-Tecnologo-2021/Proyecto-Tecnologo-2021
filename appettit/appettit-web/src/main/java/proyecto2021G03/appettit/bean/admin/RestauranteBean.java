package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.event.RowEditEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAdminRestaurante")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RestauranteBean.class);

	private List<RestauranteDTO> restaurantes;
	private List<RestauranteDTO> filterRestaurantes;
	//private List<MenuDTO> menuRestaurante;
	private RestauranteDTO selRestaurante;
	private Boolean menuDeshabilitado; 
	
	
	private Long id;
	private boolean globalFilterOnly;
	
	@EJB
	IUsuarioService usrSrv;
	
	@EJB
	IMenuService menuSRV;

	@PostConstruct
	public void init() {
		try {
			restaurantes = usrSrv.listarRestaurantes();
			logger.info(restaurantes.size());
			
			//menuRestaurante = null;
			menuDeshabilitado = false;
			selRestaurante = null;
			
			
		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void toggleGlobalFilter() {
		setGlobalFilterOnly(!isGlobalFilterOnly());
	}
	
	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}

	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;
	}

	public EstadoRegistro[] getRestauranteEstado() {
        return EstadoRegistro.values();
        
    }
	
	public List<MenuDTO> getMenuRestaurante() {
		List <MenuDTO> menus = new ArrayList<MenuDTO>();
		
		try {
			menus = menuSRV.listarPorRestaurante(getSelRestaurante().getId());
			
			if (menus.size()<3)
				menuDeshabilitado = true;
			
		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
		}
		
		return menus;
	}

	public void onRowEdit(RowEditEvent<RestauranteDTO> event) {
		
		try {
			
			if(!event.getObject().getBloqueado() && getMenuRestaurante().size()<3) {
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Ël Restaurante tiene menos de 3 men\\00FAs, no es posible desbloquearlo.", null));
				
			} else {
				usrSrv.editarRestaurante(event.getObject());
				restaurantes = usrSrv.listarRestaurantes();
				
		        FacesContext.getCurrentInstance().addMessage(null, 
		        		new FacesMessage("Edición correcta", event.getObject().getNombre()));
				
			}
			
			
		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
    }
	
	

    public void onRowCancel(RowEditEvent<RestauranteDTO> event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", String.valueOf(event.getObject().getNombre()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
