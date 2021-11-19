package proyecto2021G03.appettit.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.entity.Pedido;

@Stateless
public class EstadisticasDAO implements IEstadisticasDAO {
	
	static Logger logger = Logger.getLogger(EstadisticasDAO.class);
	
	@EJB
	IDepartamentoDAO deptoDAO;
	
	@EJB
	IPromocionDAO promDAO;
	
	@EJB
	IMenuRDAO menuDAO;
	
	@EJB
	IUsuarioDAO usrDAO;
	
	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	


	public EstadisticasDAO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Pedido> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		
		 pedidos =  em.createQuery("SELECT p FROM Pedido p "
		 		+ "where id_restaurante=:id "
		 		+ "and fecha>= :fechaDesde "
		 		+ "and fecha<= :fechaHasta "
		 		+ "and estado not in (:rechazado, :entregado, :cancelado) "
		 		+ "order by fecha desc", Pedido.class)
				 .setParameter("id", id)
				 .setParameter("fechaDesde", fechaDesde)
				 .setParameter("fechaHasta", fechaHasta)
				 .setParameter("rechazado", EstadoPedido.RECHAZADO)
				 .setParameter("entregado", EstadoPedido.ENTREGADO)
				 .setParameter("cancelado", EstadoPedido.CANCELADO)
				 .getResultList();
		 
		 return pedidos;
		
	}

	@Override
	public List<DashCalificacionResDTO> listarCalificacionPorRestaurante(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashTotalDTO> listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalificacionPedidoDTO> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) {
		// TODO Auto-generated method stub
		return null;
	}

}
