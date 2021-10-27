package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Pedido;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class PeridoRDAO implements IPedidoRDAO{
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public Pedido crear(Pedido pedido) {
        em.persist(pedido);
        return pedido;
    }
    }

