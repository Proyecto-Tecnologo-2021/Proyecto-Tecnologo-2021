package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;

public class DireccionPerfilAdapter extends BaseAdapter {
    private static final String TAG = "DireccionPerfilAdapter";
    private Context context;
    private List<DtDireccion> direcciones;

    public DireccionPerfilAdapter(Context context, List<DtDireccion> list) {
        this.context = context;
        this.direcciones = list;
    }


    @Override
    public int getCount() {
        return direcciones.size();
    }

    @Override
    public Object getItem(int position) {
        return direcciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DtDireccion dtd = direcciones.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.direccion_list_item, null);
        }

        TextView alias = convertView.findViewById(R.id.dir_alias);
        TextView calle = convertView.findViewById(R.id.dir_calle);
        TextView nro = convertView.findViewById(R.id.dir_nro);
        TextView apto = convertView.findViewById(R.id.dir_apto);
        TextView refer = convertView.findViewById(R.id.dir_ref);

        alias.setText(dtd.getAlias());
        calle.setText(dtd.getCalle());
        nro.setText(dtd.getNumero());
        apto.setText(dtd.getApartamento());
        refer.setText(dtd.getReferencias());

        return convertView;


    }
}
