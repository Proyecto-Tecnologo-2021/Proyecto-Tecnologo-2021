package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Menu;

@Singleton
public class MenuDAO implements IMenuDAO {

	static Logger logger = Logger.getLogger(MenuDAO.class);

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> listar() {
		Query consulta = em.createQuery("SELECT m FROM Menu m");
		return consulta.getResultList();
	}

	@Override
	public Menu listarPorId(Long id, Long id_restaurante) {
		Menu menu = null;

		menu = em.createQuery("SELECT _m "
						+ "from Menu as _m "
						+ "where _m.id_restaurante = :id_restaurante "
						+ "and _m.id = :id", Menu.class)
				.setParameter("id_restaurante", id_restaurante)
				.setParameter("id", id)
				.getSingleResult();

		return menu;

	}

	@Override
	public Menu crear(Menu menu) {
		em.persist(menu);
		return menu;
	}

	@Override
	public Menu editar(Menu menu) {
		em.persist(menu);
		return menu;
	}

	@Override
	public void eliminar(Menu menu) {
		em.remove(menu);
	}

	@Override
	public List<Menu> listarPorRestaurate(Long id) {
		List<Menu> menus = new ArrayList<Menu>();
		try {
			menus = em.createQuery("select _m " 
					+ "from Menu _m "
					+ "inner join _m.restaurante _r "
					+ "where _r.id =:id", Menu.class)
					.setParameter("id", id).getResultList();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return menus;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Menu> listara() {
		Query consulta = em.createQuery("SELECT m FROM Menu m");
		return consulta.getResultList();	
		
	}
}
