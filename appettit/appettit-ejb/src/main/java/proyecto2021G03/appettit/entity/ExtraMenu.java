package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "extramenus")
public class ExtraMenu implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long id_producto;
	private Long id_restaurante;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumns( {
		@JoinColumn(name="id_producto", referencedColumnName="id", insertable=false, updatable=false),
		@JoinColumn(name="id_restaurante", referencedColumnName="id_restaurante", insertable=false, updatable=false)
    })
	private Producto producto;

	private Double precio;
}
