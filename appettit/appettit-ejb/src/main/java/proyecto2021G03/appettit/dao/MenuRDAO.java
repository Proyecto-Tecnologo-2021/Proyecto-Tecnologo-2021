package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.entity.Menu;
@Singleton
public class MenuRDAO implements IMenuRDAO{

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @SuppressWarnings("unchecked")
	@Override
    public List<Menu> listar() {
            Query consulta = em.createQuery("SELECT m FROM Menu m");
            return consulta.getResultList();
        }

	@Override
	public Menu listarPorId(Long id_restaurante, Long id) {

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
}
