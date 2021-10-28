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
@IdClass(ProductoId.class)
@Table(name = "productos")
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="producto_seq")
	@SequenceGenerator(
		name="producto_seq",
		sequenceName="producto_sequence",
		allocationSize=1
	)
	private Long id;

	@Id
	@Column(name="id_restaurante")
	private Long id_restaurante;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	private Long id_categoria;
	
	@ManyToOne(optional = false)
	//@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_restaurante", referencedColumnName="id", insertable=false, updatable=false)
	private Restaurante restaurante;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="id_categoria", referencedColumnName="id", insertable=false, updatable=false)
	private Categoria categoria;
	
	
	
}
