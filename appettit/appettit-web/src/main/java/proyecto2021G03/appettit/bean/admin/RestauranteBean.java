package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAdminRestaurante")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestauranteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RestauranteBean.class);

	private List<RestauranteDTO> restaurantes;
	
	private Long id;
	
	@EJB
	IUsuarioService usrSrv;

	@PostConstruct
	public void init() {
		try {
			restaurantes = usrSrv.listarRestaurantes();
			
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}


}
