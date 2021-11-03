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
    private Long iddir;
    private TipoPago tipo;
    private Boolean pago;
    private LocalDateTime fecha;
    private Double total;
    private Long idcli;
    private Long idrest;
    private List<MenuRDTO> menus;



    public List<MenuRDTO> filtroMenu(){
       //FILTRA SOLO LOS MENUS
        List<MenuRDTO> listafinal= new ArrayList<>();
        for(MenuRDTO menu: this.menus) {
            if (menu.getDescuento() == 0D) {
                listafinal.add(menu);
            }
        }
        return listafinal;
    }




    public List<MenuRDTO> filtroPromo(){
                //FILTRA SOLO LAS PROMOCIONES
        List<MenuRDTO> listafinal= new ArrayList<>();
        for(MenuRDTO menu: this.menus) {
            if (menu.getDescuento() != 0D) {
                listafinal.add(menu);
            }
        }
        return listafinal;
    }

}

