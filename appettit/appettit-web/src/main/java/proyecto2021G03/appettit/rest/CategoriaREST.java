package proyecto2021G03.appettit.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.exception.AppettitException;
//import proyecto2021G03.appettit.security.RecursoProtegidoJWT;

@RequestScoped
@Path("/categorias")
@Consumes("application/json")
@Produces("application/json")
public class CategoriaREST {

	@EJB
	ICategoriaService cService;
	
	@GET
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
	
	
	@POST
	//@RecursoProtegidoJWT
	public Response crear(CategoriaCrearDTO request) {
		RespuestaREST<CategoriaDTO> respuesta = null;
		try {
			CategoriaDTO categoria = cService.crear(request);
			respuesta = new RespuestaREST<CategoriaDTO>(true, "Categoria creada con éxito.", categoria);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<CategoriaDTO>(false, e.getLocalizedMessage());
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

}