package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.entity.Producto;

@Singleton
public class ProductoDAO implements IProductoDAO{

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Producto> listar() {
		Query consulta = em.createQuery("SELECT p FROM Producto p");
		return consulta.getResultList();
	}

	@Override
	public Producto listarPorId(Long id) {
		return em.find(Producto.class, id);
	}

	@Override
	public Producto crear(Producto producto) {
		em.persist(producto);
		return producto;
	}

	@Override
	public Producto editar(Producto producto) {
		em.persist(producto);
		return producto;
	}

	@Override
	public void eliminar(Producto producto) {
		em.remove(producto);
		
	}

	@Override
	public  List<Producto> listarPorRestaurante(Long id) {
		 List<Producto> productos = em.createQuery("select _p "
		 		+ "from Producto _p "
		 		+ "inner join _p.restaurante _r "
				+ "where _r.id =:id", Producto.class)
				.setParameter("id", id).getResultList();
		
		return productos;
	}
	
}
