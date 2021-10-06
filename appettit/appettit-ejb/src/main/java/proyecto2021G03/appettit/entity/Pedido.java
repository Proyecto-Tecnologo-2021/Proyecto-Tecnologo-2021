package proyecto2021G03.appettit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.TipoPago;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	private EstadoPedido estado;
	private TipoPago tipo;
	private Boolean pago;
	private Integer tiempoEstimado;
	private String motivo;
	private LocalDateTime fecha;
	private Double total;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_restaurante", referencedColumnName="id", insertable=false, updatable=false)
	private Restaurante restaurante;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_cliente", referencedColumnName="id", insertable=false, updatable=false)
	private Cliente cliente;

	@OneToMany
	private List<Menu> menus;
	
	@OneToMany
	private List<Promocion> promociones;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_entrega", referencedColumnName="id", insertable=false, updatable=false)
	private Direccion entrega;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_reclamo", referencedColumnName="id")
	private Reclamo reclamo;
	
}
