package proyecto2021G03.appettit.bean.restaurante;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("HomeRestaurante")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeRestauranteBean implements Serializable{

	static Logger logger = Logger.getLogger(HomeRestauranteBean.class);
	
	private static final long serialVersionUID = 1L;
	
	List<DepartamentoDTO> departamentos;
	Boolean abierto;
	String fechaHora;
	RestauranteDTO restauranteDTO;
	Long id_restaurante;
	FacesContext facesContext;
	HttpSession session;
	
	@EJB
	IDepartamentoService departamentoService;
	
	@EJB
	IUsuarioService usrService;
	
	@PostConstruct
	public void init() {
		try {
			departamentos = departamentoService.listar();
			
			facesContext = FacesContext.getCurrentInstance();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);

			UsuarioDTO usuarioDTO = getUserSession();
			
			if(usuarioDTO==null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));	
			}else {
				Date fechaBase = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				fechaHora = dateFormat.format(fechaBase);
				
				restauranteDTO = usrService.buscarRestaurantePorId(usuarioDTO.getId());
				abierto = restauranteDTO.getAbierto();
			}
			

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}

	}

	public void abrirRestaurante () throws AppettitException {
		logger.info("abrio");
		restauranteDTO = usrService.abrirRestaurante(restauranteDTO.getId());
		abierto = restauranteDTO.getAbierto();
	}
	
	public void cerrarRestaurante () throws AppettitException {
		logger.info("cerro");
		restauranteDTO = usrService.cerrarRestaurante(restauranteDTO.getId());
		abierto = restauranteDTO.getAbierto(); 
	}
	
	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO = (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}

		return usuarioDTO;

	}
}
