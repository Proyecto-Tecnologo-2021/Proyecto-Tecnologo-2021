package proyecto2021G03.appettit.business;

import java.util.List;

import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.PromocionRDTO;
import proyecto2021G03.appettit.exception.AppettitException;

public interface IPromocionService {

    public List<PromocionDTO> listar() throws AppettitException;
    
    public List<PromocionRDTO> listarRPromocion() throws AppettitException;

    public PromocionDTO listarPorId(Long id, Long id_restaurante) throws AppettitException;

    public PromocionDTO editar(Long id, Long id_restaurnate, ProductoCrearDTO pcDTO) throws AppettitException;

    public void eliminar(Long id, Long id_restaurante) throws AppettitException;

    public List<PromocionDTO> listarPorRestaurante(Long id) throws AppettitException;

    public PromocionDTO crear(PromocionDTO pcDTO) throws AppettitException;

    public PromocionDTO editar(PromocionDTO ccDTO) throws AppettitException;

	public List<PromocionRDTO> listarPorPunto(String punto) throws AppettitException;
	public PromocionRDTO buscarPorId(Long id_restaurante, Long id) throws AppettitException;
	public List<PromocionRDTO> listarPorRestaurnateRest(Long id_restaurante) throws AppettitException;
}
