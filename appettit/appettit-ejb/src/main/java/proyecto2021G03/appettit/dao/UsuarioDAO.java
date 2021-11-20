package proyecto2021G03.appettit.dao;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.sql.Select;
import org.jboss.logging.Logger;

import proyecto2021G03.appettit.dto.CalificacionGralClienteDTO;
import proyecto2021G03.appettit.dto.CalificacionGralRestauranteDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.RestauranteRDTO;
import proyecto2021G03.appettit.entity.*;

@Singleton
public class UsuarioDAO implements IUsuarioDAO {
	
	static Logger logger = Logger.getLogger(UsuarioDAO.class);

	@PersistenceContext(name = "Proyecto2021G03")
	
	private EntityManager em;

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
	public Boolean existeCorreo(String correo) {
		Query consulta = em
				.createQuery("SELECT _usu FROM Usuario as _usu where correo = :correo");
		consulta.setParameter("correo", correo);

		return consulta.getResultList().size() != 0;
	}

	/* ADMINISTRADOR */

	@Override
	public Administrador buscarAdministradorPorId(Long id) {
		return em.find(Administrador.class, id);
	}

	@Override
	public Administrador crearAdministrador(Administrador administrador) {
		em.persist(administrador);

		return administrador;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Administrador> listarAdministradores() {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type").setParameter("type", "administrador");

		List<Administrador> usuarios = consulta.getResultList();
		return usuarios;
	}

	@Override
	public List<Administrador> buscarPorNombreAdministrador(String nombre) {
		List<Administrador> usuarios = em.createQuery("from Administrador _a where _a.nombre = :nombre", Administrador.class)
		.setParameter("nombre", nombre)
		.getResultList();
		return usuarios;
	}

	/* RESTAURANTE */
	
	@Override
	public Restaurante buscarRestaurantePorId(Long id) {
		return em.find(Restaurante.class, id);
	}


	@Override
	public Restaurante crearRestaurante(Restaurante restaurante) {
		em.persist(restaurante);

		return restaurante;
	}

	@Override
	public Restaurante editarRestaurante(Restaurante restaurante) {
		
		restaurante =  em.merge(restaurante);
		
		return restaurante;
		
	}

	@Override
	public List<Restaurante> listarRestaurantes() {

		List<Restaurante> restaurantes = em.createQuery("select r "
				+ "from Restaurante r", Restaurante.class)
				.getResultList();

		return restaurantes;

	}
	
	@Override
	public List<Restaurante> listarRestaurantesAbiertos() {

		List<Restaurante> restaurantes = em.createQuery("select r "
				+ "from Restaurante r "
				+ "where abierto=true", Restaurante.class)
				.getResultList();

		return restaurantes;

	}

	@Override
	public List<Restaurante> buscarPorNombreRestaurante(String nombre) {
		List<Restaurante> usuarios = em.createQuery("Select _r from Restaurante _r where _r.nombre = :nombre", Restaurante.class)
		.setParameter("nombre", nombre)
		.getResultList();
		return usuarios;
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

	@SuppressWarnings("unchecked")
	@Override
	public CalificacionGralRestauranteDTO calificacionRestaurante(Long id) {
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
		
		consulta.setParameter("id_restaurante", id);

		
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
		
		return new CalificacionGralRestauranteDTO(rapidez, comida, servicio, general);
				  	
	}

	@Override
	public RestauranteRDTO buscarPorId(Long id) {
		return null;
	}




	/* CLIENTE */

	@Override
	public Cliente crearCliente(Cliente cliente) {
		em.persist(cliente);
		return cliente;
	}

	@Override
	public Cliente editarCliente(Cliente cliente) {

		return em.merge(cliente);
	}

	@Override
	public Cliente agregarDireccion(Cliente cliente) {
		
		return em.merge(cliente);
	}
	
	@Override
	public Cliente eliminarDireccion(Cliente cliente, Direccion direccion) {

		Cliente cliente_sin_direccion = em.merge(cliente);
		
		em.remove(direccion);
		
		return cliente_sin_direccion;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> listarClientes() {
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type order by id asc").setParameter("type", "cliente");

		List<Cliente> usuarios = consulta.getResultList();
		return usuarios;
	}

	@Override
	public List<Cliente> buscarPorNombreCliente(String nombre) {

		List<Cliente> usuarios = em.createQuery("select _c from Cliente where _c.nombre=:nombre", Cliente.class)
				.setParameter("nombre", nombre)
				.getResultList();
		
		return usuarios;
	}

	@Override
	public Cliente buscarPorIdCliente(Long id) {
		return em.find(Cliente.class, id);
	}

	@Override
	public List<Cliente> buscarPorIdClienteInteger(Long id) {
		
		Query consulta = em.createQuery("from Usuario _usr where dtype = :type and id = :id");
		consulta.setParameter("type", "cliente");
		consulta.setParameter("id", id);

		@SuppressWarnings("unchecked")
		List<Cliente> usuarios = consulta.getResultList();
		/*
		List<Cliente> usuarios = em.createQuery("select _c from Cliente where _c.id=:id", Cliente.class)
				.setParameter("id", id)
				.getResultList();
		*/
		return usuarios;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CalificacionGralClienteDTO calificacionGralCliente(Long id) {
		Query consulta = em
				.createNativeQuery("SELECT "
						+ "c.cla, "
						+ "COUNT(CASE c.cla = cc.clasificacion "
						+ "	WHEN TRUE THEN cc.clasificacion "
						+ "	ELSE NULL "
						+ "	END) AS clasifica, "
						+ "COUNT(DISTINCT cc.id_restaurante) res "
						+ "FROM clasificacionesclientes cc "
						+ "RIGHT JOIN  generate_series(1, 5, 1) AS c(cla) ON 1=1 "
						+ "and cc.id_cliente = :id_cliente "
						+ "GROUP BY c.cla "
						+ "");
		
		consulta.setParameter("id_cliente", id);

		
		List<Object[]> datos = consulta.getResultList();
		Integer general = 0;
		Integer tgeneral = 0;
		Integer restaurantes = 0;
		
		
		Iterator<Object[]> it = datos.iterator();
		while (it.hasNext()) {
			Object[] line = it.next();
			general += Integer.valueOf(line[0].toString()) * Integer.valueOf(line[1].toString());
			tgeneral += Integer.valueOf(line[1].toString());
			restaurantes = Integer.valueOf(line[2].toString());
			
		}
		if(tgeneral != 0)
			general = general/tgeneral;
		
		return new CalificacionGralClienteDTO(general, restaurantes);
	}

	@Override
	public Direccion buscarDireccionPorId(Long id) {
		return em.find(Direccion.class, id);
	}

	@Override
	public PedidoRDTO buscarultimo(Long id) {
		Query ultimo = 	em.createQuery("SELECT u from Usuario u order by u.id desc ");
				List<Usuario> listita= ultimo.setMaxResults(1).getResultList();
				for(Usuario usr:listita){
					System.out.println(usr.getId());
					System.out.println("imprimo char pija");

				}
		return null;


	}

	@Override
	public Cliente buscarPorCorreoCliente(String correo) {
		Cliente cliente = null;

		cliente = em.createQuery("SELECT _c "
						+ "from Cliente as _c "
						+ "where _c.correo = :correo", Cliente.class)
				.setParameter("correo", correo)
				.getSingleResult();

		return cliente;
	}
	
}
