package proyecto2021G03.appettit.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.business.ITokenService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

import java.net.URI;
import java.net.URL;

//import proyecto2021G03.appettit.security.RecursoProtegidoJWT;

@RequestScoped
@Path("/usuarios")
@Consumes("application/json")
@Produces("application/json")
public class UsuarioREST {

	@EJB
	IUsuarioService uService;
	@EJB
	IUsuarioDAO iUsuarioDAO;
	@EJB
	ITokenService iTokenService;


	
	@POST
	//@RecursoProtegidoJWT
	public Response crear(ClienteCrearDTO request) throws ParseException {
		RespuestaREST<ClienteMDTO> respuesta = null;
		try {
			ClienteMDTO cliente = uService.crearCliente(request);
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Cliente creado con éxito.", cliente);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteMDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				System.out.println(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@PUT
	@Path("/editarRespuestaExtendida/{id}")
	//@RecursoProtegidoJWT
	public Response editarRespuestaExtendida(@PathParam("id") Long id, ClienteModificarDTO request) {
		RespuestaREST<ClienteMDTO> respuesta = null;
		try {
			ClienteMDTO usuario = uService.editarClienteRE(id, request);
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Cliente editado con éxito.", usuario);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteMDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO || e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
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
		RespuestaREST<String> respuesta = null;
		try {
			String jwt = uService.editarCliente(id, request);
			respuesta = new RespuestaREST<String>(true, "Cliente editado con éxito.", jwt);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<String>(false, e.getLocalizedMessage());
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
	
	@PUT
	@Path("/eliminarDireccion/{id}")
	//@RecursoProtegidoJWT
	public Response eliminarDireccion(@PathParam("id") Long id, EliminarDeClienteDTO request) {
		RespuestaREST<ClienteDTO> respuesta = null;
		try {
			ClienteDTO usuario = uService.eliminarDireccion(id, request);
			respuesta = new RespuestaREST<ClienteDTO>(true, "Dirección eliminada con éxito.", usuario);
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
	
	@POST
	@Path("/loginMobile")
	public Response loginMobile(LoginDTO request) {
		RespuestaREST<ClienteMDTO> respuesta = null;
		try {
			ClienteMDTO clienteMDTO = uService.loginMobile(request);
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Inicio de sesión correcto.", clienteMDTO);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteMDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.DATOS_INCORRECTOS) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@POST
	@Path("/loginFireBase")
	public Response loginFireBase(LoginDTO request) {
		RespuestaREST<ClienteMDTO> respuesta = null;
		try {
			ClienteMDTO clienteMDTO = uService.loginFireBase(request);
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Inicio de sesión correcto.", clienteMDTO);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ClienteMDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.DATOS_INCORRECTOS) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	@GET
	@Path("/getAddressId")
	public Response getDireccionId(DirreccionAliasDTO request) {
		RespuestaREST<Long> respuesta = null;
		try {
			Long dirId = uService.obtenerIdDireccion(request.getUserId(), request.getAlias());

			respuesta = new RespuestaREST<Long>(true, "Id de la dirección obtenida con éxito.", dirId);
			return Response.ok(respuesta).build();

		} catch (AppettitException e) {
			respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
		}
	}

	@GET
	@Path("/getAddresses/{userId}")
	public Response getDireccionId(@PathParam("userId")Long userId) {
		RespuestaREST<List<DireccionDTO>> respuesta = null;
		try {

			List<DireccionDTO> direcciones = uService.obtenerDireccionesCliente(userId);

			respuesta = new RespuestaREST<List<DireccionDTO>>(true, "Lista de direcciones obtenida con éxito", direcciones);
			return Response.ok(respuesta).build();

		} catch (AppettitException e) {
			respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
		}
	}
	
	@GET
	@Path("/listarAbiertos")
	public Response listarAbiertos() {
		RespuestaREST<List<RestauranteRDTO>> respuesta = null;
		try {

			List<RestauranteRDTO> rests = uService.listarRestaurantesAbiertos();

			respuesta = new RespuestaREST<List<RestauranteRDTO>>(true, "Lista de restaurantes abiertos.", rests);
			return Response.ok(respuesta).build();

		} catch (AppettitException e) {
			respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
		}
	}

	@POST
	@Path("/requestMailLink/")
	public Response requestMailLink(MailDTO correo) {
		RespuestaREST respuesta = null;
		try {
			uService.solicitarCorreoVerificador(correo);
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Correo enviado correctamente.");
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.DATOS_INCORRECTOS) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	@GET
	@Path("/verifyMailLink/{token}")
	public Response verifyMailLink(@PathParam("token")String token) throws Exception {
		RespuestaREST respuesta = null;
		if(iTokenService.tokenVerificator(token)) {
			String id = iTokenService.tokenGetClaim(token,"idUsuario");
			String email = iTokenService.tokenGetClaim(token, "correo");
			//MANDAR AL LINK DE LA PAGINA DE RESETEO DE PASS CON LA DATA DEL USER
			String url ="http://localhost:3000/recover-pass"; // + id
			return Response.temporaryRedirect(URI.create(url)).build();
		} else {
			respuesta = new RespuestaREST<ClienteMDTO>(false, "Token no valido.");
			return Response.ok(respuesta).build();
		}
	}

	@POST
	@Path("/changePassword/")
	public Response requestMailLink(PassDTO password) {
		RespuestaREST respuesta = null;
		try {
			uService.cambioContraseña(password.getPassword());
			respuesta = new RespuestaREST<ClienteMDTO>(true, "Contraseña cambiada correctamente.");
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST(false, e.getLocalizedMessage());
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