package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.ProductoConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dao.IProductoDAO;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.entity.Categoria;
import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class ProductoService implements IProductoService{

	@EJB
	public IProductoDAO pDAO;
	
	@EJB
	public ICategoriaDAO cDAO;
	
	@EJB
	public ProductoConverter pConverter;
	
	@Override
	public List<ProductoDTO> listar() throws AppettitException {
		try {
			return pConverter.fromEntity(pDAO.listar());
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}
	}

	@Override
	public ProductoDTO listarPorId(Long id) {
		return pConverter.fromEntity(pDAO.listarPorId(id));
	}

	@Override
	public ProductoDTO crear(ProductoCrearDTO pcDTO) throws AppettitException {
		
		//HACER EL CONTROL POR RESTAURANTE Y NO GENERAL
		if(existeNombreProducto(pcDTO.getNombre())) {
			throw new AppettitException("Ya existe un producto con ese nombre.", AppettitException.EXISTE_REGISTRO);
		}else {	
			try {
				Producto producto = pConverter.fromCrearDTO(pcDTO);
				return pConverter.fromEntity(pDAO.crear(producto));
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public ProductoDTO editar(Long id, ProductoCrearDTO pcDTO) throws AppettitException {
		
		//HACER EL CONTROL POR RESTAURANTE Y NO GENERAL
		if(existeNombreProductoExcluirId (id, pcDTO.getNombre())) {
			throw new AppettitException ("Ya existe un producto con ese nombre.", AppettitException.EXISTE_REGISTRO);
		}else {	
			try {
				Producto producto = pDAO.listarPorId(id);
				producto.setNombre(pcDTO.getNombre());
				Categoria categoria = cDAO.listarPorId(pcDTO.getId_categoria());
				producto.setCategoria(categoria);
				return pConverter.fromEntity(pDAO.editar(producto));
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	@Override
	public void eliminar(Long id) throws AppettitException {
		/* Se valida que exista el prodcuto */
		Producto producto= pDAO.listarPorId(id);
		if(producto == null) {
			throw new AppettitException("El producto indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
		} else {
			try {
				pDAO.eliminar(producto);
			} catch (Exception e) {
				throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
			}
		}
	}

	public boolean existeNombreProducto (String nombre) {
		
		List<ProductoDTO> productos = pConverter.fromEntity(pDAO.listar());
		for (ProductoDTO p: productos) {
			if (p.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existeNombreProductoExcluirId (Long id, String nombre) {
		
		List<ProductoDTO> productos = pConverter.fromEntity(pDAO.listar());
		for (ProductoDTO p: productos) {
			if (!p.getId().equals(id)) {
				if (p.getNombre().equals(nombre)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
