package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;

public class ProductoPedidoAdapter  extends BaseAdapter {
    private static final String TAG = "ProductoPedidoAdapter";
    private Context context;
    private List<Object> productos;

    public ProductoPedidoAdapter(Context context, List<Object> list) {
        this.context = context;
        this.productos = list;
    }


    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int position) {
        return productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bmp = null;
        String nombre = null;
        String precio = null;

        if (productos.get(position) instanceof DtMenu){
            DtMenu dtp = (DtMenu) productos.get(position);
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getNombre();
            precio = context.getString(R.string.carr_symbol) + " " + dtp.getPrecioTotal();

        } else if (productos.get(position) instanceof DtPromocion){
            DtPromocion dtp = (DtPromocion) productos.get(position);
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getDescuento() + context.getString(R.string.carr_dto) +
                    " " + dtp.getNombre();
            precio = context.getString(R.string.carr_symbol) + " " + dtp.getPrecio();
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.producto_list_item, null);
        }

        ImageView menu_img = convertView.findViewById(R.id.producto_img);
        TextView menu_name = convertView.findViewById(R.id.producto_nombre);
        TextView menu_cantidad = convertView.findViewById(R.id.producto_cantidad);
        TextView menu_precio = convertView.findViewById(R.id.producto_precio);

        menu_img.setImageBitmap(bmp);

        if(nombre.length() > 17){
            nombre = nombre.substring(0, 17) + "...";
        }

        menu_name.setText(nombre);
        menu_cantidad.setText("");
        menu_precio.setText(precio);

        return convertView;


    }
}
