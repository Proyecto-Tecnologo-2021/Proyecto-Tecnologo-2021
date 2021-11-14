package uy.edu.fing.proyecto.appetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.PedidoAdapter;
import uy.edu.fing.proyecto.appetit.adapter.ProductoPedidoAdapter;
import uy.edu.fing.proyecto.appetit.adapter.ProductoVPedidoAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtCotizacion;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtExtraMenu;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtRCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtRestaurante;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;
import uy.edu.fing.proyecto.appetit.obj.DtVPedido;
import uy.edu.fing.proyecto.appetit.obj.ETipoPago;

public class VerPedidoHechoActivity extends AppCompatActivity {
    private static final String TAG = "VerPedidoHechoActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    Long id_restaurante;
    Long id_pedido;


    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    ImageView rest_img;
    TextView rest_name;
    TextView rest_cal;
    RatingBar rest_star;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        id_pedido = bundle.getLong("id");
        id_restaurante = bundle.getLong("id_restaurante");

        setContentView(R.layout.activity_ver_pedido_hecho);

        buscarPedidos();
        buscarRestaurnate();

        progressBar = findViewById(R.id.vpBarPedidos);
        rest_img = findViewById(R.id.vrestaurante_img);
        rest_name = findViewById(R.id.vrestaurante_name);
        rest_star = findViewById(R.id.vrestaurante_star);
        rest_cal = findViewById(R.id.vrestaurante_rating);

        progressBar.setVisibility(View.VISIBLE);


        bottomNavigationView = findViewById(R.id.bottomNavViewMenu);

        bottomNavigationView.setSelectedItemId(R.id.menu_pedido);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.menu_menus:
                    Intent imenu = new Intent(VerPedidoHechoActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_rest:
                    return true;
                case R.id.menu_pedido:
                    Intent ipedidos = new Intent(VerPedidoHechoActivity.this, VerPedidosActivity.class);
                    startActivity(ipedidos);
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ver_pedido_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pmenu_ver:
                Intent ipedido = new Intent(VerPedidoHechoActivity.this, VerPedidoHechoActivity.class);
                startActivity(ipedido);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void buscarPedidos() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETPEDIDOID_URL;
        stringUrl = stringUrl.replace("{id}", id_pedido.toString());

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerPedidoHechoActivity.DownloadPedidoTask().execute(stringUrl);
        } else {
        }
    }

    private class DownloadPedidoTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return PedidoInfoGralUrl(urls[0]);
            } catch (IOException | ParseException e) {
                return getString(R.string.err_recuperarpag);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result instanceof DtResponse) {
                DtResponse response = (DtResponse) result;
                //Log.i(TAG, "onPostExecute:" + response.getMensaje());
                if (response.getOk()) {
                    DtVPedido pedido = (DtVPedido) response.getCuerpo();
                    addPedido(pedido);
                } else {
                    if (ContextCompat.checkSelfPermission(VerPedidoHechoActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VerPedidoHechoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(VerPedidoHechoActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();
                    }
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerPedidoHechoActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    private DtResponse PedidoInfoGralUrl(String myurl) throws IOException, ParseException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            //String authorization ="Bearer  " + usuario.getToken();

            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            //conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            //conn.setDoOutput(true);
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            //Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readInfoGralJsonStream(is, "GETP");
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "GETP");
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "GETP");
            } else {
                return new DtResponse(false, response + " - " + conn.getResponseMessage(), null);
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }
    public DtResponse readGETPMessage(JsonReader reader) throws IOException, ParseException {
        Boolean ok = false;
        String mensaje = null;
        DtResponse res = null;
        DtVPedido pedido = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                pedido = readPedido(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, pedido);

    }

    public DtVPedido readPedido(JsonReader reader) throws IOException, ParseException {
        Long id = null;
        Long idcli = null;
        Long iddir = null;
        Long idrest = null;
        Boolean pago = true;
        String stipo;
        ETipoPago tipo = null;
        Double total = null;
        Date fecha = null;
        String geometry = null;
        DtCotizacion cotizacion = null;
        String id_paypal = null;
        String estado = null;
        List<Object> menus = null;
        DtRCalificacion calificacion = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("idcli") && reader.peek() != JsonToken.NULL) {
                idcli = reader.nextLong();
            } else if (name.equals("iddir") && reader.peek() != JsonToken.NULL) {
                iddir = reader.nextLong();
            } else if (name.equals("idrest") && reader.peek() != JsonToken.NULL) {
                idrest = reader.nextLong();
            } else if (name.equals("pago") && reader.peek() != JsonToken.NULL) {
                pago = reader.nextBoolean();
            } else if (name.equals("tipo") && reader.peek() != JsonToken.NULL) {
                stipo = reader.nextString();
                if(stipo.equalsIgnoreCase("EFECTIVO")){
                    tipo = ETipoPago.EFECTIVO;
                } else if(stipo.equalsIgnoreCase("PAYPAL")){
                    tipo = ETipoPago.PAYPAL;
                }
            } else if (name.equals("total") && reader.peek() != JsonToken.NULL) {
                total = reader.nextDouble();
            } else if (name.equals("fecha") && reader.peek() != JsonToken.NULL) {
                fecha = readFecha(reader);
            } else if (name.equals("geometry") && reader.peek() != JsonToken.NULL) {
                geometry = reader.nextString();
            } else if (name.equals("cotizacion") && reader.peek() != JsonToken.NULL) {
                cotizacion = new DtCotizacion("USD", 1d, reader.nextDouble());
            } else if (name.equals("id_paypal") && reader.peek() != JsonToken.NULL) {
                id_paypal = reader.nextString();
            } else if (name.equals("estado") && reader.peek() != JsonToken.NULL) {
                estado = reader.nextString();
            } else if (name.equals("estado") && reader.peek() != JsonToken.NULL) {
                estado = reader.nextString();
            } else if (name.equals("menus") && reader.peek() != JsonToken.NULL) {
                menus = readMenusArray(reader);
            } else if (name.equals("calificacion") && reader.peek() != JsonToken.NULL) {
                calificacion = readCalificacionObj(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtVPedido(id, idcli, iddir, idrest, pago, tipo, total, fecha, geometry, cotizacion, id_paypal, estado, menus, calificacion);
    }

    public Date readFecha(JsonReader reader) throws IOException, ParseException {
        Date fecha = null;
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer min = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("year") && reader.peek() != JsonToken.NULL) {
                year = reader.nextInt();
            } else if (name.equals("monthValue") && reader.peek() != JsonToken.NULL) {
                month = reader.nextInt();
            } else if (name.equals("dayOfMonth") && reader.peek() != JsonToken.NULL) {
                day = reader.nextInt();
            } else if (name.equals("hour") && reader.peek() != JsonToken.NULL) {
                hour = reader.nextInt();
            } else if (name.equals("minute") && reader.peek() != JsonToken.NULL) {
                min = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(year != null){
            String sDate= year + (month<10?"0":"") + month +
                    (day<10?"0":"") + day + " " +
                    (hour<10?"0":"") + hour +":" +
                    (min<10?"0":"") + min;
            fecha =new SimpleDateFormat("yyyyMMdd HH:mm").parse(sDate);
        }

        return fecha;
    }

    public List<Object> readMenusArray(JsonReader reader) throws IOException {
        List<Object> menus = new ArrayList<Object>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readMenu(reader));
        }
        reader.endArray();
        return menus;
    }

    public Object readMenu(JsonReader reader) throws IOException {
        Long id = null;
        Long id_restaurante = null;
        String nom_restaurante = null;
        Double descuento = null;
        String nombre = null;
        String descripcion = null;
        Double precioSimple = null;
        Double precioTotal = null;
        Double precio = null;
        List<DtExtraMenu> extras = null;
        List<DtMenu> menus = null;
        List<DtProducto> productos = null;
        byte[] imagen = null;
        String tipo = null;
        Integer cal_restaurante = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("descuento") && reader.peek() != JsonToken.NULL) {
                descuento = reader.nextDouble();
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                nombre = reader.nextString();
            } else if (name.equals("descripcion") && reader.peek() != JsonToken.NULL) {
                descripcion = reader.nextString();
            } else if (name.equals("imagen") && reader.peek() != JsonToken.NULL) {
                imagen = readImagenObj(reader);;
            }else if (name.equals("tipo") && reader.peek() != JsonToken.NULL) {
                tipo = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(tipo.equalsIgnoreCase("MENU")){
            return new DtMenu(id, id_restaurante, null,
                    descuento, nombre, descripcion, precioSimple,
                    precioTotal, null, null, imagen, cal_restaurante);
        } else if (tipo.equalsIgnoreCase("PROM")){
            return new DtPromocion(id, nombre, id_restaurante, null,
                    descripcion, descuento, precio, null, imagen, cal_restaurante);
        } else {
            return null;
        }
    }
    
    public byte[] readImagenObj(JsonReader reader) throws IOException {
        byte[] imagen = null;
        String simagen = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("imagen") && reader.peek() != JsonToken.NULL) {
                simagen = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(simagen!=null){
            imagen = Base64.decode(simagen, Base64.DEFAULT);
        }

        return imagen;
    }

    private void addRestaurante(DtRestaurante res) {
        Bitmap bmp = BitmapFactory.decodeByteArray(res.getImagen(), 0, res.getImagen().length);
        rest_img.setImageBitmap(bmp);
        rest_name.setText(res.getNombre());
        rest_cal.setText(res.getCalificacion().getGeneral().toString());

        Drawable progressDrawable = rest_star.getProgressDrawable();

        switch (res.getCalificacion().getGeneral()) {
            case 0:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.white_trans));
                rest_cal.setTextColor(getColor(R.color.white_trans));
                break;
            case 1:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.star_1));
                rest_cal.setTextColor(getColor(R.color.star_1));
                break;
            case 2:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.star_2));
                rest_cal.setTextColor(getColor(R.color.star_2));
                break;
            case 3:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.star_3));
                rest_cal.setTextColor(getColor(R.color.star_3));
                break;
            case 4:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.star_4));
                rest_cal.setTextColor(getColor(R.color.star_4));
                break;
            case 5:
                DrawableCompat.setTint(progressDrawable, getColor(R.color.star_5));
                rest_cal.setTextColor(getColor(R.color.star_5));
                break;
        }
    }
    
    private void addPedido(DtVPedido pedido) {
        List<Object> productos = pedido.getMenus();
        TextView pedido_estado = findViewById(R.id.pedido_estado);
        ProgressBar progressBarEstado = findViewById(R.id.progressBarEstado);
        TextView pedido_fecha = findViewById(R.id.pedido_fecha);
        TextView pedido_fp = findViewById(R.id.pedido_fp);
        TextView pedido_calificar = findViewById(R.id.pedido_calificar);
        
        pedido_estado.setText(pedido.getEstado());
        pedido_fp.setText(pedido.getTipo().toString());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        pedido_fecha.setText(dateFormat.format(pedido.getFecha()));

        if(pedido.getCalificacion() != null){
            pedido_calificar.setText(R.string.pedidos_califica_true);
            pedido_calificar.setBackgroundColor(getColor(R.color.star_5));
            pedido_calificar.setTextColor(getColor(R.color.white));
        } else {
            pedido_calificar.setText(R.string.pedidos_califica_false);
            pedido_calificar.setBackgroundColor(getColor(R.color.star_3));
            pedido_calificar.setTextColor(getColor(R.color.white));
        }

        Drawable progressDrawable = progressBarEstado.getProgressDrawable();

        if (pedido.getEstado().equalsIgnoreCase("CONFIRMADO")){
            progressDrawable.setColorFilter(getColor(R.color.star_3), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBar.setProgressDrawable(progressDrawable);

            //DrawableCompat.setTint(progressDrawable, getColor(R.color.star_3));
            progressBarEstado.setProgress(50);
        } else if (pedido.getEstado().equalsIgnoreCase("RECHAZADO")){
            DrawableCompat.setTint(progressDrawable, getColor(R.color.star_1));
            progressBarEstado.setProgress(100);
        } else if (pedido.getEstado().equalsIgnoreCase("ENVIADO")){
            DrawableCompat.setTint(progressDrawable, getColor(R.color.star_2));
            progressBarEstado.setProgress(75);
        } else if (pedido.getEstado().equalsIgnoreCase("ENTREGADO")){
            DrawableCompat.setTint(progressDrawable, getColor(R.color.star_5));
            progressBarEstado.setProgress(100);
        } else if (pedido.getEstado().equalsIgnoreCase("CANCELADO")){
            DrawableCompat.setTint(progressDrawable, getColor(R.color.star_1));
            progressBarEstado.setProgress(100);
        } else if (pedido.getEstado().equalsIgnoreCase("SOLICITADO")){
            //DrawableCompat.setTint(progressDrawable, getColor(R.color.star_3));
            progressBarEstado.setProgress(25);
            progressDrawable.setColorFilter(getColor(R.color.star_3), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBarEstado.setProgressDrawable(progressDrawable);

        } else{
            DrawableCompat.setTint(progressDrawable, getColor(R.color.white_trans));

        }
        

        if (productos.size() != 0) {
            listView = findViewById(R.id.productosListView);
            listAdapter = new ProductoVPedidoAdapter(this, productos);
            listView.setAdapter(listAdapter);

            String titulo = getString(R.string.pedidos_id) + " " +
                    pedido.getId() + " " +
                    getString(R.string.carr_symbol) + " " +
                    pedido.getTotal();

            setTitle(titulo);

            listView.setOnItemClickListener((adapterView, view, position, id) -> {
            });

        } else {
            Intent imenu = new Intent(VerPedidoHechoActivity.this, VerPedidosActivity.class);
            startActivity(imenu);

            //onBackPressed();
        }
    }

    private void buscarRestaurnate() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETRESTAURANTE_URL;
        stringUrl = stringUrl.replace("{id}", id_restaurante.toString());

        //Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerPedidoHechoActivity.DownloadRestauranteTask().execute(stringUrl);
        } else {
        }
    }

    private class DownloadRestauranteTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return RestaurnateInfoGralUrl(urls[0]);
            } catch (IOException | ParseException e) {
                return getString(R.string.err_recuperarpag);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result instanceof DtResponse) {
                DtResponse response = (DtResponse) result;
                //Log.i(TAG, "onPostExecute:" + response.getMensaje());
                if (response.getOk()) {
                    DtRestaurante restaurante = (DtRestaurante) response.getCuerpo();
                    addRestaurante(restaurante);
                } else {
                    if (ContextCompat.checkSelfPermission(VerPedidoHechoActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VerPedidoHechoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(VerPedidoHechoActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerPedidoHechoActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    private DtResponse RestaurnateInfoGralUrl(String myurl) throws IOException, ParseException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            //String authorization ="Bearer  " + usuario.getToken();

            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            //conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            //conn.setDoOutput(true);
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            //Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readInfoGralJsonStream(is, "GETR");
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "GETR");
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "GETR");
            } else {
                return new DtResponse(false, response + " - " + conn.getResponseMessage(), null);
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    public DtResponse readInfoGralJsonStream(InputStream in, String mType) throws IOException, ParseException {
        DtResponse ret = null;
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(in);
        //Creating a BufferedReader object
        BufferedReader breader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = breader.readLine()) != null) {
            sb.append(str);
        }
        JsonReader reader = new JsonReader(new StringReader(sb.toString()));
        try {
            if (mType.equalsIgnoreCase("GETR")) {
                ret = readRESTMessage(reader);
            } else if (mType.equalsIgnoreCase("GETP")) {
                return readGETPMessage(reader);
            }

            return ret;
        } finally {
            reader.close();
        }
    }

    public DtResponse readRESTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        DtRestaurante restaurante = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                restaurante = readRestaurante(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, restaurante);

    }

    public DtRestaurante readRestaurante(JsonReader reader) throws IOException {

        Long id = null;
        String nombre = null;
        String telefono = null;
        byte[] imagen = null;
        String tipo = null;
        DtRCalificacion calificacion = null;
        DtDireccion direccion = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                nombre = reader.nextString();
            } else if (name.equals("telefono") && reader.peek() != JsonToken.NULL) {
                telefono = reader.nextString();
            } else if (name.equals("imagen") && reader.peek() != JsonToken.NULL) {
                imagen = readImagenObj(reader);
            } else if (name.equals("calificacion") && reader.peek() != JsonToken.NULL) {
                calificacion = readCalificacionObj(reader);
            } else if (name.equals("direccion") && reader.peek() != JsonToken.NULL) {
                direccion = readDireccion(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtRestaurante(id, nombre, null, telefono, null, direccion, imagen, calificacion);
    }

    public DtDireccion readDireccion(JsonReader reader) throws IOException {
        Long id = null;
        String alias = null;
        String calle = null;
        String numero = null;
        String apartamento = null;
        String referencias = null;
        String geometry = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("alias") && reader.peek() != JsonToken.NULL) {
                alias = reader.nextString();
            } else if (name.equals("calle") && reader.peek() != JsonToken.NULL) {
                calle = reader.nextString();
            } else if (name.equals("numero") && reader.peek() != JsonToken.NULL) {
                numero = reader.nextString();
            } else if (name.equals("apartamento") && reader.peek() != JsonToken.NULL) {
                apartamento = reader.nextString();
            } else if (name.equals("referencias") && reader.peek() != JsonToken.NULL) {
                referencias = reader.nextString();
            } else if (name.equals("geometry") && reader.peek() != JsonToken.NULL) {
                geometry = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new DtDireccion(id, alias, calle, numero, apartamento, referencias, geometry);
    }

    public DtRCalificacion readCalificacionObj(JsonReader reader) throws IOException {
        Integer rapidez = null;
        Integer comida = null;
        Integer servicio = null;
        Integer general = null;
        String comentario = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("rapidez") && reader.peek() != JsonToken.NULL) {
                rapidez = reader.nextInt();
            } else if (name.equals("comida") && reader.peek() != JsonToken.NULL) {
                comida = reader.nextInt();
            } else if (name.equals("servicio") && reader.peek() != JsonToken.NULL) {
                servicio = reader.nextInt();
            } else if (name.equals("general") && reader.peek() != JsonToken.NULL) {
                general = reader.nextInt();
            } else if (name.equals("comentario") && reader.peek() != JsonToken.NULL) {
                comentario = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new DtRCalificacion(rapidez, comida, servicio, general, comentario);
    }


}