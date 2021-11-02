package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

public interface IPromocionService {

    public List<PromocionDTO> listar() throws AppettitException;
    
    public List<PromocionRDTO> listarRPromocion() throws AppettitException;

    public PromocionDTO listarPorId(Long id) throws AppettitException;

    public PromocionDTO editar(Long id, ProductoCrearDTO pcDTO) throws AppettitException;

    public void eliminar(Long id) throws AppettitException;

    public List<PromocionDTO> listarPorRestaurante(Long id) throws AppettitException;

    public PromocionDTO crear(PromocionDTO pcDTO) throws AppettitException;

    public PromocionDTO editar(PromocionDTO ccDTO) throws AppettitException;
}
