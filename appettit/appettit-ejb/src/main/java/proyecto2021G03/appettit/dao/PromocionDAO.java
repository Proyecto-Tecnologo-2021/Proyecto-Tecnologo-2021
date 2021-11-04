package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Promocion;

@Singleton
public class PromocionDAO implements IPromocionDAO {

    static Logger logger = Logger.getLogger(PromocionDAO.class);

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Promocion> listar() {
        Query consulta = em.createQuery("SELECT p FROM Promocion p");
        return consulta.getResultList();
    }

    @Override
    public Promocion listarPorId(Long id, Long id_restaurante) {
    	Promocion promocion = null;

		promocion = em.createQuery("SELECT _p "
						+ "from Promocion as _p "
						+ "where _p.id_restaurante = :id_restaurante "
						+ "and _p.id = :id", Promocion.class)
				.setParameter("id_restaurante", id_restaurante)
				.setParameter("id", id)
				.getSingleResult();

		return promocion;
    }

    @Override
    public Promocion crear(Promocion promo) {
        em.persist(promo);
        return promo;
    }

    @Override
    public Promocion editar(Promocion promo) {

        return em.merge(promo);
    }

    @Override
    public void eliminar(Promocion promo) {

        em.remove(promo);
    }

    @Override
    public List<Promocion> listarPorRestaurante(Long id) {
        List<Promocion> promos = em.createQuery("select _p "
                        + "from Promocion _p "
                        + "inner join _p.restaurante _r "
                        + "where _r.id =:id", Promocion.class)
                .setParameter("id", id).getResultList();

        return promos;
    }

	@Override
	public Promocion buscarPorId(Long id_restaurante, Long id) {
		Promocion promocion = null;

		promocion = em.createQuery("SELECT _p "
						+ "from Promocion as _p "
						+ "where _p.id_restaurante = :id_restaurante "
						+ "and _p.id = :id", Promocion.class)
				.setParameter("id_restaurante", id_restaurante)
				.setParameter("id", id)
				.getSingleResult();

		return promocion;
	}

}
