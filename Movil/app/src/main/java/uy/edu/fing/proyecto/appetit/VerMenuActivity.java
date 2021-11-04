package uy.edu.fing.proyecto.appetit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtExtraMenu;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;

public class VerMenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    Long id_restaurante;
    Long id_menu;
    String tipo;

    ImageView menu_img;
    TextView menu_name;
    TextView menu_detalle;
    TextView menu_precio;
    TextView menu_restaurante;
    TextView menu_cantidad;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    Button add_pedido;
    ImageButton add_cantidad;
    ImageButton menos_cantidad;
    DtMenu viewMenu = null;
    DtPromocion viewProm = null;

    DtPedido dtPedido = DtPedido.getInstance();
    Integer cantidad = 1;
    Double total = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        id_menu = bundle.getLong("id");
        id_restaurante = bundle.getLong("id_restaurante");
        tipo = bundle.getString("tipo");


        setContentView(R.layout.activity_ver_menu);

        menu_img = findViewById(R.id.vmenu_img);
        menu_name = findViewById(R.id.vmenu_name);
        menu_detalle = findViewById(R.id.vmenu_detalle);
        menu_precio = findViewById(R.id.vmenu_precio);
        menu_restaurante = findViewById(R.id.vmenu_restaurante);
        add_pedido = findViewById(R.id.vmenu_add_pedido);
        add_cantidad = findViewById(R.id.addCantidad);
        menos_cantidad = findViewById(R.id.minusCantidad);
        menu_cantidad = findViewById(R.id.vmenu_cantidad);

        progressBar = findViewById(R.id.pBarMenus);
        progressBar.setVisibility(View.VISIBLE);

        buscarMenu();

        bottomNavigationView = findViewById(R.id.bottomNavViewMenu);

        add_cantidad.setOnClickListener(v -> {
            cantidad ++;
            menu_cantidad.setText(cantidad.toString());

            String precio = getString(R.string.carr_symbol) + " " + total * cantidad;
            String b_text = getString(R.string.carr_add_prod) + " " + precio;
            add_pedido.setText(b_text);
        });

        menos_cantidad.setOnClickListener(v -> {
            if(cantidad > 1){
                cantidad --;
            }
            menu_cantidad.setText(cantidad.toString());
            String precio = getString(R.string.carr_symbol) + " " + total * cantidad;
            String b_text = getString(R.string.carr_add_prod) + " " + precio;
            add_pedido.setText(b_text);
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.menu_menus:
                    Intent imenu = new Intent(VerMenuActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_pedido:
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

    }
    private void addMenu(Object obj){
        Bitmap bmp = null;
        String nombre = null;
        String precio = null;
        String detalle = null;
        String restaurante = null;


        if (obj instanceof DtMenu){
            DtMenu dtp = (DtMenu) obj;
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getNombre();
            precio = getString(R.string.carr_symbol) + " " + dtp.getPrecioTotal();
            detalle = dtp.getDescripcion();
            restaurante = dtp.getNom_restaurante();

        } else if (obj instanceof DtPromocion){
            DtPromocion dtp = (DtPromocion) obj;
            bmp = BitmapFactory.decodeByteArray(dtp.getImagen(), 0, dtp.getImagen().length);
            nombre = dtp.getDescuento() + getString(R.string.carr_dto) +
                    " " + dtp.getNombre();
            precio = getString(R.string.carr_symbol) + " " + dtp.getPrecio();
            detalle = dtp.getDescripcion();
            restaurante = dtp.getNom_restaurante();
        }
        menu_img.setImageBitmap(bmp);
        menu_name.setText(nombre);
        menu_detalle.setText(detalle);
        menu_precio.setText(precio);
        menu_restaurante.setText(restaurante);
        menu_cantidad.setText(cantidad.toString());
        String b_text = getString(R.string.carr_add_prod) + " " +  precio;
        add_pedido.setText(b_text);

    }

    private void buscarMenu() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = null;

        if(tipo.equalsIgnoreCase("M")){
            stringUrl = ConnConstants.API_GETMENU_URL;
        } else if (tipo.equalsIgnoreCase("P")){
            stringUrl = ConnConstants.API_GETPROMO_URL;
        }

        stringUrl = stringUrl.replace("{id}", id_menu.toString());
        stringUrl = stringUrl.replace("{id_restaurante}", id_restaurante.toString());

        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerMenuActivity.DownloadMenuTask().execute(stringUrl);
        } else {
        }
    }

    private class DownloadMenuTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return MenuInfoGralUrl(urls[0]);
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
                    //DtMenu menu = (DtMenu) response.getCuerpo();
                    Object menu = (Object) response.getCuerpo();
                    addMenu(menu);
                } else {
                    if (ContextCompat.checkSelfPermission(VerMenuActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VerMenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(VerMenuActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerMenuActivity.this).create();
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

    private DtResponse MenuInfoGralUrl(String myurl) throws IOException {
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
                return readInfoGralJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
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

    public DtResponse readInfoGralJsonStream(InputStream in) throws IOException {
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
            return readRESTMessage(reader);
        } finally {
            reader.close();
        }
    }

    public DtResponse readRESTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        DtResponse res = null;
        Object menu = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                menu = readMenu(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, menu);

    }

    public List<DtMenu> readMenusArray(JsonReader reader) throws IOException {
        List<DtMenu> menus = new ArrayList<DtMenu>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add((DtMenu) readMenu(reader));
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

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("id_restaurante") && reader.peek() != JsonToken.NULL) {
                id_restaurante = reader.nextLong();
            } else if (name.equals("nom_restaurante") && reader.peek() != JsonToken.NULL) {
                nom_restaurante = reader.nextString();
            } else if (name.equals("descuento") && reader.peek() != JsonToken.NULL) {
                descuento = reader.nextDouble();
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                nombre = reader.nextString();
            } else if (name.equals("descripcion") && reader.peek() != JsonToken.NULL) {
                descripcion = reader.nextString();
            } else if (name.equals("precioSimple") && reader.peek() != JsonToken.NULL) {
                precioSimple = reader.nextDouble();
            } else if (name.equals("precioTotal") && reader.peek() != JsonToken.NULL) {
                precioTotal = reader.nextDouble();
            } else if (name.equals("precio") && reader.peek() != JsonToken.NULL) {
                precio = reader.nextDouble();
            } else if (name.equals("extras") && reader.peek() != JsonToken.NULL) {
                extras = readExtrasMenusArray(reader);
            } else if (name.equals("productos") && reader.peek() != JsonToken.NULL) {
                productos = readProductosArray(reader);
            } else if (name.equals("menus") && reader.peek() != JsonToken.NULL) {
                menus = readMenusArray(reader);
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
            return new DtMenu(id, id_restaurante, nom_restaurante,
                    descuento, nombre, descripcion, precioSimple,
                    precioTotal, extras, productos, imagen);
        } else if (tipo.equalsIgnoreCase("PROM")){
            return new DtPromocion(id, nombre, id_restaurante, nom_restaurante,
                    descripcion, descuento, precio, menus, imagen);
        } else {
            return null;
        }
    }

    public List<DtExtraMenu> readExtrasMenusArray(JsonReader reader) throws IOException {
        List<DtExtraMenu> menus = new ArrayList<DtExtraMenu>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readExtraMenu(reader));
        }
        reader.endArray();
        return menus;
    }

    public DtExtraMenu readExtraMenu(JsonReader reader) throws IOException {
        Long id = null;
        Long id_producto = null;
        Long id_restaurante = null;
        String producto = null;
        Double precio = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("id_producto") && reader.peek() != JsonToken.NULL) {
                id_producto = reader.nextLong();
            } else if (name.equals("id_restaurante") && reader.peek() != JsonToken.NULL) {
                id_restaurante = reader.nextLong();
            } else if (name.equals("precio") && reader.peek() != JsonToken.NULL) {
                precio = reader.nextDouble();
            } else if (name.equals("producto") && reader.peek() != JsonToken.NULL) {
                producto = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtExtraMenu(id, id_producto, id_restaurante, producto, precio);
    }

    public List<DtProducto> readProductosArray(JsonReader reader) throws IOException {
        List<DtProducto> menus = new ArrayList<DtProducto>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readProducto(reader));
        }
        reader.endArray();
        return menus;
    }

    public DtProducto readProducto(JsonReader reader) throws IOException {
        Long id = null;
        Long id_restaurante = null;
        String nombre = null;
        Long id_categoria = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("id_restaurante") && reader.peek() != JsonToken.NULL) {
                id_restaurante = reader.nextLong();
            } else if (name.equals("id_categoria") && reader.peek() != JsonToken.NULL) {
                id_categoria = reader.nextLong();
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                nombre = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtProducto(id, id_restaurante, nombre, id_categoria, null, null);
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

    private String MenuToJSON() {
        String res = "";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id_menu);
            jsonObject.put("id_restaurante", id_restaurante);

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.i(TAG, String.valueOf(requestCode));
        if (requestCode == PERMISOS_REQUERIDOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent adduseractivity = new Intent(VerMenuActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerMenuActivity.this).create();
                dialog.setTitle(R.string.access_title_err);
                dialog.setMessage(getString(R.string.access_msg_err));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> requestPermissions(permissions, PERMISOS_REQUERIDOS));
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> finish());
                dialog.show();


            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}