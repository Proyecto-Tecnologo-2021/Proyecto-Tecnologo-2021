package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Promocion;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IPromocionDAO {

    public List<Promocion> listar();

    public Promocion listarPorId(Long id, Long id_restaurante);

    public Promocion crear(Promocion promo);

    public Promocion editar(Promocion promo);

    public void eliminar(Promocion promo);

    public List<Promocion> listarPorRestaurante(Long id);
    
    public Promocion buscarPorId(Long id_restaurante, Long id);

}
