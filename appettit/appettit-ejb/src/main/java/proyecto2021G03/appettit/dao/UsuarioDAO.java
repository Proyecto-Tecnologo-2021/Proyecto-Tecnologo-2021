package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;
import vacunasuy.componentecentral.dto.ReporteEvolucionTiempoDTO;

@Singleton
public class UsuarioDAO implements IUsuarioDAO {

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;

	@Override
	public Usuario crear(Usuario usuario) {
		em.persist(usuario);

		return usuario;
	}

	@Override
	public Usuario editar(Usuario usuario) {
		em.merge(usuario);
		return usuario;
	}

	@Override
	public void eliminar(Usuario usuario) {
		em.remove(usuario);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> listar() {
		Query consulta = em.createQuery("SELECT _usu FROM Usuario as _usu");
		return consulta.getResultList();
	}

	@Override
	public Usuario buscarPorId(Long id) {
		return em.find(Usuario.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscarPorNombre(String nombre) {
		Query consulta = em.createQuery("SELECT _usu FROM Usuario as _usu where nombre = :nombre")
				.setParameter("nombre", nombre);
		return consulta.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Administrador> listarAdministradores() {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type").setParameter("type", "administrador");

		List<Administrador> usuarios = consulta.getResultList();
		return usuarios;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Administrador> buscarPorNombreAdministrador(String nombre) {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type and nombre = :nombre");
		consulta.setParameter("type", "administrador");
		consulta.setParameter("nombre", nombre);

		List<Administrador> usuarios = consulta.getResultList();
		return usuarios;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscarPorCorreo(String correo) {
		Query consulta = em.createQuery("SELECT _usu FROM Usuario as _usu where correo = :correo")
				.setParameter("correo", correo);
		return consulta.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscarPorTelefono(String telefono) {
		Query consulta = em.createQuery("SELECT _usu FROM Usuario as _usu where telefono = :telefono")
				.setParameter("telefono", telefono);
		return consulta.getResultList();
	}

	@Override
	public Boolean existeCorreoTelefono(String correo, String telefono) {
		Query consulta = em
				.createQuery("SELECT _usu FROM Usuario as _usu where telefono = :telefono or correo = :correo");
		consulta.setParameter("correo", correo);
		consulta.setParameter("telefono", telefono);

		return consulta.getResultList().size() != 0;
	}

	@Override
	public Administrador crearAdministrador(Administrador administrador) {
		em.persist(administrador);

		return administrador;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Restaurante> listarRestaurantes() {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type").setParameter("type", "restaurante");

		List<Restaurante> usuarios = consulta.getResultList();
		return usuarios;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Restaurante> buscarPorNombreRestaurante(String nombre) {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type and nombre = :nombre");
		consulta.setParameter("type", "restaurante");
		consulta.setParameter("nombre", nombre);

		List<Restaurante> usuarios = consulta.getResultList();
		return usuarios;
	}

	@Override
	public Restaurante crearRestaurante(Restaurante restaurante) {
		em.persist(restaurante);

		return restaurante;
	}

	@Override
	public CalificacionRestauranteDTO calificcionRestaurante(RestauranteDTO restauranteDTO) {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type").setParameter("type", "restaurante");

		CalificacionRestauranteDTO calificacion = new CalificacionRestauranteDTO();
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			ReporteEvolucionTiempoDTO eq = new ReporteEvolucionTiempoDTO();
			eq.setFecha(line[0].toString());
			eq.setCantidad(Integer.valueOf(line[1].toString()));
			reportefinal.add(eq);
		}
		return reportefinal;
	
	}

}
