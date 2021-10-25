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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@IdClass(MenuId.class)
@Table(name = "menus")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="menu_seq")
	@SequenceGenerator(
		name="menu_seq",
		sequenceName="menu_sequence",
		allocationSize=1
	)
	private Long id;
	
	@Id
	@Column(name="id_restaurante")
	private Long id_restaurante;
	
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_restaurante", referencedColumnName="id", insertable=false, updatable=false)
	private Restaurante restaurante;


	private String descripcion;
	private Double precioSimple;
	private Double precioTotal;
	
	@OneToMany
	private List<Producto> productos;
	
	@OneToMany
	private List<ExtraMenu> extras;
	
	private String id_imagen;
	
}
