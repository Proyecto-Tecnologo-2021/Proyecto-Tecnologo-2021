package proyecto2021G03.appettit.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//import com.vividsolutions.jts.geom.MultiPolygon;

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
@Entity
@IdClass(CiudadId.class)
@Table(name = "ciudades")
public class Ciudad implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@Id
	@Column(name="id_departamento")
	private Long id_departamento;
	
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_departamento", referencedColumnName="id", insertable=false, updatable=false)
	private Departamento departamento;
	
	@OneToMany
	private List<Localidad> localidades;


	//@Column(name = "geom", columnDefinition = "geometry(MultiPolygon, 32721)")
	//private MultiPolygon geometry;
	
	@Column(name="geom", columnDefinition="TEXT")
	private String geometry;

}
