package proyecto2021G03.appettit.dao;

import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import proyecto2021G03.appettit.entity.Pedido;

@Singleton
public class PedidoDAO implements IPedidoDao {
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @SuppressWarnings("unchecked")
	@Override
    public List<Pedido> listar() {
        Query consulta = em.createQuery("SELECT p FROM Pedido p");
        return consulta.getResultList();    }

    @Override
    public Pedido listarPorId(Long id) {
        return em.find(Pedido.class, id);    }

    @Override
    public Pedido crear(Pedido pedido) {
        em.persist(pedido);
        return pedido;
    }

    @Override
    public Pedido editar(Pedido pedido) {
        em.persist(pedido);
        return pedido;    }

    @Override
    public void eliminar(Pedido pedido) {
        em.remove(pedido);
    }

	@Override
	public List<Pedido> listarPorCliente(Long id_cliente) {
		List<Pedido> pedido = null;

		pedido = em.createQuery("SELECT _p "
						+ "from Pedido as _p "
						+ "where _p.id_cliente = :id "
						+ "order by _p.id desc", Pedido.class)
				.setParameter("id", id_cliente)
				.getResultList();

		return pedido;

	}
	
	@Override
	public List<Pedido> listarPorRestaurante(Long id_restaurante) {
		List<Pedido> pedido = null;

		pedido = em.createQuery("SELECT _p "
						+ "from Pedido as _p "
						+ "where _p.id_restaurante = :id "
						+ "order by id asc", Pedido.class)
				.setParameter("id", id_restaurante)
				.getResultList();

		return pedido;

	}

	@Override
			public Pedido ultimo(Long idCliente) {
        Query ultimo =  em.createQuery("SELECT p from Pedido p where p.id_cliente = :idC order by p.id desc").setParameter("idC", idCliente);;
			@SuppressWarnings("unchecked")
			List<Pedido> listita= ultimo.setMaxResults(1).getResultList();
			Pedido last	= null;
			for(Pedido usr:listita){
				//System.out.println(usr.getId());
				last = usr;
			}
			return last;


		}

}
