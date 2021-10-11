package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAdmin")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdministradorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AdministradorBean.class);

	private String columnTemplate;
	private List<AdministradorDTO> administradores;
	
	@SuppressWarnings("rawtypes")
	private Map<String, Class> validColumns;
	private List<ColumnModel> columns;
	private List<AdministradorDTO> filteredAdm;

	private Long id;
	private String nombre;
	private String username;
	private String password;
	private String telefono;
	private String correo;
	private String token;
	private String tokenFireBase;
	
	

	@EJB
	IUsuarioService usrSrv;

	@PostConstruct
	public void init() {
		try {
			administradores = usrSrv.listarAdminsitradores();
			columnTemplate = "nombre username correo telefono";
			
			validColumns = Stream.of(AdministradorDTO.class.getSuperclass().getDeclaredFields())
					.collect(Collectors.toMap(Field::getName, Field::getType));
			createDynamicColumns();

		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void addAdministrador() {
		
		logger.info("addAdministrador 'nombre': " + nombre);
		
		AdministradorDTO adminDTO = new AdministradorDTO(null, nombre, username, password, telefono, correo, null, null);
	
		try {
			adminDTO = usrSrv.crear(adminDTO);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Administrador " + adminDTO.getCorreo() + " creado con éxito.", null));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				administradores = usrSrv.listarAdminsitradores();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}
		
	}

	private void createDynamicColumns() {
		String[] columnKeys = columnTemplate.split(" ");
		columns = new ArrayList<>();

		for (String columnKey : columnKeys) {
			String key = columnKey.trim();

			if (validColumns.containsKey(key)) {
				columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey, validColumns.get(key)));
			}
		}
		
		logger.info(Arrays.deepToString(columns.toArray()));
	}

	public void updateColumns() {
		// reset table state
		UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":formAdmin:administradores");
		table.setValueExpression("sortBy", null);

		// update columns
		createDynamicColumns();
	}

	public static class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;

		private String header;
		private String property;
		private String type;
		private Class<?> klazz;

		@SuppressWarnings("rawtypes")
		public ColumnModel(String header, String property, Class klazz) {
			this.header = header;
			this.property = property;
			this.klazz = klazz;
			initType();
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}

		public String getType() {
			return type;
		}

		public Class<?> getKlazz() {
			return klazz;
		}

		private void initType() {
			if (Temporal.class.isAssignableFrom(klazz)) {
				type = "date";
			} else if (klazz.isEnum()) {
				type = "enum";
			}
		}
	}
	
	private void clearParam() {
		this.id = null;
		this.nombre = null;
		this.password = null;
		this.correo = null;
		this.telefono = null;
		this.username = null;
		this.token = null;
		this.tokenFireBase = null;
		
	}

}
