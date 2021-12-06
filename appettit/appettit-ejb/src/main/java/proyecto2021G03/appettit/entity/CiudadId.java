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
public class CiudadId implements Serializable {

	private static final long serialVersionUID = 1L;


	private Long id;
	private Long id_departamento;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_departamento == null) ? 0 : id_departamento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null) return false;
		if(!(arg0 instanceof CiudadId)) return false;
		CiudadId arg1 = (CiudadId) arg0;
		return (this.id.equals(arg1.id)) && (this.id_departamento.equals(arg1.id_departamento));
    }

	

}
