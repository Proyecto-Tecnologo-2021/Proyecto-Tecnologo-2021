package proyecto2021G03.appettit.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.dto.DashCalificacionResDTO;
import proyecto2021G03.appettit.dto.DashGeoDTO;
import proyecto2021G03.appettit.dto.DashInformeDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashReclamoDTO;
import proyecto2021G03.appettit.dto.DashRestauranteDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.RestauranteRDTO;
import proyecto2021G03.appettit.dto.TipoPago;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.entity.Pedido;

@Singleton
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


	@Override
	public List<Pedido> listarPedidosPendientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		
		 pedidos =  em.createQuery("SELECT p FROM Pedido p "
		 		+ "where id_restaurante=:id "
		 		+ "and fecha> :fechaDesde "
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
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
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
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	SUM(CASE p.total IS NULL "
						   + "	when true THEN 0 "
						   + "	else p.total "
						   + "	end) "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.id_restaurante = " + id.toString()
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.id_restaurante = " + id.toString()
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.id_restaurante = " + id.toString()
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.id_restaurante = " + id.toString()
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

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarClientesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	'ANTERIOR' as tipo, "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) " 
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
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
	public DashTotalDTO listarOrdenesPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	'ANTERIOR' as tipo, "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) " 
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ " and p.id_restaurante=" + id.toString()
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
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
	public DashTotalDTO listarOrdenesPromedioPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("Select " 
						+ "	case date_part('day',aux.d) < 10 "
						+ "	when true then '0' "
				+ "	else '' "
				+ "	end||date_part('day',aux.d), "
				+ "	case COUNT(p.id) is null "
				+ "	when true then 0 "
				+ "	else COUNT(p.id) "
				+ "	end total, "
				+ "	ac.total as actual, "
				+ "	an.total as anterior "
			+ "	from pedidos p "
			+ "	right join "
			+ "	generate_series "
			 + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
		        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
		        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
				+ "									and p.estado = 4 "
				+ "									and p.id_restaurante=" + id.toString()
			+ "	join ( "
			+ "	Select "
									+ "	'ACTUAL' as tipo, "
									+ "	case COUNT(p.id) is null "
									+ "	when true then 0 "
									+ "	else COUNT(p.id) "
									+ "	end total "
								+ "	from pedidos p "
								+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
								+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ "	and p.estado = 4 "
								+ "	and p.id_restaurante=" + id.toString()
								+ "	) as ac ON 1=1 "										
			+ "	join ( "
			+ "	Select "
									+ "	'ANTERIOR' as tipo, "
									+ "	case COUNT(p.id) is null "
									+ "	when true then 0 "
									+ "	else COUNT(p.id) "
									+ "	end total "
								+ "	from pedidos p "
								+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
								+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ "	and p.estado = 4 "
								+ "	and p.id_restaurante=" + id.toString()
								+ "	) as an ON 1=1 "										
			+ "	GROUP BY aux.d, ac.total, an.total "
			+ "	ORDER BY 1 "
						+ "");
				
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			Double cantidad = 0D;
			if(Double.valueOf(line[2].toString())>0)
				cantidad = Double.valueOf(line[1].toString())/Double.valueOf(line[2].toString());
			
			valores.put(line[0].toString(), cantidad);
			actual =  Double.valueOf(line[2].toString());
			anterior =  Double.valueOf(line[3].toString());
			
		}
		
		return new DashTotalDTO(valores, actual, anterior);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarCalificacionesDetPorRestaurante(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, String calificacion) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "c.cla, "
						+ "COUNT(cp." + calificacion + ") "
					+ "from clasificacionespedidos cp "
					+ "JOIN pedidos p ON p.id = cp.id_pedido "
					+ "	            and p.id_restaurante=" + id.toString()
					+ "	            and date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	            and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "RIGHT JOIN generate_series(1, 5, 1) AS c(cla) ON c.cla = cp." + calificacion
					+ " group by c.cla "
					+ "order by c.cla "
						+ "");
				
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			Double cantidad = Double.valueOf(line[1].toString());
			
			valores.put(line[0].toString(), cantidad);
			actual =  actual + Double.valueOf(line[1].toString());
			
		}
		
		return new DashTotalDTO(valores, actual, anterior);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashGeoDTO listarGeoEntregasPorRestaurante(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		List<String> valores = new ArrayList<String>();
		String centro = null;
		String reparto = null;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "'[' || ST_Y(ST_Centroid(ST_Transform(ST_GeometryFromText(dr.geom, 32721), 4326))) || ', ' || "
						+ "ST_X(ST_Centroid(ST_Transform(ST_GeometryFromText(dr.geom, 32721), 4326))) || ']' AS restaurante, "
						+ "'[' || ST_Y(ST_Centroid(ST_Transform(ST_GeometryFromText(d.geom, 32721), 4326))) || ', ' || "
						+ "ST_X(ST_Centroid(ST_Transform(ST_GeometryFromText(d.geom, 32721), 4326))) || ']' AS entregar, "
						+ "array_to_string( "
						+ "		array_agg( "
						+ "		'[' || ST_Y(ST_Centroid(ST_Transform(aux.geom, 4326))) || ', ' || "
						+ "		ST_X(ST_Centroid(ST_Transform(aux.geom, 4326))) || ']'), ',') AS reparto "
						+ "FROM pedidos p "
					+ "join direcciones d on d.id = p.id_entrega "
					+ "join usuario u on u.id = p.id_restaurante "
					+ "join direcciones dr on dr.id = u.id_direccion "
					+ "join (SELECT (ST_dumppoints(ST_GeometryFromText(u.geom, 32721))).geom FROM usuario u where u.id =" + id.toString() + ") as aux ON 1=1"
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ "	and p.id_restaurante=" + id.toString()
					+ " group by dr.geom, d.geom");
				
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			centro = line[0].toString();
			reparto = line[2].toString();
			
			valores.add(line[1].toString());
		}
		
		return new DashGeoDTO(valores,centro, reparto);
	
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarVentasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
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
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case SUM(p.total) is null "
						+ "	when true then 0 "
						+ "	else SUM(p.total) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	SUM(CASE p.total IS NULL "
						   + "	when true THEN 0 "
						   + "	else p.total "
						   + "	end) "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
							+ "									and p.estado = 4 "
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
	public DashTotalDTO listarClientesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	'ANTERIOR' as tipo, "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) " 
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	case COUNT(DISTINCT p.id_cliente) is null "
						+ "	when true then 0 "
						+ "	else COUNT(DISTINCT p.id_cliente) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
							+ "									and p.estado = 4 "
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
	public DashTotalDTO listarOrdenesPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	'ANTERIOR' as tipo, "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) " 
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') " 
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "	'ACTUAL' as tipo, "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					+ "	and p.estado = 4 "
					+ "	UNION "
					+ "	Select " 
						+ "case date_part('day',aux.d) < 10 "
						+ "when true then '0' "
						+ "else '' "
						+ "end||date_part('day',aux.d), "
						+ "	case COUNT(p.id) is null "
						+ "	when true then 0 "
						+ "	else COUNT(p.id) "
						+ "	end total "
					+ "	from pedidos p "
					+ "	right join "
					+ "	generate_series "
					        + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
					        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
							+ "									and p.estado = 4 "
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
	public DashTotalDTO listarOrdenesPromedioPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer periodo) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double actual = 0D;
		Double anterior = 0D;
		
		Query consulta = em
				.createNativeQuery("Select " 
						+ "	case date_part('day',aux.d) < 10 "
						+ "	when true then '0' "
				+ "	else '' "
				+ "	end||date_part('day',aux.d), "
				+ "	case COUNT(p.id) is null "
				+ "	when true then 0 "
				+ "	else COUNT(p.id) "
				+ "	end total, "
				+ "	ac.total as actual, "
				+ "	an.total as anterior "
			+ "	from pedidos p "
			+ "	right join "
			+ "	generate_series "
			 + "	( to_date('" + getFechaHora(fechaDesde.plusDays(1), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
		        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
		        + "	, interval '1 day') as aux(d) ON date(aux.d)= date(p.fecha) "
				+ "									and p.estado = 4 "
			+ "	join ( "
			+ "	Select "
									+ "	'ACTUAL' as tipo, "
									+ "	case COUNT(p.id) is null "
									+ "	when true then 0 "
									+ "	else COUNT(p.id) "
									+ "	end total "
								+ "	from pedidos p "
								+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
								+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ "	and p.estado = 4 "
								+ "	) as ac ON 1=1 "										
			+ "	join ( "
			+ "	Select "
									+ "	'ANTERIOR' as tipo, "
									+ "	case COUNT(p.id) is null "
									+ "	when true then 0 "
									+ "	else COUNT(p.id) "
									+ "	end total "
								+ "	from pedidos p "
								+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde.minusDays(periodo), "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
								+ "	and date(p.fecha)<=to_date('"+ getFechaHora(fechaHasta.minusDays(periodo), "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ "	and p.estado = 4 "
								+ "	) as an ON 1=1 "										
			+ "	GROUP BY aux.d, ac.total, an.total "
			+ "	ORDER BY 1 "
						+ "");
				
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			Double cantidad = 0D;
			if(Double.valueOf(line[2].toString())>0)
				cantidad = Double.valueOf(line[1].toString())/Double.valueOf(line[2].toString());
			
			valores.put(line[0].toString(), cantidad);
			actual =  Double.valueOf(line[2].toString());
			anterior =  Double.valueOf(line[3].toString());
			
		}
		
		return new DashTotalDTO(valores, actual, anterior);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashTotalDTO listarFormaPagoPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		Map<String, Double> valores = new HashMap<String, Double>();
		Double total = 0D;
		
		Query consulta = em
				.createNativeQuery("select "
						+ "	p.tipo, "
						+ "	Count(p.tipo) cantidad "
						+ "from pedidos p "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' and p.estado = 4 "
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
	public DashTotalDTO listarReclamosTPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
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
						+ "' UNION "
						+ "select "
						+ "'T' AS tipo, "
						+ "Count(p.id) cantidad "
						+ "from pedidos p "
						+ "WHERE p.fecha>= '" + getFechaHora(fechaDesde, "yyyy-MM-dd HH:mm")
						+ "' and p.fecha <= '" + getFechaHora(fechaHasta, "yyyy-MM-dd HH:mm")
						+ "' ORDER BY tipo");
		
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
	public List<RestauranteRDTO> listarRestaurantesAutorizar() {
		List<RestauranteRDTO> restaurantes = new ArrayList<RestauranteRDTO>();
		
		Query consulta = em
					.createNativeQuery("select "
							+ " u.id, "
							+ " u.nombre, "
							+ " to_char(u.horarioapertura, 'HH24:MI:SS') as apertura, "
							+ " to_char(u.horariocierre, 'HH24:MI:SS') as cierre, "
							+ " u.abierto, "
							+ " u.id_imagen, "
							+ " u.telefono, "
							+ " d.calle || ' ' || d.numero || (case d.apartamento is null when true then '' else ', ' ||d.apartamento end) "
							+ " from usuario u "
							+ " join direcciones d on d.id = u.id_direccion "
							+ " where dtype = 'restaurante' "
							+ " and estado = 0 "
							+ "");
		
			
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				LocalTime horarioApertura = LocalTime.parse(line[2].toString());
				LocalTime horarioCierre = LocalTime.parse(line[3].toString());
				restaurantes.add(new RestauranteRDTO(Long.valueOf(line[0].toString()), line[1].toString(), horarioApertura, horarioCierre, Boolean.valueOf(line[4].toString()), null, line[5].toString(), line[7].toString(), null, line[6].toString()));
			}
	
		
		return restaurantes;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashMenuDTO> listarTendenciasPorFecha(Long id, LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Integer top) {
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
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
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
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

	@SuppressWarnings("unchecked")
	@Override
	public List<DashRestauranteDTO> listarTopRestaurantesPorFecha(Long id, LocalDateTime fechaDesde,
			LocalDateTime fechaHasta, Integer top) {
		List<DashRestauranteDTO> restaurantes = new ArrayList<DashRestauranteDTO>();
		
		Query consulta = em
					.createNativeQuery("select "
							+ " u.id, "
							+ " u.nombre, "
							+ " to_char(u.horarioapertura, 'HH24:MI:SS') as apertura, "
							+ " to_char(u.horariocierre, 'HH24:MI:SS') as cierre, "
							+ " u.abierto, "
							+ " u.id_imagen, "
							+ " u.telefono, "
							+ " d.calle || ' ' || d.numero || (case d.apartamento is null when true then '' else ', ' ||d.apartamento end), "
							+ " case SUM(p.total) is null " 
							+ " when true then 0 "
							+ " else SUM(p.total) "
							+ " end total "
							+ " from pedidos p " 
							+ " join usuario u on u.id = p.id_restaurante "
							+ " join direcciones d on d.id = u.id_direccion "
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.estado = 4 "
							+ " group by "
							+ " u.id, "
							+ " u.nombre,"
							+ " to_char(u.horarioapertura, 'HH24:MI:SS'), "
							+ " to_char(u.horariocierre, 'HH24:MI:SS'), "
							+ " u.abierto, "
							+ " u.id_imagen, "
							+ " u.telefono, "
							+ " d.calle || ' ' || d.numero || (case d.apartamento is null when true then '' else ', ' ||d.apartamento end) "
							+ "");
		
		consulta.setMaxResults(top);
	
		List<Object[]> datos = consulta.getResultList();
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			
			LocalTime horarioApertura = LocalTime.parse(line[2].toString());
			LocalTime horarioCierre = LocalTime.parse(line[3].toString());
			restaurantes.add(new DashRestauranteDTO(Long.valueOf(line[0].toString()), line[1].toString(), horarioApertura, horarioCierre, Boolean.valueOf(line[4].toString()), null, line[5].toString(), line[7].toString(), null, line[6].toString(), Double.valueOf(line[8].toString())));
		}
	
		
		return restaurantes;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashInformeDTO> listarInfoVentasPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		List<DashInformeDTO> informe = new ArrayList<DashInformeDTO>();
		Double acumulado = 0D;
		
		Query consulta = em
					.createNativeQuery("SELECT "
							+ " to_char(aux.d, 'yyyy-MM-dd'), "
							+ " SUM(case p.total is null when true then 0 else p.total end) "
							+ " from pedidos p "
							+ " right join "
							+ " generate_series " 
							+ "	( to_date('" + getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ " , interval '1 day') as aux(d) on date(aux.d)= date(p.fecha) "
								+ " 						and p.estado = 4 "
							+ " group by aux.d "
							+ " ORDER by aux.d asc "
							+ "");
		
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				acumulado = acumulado + Double.valueOf(line[1].toString());
			
				informe.add(new DashInformeDTO(line[0].toString(), "", "", Double.valueOf(line[1].toString()), acumulado));
			}
	
		
		return informe;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashInformeDTO> listarInfoVentasPorFechaRestaurante(LocalDateTime fechaDesde,
			LocalDateTime fechaHasta) {
		List<DashInformeDTO> informe = new ArrayList<DashInformeDTO>();
		Double acumulado = 0D;
		
		Query consulta = em
					.createNativeQuery("SELECT "
							+ " to_char(aux.d, 'yyyy-MM-dd'), "
							+ " u.id|| '-' || u.nombre, " 
							+ " SUM(case p.total is null when true then 0 else p.total end) "
							+ " from pedidos p "
							+ " right join usuario u on u.id = p.id_restaurante"
							+ " right join "
							+ " generate_series " 
							+ "	( to_date('" + getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
					        + "	, to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
								+ " , interval '1 day') as aux(d) on date(aux.d)= date(p.fecha) "
								+ " 						and p.estado = 4 "
							+ " group by aux.d, u.id, u.nombre "
							+ " ORDER by aux.d asc, u.id "
							+ "");
		
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				acumulado = acumulado + Double.valueOf(line[2].toString());
				String nombre = line[1]==null?"":line[1].toString();
			
				informe.add(new DashInformeDTO(line[0].toString(), nombre, "", Double.valueOf(line[2].toString()), acumulado));
			}
	
		
		return informe;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashInformeDTO> listarInfoVentasPorFechaBarrio(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		List<DashInformeDTO> informe = new ArrayList<DashInformeDTO>();
		Double acumulado = 0D;
		
		Query consulta = em
					.createNativeQuery("SELECT "
							+ " '"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + " - " + getFechaHora(fechaHasta, "yyyy-MM-dd") +"' as periodo, "
							+ " dto.nombre as departamento, "
							+ " l.nombre as barrio, "
							+" SUM(case p.total is null when true then 0 else p.total end) as total"
							+" from pedidos p "
							+" join usuario u on u.id = p.id_restaurante "
							+" join direcciones d on d.id = p.id_entrega "
							+" join localidades l on st_contains(ST_GeometryFromText(l.geom), ST_GeometryFromText(d.geom)) "
							+" join departamentos dto on dto.id = l.id_departamento "
							+ "	WHERE date(p.fecha)> to_date('"+ getFechaHora(fechaDesde, "yyyy-MM-dd") + "', 'YYYY-MM-dd') " 
							+ "	and date(p.fecha)<=to_date('" + getFechaHora(fechaHasta, "yyyy-MM-dd") + "',  'YYYY-MM-dd') "
							+ " and p.estado = 4 "
							+" group by  dto.nombre, l.nombre "
							+" ORDER by dto.nombre, l.nombre "
							+ "");
		
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			while (it.hasNext()) {
				Object[] line = it.next();
				
				acumulado = acumulado + Double.valueOf(line[3].toString());
				
				informe.add(new DashInformeDTO(line[0].toString(), line[1].toString(), line[2].toString(), Double.valueOf(line[3].toString()), acumulado));
			}
	
		
		return informe;
	}
}
