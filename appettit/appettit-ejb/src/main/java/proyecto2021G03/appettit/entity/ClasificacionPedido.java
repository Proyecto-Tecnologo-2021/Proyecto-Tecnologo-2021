package proyecto2021G03.appettit.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@IdClass(ClasificacionPedidoId.class)
@Table(name = "clasificacionespedidos")
public class ClasificacionPedido implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id_pedido;
	
	@Id
	private Long id_cliente;

	
	private Integer rapidez; 
	private Integer comida;
	private Integer servicio;
	private String comentario;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_pedido", referencedColumnName="id", insertable=false, updatable=false)
	private Pedido pedido;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_cliente", referencedColumnName="id", insertable=false, updatable=false)
	private Cliente cliente;

	

}
