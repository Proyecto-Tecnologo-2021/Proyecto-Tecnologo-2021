package proyecto2021G03.appettit.converter;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IGeoService;
import proyecto2021G03.appettit.dto.DireccionCrearDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.DireccionRDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.entity.Direccion;
import proyecto2021G03.appettit.exception.AppettitException;

@Singleton
public class DireccionConverter extends AbstractConverter<Direccion, DireccionDTO> {

	@EJB
	public LocalidadConverter localidadConverter;
	
	@EJB
	public IGeoService gService;
	
	@EJB
	public IDepartamentoService deptoSrv;
	
	@Override
	public DireccionDTO fromEntity(Direccion e) {
		if(e == null) return null;
		return DireccionDTO.builder()
				.id(e.getId())
				.alias(e.getAlias())
				.calle(e.getCalle())
				.numero(e.getNumero())
				.apartamento(e.getApartamento())
				.referencias(e.getReferencias())
				.barrio(localidadConverter.fromEntity(e.getBarrio()))
				.geometry(e.getGeometry())
				.build();
	}

	@Override
	public Direccion fromDTO(DireccionDTO d) {
		if(d == null) return null;
		return Direccion.builder()
				.id(d.getId())
				.alias(d.getAlias())
				.calle(d.getCalle())
				.numero(d.getNumero())
				.apartamento(d.getApartamento())
				.referencias(d.getReferencias())
				.barrio(localidadConverter.fromDTO(d.getBarrio()))
				.geometry(d.getGeometry())
				.build();
	}

	public Direccion fromCrearDTO(DireccionCrearDTO d) throws AppettitException, ParseException {
		if(d == null) return null;
		
		LocalidadDTO barrio = gService.localidadPorPunto(d.getGeometry());
		
		return Direccion.builder()
				.alias(d.getAlias())
				.calle(d.getCalle())
				.numero(d.getNumero())
				.apartamento(d.getApartamento())
				.barrio(localidadConverter.fromDTO(barrio))
				.geometry(d.getGeometry())
				.build();
	}
	
	public DireccionRDTO fromEntityToRDTO(Direccion e) {
		if(e == null) return null;
		
		return DireccionRDTO.builder()
				.id(e.getId())
				.alias(e.getAlias())
				.calle(e.getCalle())
				.numero(e.getNumero())
				.apartamento(e.getApartamento())
				.referencias(e.getReferencias())
				.barrio(e.getBarrio().getNombre())
				.geometry(e.getGeometry())
				.build();		
	}
	
}
