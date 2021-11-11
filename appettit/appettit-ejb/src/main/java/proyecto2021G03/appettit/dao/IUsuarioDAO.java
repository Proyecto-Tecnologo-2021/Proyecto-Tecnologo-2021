package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.CalificacionGralClienteDTO;
import proyecto2021G03.appettit.dto.CalificacionGralRestauranteDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.RestauranteRDTO;
import proyecto2021G03.appettit.entity.*;

@Local
public interface IUsuarioDAO {
	/*GENERAL*/
	//public Usuario crear(Usuario usuario);
	//public Usuario editar (Usuario usuario);
	//public void eliminar (Usuario usuario);
	//public List<Usuario> listar();
	//public Usuario buscarPorId(Long id);
	//public List<Usuario> buscarPorNombre(String nombre);
	
	public List<Usuario> buscarPorCorreo(String correo);
	public List<Usuario> buscarPorTelefono(String telefono);
	
	public Boolean existeCorreoTelefono(String correo, String telefono);

	/*ADMINISTRADOR*/
	public Administrador buscarAdministradorPorId(Long id);
	public Administrador crearAdministrador(Administrador administrador);
	public List<Administrador> listarAdministradores();
	public List<Administrador> buscarPorNombreAdministrador(String nombre);

	/*RESTAURANTE*/
	public Restaurante buscarRestaurantePorId(Long id);
	public Restaurante crearRestaurante(Restaurante restaurante);
	public Restaurante editarRestaurante(Restaurante restaurante);
	public List<Restaurante> listarRestaurantes();
	public List<Restaurante> listarRestaurantesAbiertos();
	public List<Restaurante> buscarPorNombreRestaurante(String nombre);
	public Restaurante buscarPorCorreoRestaurante(String correo);
	public CalificacionGralRestauranteDTO calificacionRestaurante(Long id);
 	public RestauranteRDTO buscarPorId(Long id);

	/*CLIENTE*/
	public Cliente crearCliente(Cliente cliente);
	public Cliente editarCliente(Cliente cliente);
	public Cliente agregarDireccion(Cliente cliente);
	public Cliente eliminarDireccion(Cliente cliente, Direccion direccion);
	public List<Cliente> listarClientes();
	public List<Cliente> buscarPorNombreCliente(String nombre);
	public Cliente buscarPorIdCliente(Long id);
	public List<Cliente> buscarPorIdClienteInteger(Long id);
	public CalificacionGralClienteDTO calificacionGralCliente(Long id);
	public Direccion buscarDireccionPorId(Long id);
	public PedidoRDTO buscarultimo(Long id);

}
