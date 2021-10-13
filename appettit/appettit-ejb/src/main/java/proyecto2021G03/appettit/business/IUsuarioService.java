package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IUsuarioService {
	/* GENERAL*/
	public void eliminar(Long id) throws AppettitException;
	public List<UsuarioDTO> listar() throws AppettitException;
	public UsuarioDTO buscarPorId(Long id) throws AppettitException;
	public List<UsuarioDTO> buscarPorNombre(String nombre) throws AppettitException;
	
	
	/*ADMINISTRADOR*/
	public AdministradorDTO crear(AdministradorDTO administradorDTO) throws AppettitException;
	public List<AdministradorDTO> listarAdminsitradores() throws AppettitException;
	public List<AdministradorDTO> buscarPorNombreAdministrador(String nombre) throws AppettitException;
	
	/*RESTAURANTE*/
	public RestauranteDTO crearRestaurante(RestauranteDTO restauranteDTO) throws AppettitException;
	public List<RestauranteDTO> listarRestaurantes() throws AppettitException;
	public List<RestauranteDTO> buscarPorNombreRestaurante(String nombre) throws AppettitException;
	
	
	
	

}
