package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.entity.Ciudad;
import proyecto2021G03.appettit.entity.Departamento;
import proyecto2021G03.appettit.entity.Localidad;

@Singleton
public class DepartamentoDAO implements IDepartamentoDAO {

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    

	@Override
	public Departamento crear(Departamento departamento) {
		//em.persist(departamento);
		em.merge(departamento);
		
		
		return departamento;
	}

	@Override
	public Departamento editar(Departamento departamento) {
		em.persist(departamento);
		return departamento;
	}

	@Override
	public void eliminar(Departamento departamento) {
		em.remove(departamento);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Departamento> listar() {
		Query consulta = em.createQuery("SELECT d FROM Departamento d");
		return consulta.getResultList();
	}

	@Override
	public Departamento buscarPorId(Long id) {
		return em.find(Departamento.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Departamento> buscarPorNombre(String nombre) {
		String query = "SELECT _d.id, _d.nombre from Departamento _d " + 
				   "where nombre = :nombre ";
	
		Query q = em.createQuery(query).setParameter("nombre",nombre);
		
		List<Object[]> datos =  q.getResultList();
		List<Departamento> departamentos = new ArrayList<Departamento>();
		Iterator<Object[]> it = datos.iterator();
		while(it.hasNext()){
		     Object[] line = it.next();
		     Departamento eq = new Departamento();
		     eq.setId(Long.valueOf(line[0].toString()));
		     eq.setNombre(line[1].toString());
		     departamentos.add(eq);
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

}
