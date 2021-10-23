package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ClasificacionCliente;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Singleton
public class ClasificacionClienteDAO implements IClasificacionClienteDAO{

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @SuppressWarnings("unchecked")
	@Override
    public List<ClasificacionCliente> listar() {
        Query consulta = em.createQuery("SELECT c FROM ClasificacionCliente c");
        return consulta.getResultList();    }

    @Override
    public ClasificacionCliente listarPorId(Long id) {
        return em.find(ClasificacionCliente.class, id);
    }

    @Override
    public ClasificacionCliente crear(ClasificacionCliente clasificacionCliente) {
        em.persist(clasificacionCliente);
        return clasificacionCliente;    }

    @Override
    public ClasificacionCliente editar(ClasificacionCliente clasificacionCliente) {
        em.persist(clasificacionCliente);
        return clasificacionCliente;    }


    @Override
    public void eliminar(ClasificacionCliente clasificacionCliente) {
    em.remove(clasificacionCliente);
    }
}
