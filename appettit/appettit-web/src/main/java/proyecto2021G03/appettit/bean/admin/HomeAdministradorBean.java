package proyecto2021G03.appettit.bean.admin;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Named("beanHomeAdministrador")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeAdministradorBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(HomeAdministradorBean.class);

	String fechaHora;
	
	@PostConstruct
	public void init() {
		Date fechaBase = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		fechaHora = dateFormat.format(fechaBase);

	}


}
