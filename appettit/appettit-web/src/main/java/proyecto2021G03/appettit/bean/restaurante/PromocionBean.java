package proyecto2021G03.appettit.bean.restaurante;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IPromocionService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanPromocion")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(PromocionBean.class);

	private Long id;
	private Long id_restaurante;
	private String nombre;
	private RestauranteDTO restaurante;
	private String descripcion;
	private Double descuento;
	private Double precio;
	private String id_imagen;
	private ImagenDTO imagen;
	private List<MenuDTO> menus;

	private List<PromocionDTO> promos;
	private List<PromocionDTO> filterPromos;

	private boolean globalFilterOnly;
	FacesContext facesContext;
	HttpSession session;
	UsuarioDTO usuarioDTO;
	private Boolean disabledBloquedado = true;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IPromocionService promoSrv;

	@PostConstruct
	public void init() {

		try {
			facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);

			usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				externalContext.invalidateSession();
				externalContext.redirect(Constantes.REDIRECT_URI);

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
			} else {

				if (!(usuarioDTO instanceof RestauranteDTO)) {
					externalContext.invalidateSession();
					externalContext.redirect(Constantes.REDIRECT_URI);

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
				} else {

					promos = promoSrv.listarPorRestaurante(usuarioDTO.getId());
					restaurante = (RestauranteDTO) usuarioDTO;
				}
			}
		} catch (AppettitException | IOException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
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
