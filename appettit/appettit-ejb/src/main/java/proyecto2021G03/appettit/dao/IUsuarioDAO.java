package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.entity.Administrador;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Restaurante;
import proyecto2021G03.appettit.entity.Usuario;

@Local
public interface IUsuarioDAO {
	/*GENERAL*/
	public Usuario crear(Usuario usuario);
	public Usuario editar (Usuario usuario);
	public void eliminar (Usuario usuario);
	public List<Usuario> listar();
	public Usuario buscarPorId(Long id);
	public List<Usuario> buscarPorNombre(String nombre);
	
	public List<Usuario> buscarPorCorreo(String correo);
	public List<Usuario> buscarPorTelefono(String telefono);
	
	public Boolean existeCorreoTelefono(String correo, String telefono);

	/*ADMINISTRADOR*/
	public List<Administrador> listarAdministradores();
	public List<Administrador> buscarPorNombreAdministrador(String nombre);
	public Administrador crearAdministrador(Administrador administrador);
	
	/*RESTAURANTE*/
	public List<Restaurante> listarRestaurantes();
	public List<Restaurante> buscarPorNombreRestaurante(String nombre);
	public Restaurante crearRestaurante(Restaurante restaurante);
	public CalificacionRestauranteDTO calificcionRestaurante(RestauranteDTO restauranteDTO);
	public Restaurante buscarPorCorreoRestaurante(String correo);


	/*CLIENTE*/
	public Cliente crearCliente(Cliente cliente);
	public List<Cliente> listarClientes();
	public List<Cliente> buscarPorNombreCliente(String nombre);
	public List<Cliente> buscarPorIdCliente(String nombre);
	public CalificacionClienteDTO calificacionCliente(ClienteDTO clienteData);

}
