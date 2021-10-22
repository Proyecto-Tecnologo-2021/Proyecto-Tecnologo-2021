package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Table;

//import com.vividsolutions.jts.geom.Point;

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
@Table(name = "direcciones")
public class Direccion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String alias;
	private String calle;
	private String numero;
	private String apartamento;
	private String referencias;
	
	@JoinColumns( {
		@JoinColumn(name="id_localidad", referencedColumnName="id"),
		@JoinColumn(name="id_ciudad", referencedColumnName="id_ciudad"),
		@JoinColumn(name="id_departamento", referencedColumnName="id_departamento")
    })
	private Localidad barrio;
	
	//@Column(name = "geom", columnDefinition = "geometry(Point, 32721)")
	//private Point geometry;
	
	@Column(name="geom", columnDefinition="TEXT")
	private String geometry;


	 

}
