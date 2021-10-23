package proyecto2021G03.appettit.rest;

import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/menu")
@Consumes("application/json")
@Produces("application/json")
public class MenuREST {

    @EJB
    IMenuService iMenuService;

    @GET

    public Response listar() {
        RespuestaREST<List<MenuDTO>> respuesta = null;
        try {
            List<MenuDTO> menuDTOS = iMenuService.listar();
            respuesta = new RespuestaREST<List<MenuDTO>>(true, "Menues listadas con éxito.", menuDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }




}
