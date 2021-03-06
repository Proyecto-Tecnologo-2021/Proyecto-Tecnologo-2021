package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IUsuarioService {
	/* GENERAL*/
	//public void eliminar(Long id) throws AppettitException;
	//public List<UsuarioDTO> listar() throws AppettitException;
	//public UsuarioDTO buscarPorId(Long id) throws AppettitException;
	//public List<UsuarioDTO> buscarPorNombre(String nombre) throws AppettitException;
	public String login(LoginDTO loginDTO) throws AppettitException;
	public ClienteMDTO loginMobile(LoginDTO loginDTO) throws AppettitException;
	public ClienteMDTO loginFireBase(LoginDTO loginDTO) throws AppettitException;
	String loginGoogle(String correo, String nombre) throws AppettitException;
	
	/*ADMINISTRADOR*/
	public AdministradorDTO crear(AdministradorDTO administradorDTO) throws AppettitException;
	public AdministradorDTO buscarAdministradorPorId(Long id) throws AppettitException;
	public List<AdministradorDTO> listarAdminsitradores() throws AppettitException;
	public List<AdministradorDTO> buscarPorNombreAdministrador(String nombre) throws AppettitException;
	
	/*RESTAURANTE*/
	public RestauranteDTO crearRestaurante(RestauranteDTO restauranteDTO) throws AppettitException;
	public RestauranteDTO buscarRestaurantePorId(Long id) throws AppettitException;
	public RestauranteRDTO buscarRestaurantePorIdBasico(Long id) throws AppettitException;
	public List<RestauranteDTO> listarRestaurantes() throws AppettitException;
	public List<RestauranteRDTO> listarRestaurantesAbiertos() throws AppettitException;
	public List<RestauranteDTO> buscarPorNombreRestaurante(String nombre) throws AppettitException;
	public RestauranteDTO buscarPorCorreoRestaurante(String correo) throws AppettitException;
	public CalificacionGralRestauranteDTO calificacionRestaurante(Long id) throws AppettitException;
	public RestauranteDTO editarRestaurante(RestauranteDTO restauranteDTO) throws AppettitException;
	public RestauranteDTO abrirRestaurante(Long id) throws AppettitException;
	public RestauranteDTO cerrarRestaurante(Long id) throws AppettitException;
	//public RestauranteRDTO buscarPorId(Long id) throws AppettitException;
	public List<RestauranteRDTO> listarRestaurantesPorPunto(String punto) throws AppettitException;
	

	/*CLIENTE*/
	public ClienteMDTO crearCliente(ClienteCrearDTO clienteData) throws AppettitException, ParseException;
	public String editarCliente(Long id, ClienteModificarDTO clienteData) throws AppettitException;
	public ClienteMDTO editarClienteRE(Long id, ClienteModificarDTO clienteData) throws AppettitException;
	public void bloquearCliente(Long id) throws AppettitException;
	public void desbloquearCliente(Long id) throws AppettitException;
	public ClienteDTO editarDireccion(Long id, DireccionCrearDTO direccionDTO) throws AppettitException;
	public ClienteDTO eliminarDireccion(Long id_direccion, EliminarDeClienteDTO ec) throws AppettitException;
	public List<ClienteDTO> listarClientes() throws AppettitException;
	public List<ClienteDTO> buscarPorNombreCliente(String nombre) throws AppettitException;
	public ClienteDTO buscarPorIdCliente(Long id) throws AppettitException;
	public CalificacionGralClienteDTO calificacionGralCliente(Long id) throws AppettitException;
	public ClienteDTO agregarDireccion(DireccionCrearDTO direccion) throws AppettitException;
	public Long obtenerIdDireccion(Long idUser, String alias) throws AppettitException;
	public List<DireccionDTO> obtenerDireccionesCliente(Long idUser) throws AppettitException;
	public DireccionDTO buscarDireccionPorId(Long id) throws AppettitException;
	void solicitarCorreoVerificador(MailDTO correo) throws AppettitException;
	void cambioContrase??a(String password, Long clientId) throws AppettitException;
	void setFirebaseTokenWeb(String webToken, Long clientId) throws AppettitException;
	ClienteDTO buscarPorCorreoCliente(String correo) throws AppettitException;
	Boolean existeCorreoUsuario(String correo);
	
	

}
