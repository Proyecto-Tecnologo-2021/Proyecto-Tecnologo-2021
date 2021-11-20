package proyecto2021G03.appettit.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.TipoPago;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<DashMenuDTO> listarTendenciasPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) {
		
		List<DashMenuDTO> tendencias = new ArrayList<DashMenuDTO>();
		
		Query consulta = em
					.createNativeQuery("SELECT "
							+ "	pm.menus_id AS id, "
							+ "	m.nombre AS nombre, "
							+ "	u.nombre AS r_nombre, "
							+ "	m.preciototal AS precio, "
							+ "	m.id_imagen AS imagen, "
							+ "	COUNT(pm.menus_id) cantidad "
							+ "FROM pedidos_menus pm "
							+ "JOIN pedidos p ON p.id = pm.pedido_id "
							+ "JOIN menus m ON m.id = pm.menus_id "
							+ "JOIN usuario u ON u.id = m.id_restaurante "
							+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
							+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
							+ "' and p.id_restaurante = " + id.toString()
							+ " and p.pago = true "
							+ "and p.estado = 4 "
							+ "GROUP BY "
							+ "	pm.menus_id, "
							+ "	m.nombre, "
							+ "	u.nombre, "
							+ "	m.preciototal,  "
							+ "	m.id_imagen "
							+ "UNION 	 "
							+ "SELECT  "
							+ "	pp.promociones_id AS id, "
							+ "	pr.nombre AS nombre, "
							+ "	u.nombre AS r_nombre, "
							+ "	pr.precio AS precio, "
							+ "	pr.id_imagen AS imagen, "
							+ "	COUNT(pp.promociones_id) cantidad "
							+ "FROM pedidos_promociones pp "
							+ "JOIN pedidos p ON p.id = pp.pedido_id "
							+ "JOIN promociones pr ON pr.id = pp.promociones_id "
							+ "JOIN usuario u ON u.id = pr.id_restaurante "
							+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
							+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
							+ "' and p.id_restaurante = " + id.toString()
							+ " and p.pago= true "
							+ "and p.estado = 4 "
							+ "GROUP BY "
							+ "	pp.promociones_id, "
							+ "	pr.nombre, "
							+ "	u.nombre, "
							+ "	pr.precio,  "
							+ "	pr.id_imagen "
							+ "ORDER BY cantidad DESC"
							+ "");
		
			consulta.setMaxResults(top);
			
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				
				Double total = Double.valueOf(line[3].toString())*
						(line[5] instanceof BigInteger ? ((BigInteger) line[5]).intValue(): 1);
				
				Integer cantidad =  line[5] instanceof BigInteger ? ((BigInteger) line[5]).intValue(): 0;
				
				tendencias.add(new DashMenuDTO(Long.valueOf(line[0].toString()), line[2].toString(), line[1].toString(), Double.valueOf(line[3].toString()), total, cantidad, line[4].toString(), null));
			}
	
		
		return tendencias;
	}

	@Override
	public List<ClasificacionPedido> listarCalificacionesPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) {
		List<ClasificacionPedido> clasificacion = new ArrayList<ClasificacionPedido>();
		
		clasificacion = em.createQuery("select _c "
				+ "from ClasificacionPedido _c "
				+ "inner join _c.pedido _p "
				+ "where _p.id_restaurante =:id "
				+ "and fecha>= :fechaDesde "
				+ "and fecha<= :fechaHasta "
				+ "order by fecha desc", ClasificacionPedido.class)
				.setParameter("id", id)
				 .setParameter("fechaDesde", fechaDesde)
				 .setParameter("fechaHasta", fechaHasta)
				 .setMaxResults(top)
				 .getResultList();
		
		 return clasificacion;
	}

	public String getFechaHora(LocalDateTime fecha, String formato) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
		 
		return fecha.format(formatter);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashMenuDTO> listarPediosRecientesPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) {
		List<DashMenuDTO> tendencias = new ArrayList<DashMenuDTO>();
		
		Query consulta = em
					.createNativeQuery("SELECT "
							+ "	pm.menus_id AS id, "
							+ "	m.nombre AS nombre, "
							+ "	u.nombre AS r_nombre, "
							+ "	m.preciototal AS precio, "
							+ "	m.id_imagen AS imagen,"
							+ " p.fecha AS fecha "
							+ "FROM pedidos_menus pm "
							+ "JOIN pedidos p ON p.id = pm.pedido_id "
							+ "JOIN menus m ON m.id = pm.menus_id "
							+ "JOIN usuario u ON u.id = m.id_restaurante "
							+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
							+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
							+ "' and p.id_restaurante = " + id.toString()
							+ " and p.pago = true "
							+ "and p.estado not in (2, 5)  "
							+ "UNION 	 "
							+ "SELECT  "
							+ "	pp.promociones_id AS id, "
							+ "	pr.nombre AS nombre, "
							+ "	u.nombre AS r_nombre, "
							+ "	pr.precio AS precio, "
							+ "	pr.id_imagen AS imagen,"
							+ " p.fecha AS fecha "
							+ "FROM pedidos_promociones pp "
							+ "JOIN pedidos p ON p.id = pp.pedido_id "
							+ "JOIN promociones pr ON pr.id = pp.promociones_id "
							+ "JOIN usuario u ON u.id = pr.id_restaurante "
							+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
							+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
							+ "' and p.id_restaurante = " + id.toString()
							+ " and p.pago= true "
							+ "and p.estado not in (2, 5) "
							+ "ORDER BY fecha DESC"
							+ "");
		
			consulta.setMaxResults(top);
			
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				
				Double total = Double.valueOf(line[3].toString())*
						(line[5] instanceof BigInteger ? ((BigInteger) line[5]).intValue(): 1);
				
				Integer cantidad =  line[5] instanceof BigInteger ? ((BigInteger) line[5]).intValue(): 0;
				
				tendencias.add(new DashMenuDTO(Long.valueOf(line[0].toString()), line[2].toString(), line[1].toString(), Double.valueOf(line[3].toString()), total, cantidad, line[4].toString(), null));
			}
	
		
		return tendencias;

	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarFormaPagoPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double total = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	p.tipo, "
						+ "	Count(p.tipo) cantidad "
						+ "from pedidos p "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.id_restaurante = " + id.toString()
						+ " and p.estado = 4 "
						+ "GROUP BY p.tipo "
						+ "ORDER BY cantidad "
						+ "");
		
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			TipoPago fp = TipoPago.values()[Integer.valueOf(line[0].toString())];
			Double cantidad =  line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
			valores.put(fp.toString(), cantidad);
			total = total + cantidad;
		}
		
		
		return new DashTotalDTO(valores, total);

	}
}
