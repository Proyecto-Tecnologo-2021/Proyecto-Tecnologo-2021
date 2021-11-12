package uy.edu.fing.proyecto.appetit.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import uy.edu.fing.proyecto.appetit.R;
import uy.edu.fing.proyecto.appetit.VerMenuActivity;
import uy.edu.fing.proyecto.appetit.VerPedidoActivity;
import uy.edu.fing.proyecto.appetit.VerPedidoEchoActivity;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtVPedido;

public class PedidoAdapter  extends RecyclerView.Adapter<PedidoAdapter.ViewHolder>{
    private static final String TAG = "PedidoAdapter";
    private Context context;
    private List<DtVPedido> pedidos;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView pedido_id;
        private final TextView pedido_fecha;
        private final TextView pedido_estado;
        private final TextView pedido_total;
        private final TextView pedido_fp;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            pedido_id = view.findViewById(R.id.pedido_id);
            pedido_fecha = view.findViewById(R.id.pedido_fecha);
            pedido_estado = view.findViewById(R.id.pedido_estado);
            pedido_total = view.findViewById(R.id.pedido_total);
            pedido_fp = view.findViewById(R.id.pedido_fp);
        }

        public TextView getPedido_id() {
            return pedido_id;
        }

        public TextView getPedido_fecha() {
            return pedido_fecha;
        }

        public TextView getPedido_estado() {
            return pedido_estado;
        }

        public TextView getPedido_total() {
            return pedido_total;
        }

        public TextView getPedido_fp() {
            return pedido_fp;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet List<DtVPedido> containing the data to populate views to be used
     * by RecyclerView.
     */
    public PedidoAdapter(Context context, List<DtVPedido> dataSet) {
        this.context = context;
        this.pedidos = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pedido_row_item, viewGroup, false);

        return new PedidoAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PedidoAdapter.ViewHolder viewHolder, final int position) {
        
        DtVPedido dtp = pedidos.get(position);
        viewHolder.getPedido_id().setText(dtp.getId().toString());

        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        viewHolder.getPedido_fecha().setText(dateFormat.format(dtp.getFecha()));
        viewHolder.getPedido_fp().setText(dtp.getTipo().toString());
        viewHolder.getPedido_total().setText(context.getString(R.string.carr_symbol) + " " + dtp.getTotal());

        if (dtp.getEstado().equalsIgnoreCase("CONFIRMADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_3));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else if (dtp.getEstado().equalsIgnoreCase("RECHAZADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_1));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else if (dtp.getEstado().equalsIgnoreCase("ENVIADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_2));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else if (dtp.getEstado().equalsIgnoreCase("ENTREGADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_5));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else if (dtp.getEstado().equalsIgnoreCase("CANCELADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_1));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else if (dtp.getEstado().equalsIgnoreCase("SOLICITADO")){
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.star_3));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.white));

        } else{
            viewHolder.getPedido_estado().setBackgroundColor(context.getColor(R.color.white_trans));
            viewHolder.getPedido_estado().setTextColor(context.getColor(R.color.black));

        }

        viewHolder.itemView.setOnClickListener(v -> {
            //Toast.makeText(context, "Menu: " + dtp.getNombre(), Toast.LENGTH_LONG).show()
            Intent ivpedido = new Intent(context, VerPedidoEchoActivity.class);

            ivpedido.putExtra("id", dtp.getId());
            context.startActivity(ivpedido);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pedidos.size();
    }

}
