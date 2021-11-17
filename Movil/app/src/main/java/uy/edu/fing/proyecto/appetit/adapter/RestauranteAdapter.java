package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.MenuActivity;
import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.VerMenuActivity;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtRestaurante;

public class RestauranteAdapter  extends RecyclerView.Adapter<RestauranteAdapter.ViewHolder>{
    private static final String TAG = "RestauranteAdapter";
    private Context context;
    private List<DtRestaurante> restaurantes;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView rest_img;
        private final TextView rest_abierto;
        private final TextView rest_horario;
        private final TextView rest_restaurante;
        private final TextView rest_restaurante_cal;
        private final RatingBar rest_star;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            rest_img = view.findViewById(R.id.rest_img);
            rest_abierto = view.findViewById(R.id.rest_abierto);
            rest_horario = view.findViewById(R.id.rest_horario);
            rest_restaurante = view.findViewById(R.id.menu_restaurante);
            rest_restaurante_cal = view.findViewById(R.id.menu_restaurante_rating);
            rest_star = view.findViewById(R.id.menu_star);
        }

        public ImageView getRest_img() {
            return rest_img;
        }

        public TextView getRest_abierto() {
            return rest_abierto;
        }

        public TextView getRest_horario() {
            return rest_horario;
        }

        public TextView getRest_restaurante() {
            return rest_restaurante;
        }

        public TextView getRest_restaurante_cal() {
            return rest_restaurante_cal;
        }

        public RatingBar getRest_star() {
            return rest_star;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet List<DtProducto> containing the data to populate views to be used
     * by RecyclerView.
     */
    //public RestauranteAdapter(Context context, List<DtMenu> dataSet) {
    public RestauranteAdapter(Context context, List<DtRestaurante> dataSet) {
        this.context = context;
        this.restaurantes = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RestauranteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurante_row_item, viewGroup, false);

        return new RestauranteAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RestauranteAdapter.ViewHolder viewHolder, final int position) {
        DtRestaurante dtr = restaurantes.get(position);
        Bitmap bmp = BitmapFactory.decodeByteArray(dtr.getImagen(), 0, dtr.getImagen().length);;

        String abierto = dtr.getAbierto()?context.getString(R.string.res_abierto): context.getString(R.string.res_cerrado) ;
        String horario = dtr.getHorarioApertura() + " - "+ dtr.getHorarioCierre();

        ImageView image = viewHolder.getRest_img();
        image.setImageBitmap(bmp);
        viewHolder.getRest_restaurante().setText(dtr.getNombre());
        viewHolder.getRest_restaurante_cal().setText(dtr.getCalificacion().getGeneral().toString());
        viewHolder.getRest_star().setRating(5);


        viewHolder.getRest_horario().setText(horario);
        viewHolder.getRest_abierto().setText(abierto);

        Drawable progressDrawable = viewHolder.getRest_star().getProgressDrawable();


        switch (dtr.getCalificacion().getGeneral()) {
            case 0:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.white_trans));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.white_trans));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.white_trans)));
                break;
            case 1:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_1));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.star_1));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_1)));
                break;
            case 2:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_2));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.star_2));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_2)));
                break;
            case 3:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_3));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.star_3));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_3)));
                break;
            case 4:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_4));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.star_4));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_4)));
                break;
            case 5:
                DrawableCompat.setTint(progressDrawable, context.getColor(R.color.star_5));
                viewHolder.getRest_restaurante_cal().setTextColor(context.getColor(R.color.star_5));
                viewHolder.getRest_star().setProgressTintList(ColorStateList.valueOf(context.getColor(R.color.star_5)));
                break;
        }

        viewHolder.itemView.setOnClickListener(v -> {
            Intent ivmenu = new Intent(context, MenuActivity.class);
            context.startActivity(ivmenu);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

}
