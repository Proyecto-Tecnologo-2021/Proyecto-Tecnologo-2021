package uy.edu.fing.proyecto.appetit.obj;

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

    @Builder.Default
    private List<DtMenu> menus = new ArrayList<DtMenu>();

    private DtPedido(){}

    public static synchronized DtPedido getInstance() {
        if(uInstance == null){
            uInstance = new DtPedido();
        }
        return uInstance;
    }
}


/*
{
    "idcli":10,
    "iddir":1,
    "menus":[{
"id": 2,
"id_restaurante": 9,
"nom_restaurante": "Como en Casa GlutenFree",
"descuento": 0.0,
"nombre": "Hamburguesa al pan",
"descripcion": "Hamburguesa al pan con lechuga tomate queso",
"precioSimple": 100.0,
"precioTotal": 100.0,
"extras": null,
"productos": null,
"id_imagen": null,
"imagen": null
}],
    "pago":true,
    "tipo":"EFECTIVO",
    "total":"500",
    "idrest":9,
    "fecha":null
}
 */