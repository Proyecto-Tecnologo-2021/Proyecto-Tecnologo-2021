package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.entity.Categoria;

@Singleton
public class CategoriaDAO implements ICategoriaDAO{

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> listar() {
		Query consulta = em.createQuery("SELECT c FROM Categoria c");
		return consulta.getResultList();
	}

	@Override
	public Categoria listarPorId(Long id) {
		return em.find(Categoria.class, id);
	}

	@Override
	public Categoria crear(Categoria categoria) {
		em.persist(categoria);
		return categoria;
	}

	@Override
	public Categoria editar(Categoria categoria) {
		em.persist(categoria);
		return categoria;
	}

	@Override
	public void eliminar(Categoria categoria) {
		em.remove(categoria);
		
	}
		
}
