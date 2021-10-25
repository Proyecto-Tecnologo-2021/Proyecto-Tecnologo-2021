package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IProductoService {

	public ProductoDTO crear(ProductoDTO productoDTO) throws AppettitException;
	public List<ProductoDTO> listar() throws AppettitException;
	public ProductoDTO listarPorId(Long id) throws AppettitException;
	public ProductoDTO editar(Long id, ProductoCrearDTO ccDTO)throws AppettitException;
	public void eliminar(Long id)throws AppettitException;
	
	public List<ProductoDTO> listarPorRestaurante(Long id) throws AppettitException;
	
}