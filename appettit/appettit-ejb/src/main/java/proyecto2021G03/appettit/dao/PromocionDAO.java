package proyecto2021G03.appettit.dao;

import org.jboss.logging.Logger;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
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
    public Promocion listarPorId(Long id) {

        return em.find(Promocion.class, id);
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
}
