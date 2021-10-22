package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;
import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.entity.Localidad;

@Singleton
public class DepartamentoDAO implements IDepartamentoDAO {

	static Logger logger = Logger.getLogger(DepartamentoDAO.class);

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@Override
	public Departamento crear(Departamento departamento) {
		em.persist(departamento);

		return departamento;
	}

	@Override
	public Departamento editar(Departamento departamento) {
		em.merge(departamento);
		return departamento;
	}

	@Override
	public void eliminar(Departamento departamento) {
		em.remove(departamento);

	}

	@Override
	public List<Departamento> listar() {

		List<Departamento> departamentos = new ArrayList<Departamento>();

		try {
			departamentos = em.createQuery("select d from Departamento d ", Departamento.class).getResultList();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return departamentos;
	}

	@Override
	public Departamento buscarPorId(Long id) {
		return em.find(Departamento.class, id);
	}

	@Override
	public List<Departamento> buscarPorNombre(String nombre) {

		List<Departamento> departamentos = new ArrayList<Departamento>();

		try {

			departamentos = em
					.createQuery("select d " + "from Departamento d " + "where nombre =:nombre", Departamento.class)
					.setParameter("nombre", nombre).getResultList();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return departamentos;

	}

	@Override
	public Ciudad crearCiudad(Ciudad ciudad) {
		em.persist(ciudad);
		return ciudad;
	}

	@Override
	public Localidad crearLocalidad(Localidad localidad) {
		em.persist(localidad);
		return localidad;
	}

	@Override
	public Ciudad ciudadPorId(Long id, Long id_departamento) {
		Ciudad ciudad = null;
		
		try {
			ciudad = em.createQuery("select c "
					+ "from Ciudad c "
					+ "where id= :id and id_departamento = :id_departamento", Ciudad.class)
					.setParameter("id", id)
					.setParameter("id_departamento", id_departamento)
					.getSingleResult();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return ciudad;
	}

	@Override
	public Localidad localidadPorId(Long id, Long id_ciudad, Long id_departamento) {
		Localidad localidad = null;
		
		try {
			localidad = em.createQuery("select c "
					+ "from Localidad c "
					+ "where id= :id and "
					+ "id_ciudad = :id_ciudad and "
					+ "id_departamento = :id_departamento", Localidad.class)
					.setParameter("id", id)
					.setParameter("id_ciudad", id_ciudad)
					.setParameter("id_departamento", id_departamento)
					.getSingleResult();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return localidad;

	}

}
