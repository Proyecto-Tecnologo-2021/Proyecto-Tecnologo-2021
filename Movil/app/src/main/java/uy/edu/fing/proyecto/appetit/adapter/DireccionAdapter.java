package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;

public class DireccionAdapter extends ArrayAdapter<DtDireccion> {
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<DtDireccion> direcciones;



    public DireccionAdapter(Context context, int textViewResourceId,
                       List<DtDireccion> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.direcciones = values;
    }

    @Override
    public int getCount(){
        return direcciones.size();
    }

    @Override
    public DtDireccion getItem(int position){
        return direcciones.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);

        String detalle = direcciones.get(position).getAlias() +" - " +
                direcciones.get(position).getCalle() + " " +
                direcciones.get(position).getNumero() ;

        if(!direcciones.get(position).getApartamento().equals(""))
            detalle += ", " + context.getString(R.string.map_text_apto) + direcciones.get(position).getApartamento();

        label.setText(detalle);

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        //label.setTextColor(Color.BLACK);

        String detalle = direcciones.get(position).getAlias() +" - " +
                direcciones.get(position).getCalle() + " " +
                direcciones.get(position).getNumero() ;

        if(!direcciones.get(position).getApartamento().equals(""))
            detalle += ", " + context.getString(R.string.map_text_apto) + direcciones.get(position).getApartamento();

        label.setText(detalle);

        return label;
    }


}
