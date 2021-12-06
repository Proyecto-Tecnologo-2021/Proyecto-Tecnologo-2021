package proyecto2021G03.appettit.rest;

import java.util.List;

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
import proyecto2021G03.appettit.dto.RestauranteRDTO;
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
	public Response buscarRestaurantePorId(@PathParam("id") Long id) {
		RespuestaREST<RestauranteDTO> respuesta = null;
		try {
			RestauranteDTO cliente = uService.buscarRestaurantePorId(id);
			respuesta = new RespuestaREST<RestauranteDTO>(true, "Dirección agregada con éxito.", cliente);
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
	
	@GET
	@Path("/listarDatosBasicos/{id}")
	//@RecursoProtegidoJWT
	public Response buscarRestaurantePorIdBasico(@PathParam("id") Long id) {
		RespuestaREST<RestauranteRDTO> respuesta = null;
		try {
			RestauranteRDTO restaurante = uService.buscarRestaurantePorIdBasico(id);
			respuesta = new RespuestaREST<RestauranteRDTO>(true, "Datos basicos del restaurante.", restaurante);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<RestauranteRDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}	
		
	}
	
	@GET
    @Path("/getZona/{punto}")
    public Response listarZonaReparto(@PathParam("punto") String punto) {
    	RespuestaREST<List<RestauranteRDTO>> respuesta = null;
        try {
            List<RestauranteRDTO> restaurantesDTOS = uService.listarRestaurantesPorPunto(punto);
            respuesta = new RespuestaREST<List<RestauranteRDTO>>(true, "Restaurantes listados con éxito.", restaurantesDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }
	

}
