package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.entity.ExtraMenu;


@Singleton
public class ExtraMenuDAO implements IExtraMenuDAO {
	
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @SuppressWarnings("unchecked")
	@Override
    public List<ExtraMenu> listar() {
        Query consulta = em.createQuery("SELECT m FROM ExtraMenu m");
        return consulta.getResultList();
    }

    @Override
    public ExtraMenu listarPorId(Long id) {

        return em.find(ExtraMenu.class, id);
    }

    @Override
    public ExtraMenu crear(ExtraMenu extraMenu) {

        em.persist(extraMenu);
        return extraMenu;
    }

    @Override
    public ExtraMenu editar(ExtraMenu extraMenu) {
        em.persist(extraMenu);
        return extraMenu;
    }

    @Override
    public void eliminar(ExtraMenu extraMenu) {
    em.remove(extraMenu);
    }

	@Override
	public List<ExtraMenu> listarPorRestaurante(Long id) {
		 List<ExtraMenu> extramenu = em.createQuery("select _e "
			 		+ "from ExtraMenu _e "
			 		+ "inner join _e.producto _p "
			 		+ "inner join _p.restaurante _r "
					+ "where _r.id =:id", ExtraMenu.class)
					.setParameter("id", id).getResultList();
			
			return extramenu;
	}


}
