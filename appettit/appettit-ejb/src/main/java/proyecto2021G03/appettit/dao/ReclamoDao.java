package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.entity.Reclamo;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Singleton
public class ReclamoDao implements IReclamoDao{

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public List<Reclamo> listar() {
        Query consulta = em.createQuery("SELECT r FROM Reclamo r");
        return consulta.getResultList();    }

    @Override
    public Reclamo listarPorId(Long id) {
        return em.find(Reclamo.class, id);    }

    @Override
    public Reclamo crear(Reclamo reclamo) {
        em.persist(reclamo);
        return reclamo;    }

    @Override
    public Reclamo editar(Reclamo reclamo) {
        em.persist(reclamo);
        return reclamo;    }
    @Override
    public void eliminar(Reclamo reclamo) {
    em.remove(reclamo);
    }
}
