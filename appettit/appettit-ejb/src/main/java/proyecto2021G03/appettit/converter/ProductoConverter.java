package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.entity.Producto;

@Singleton
public class ProductoConverter extends AbstractConverter<Producto, ProductoDTO>{

	@EJB
	CategoriaConverter catConverter;
	
	@Override
	public ProductoDTO fromEntity(Producto p) {
		if(p == null) return null;
		return ProductoDTO.builder()
				.id(p.getId())
				.id_restaurante(p.getId_restaurante())
				.nombre(p.getNombre())
				.id_categoria(p.getId_categoria())
				.categoria(catConverter.fromEntity(p.getCategoria()))
				.build();
	}
	
	@Override
	public Producto fromDTO(ProductoDTO p) {
		if(p == null) return null;
		return Producto.builder()
				.id(p.getId())
				.id_restaurante(p.getId_restaurante())
				.nombre(p.getNombre())
				.id_categoria(p.getId_categoria())
				.categoria(catConverter.fromDTO(p.getCategoria()))
				.build();
	}
	
	public Producto fromCrearDTO(ProductoCrearDTO p) {
		if(p == null) return null;
		return Producto.builder()
			.nombre(p.getNombre())
			.id_restaurante(p.getId_restaurante())
			.id_categoria(p.getId_categoria())
			.build();
	}
	
}
