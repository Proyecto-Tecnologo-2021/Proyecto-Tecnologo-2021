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
import proyecto2021G03.appettit.dto.DashReclamoDTO;
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

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarVentasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	'ANTERIOR' as tipo, "
						+ "	case SUM(p.total) is null "
						+ "	when true then 0 "
						+ "	else SUM(p.total) " 
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)>= to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case SUM(p.total) is null "
						+ "	when true then 0 "
						+ "	else SUM(p.total) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)>= to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "	'd'||date_part('day',aux.d), "
						+ "	SUM(CASE p.total IS NULL "
						   + "	when true THEN 0 "
						   + "	else p.total "
						   + "	end) "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
							+ "									and p.estado = 4 "
							+ "									and p.id_restaurante=" + id.toString()
					+ "	GROUP BY aux.d "
					+ "	ORDER BY 1 "
						+ "");
				
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			Double cantidad = Double.valueOf(line[1].toString());     //line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
			if(!(line[0].toString().equalsIgnoreCase("ACTUAL") || line[0].toString().equalsIgnoreCase("ANTERIOR")))
				valores.put(line[0].toString(), cantidad);
			
			if(line[0].toString().equalsIgnoreCase("ACTUAL"))
				actual =  Double.valueOf(line[1].toString()); //line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
			if(line[0].toString().equalsIgnoreCase("ANTERIOR"))
				anterior =  Double.valueOf(line[1].toString()); //line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
		}
		
		return new DashTotalDTO(valores, actual, anterior);
		
		
		
		
		
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
		
		
		return new DashTotalDTO(valores, total, 0D);

	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarReclamosTPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double total = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "'R' AS tipo, "
						+ "Count(r.id) cantidad "
						+ "from pedidos p "
						+ "join reclamos r on r.id = p.id_reclamo "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.id_restaurante = " + id.toString()
						+ " UNION "
						+ "select "
						+ "'T' AS tipo, "
						+ "Count(p.id) cantidad "
						+ "from pedidos p "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.id_restaurante = " + id.toString()
						+ " ORDER BY tipo");
		
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			Double cantidad =  line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
			valores.put(line[0].toString(), cantidad);
			if(line[0].toString().equalsIgnoreCase("T"))	
			total = line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
		}
		
		
		return new DashTotalDTO(valores, total, 0D);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarEstadoPedidosPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double total = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	p.estado, "
						+ "	Count(p.estado) cantidad "
						+ "from pedidos p "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.id_restaurante = " + id.toString()
						+ "GROUP BY p.estado "
						+ "ORDER BY cantidad "
						+ "");
		
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			EstadoPedido ep = EstadoPedido.values()[Integer.valueOf(line[0].toString())]; 
			Double cantidad =  line[1] instanceof BigInteger ? ((BigInteger) line[1]).doubleValue(): 0;
			
			valores.put(ep.toString(), cantidad);
			total = total + cantidad;
		}
		
		
		return new DashTotalDTO(valores, total, 0D);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashReclamoDTO> listarReclamosPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta) {
		List<DashReclamoDTO> reclamos = new ArrayList<DashReclamoDTO>();
		
		Query consulta = em
				.createNativeQuery("select "
						+ "p.id, "
						+ "r.motivo, "
						+ "r.detalles, "
						+ "to_char(r.fecha, 'yyyy-MM-dd HH:mm') AS fecha, "
						+ "u.nombre "
						+ "from reclamos r "
						+ "join pedidos p on p.id_reclamo=r.id "
						+ "join usuario u on u.id=p.id_cliente "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.id_restaurante = " + id.toString()
						+ "ORDER BY fecha desc "
						+ "");
		
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			Long idq =  line[0] instanceof BigInteger ? ((BigInteger) line[0]).longValue(): 0L;
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime fecha = LocalDateTime.parse(line[3].toString(), formatter);


			reclamos.add(new DashReclamoDTO(idq, line[1].toString(), line[2].toString(), fecha, line[4].toString()));
		}
		
		return reclamos;
	}
}
