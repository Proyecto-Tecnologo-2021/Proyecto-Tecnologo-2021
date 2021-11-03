package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private static final String TAG = "ProductAdapter";
    private Context context;
    private List<DtMenu> menus;

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

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            menu_img = view.findViewById(R.id.menu_img);
            menu_name = view.findViewById(R.id.menu_name);
            menu_detalle = view.findViewById(R.id.menu_detalle);
            menu_precio = view.findViewById(R.id.menu_precio);
            menu_restaurante = view.findViewById(R.id.menu_restaurante);
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
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet List<DtProducto> containing the data to populate views to be used
     * by RecyclerView.
     */
    public ProductAdapter(Context context, List<DtMenu> dataSet) {
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
        DtMenu dtp = menus.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Log.i(TAG, dtp.getImagen().toString());
        Bitmap bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
        ImageView image = viewHolder.getMenu_img();

        image.setImageBitmap(bmp);

        viewHolder.getMenu_name().setBackgroundColor(context.getColor(R.color.menu_bg_card_name));
        viewHolder.getMenu_name().setText(dtp.getNombre());
        viewHolder.getMenu_detalle().setText(dtp.getDescripcion());
        String precio = context.getString(R.string.carr_symbol) + " " + dtp.getPrecioTotal();
        viewHolder.getMenu_precio().setText(precio);
        viewHolder.getMenu_restaurante().setText(dtp.getNom_restaurante());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return menus.size();
    }
}