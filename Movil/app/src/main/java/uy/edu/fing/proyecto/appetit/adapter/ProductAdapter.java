package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.MenuActivity;
import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.VerMenuActivity;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private static final String TAG = "ProductAdapter";
    private Context context;
    //private List<DtMenu> menus;
    private List<Object> menus;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView menu_img;
        private final TextView menu_name;
        private final TextView menu_detalle;
        private final TextView menu_precio;
        private final TextView menu_restaurante;
        private final TextView menu_restaurante_cal;
        private final RatingBar menu_star;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            menu_img = view.findViewById(R.id.menu_img);
            menu_name = view.findViewById(R.id.menu_name);
            menu_detalle = view.findViewById(R.id.menu_detalle);
            menu_precio = view.findViewById(R.id.menu_precio);
            menu_restaurante = view.findViewById(R.id.menu_restaurante);
            menu_restaurante_cal = view.findViewById(R.id.menu_restaurante_rating);
            menu_star = view.findViewById(R.id.menu_star);
        }

        public ImageView getMenu_img() {
            return menu_img;
        }

        public TextView getMenu_name() {
            return menu_name;
        }

        public TextView getMenu_detalle() {
            return menu_detalle;
        }

        public TextView getMenu_precio() {
            return menu_precio;
        }

        public TextView getMenu_restaurante() {
            return menu_restaurante;
        }

        public TextView getMenu_restaurante_cal() {
            return menu_restaurante_cal;
        }

        public RatingBar getMenu_star() {
            return menu_star;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet List<DtProducto> containing the data to populate views to be used
     * by RecyclerView.
     */
    //public ProductAdapter(Context context, List<DtMenu> dataSet) {
    public ProductAdapter(Context context, List<Object> dataSet) {
        this.context = context;
        this.menus = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Bitmap bmp = null;
        String nombre = null;
        String precio = null;
        String detalle = null;
        String restaurante = null;
        Integer calificacion = null;
        Long id = null;
        Long id_restaurante = null;
        String tipo = null;



        if (menus.get(position) instanceof DtMenu){
            DtMenu dtp = (DtMenu) menus.get(position);
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getNombre();
            precio = context.getString(R.string.carr_symbol) + " " + dtp.getPrecioTotal();
            detalle = dtp.getDescripcion();
            restaurante = dtp.getNom_restaurante();
            id = dtp.getId();
            id_restaurante = dtp.getId_restaurante();
            tipo = "M";
            calificacion = dtp.getCalificacion();

        } else if (menus.get(position) instanceof DtPromocion){
            DtPromocion dtp = (DtPromocion) menus.get(position);
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getDescuento() + context.getString(R.string.carr_dto) +
                    " " + dtp.getNombre();
            precio = context.getString(R.string.carr_symbol) + " " + dtp.getPrecio();
            detalle = dtp.getDescripcion();
            restaurante = dtp.getNom_restaurante();
            id = dtp.getId();
            id_restaurante = dtp.getId_restaurante();
            tipo = "P";
            calificacion = dtp.getCalificacion();
        }


        ImageView image = viewHolder.getMenu_img();
        image.setImageBitmap(bmp);

        viewHolder.getMenu_name().setBackgroundColor(context.getColor(R.color.menu_bg_card_name));
        viewHolder.getMenu_name().setText(nombre);

        viewHolder.getMenu_detalle().setText(detalle);
        viewHolder.getMenu_precio().setText(precio);
        viewHolder.getMenu_restaurante().setText(restaurante);
        viewHolder.getMenu_restaurante_cal().setText(calificacion.toString());

        viewHolder.getMenu_star().setRating(5);

        Drawable progressDrawable = viewHolder.getMenu_star().getProgressDrawable();


        switch (calificacion) {
            case 0:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.white_trans));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.white_trans));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.white_trans)));
                break;
            case 1:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_1));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.star_1));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_1)));
                break;
            case 2:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_2));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.star_2));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_2)));
                break;
            case 3:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_3));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.star_3));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_3)));
                break;
            case 4:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_4));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.star_4));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_4)));
                break;
            case 5:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_5));
                viewHolder.getMenu_restaurante_cal().setTextColor(context.getColor(R.color.star_5));
                viewHolder.getMenu_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_5)));
                break;
        }

        Long finalId = id;
        Long finalId_restaurante = id_restaurante;
        String finalTipo = tipo;

        viewHolder.itemView.setOnClickListener(v -> {
            //Toast.makeText(context, "Menu: " + dtp.getNombre(), Toast.LENGTH_LONG).show()
            Intent ivmenu = new Intent(context, VerMenuActivity.class);

            ivmenu.putExtra("id", finalId);
            ivmenu.putExtra("id_restaurante", finalId_restaurante);
            ivmenu.putExtra("tipo", finalTipo);
            context.startActivity(ivmenu);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return menus.size();
    }
}
