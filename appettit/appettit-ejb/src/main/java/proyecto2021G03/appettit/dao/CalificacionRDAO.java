package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.entity.ClasificacionPedidoId;
import proyecto2021G03.appettit.entity.Pedido;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class CalificacionRDAO implements ICalificacionRDao {
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public List<CalificacionRestauranteDTO> listar() {
        Query consulta = em.createQuery("SELECT c FROM ClasificacionPedido c");
        return consulta.getResultList();
    }

    @Override
    public CalificacionRestauranteDTO listarPorId(Long id) {
       return null;    }

    @Override
    public CalificacionRestauranteDTO crear(ClasificacionPedido clasificacionPedido) {
        return null;
    }

    @Override
    public CalificacionRestauranteDTO editar(ClasificacionPedido clasificacionPedido) {
        return null;
    }

    @Override
    public void eliminar(ClasificacionPedido clasificacionPedido) {

    }
}
