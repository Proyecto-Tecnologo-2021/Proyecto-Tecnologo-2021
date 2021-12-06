package proyecto2021G03.appettit.dao;

import org.jboss.logging.Logger;
import proyecto2021G03.appettit.entity.*;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class CalificacionRDAO implements ICalificacionRDao {
 
	@PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;
    static Logger logger = Logger.getLogger(CalificacionRDAO.class);

    
    @SuppressWarnings("unchecked")
	@Override
    public List<ClasificacionPedido> listar() {
        Query consulta = em.createQuery("SELECT c FROM ClasificacionPedido c");
        return consulta.getResultList();
    }

    @Override
    public ClasificacionPedido listarPorId(Long id_pedido, Long id_cliente) {
        ClasificacionPedido clasificacion = null;

        try {
            clasificacion =  em.createQuery("select c "
                            + "from ClasificacionPedido c "
                            + "where id_pedido = :id_pedido "
                            + "and id_cliente = :id_cliente", ClasificacionPedido.class)
                    .setParameter("id_pedido", id_pedido)
                    .setParameter("id_cliente", id_cliente)
                    .getSingleResult();

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return clasificacion;
    }

    @Override
    public ClasificacionPedido crear(ClasificacionPedido clasificacionPedido) {
        em.persist(clasificacionPedido);
   return clasificacionPedido;
    }

    @Override
    public ClasificacionPedido editar(ClasificacionPedido clasificacionPedido) {

        em.merge(clasificacionPedido);
        return clasificacionPedido;
    }

    @Override
    public void eliminar(ClasificacionPedido clasificacionPedido) {
    	em.remove(clasificacionPedido);
    }
}
