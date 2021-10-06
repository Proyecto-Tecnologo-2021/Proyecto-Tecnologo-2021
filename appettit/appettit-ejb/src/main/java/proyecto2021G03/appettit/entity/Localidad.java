package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.MultiPolygon;

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
@IdClass(LocalidadId.class)
@Table(name = "localidades")
public class Localidad implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Id
	@Column(name="id_ciudad")
	private Long id_ciudad;

	@Id
	@Column(name="id_departamento")
	private Long id_departamento;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumns( {
		@JoinColumn(name="id_ciudad", referencedColumnName="id", insertable=false, updatable=false),
		@JoinColumn(name="id_departamento", referencedColumnName="id_departamento", insertable=false, updatable=false)
    })
	private Ciudad ciudad;
	
	@Column(name = "geom", columnDefinition = "geometry(MultiPolygon, 32721)")
	private MultiPolygon geometry;


}
