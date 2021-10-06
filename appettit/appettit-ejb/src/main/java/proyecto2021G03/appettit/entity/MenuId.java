package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuId implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long id;
	private Long id_restaurante;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_restaurante == null) ? 0 : id_restaurante.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null) return false;
		if(!(arg0 instanceof MenuId)) return false;
		MenuId arg1 = (MenuId) arg0;
		return (this.id.equals(arg1.id)) && (this.id_restaurante.equals(arg1.id_restaurante));
    }


}
