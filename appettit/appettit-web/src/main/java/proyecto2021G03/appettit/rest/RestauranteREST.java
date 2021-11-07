package proyecto2021G03.appettit.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@RequestScoped
@Path("/restaurante")
@Consumes("application/json")
@Produces("application/json")
public class RestauranteREST {
	
	@EJB
	IUsuarioService uService;
	
	@GET
	@Path("/listar/{id}")
	//@RecursoProtegidoJWT
	public Response listarPorId(@PathParam("id") Long id) {
		RespuestaREST<RestauranteDTO> respuesta = null;
		try {
			RestauranteDTO cliente = uService.buscarRestaurantePorId(id);
			respuesta = new RespuestaREST<RestauranteDTO>(true, "Restaurante encontrado!.", cliente);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<RestauranteDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}	
		
	}

}
