package uy.edu.fing.proyecto.appetit.obj;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DtPedido {
    private static DtPedido uInstance= null;
    private Long idcli;
    private Long iddir;
    private Long idrest;
    private Boolean pago;
    private ETipoPago tipo;
    private Double total;
    private Date fecha;
    private String geometry;
    private DtCotizacion cotizacion;
    private String id_paypal;
    private String res_nombre;

    private List<Object> menus = new ArrayList<Object>();

    private DtPedido(){}

    public static synchronized DtPedido getInstance() {
        if(uInstance == null){
            uInstance = new DtPedido();
        }
        return uInstance;
    }

    public void addMenu(Object producto){
        menus.add(producto);
    }

    public void remMenu(int index){
        menus.remove(index);
    }

    public Double getTotal(){
        Double ret = 0.0;
        for (int t = 0; t < menus.size(); t++){
            if (menus.get(t) instanceof DtMenu){
                ret += ((DtMenu) menus.get(t)).getPrecioTotal();
            } else if (menus.get(t) instanceof DtPromocion){
                ret += ((DtPromocion) menus.get(t)).getPrecio();
            }
        }

        return ret;
    }

}