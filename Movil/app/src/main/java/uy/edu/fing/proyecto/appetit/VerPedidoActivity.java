package uy.edu.fing.proyecto.appetit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.ProductoPedidoAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtRCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtRestaurante;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;
import uy.edu.fing.proyecto.appetit.obj.ETipoPago;

public class VerPedidoActivity extends AppCompatActivity {
    private static final String TAG = "VerPedidoActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private static final int PAYPAL_REQUEST_CODE = 20213;
    final static Integer RC_SIGN_IN = 20213;
    final CharSequence[] itemsFP = {"Contado", "PayPal"};
    private ETipoPago opFP = ETipoPago.EFECTIVO;
    private String paypal_answer = null;

    DtPedido dtPedido = DtPedido.getInstance();
    DtUsuario dtUsuario = DtUsuario.getInstance();
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    ListView listView;
    ListAdapter listAdapter;



    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    ImageView rest_img;
    TextView rest_name;
    TextView rest_cal;
    RatingBar rest_star;
    Button pedido_confirm;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Pedido);
        setTitle(title);
        
        setContentView(R.layout.activity_ver_pedido);

        if(dtPedido.getIdrest() == null){
            Intent imenu = new Intent(VerPedidoActivity.this, MenuActivity.class);
            startActivity(imenu);
        } else {
            buscarRestaurnate();
            addProductos(dtPedido.getMenus());
        }


        progressBar = findViewById(R.id.vpBarPedidos);
        bottomNavigationView = findViewById(R.id.bottomNavViewMenu);
        rest_img = findViewById(R.id.vrestaurante_img);
        rest_name = findViewById(R.id.vrestaurante_name);
        rest_star = findViewById(R.id.vrestaurante_star);
        rest_cal = findViewById(R.id.vrestaurante_rating);
        pedido_confirm = findViewById(R.id.pedido_confirmar);

        progressBar.setVisibility(View.VISIBLE);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.menu_menus:
                    Intent imenu = new Intent(VerPedidoActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_pedido:
                    //Intent ipedido = new Intent(VerPedidoActivity.this, VerPedidoActivity.class);
                    //startActivity(ipedido);
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

        pedido_confirm.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.carr_t_fp);
            builder.setSingleChoiceItems(itemsFP, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                    if(item ==0 ){
                        opFP = ETipoPago.EFECTIVO;
                    } else if(item == 1){
                        opFP = ETipoPago.PAYPAL;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_confirmar), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(VerPedidoActivity.this, R.string.alert_btn_confirmar,Toast.LENGTH_SHORT).show();
                    if (opFP == ETipoPago.EFECTIVO){
                        dtPedido.setPago(false);
                        confirmarPContado();
                    } else if (opFP == ETipoPago.PAYPAL){
                        Toast.makeText(VerPedidoActivity.this, "PayPal",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_cancel), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(VerPedidoActivity.this, R.string.alert_btn_cancel,Toast.LENGTH_SHORT).show();
                        }
                    });
            dialog.show();
        });
    }

    private void addRestaurante(DtRestaurante res){
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

    private void addProductos(List<Object> productos){
        if(productos.size()!=0){
            listView = findViewById(R.id.productosListView);
            listAdapter = new ProductoPedidoAdapter(this, productos);
            listView.setAdapter(listAdapter);

            String titulo = getString(R.string.title_Pedido) +" - " +
                    getString(R.string.carr_titulo) + " " +
                    getString(R.string.carr_symbol) + " " +
                    dtPedido.getTotal();

            setTitle(titulo);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    String mensaje = getString(R.string.carr_quitar_item) +"\n";

                    if (productos.get(position) instanceof DtMenu){
                        DtMenu dtp = (DtMenu) productos.get(position);
                        mensaje += dtp.getNombre();
                    } else if (productos.get(position) instanceof DtPromocion){
                        DtPromocion dtp = (DtPromocion) productos.get(position);
                        mensaje += dtp.getNombre();
                    }

                    AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
                    dialog.setTitle(R.string.info_title);
                    dialog.setMessage(mensaje);
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> {
                        dtPedido.remMenu(position);
                        addProductos(dtPedido.getMenus());

                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> {});
                    dialog.show();
                }
            });

        }else {
            onBackPressed();
        }
    }

    private void buscarRestaurnate() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETRESTAURANTE_URL;
        stringUrl = stringUrl.replace("{id}", dtPedido.getIdrest().toString());

        //Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerPedidoActivity.DownloadRestauranteTask().execute(stringUrl);
        } else {
        }
    }

    private class DownloadRestauranteTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return RestaurnateInfoGralUrl(urls[0]);
            } catch (IOException e) {
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
                    if (ContextCompat.checkSelfPermission(VerPedidoActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VerPedidoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
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

    private DtResponse RestaurnateInfoGralUrl(String myurl) throws IOException {
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

    public DtResponse readInfoGralJsonStream(InputStream in, String mType) throws IOException {
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
            if(mType.equalsIgnoreCase("GETR")){
                ret = readRESTMessage(reader);
            } else if (mType.equalsIgnoreCase("POSTP")){
                ret = readPOSTMessage(reader);
            }

            return  ret;
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

    public DtResponse readPOSTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, null);

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
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new DtRCalificacion(rapidez, comida, servicio, general);
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
            //imagen = simagen.getBytes();
            imagen = Base64.decode(simagen, Base64.DEFAULT);
        }

        return imagen;
    }

    private void confirmarPContado() {

        String stringUrl = ConnConstants.API_ADDPEDIDO_URL;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerPedidoActivity.POSTAddPedidoTask().execute(stringUrl);
        }
    }

    private class POSTAddPedidoTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return AddPedido(urls[0]);
            } catch (IOException e) {
                //return getString(R.string.err_recuperarpag);
                return e.getMessage();
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result instanceof DtResponse) {
                DtResponse response = (DtResponse) result;

                if (response.getOk()) {
                    //Se vacía el pedido
                    dtPedido.setMenus(new ArrayList<>());
                    dtPedido.setIdrest(null);

                    AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
                    dialog.setTitle(R.string.alert_t_info);
                    dialog.setIcon(android.R.drawable.ic_dialog_info);
                    dialog.setMessage(response.getMensaje());

                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                        Intent menuactivity = new Intent(VerPedidoActivity.this, MenuActivity.class);
                        startActivity(menuactivity);
                    });
                    dialog.show();

                } else {
                    AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
                    dialog.setTitle(R.string.alert_t_error);
                    dialog.setIcon(android.R.drawable.ic_dialog_alert);

                    dialog.setMessage(response.getMensaje());

                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {});
                    dialog.show();
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerPedidoActivity.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> onBackPressed());
                dialog.show();
            }
        }
    }

    private DtResponse AddPedido(String myurl) throws IOException {
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
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);


            String data = AddPedidoToJSON();
            //Log.i(TAG, data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            //Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readInfoGralJsonStream(is, "POSTP");
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "POSTP");
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is, "POSTP");
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

    private String AddPedidoToJSON() {
        String res = "";

        /*
        {
    "idcli":10,
    "iddir":2,
    "menus":[{
              "id": 2,
              "id_restaurante": 9,
              "tipo": "MENU"
             },
             {
              "id": 2,
              "id_restaurante": 9,
              "tipo": "PROM"
             }],
    "pago":true,
    "tipo":"EFECTIVO",
    "id_paypal": null,
    "total":"516",
    "idrest":9,
    "fecha":null
}
         */

        JSONObject jsonObject = new JSONObject();
        JSONArray menusArray = new JSONArray();
        try {
            jsonObject.put("idcli", dtPedido.getIdcli());
            jsonObject.put("iddir", dtPedido.getIddir());
            jsonObject.put("pago", dtPedido.getPago());
            jsonObject.put("tipo", opFP);
            jsonObject.put("id_paypal", paypal_answer);
            jsonObject.put("total", dtPedido.getTotal());
            jsonObject.put("idrest", dtPedido.getIdrest());
            jsonObject.put("fecha", null);

            for (int p =0; p < dtPedido.getMenus().size(); p++){
                JSONObject prodObject = new JSONObject();
                Long id = null;
                String tipo = null;

                if (dtPedido.getMenus().get(p) instanceof DtMenu){
                    id = ((DtMenu) dtPedido.getMenus().get(p)).getId();
                    tipo = "MENU";
                } else if (dtPedido.getMenus().get(p) instanceof DtPromocion){
                   id = ((DtPromocion) dtPedido.getMenus().get(p)).getId();
                   tipo = "PROM";
                }

                prodObject.put("id", id);
                prodObject.put("id_restaurante", dtPedido.getIdrest());
                prodObject.put("tipo", tipo);

                menusArray.put(prodObject);
            }

            jsonObject.put("menus", menusArray);

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }


}