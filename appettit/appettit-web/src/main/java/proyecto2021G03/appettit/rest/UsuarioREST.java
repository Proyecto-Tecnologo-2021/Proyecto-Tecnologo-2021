package proyecto2021G03.appettit.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.ClienteCrearDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.ClienteModificarDTO;
import proyecto2021G03.appettit.dto.DireccionCrearDTO;
import proyecto2021G03.appettit.dto.LoginDTO;
import proyecto2021G03.appettit.exception.AppettitException;
//import proyecto2021G03.appettit.security.RecursoProtegidoJWT;

@RequestScoped
@Path("/usuarios")
@Consumes("application/json")
@Produces("application/json")
public class UsuarioREST {

	@EJB
	IUsuarioService uService;
	
	/*@GET
	public Response listar() {
		RespuestaREST<List<CategoriaDTO>> respuesta = null;
		try {
			List<CategoriaDTO> categorias = cService.listar();
			respuesta = new RespuestaREST<List<CategoriaDTO>>(true, "Categorias listadas con éxito.", categorias);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
		}
	}
	
	*/
	
	@POST
	//@RecursoProtegidoJWT
	public Response crear(ClienteCrearDTO request) throws ParseException {
		RespuestaREST<ClienteDTO> respuesta = null;
		try {
			ClienteDTO cliente = uService.crearCliente(request);
			respuesta = new RespuestaREST<ClienteDTO>(true, "Cliente creado con éxito.", cliente);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@PUT
	@Path("/editar/{id}")
	//@RecursoProtegidoJWT
	public Response editar(@PathParam("id") Long id, ClienteModificarDTO request) {
		RespuestaREST<ClienteDTO> respuesta = null;
		try {
			ClienteDTO usuario = uService.editarCliente(id, request);
			respuesta = new RespuestaREST<ClienteDTO>(true, "Cliente editado con éxito.", usuario);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO || e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@POST
	@Path("/agregarDireccion")
	//@RecursoProtegidoJWT
	public Response agregarDireccion(DireccionCrearDTO request) {
		RespuestaREST<ClienteDTO> respuesta = null;
		try {
			ClienteDTO cliente = uService.agregarDireccion(request);
			respuesta = new RespuestaREST<ClienteDTO>(true, "Dirección agregada con éxito.", cliente);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@PUT
	@Path("/editarDireccion/{id}")
	//@RecursoProtegidoJWT
	public Response editarDireccion(@PathParam("id") Long id, DireccionCrearDTO request) {
		RespuestaREST<ClienteDTO> respuesta = null;
		try {
			ClienteDTO usuario = uService.editarDireccion(id, request);
			respuesta = new RespuestaREST<ClienteDTO>(true, "Dirección editada con éxito.", usuario);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO || e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@POST
	@Path("/login")
	public Response login(LoginDTO request) {
		RespuestaREST<String> respuesta = null;
		try {
			String token = uService.login(request);
			respuesta = new RespuestaREST<String>(true, "Inicio de sesión correcto.", token);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<String>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.DATOS_INCORRECTOS) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
/*
	@PUT
	@Path("/editar/{id}")
	//@RecursoProtegidoJWT
	public Response editar(@PathParam("id") Long id, CategoriaCrearDTO request) {
		RespuestaREST<CategoriaDTO> respuesta = null;
		try{
			CategoriaDTO categoria = cService.editar(id, request);
			respuesta = new RespuestaREST<CategoriaDTO>(true, "Categoria editada con éxito.", categoria);
			return Response.ok(respuesta).build();
		}catch (AppettitException e) {
			respuesta = new RespuestaREST<CategoriaDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO || e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@DELETE
	@Path("/eliminar/{id}")
	//@RecursoProtegidoJWT
	public Response eliminar(@PathParam("id") Long id) {
		RespuestaREST<CategoriaDTO> respuesta = null;
		try {
			cService.eliminar(id);
			respuesta = new RespuestaREST<CategoriaDTO>(true, "Categoria eliminada con éxito.");
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<CategoriaDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
*/
}