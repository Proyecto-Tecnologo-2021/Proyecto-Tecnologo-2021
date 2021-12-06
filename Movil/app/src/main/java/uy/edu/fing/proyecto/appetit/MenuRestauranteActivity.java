package uy.edu.fing.proyecto.appetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.DireccionAdapter;
import uy.edu.fing.proyecto.appetit.adapter.ProductAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtExtraMenu;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class MenuRestauranteActivity extends AppCompatActivity {
    private static final String TAG = "MenuRestauranteActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    DtPedido dtPedido = DtPedido.getInstance();
    DtUsuario dtUsuario = DtUsuario.getInstance();
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProductAdapter adapter;
    Long id_restaurante;
    String nombre;


    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    Spinner sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        id_restaurante = bundle.getLong("id_restaurante");
        nombre = bundle.getString("nombre");


        //String title = getString(R.string.title_PedidosGral);
        //setTitle(title);
        setTitle(nombre);

        setContentView(R.layout.activity_menu);

        sp = findViewById(R.id.dir_spinner);
        progressBar = findViewById(R.id.pBarMenus);
        progressBar.setVisibility(View.VISIBLE);

        DireccionAdapter adapter = new DireccionAdapter(this, R.layout.dir_spinner, dtUsuario.getDirecciones());
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                DtDireccion dir = adapter.getItem(position);
                dtPedido.setIddir(dir.getId());
                dtPedido.setGeometry(dir.getGeometry());
                Log.i(TAG, dir.getAlias());
                progressBar.setVisibility(View.VISIBLE);
                buscarMenus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(dtPedido.getIddir() != null){
            DtDireccion pos = new DtDireccion();
            pos.setId(dtPedido.getIddir());
            sp.setSelection(adapter.getPosition(pos));
            //mySpinner.setSelection(arrayAdapter.getPosition("Category 2")
        }
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menu_menus);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.menu_menus:
                    return true;
                case R.id.menu_rest:
                    Intent irest = new Intent(MenuRestauranteActivity.this, RestaurantesActivity.class);
                    startActivity(irest);
                    return true;
                case R.id.menu_pedido:
                    Intent ipedido = new Intent(MenuRestauranteActivity.this, VerPedidosActivity.class);
                    startActivity(ipedido);
                    return true;
                case R.id.menu_perfil:
                    Intent iusr = new Intent(MenuRestauranteActivity.this, PerfilActivity.class);
                    startActivity(iusr);
                    return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(dtPedido.getIdrest()!=null){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.pedido_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pmenu_ver:
                Intent ipedido = new Intent(MenuRestauranteActivity.this, VerPedidoActivity.class);
                startActivity(ipedido);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addMenus(List<Object> menus){
        recyclerView = findViewById(R.id.recyclerView);
        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuRestauranteActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        if(menus.size()!=0){
            adapter = new ProductAdapter(MenuRestauranteActivity.this, menus);
            // Set CustomAdapter as the adapter for RecyclerView.
        } else {
            adapter = null;
        }
        recyclerView.setAdapter(adapter);

    }

    private void buscarMenus() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

            String stringUrl = ConnConstants.API_GETMENUSPROMORESTAURANTE_URL;
            stringUrl = stringUrl.replace("{id_restaurante}", id_restaurante.toString());

        //Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new MenuRestauranteActivity.DownloadMenusTask().execute(stringUrl);
        }
    }

    private class DownloadMenusTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return MenusInfoGralUrl(urls[0]);
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
                Log.i(TAG, "ServerLoginFirebase:" + response.getOk());
                if (response.getOk()) {
                    List<Object> menus = (List<Object>) response.getCuerpo();
                    addMenus(menus);
                } else {
                    if (ContextCompat.checkSelfPermission(MenuRestauranteActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(MenuRestauranteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        onBackPressed();
                    }
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MenuRestauranteActivity.this).create();
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

    private DtResponse MenusInfoGralUrl(String myurl) throws IOException {
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
        List<Object> menus = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                menus = readOBJArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, menus);

    }

    public List<Object> readOBJArray(JsonReader reader) throws IOException {
        List<Object> menus = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readMenu(reader));
        }
        reader.endArray();
        return menus;
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
        Integer cal_restaurante = null;

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
            }else if (name.equals("cal_restaurante") && reader.peek() != JsonToken.NULL) {
                cal_restaurante = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(tipo.equalsIgnoreCase("MENU")){
            return new DtMenu(id, id_restaurante, nom_restaurante,
                    descuento, nombre, descripcion, precioSimple,
                    precioTotal, extras, productos, imagen, cal_restaurante);
        } else if (tipo.equalsIgnoreCase("PROM")){
            return new DtPromocion(id, nombre, id_restaurante, nom_restaurante,
                    descripcion, descuento, precio, menus, imagen, cal_restaurante);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.i(TAG, String.valueOf(requestCode));
        if (requestCode == PERMISOS_REQUERIDOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent adduseractivity = new Intent(MenuRestauranteActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MenuRestauranteActivity.this).create();
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

    @Override
    protected void onStart() {
        super.onStart();
        //buscarMenus();
    }
}