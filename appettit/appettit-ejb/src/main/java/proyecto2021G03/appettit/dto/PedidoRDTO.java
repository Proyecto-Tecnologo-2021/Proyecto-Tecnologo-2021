package proyecto2021G03.appettit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRDTO {
	private Long id;
	private EstadoPedido estado;
    private Long iddir;
    private TipoPago tipo;
    private Boolean pago;
    private LocalDateTime fecha;
    private Double total;
    private Long idcli;
    private Long idrest;
    private List<MenuRDTO> menus;
    private List<ExtraMenuRDTO> extras;
    private String id_paypal;
    private Double cotizacion;


    public List<MenuRDTO> filtroMenu(){
       //FILTRA SOLO LOS MENUS
        List<MenuRDTO> listafinal= new ArrayList<MenuRDTO>();
        for(MenuRDTO menu: this.menus) {
        	if(menu.getTipo().equalsIgnoreCase("MENU")) {
        		listafinal.add(menu);
        	}
        }
        return listafinal;
    }


    public List<PromocionRDTO> filtroPromo(){
    	List<PromocionRDTO> listafinal = new ArrayList<PromocionRDTO>();
    	for(MenuRDTO menu: this.menus) {
        	if(menu.getTipo().equalsIgnoreCase("PROM")) {
        		listafinal.add(new PromocionRDTO(menu.getId(), menu.getNombre(), menu.getId_restaurante(), 
        				menu.getNom_restaurante(), null, null, null, null, null, null, null, null));
        	}
        }
        return listafinal;
    }

}

