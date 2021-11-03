package proyecto2021G03.appettit.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import proyecto2021G03.appettit.business.IMenuRService;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@RequestScoped
@Path("/menu")
@Consumes("application/json")
@Produces("application/json")
public class MenuREST {

    @EJB
    IMenuRService iMenuRService;

    @GET

    public Response listar() {
        RespuestaREST<List<MenuRDTO>> respuesta = null;
        try {
            List<MenuRDTO> menuDTOS = iMenuRService.listar();
            respuesta = new RespuestaREST<List<MenuRDTO>>(true, "Menues listadas con éxito.", menuDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }


    @GET
	@Path("/getMenu")
  //@RecursoProtegidoJWT
	public Response listarPorId(MenuRDTO request) {
		RespuestaREST<MenuRDTO> respuesta = null;
		try {
            MenuRDTO menuDTOS = iMenuRService.listarPorId(request.getId_restaurante(), request.getId());
            respuesta = new RespuestaREST<MenuRDTO>(true, "Menú listado con éxito.", menuDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
	}


}
