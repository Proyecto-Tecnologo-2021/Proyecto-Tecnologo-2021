package proyecto2021G03.appettit.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@IdClass(PromocionId.class)
@Table(name = "promociones")
public class Promocion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="promocion_seq")
	@SequenceGenerator(
		name="promocion_seq",
		sequenceName="promocion_sequence",
		allocationSize=1
	)
	private Long id;
	
	@Id
	@Column(name="id_restaurante")
	private Long id_restaurante;
	
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@ManyToOne
	@JoinColumn(name="id_restaurante", referencedColumnName="id", insertable=false, updatable=false)
	private Restaurante restaurante;

	private String descripcion;
	private Double descuento;
	private Double precio;
	
	/*
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumns( {
		@JoinColumn(name="id_menu", referencedColumnName="id"),
		@JoinColumn(name="id_restaurante", referencedColumnName="id_restaurante"),
    })
    */
	@ManyToMany
	private List<Menu> menus;
	
	private String id_imagen;

}
