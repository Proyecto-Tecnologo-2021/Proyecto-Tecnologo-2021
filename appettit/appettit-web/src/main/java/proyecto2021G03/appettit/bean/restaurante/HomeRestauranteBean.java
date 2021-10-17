package proyecto2021G03.appettit.bean.restaurante;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("HomeRestaurante")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeRestauranteBean implements Serializable{

	static Logger logger = Logger.getLogger(HomeRestauranteBean.class);
	
	private static final long serialVersionUID = 1L;
	
	List<DepartamentoDTO> departamentos;
	Boolean abierto;
	String fechaHora;
	

	@EJB
	IDepartamentoService departamentoService;
	
	
	@PostConstruct
	public void init() {
		logger.info("Dentro de init");
		try {
			departamentos = departamentoService.listar();
			abierto = false;
			
			Date fechaBase = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			fechaHora = dateFormat.format(fechaBase);

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}

	}

}