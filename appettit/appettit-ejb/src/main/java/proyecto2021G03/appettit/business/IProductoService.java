package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.entity.Producto;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IProductoService {

	public List<ProductoDTO> listar() throws AppettitException;
	public Producto listarPorId(Long id);
	public ProductoDTO crear(ProductoCrearDTO ccDTO)throws AppettitException;
	public ProductoDTO editar(Long id, ProductoCrearDTO ccDTO)throws AppettitException;
	public void eliminar(Long id)throws AppettitException;
}