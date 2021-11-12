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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtVPedido {
    private Long id;
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
    private String estado;
    private List<Object> menus = new ArrayList<Object>();

}