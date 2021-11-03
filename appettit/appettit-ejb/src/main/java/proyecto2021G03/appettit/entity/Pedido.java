package proyecto2021G03.appettit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

	/*
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumns( {
		@JoinColumn(name="id_menu", referencedColumnName="id"),
		@JoinColumn(name="id_restaurante", referencedColumnName="id_restaurante"),
    })
    */
	@ManyToMany
	@Builder.Default
	private List<Menu> menus = new ArrayList<Menu>();
	
	/*
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumns( {
		@JoinColumn(name="id_promocion", referencedColumnName="id"),
		@JoinColumn(name="id_restaurante", referencedColumnName="id_restaurante"),
    })
    */
	@ManyToMany
	@Builder.Default
	private List<Promocion> promociones = new ArrayList<Promocion>();;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_entrega", referencedColumnName="id", insertable=false, updatable=false)
	private Direccion entrega;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_reclamo", referencedColumnName="id")
	private Reclamo reclamo;

	@ManyToMany
	@Builder.Default
	private List<ExtraMenu> extraMenus = new ArrayList<ExtraMenu>();


	public Pedido( TipoPago tipo, Boolean pago, LocalDateTime fecha, Double total, Restaurante restaurante, Cliente cliente, List<Menu> menus, List<Promocion> promociones, Direccion entrega, EstadoPedido estado) {
		this.id = 0L;
		this.tipo = tipo;
		this.pago = pago;
		this.fecha = fecha;
		this.total = total;
		this.restaurante = restaurante;
		this.cliente = cliente;
		this.menus = menus;
		this.promociones = promociones;
		this.entrega = entrega;
		this.estado = estado;


	}
}


