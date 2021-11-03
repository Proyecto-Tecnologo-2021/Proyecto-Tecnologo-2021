package uy.edu.fing.proyecto.appetit.obj;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtPedido {
    private Long idcli;
    private Long iddir;
    private Long idrest;
    private Boolean pago;
    private ETipoPago tipo;
    private Double total;
    private Date fecha;
    private List<DtMenu> menus;
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