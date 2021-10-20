package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Producto;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class PedidoDAO implements IPedidoDao {
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;
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
        return pedido;    }

    @Override
    public Pedido editar(Pedido pedido) {
        em.persist(pedido);
        return pedido;    }

    @Override
    public void eliminar(Pedido pedido) {
        em.remove(pedido);
    }
}
