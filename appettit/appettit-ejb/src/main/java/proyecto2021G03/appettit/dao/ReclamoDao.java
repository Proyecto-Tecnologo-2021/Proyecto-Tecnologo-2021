package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Reclamo;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mongodb.internal.connection.tlschannel.NeedsReadException;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ReclamoDao implements IReclamoDao {

	@EJB
	IPedidoDao pedDao;
	
	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Reclamo> listar() {
		Query consulta = em.createQuery("SELECT r FROM Reclamo r");
		return consulta.getResultList();
	}

	@Override
	public List<Reclamo> listarPorRestaurante(Long id) {
		List<Reclamo> reclamos = new ArrayList<Reclamo>();
		List<Pedido> pedidos = pedDao.listarPorRestaurante(id); 
		for (Pedido p: pedidos) {
			if (p.getReclamo() != null) {
				reclamos.add(p.getReclamo());
			}
		}
		return reclamos;
	}
	
	@Override
	public Reclamo listarPorId(Long id) {
		return em.find(Reclamo.class, id);
	}

	@Override
	public Reclamo crear(Reclamo reclamo) {
		em.persist(reclamo);
		return reclamo;
	}

	@Override
	public Reclamo editar(Reclamo reclamo) {
		return em.merge(reclamo);
	}

	@Override
	public void eliminar(Reclamo reclamo) {
		em.remove(reclamo);
	}

}
