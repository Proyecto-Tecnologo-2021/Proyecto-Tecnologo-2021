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

import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.exception.AppettitException;
//import proyecto2021G03.appettit.security.RecursoProtegidoJWT;

@RequestScoped
@Path("/productos")
@Consumes("application/json")
@Produces("application/json")
public class ProductoREST {

	@EJB
	IProductoService pService;
	
	@GET
	public Response listar() {
		RespuestaREST<List<ProductoDTO>> respuesta = null;
		try {
			List<ProductoDTO> productos = pService.listar();
			respuesta = new RespuestaREST<List<ProductoDTO>>(true, "Productos listados con éxito.", productos);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
		}
	}
	
	
	@POST
	//@RecursoProtegidoJWT
	public Response crear(ProductoCrearDTO request) {
		RespuestaREST<ProductoDTO> respuesta = null;
		try {
			ProductoDTO producto = pService.crear(request);
			respuesta = new RespuestaREST<ProductoDTO>(true, "Producto creado con éxito.", producto);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ProductoDTO>(false, e.getLocalizedMessage());
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
	public Response editar(@PathParam("id") Long id, ProductoCrearDTO request) {
		RespuestaREST<ProductoDTO> respuesta = null;
		try{
			ProductoDTO producto = pService.editar(id, request);
			respuesta = new RespuestaREST<ProductoDTO>(true, "Producto editado con éxito.", producto);
			return Response.ok(respuesta).build();
		}catch (AppettitException e) {
			respuesta = new RespuestaREST<ProductoDTO>(false, e.getLocalizedMessage());
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
		RespuestaREST<ProductoDTO> respuesta = null;
		try {
			pService.eliminar(id);
			respuesta = new RespuestaREST<ProductoDTO>(true, "Producto eliminado con éxito.");
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ProductoDTO>(false, e.getLocalizedMessage());
			if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

}