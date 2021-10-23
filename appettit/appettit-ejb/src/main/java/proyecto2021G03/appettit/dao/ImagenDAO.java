package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Imagen;

@Singleton
public class ImagenDAO implements IImagenDAO {
	
	static Logger logger = Logger.getLogger(ImagenDAO.class);
	
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Imagen> listar() {
		
		List<Imagen> imagenes = em.createQuery("select i "
				+ "from Imagen i ")
		        .getResultList();
		return imagenes;
	}

	@Override
	public Imagen buscarPorId(String id) {
		Imagen imagen = null;
		
		try {
			imagen =  em.createQuery("select i "
					+ "from Imagen i "
					+ "where id = :id", Imagen.class)
					.setParameter("id", id)
					.getSingleResult();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return imagen;
	}
	
	@TransactionAttribute
    public String saveImage(Imagen imagen){
        em.persist(imagen);
        return imagen.getId();
    }

	@Override
	public Imagen buscarPorIdentificador(String identificador) {
		Imagen imagen = null;
		
		try {
			imagen =  em.createQuery("select i "
					+ "from Imagen i "
					+ "where identificador = :identificador", Imagen.class)
					.setParameter("identificador", identificador)
					.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return imagen;
	}

}
