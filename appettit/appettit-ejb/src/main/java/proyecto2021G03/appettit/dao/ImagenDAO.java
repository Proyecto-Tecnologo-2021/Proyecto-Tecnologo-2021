package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import proyecto2021G03.appettit.entity.Imagen;

@Singleton
public class ImagenDAO implements IImagenDAO {
	
	@PersistenceContext(unitName = "mongo-ogm")
	@PersistenceUnit(unitName = "mongo-ogm") 
	private EntityManager em;	

	@Override
	public Imagen crear(Imagen imagen) {
		em.persist(imagen);
		
		return imagen;
	}

	@Override
	public Imagen editar(Imagen imagen) {
		em.merge(imagen);
		return imagen;
	}

	@Override
	public void eliminar(Imagen imagen) {
		em.remove(imagen);

	}

	@Override
	public List<Imagen> listar() {
		
		return null;
	}

	@Override
	public Imagen buscarPorId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
