package proyecto2021G03.appettit.dto;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import proyecto2021G03.appettit.entity.Usuario;

@Getter
@Setter
@AllArgsConstructor
public class AdministradorDTO extends Usuario {

	private static final long serialVersionUID = 1L;

	@Builder
	public AdministradorDTO(Long id, String nombre, String username, String password, String telefono, String correo,
			String token, String tokenFireBase) {
		super(id, nombre, username, password, telefono, correo, token, tokenFireBase);
		// TODO Auto-generated constructor stub
	}
	
	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}

}
