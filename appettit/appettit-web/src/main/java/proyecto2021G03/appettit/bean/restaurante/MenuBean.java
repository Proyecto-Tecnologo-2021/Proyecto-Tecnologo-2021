package proyecto2021G03.appettit.bean.restaurante;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named("beanMenu")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(MenuBean.class);

	private Long id;
	private String nombre;


}
