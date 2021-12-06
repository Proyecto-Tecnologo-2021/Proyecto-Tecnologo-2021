package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import proyecto2021G03.appettit.entity.Categoria;

@Singleton
public class CategoriaDAO implements ICategoriaDAO{

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@Override
	public List<Categoria> listar() {
		List<Categoria> consulta = em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();;
		return consulta;
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
