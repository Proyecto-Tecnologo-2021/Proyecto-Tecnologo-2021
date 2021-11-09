
package proyecto2021G03.appettit.rest;

import proyecto2021G03.appettit.business.IMenuRService;
import proyecto2021G03.appettit.business.IPromocionService;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.MenuRDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/promo")
@Consumes("application/json")
@Produces("application/json")
public class PromocionREST {
    @EJB

    IPromocionService iPromocionService;
    IMenuRService iMenuRService;

    @GET
    public Response listar() {
        RespuestaREST<List<PromocionDTO>> respuesta = null;
        try {
            List<PromocionDTO> promocionDTOS = iPromocionService.listar();
            respuesta = new RespuestaREST<List<PromocionDTO>>(true, "Promociones listadas con éxito.", promocionDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }

    @GET
    @Path("/getZona/{punto}")
    public Response listarZonareparto(@PathParam("punto") String punto) {
        RespuestaREST<List<PromocionRDTO>> respuesta = null;
        try {
            List<PromocionRDTO> promocionDTOS = iPromocionService.listarPorPunto(punto);
            respuesta = new RespuestaREST<List<PromocionRDTO>>(true, "Promociones listadas con éxito.", promocionDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }

    
    @GET
    @Path("/getPromo/{id_restaurante}/{id}")
    public Response listarPorId(@PathParam("id_restaurante") Long id_restaurante, @PathParam("id") Long id) {
		RespuestaREST<PromocionRDTO> respuesta = null;
		try {
			PromocionRDTO promocionDTOS = iPromocionService.buscarPorId(id_restaurante, id);
            respuesta = new RespuestaREST<PromocionRDTO>(true, "Promoción listado con éxito.", promocionDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
	}

    @GET
    @Path("/getPromo/{id_restaurante}")
    public Response listarPorRestaurante(@PathParam("id_restaurante") Long id_restaurante) {
    	RespuestaREST<List<PromocionRDTO>> respuesta = null;
		try {
			List<PromocionRDTO> promocionDTOS = iPromocionService.listarPorRestaurnateRest(id_restaurante);
            respuesta = new RespuestaREST<List<PromocionRDTO>>(true, "Promociones listadas con éxito.", promocionDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
	}

    @GET
    @Path("/promoRDTO}")
    public Response listarPromoR() {
        RespuestaREST<List<PromocionRDTO>> respuesta = null;
        try {
            List<PromocionRDTO> promoRDTO = iPromocionService.listarRPromocion();
            respuesta = new RespuestaREST<List<PromocionRDTO>>(true, "Menues listadas con éxito.", promoRDTO);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }
}
