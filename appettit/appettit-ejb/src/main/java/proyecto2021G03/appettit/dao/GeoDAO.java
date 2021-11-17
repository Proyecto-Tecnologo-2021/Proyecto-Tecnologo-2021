package proyecto2021G03.appettit.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import com.vividsolutions.jts.geom.Point;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.entity.Restaurante;


@Singleton
public class GeoDAO implements IGeoDAO {

	static Logger logger = Logger.getLogger(GeoDAO.class);
	
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
	public Localidad localidadPorPunto(String point) {
		Localidad localidad = null;

		try {
			Query consulta = em.createNativeQuery("select l.id, l.id_ciudad, l.id_departamento, l.nombre, l.geom "
					+ "from localidades l "
					+ "where st_contains(ST_GeometryFromText(l.geom), ST_GeometryFromText(:point)) = true");
			consulta.setParameter("point", point);
			
			Object[] data = (Object[]) consulta.getSingleResult();
			
			localidad = new Localidad(Long.valueOf(data[0].toString()), 
					Long.valueOf(data[1].toString()), 
					Long.valueOf(data[2].toString()),
					data[3].toString(),
					deptoDAO.ciudadPorId(Long.valueOf(data[1].toString()), Long.valueOf(data[2].toString())),
					data[4].toString());
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return localidad;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(String point) {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		
		try {
			Query consulta = em.createNativeQuery("select u.id "
					+ "from usuario u "
					+ "where st_contains(ST_GeometryFromText(u.geom), ST_GeometryFromText(:point)) = true "
					+ "and bloqueado = false "
					+ "and abierto = true");
			consulta.setParameter("point", point);
			
			List<?> datos = consulta.getResultList();
			
			restaurantes = datos
	         .stream()
	         .map(item -> item instanceof BigInteger ? usrDAO.buscarRestaurantePorId(((BigInteger) item ).longValue()): null)
	         .collect(Collectors.toList());
			
			/*
			List<Object[]> datos = consulta.getResultList();
			
			Iterator<Object[]> it = datos.iterator();
			
			while (it.hasNext()) {
				Object[] data = it.next();
				Restaurante restaurante = usrDAO.buscarRestaurantePorId(Long.valueOf(data[0].toString()));
				restaurantes.add(restaurante);
			}	
				
			*/
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		
		
		return restaurantes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> menuRestaurantesPorPunto(String point) {
		List<Menu> menus = new ArrayList<Menu>();
		
		try {
			Query consulta = em.createNativeQuery("select m.id, m.id_restaurante "
					+ "from menus m "
					+ "join usuario u ON u.id = m.id_restaurante "
					+ "where st_contains(ST_GeometryFromText(u.geom), ST_GeometryFromText(:point)) = true "
					+ "and u.bloqueado = false "
					+ "and u.abierto = true");
			consulta.setParameter("point", point);
			
			List<Object[]> datas = (List<Object[]>) consulta.getResultList();
			
			Iterator<Object[]> it = datas.iterator();
			while (it.hasNext()) {
				Object[] data = it.next();
				Menu menu = menuDAO.listarPorId(Long.valueOf(data[1].toString()), Long.valueOf(data[0].toString()));
				menus.add(menu);
			}	
				
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		
		return menus;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Promocion> promocionRestaurantesPorPunto(String point) {
				
		List<Promocion> promociones = new ArrayList<Promocion>();

		try {
			Query consulta = em.createNativeQuery("select p.id, p.id_restaurante "
					+ "from promociones p "
					+ "join usuario u ON u.id = p.id_restaurante "
					+ "where st_contains(ST_GeometryFromText(u.geom), ST_GeometryFromText(:point)) = true "
					+ "and u.bloqueado = false "
					+ "and u.abierto = true");
			consulta.setParameter("point", point);
			
			List<Object[]> datas = (List<Object[]>) consulta.getResultList();
			
			Iterator<Object[]> it = datas.iterator();
			while (it.hasNext()) {
				Object[] data = it.next();
				Promocion promocion = promDAO.buscarPorId(Long.valueOf(data[1].toString()), Long.valueOf(data[0].toString()));
				promociones.add(promocion);
			}	
				
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return promociones;
	}
}
