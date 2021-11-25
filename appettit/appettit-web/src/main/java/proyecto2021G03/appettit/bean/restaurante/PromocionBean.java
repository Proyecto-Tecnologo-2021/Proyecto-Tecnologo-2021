package proyecto2021G03.appettit.bean.restaurante;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IPromocionService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.*;
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
    private Boolean disabledBloquedado = true;


    @EJB
    IUsuarioService usrSrv;

    @EJB
    IPromocionService promoSrv;

    @PostConstruct
    public void init() {

        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(true);

        UsuarioDTO usuarioDTO = getUserSession();

        if (usuarioDTO == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
        } else {
            try {

                promos = promoSrv.listarPorRestaurante(usuarioDTO.getId());
                restaurante = (RestauranteDTO) usuarioDTO;

            } catch (AppettitException e) {
                logger.info(e.getMessage().trim());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
            }

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
