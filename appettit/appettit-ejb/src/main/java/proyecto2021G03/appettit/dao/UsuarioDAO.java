package proyecto2021G03.appettit.dao;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;

@Singleton
public class UsuarioDAO implements IUsuarioDAO {
	
	static Logger logger = Logger.getLogger(UsuarioDAO.class);


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

	@Override
	public List<Restaurante> listarRestaurantes() {
		
		List<Restaurante> restaurantes = em.createQuery("select r "
				+ "from Restaurante r", Restaurante.class)
				.getResultList();

		return restaurantes;
		
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

	@SuppressWarnings("unchecked")
	@Override
	public CalificacionRestauranteDTO calificcionRestaurante(RestauranteDTO restauranteDTO) {
		Query consulta = em
				.createNativeQuery("SELECT"
						+ "	c.cla, "
						+ "	COUNT(CASE c.cla = cp.comida"
						+ "		  WHEN TRUE THEN cp.comida"
						+ "			ELSE NULL"
						+ "		  END) AS comida,"
						+ "	COUNT(CASE c.cla = cp.rapidez"
						+ "		  WHEN TRUE THEN cp.rapidez"
						+ "			ELSE NULL"
						+ "		  END) AS rapidez,"
						+ "	COUNT(CASE c.cla = cp.servicio"
						+ "		  WHEN TRUE THEN cp.servicio"
						+ "			ELSE NULL"
						+ "		  END) AS servicio"
						+ " FROM clasificacionespedidos cp"
						+ " JOIN pedidos p ON p.id = cp.id_pedido"
						+ "				AND p.id_restaurante = :id_restaurante"
						+ " RIGHT JOIN generate_series(1, 5, 1) AS c(cla) ON 1=1"
						+ " GROUP BY c.cla"
						+ "");
		
		consulta.setParameter("id_restaurante", restauranteDTO.getId());

		
		List<Object[]> datos = consulta.getResultList();
		Integer rapidez = 0; 
		Integer comida = 0;
		Integer servicio = 0;
		Integer general = 0;
		Integer trapidez = 0; 
		Integer tcomida = 0;
		Integer tservicio = 0;
		Integer tgeneral = 0;
		
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			comida += Integer.valueOf(line[0].toString()) * Integer.valueOf(line[1].toString());
			rapidez += Integer.valueOf(line[0].toString()) * Integer.valueOf(line[2].toString()); 
			servicio += Integer.valueOf(line[0].toString()) * Integer.valueOf(line[3].toString());
			general += Integer.valueOf(line[0].toString()) * (Integer.valueOf(line[1].toString()) + Integer.valueOf(line[2].toString()) + Integer.valueOf(line[3].toString()));
			tcomida += Integer.valueOf(line[1].toString());
			trapidez += Integer.valueOf(line[2].toString()); 
			tservicio += Integer.valueOf(line[3].toString());
			tgeneral += (Integer.valueOf(line[1].toString()) + Integer.valueOf(line[2].toString()) + Integer.valueOf(line[3].toString()));
			
		}
		if(tcomida != 0)
			comida = comida/tcomida;
		if(trapidez != 0)
			rapidez = rapidez/trapidez;
		if(tservicio != 0)
			servicio = servicio/tservicio;
		if(tgeneral != 0)
			general = general/tgeneral;
		
		return new CalificacionRestauranteDTO(rapidez, comida, servicio, general);
				  	
	}

	@Override
	public Cliente crearCliente(Cliente cliente) {
		return null;
	}

	@Override
	public List<Cliente> listarClientes() {
		return null;
	}

	@Override
	public List<Cliente> buscarPorNombreCliente(String nombre) {
		return null;
	}

	@Override
	public List<Cliente> buscarPorIdCliente(String nombre) {
		return null;
	}

	@Override
	public CalificacionClienteDTO calificacionCliente(ClienteDTO clienteData) {
		return null;
	}

	@Override
	public Restaurante buscarPorCorreoRestaurante(String correo) {
		Restaurante res = null;
		
		res = em.createQuery("SELECT _r "
				+ "from Restaurante as _r "
				+ "where _r.correo = :correo", Restaurante.class)
				.setParameter("correo", correo)
				.getSingleResult();
		
		return res;
	}

	@Override
	public Restaurante editarRestaurante(Restaurante restaurante) {
		return em.merge(restaurante);
	}

}
