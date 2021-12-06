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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.DireccionAdapter;
import uy.edu.fing.proyecto.appetit.adapter.ProductAdapter;
import uy.edu.fing.proyecto.appetit.adapter.RestauranteAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
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

public class RestaurantesActivity extends AppCompatActivity {
    private static final String TAG = "RestaurantesActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    DtPedido dtPedido = DtPedido.getInstance();
    DtUsuario dtUsuario = DtUsuario.getInstance();
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RestauranteAdapter adapter;

    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    Spinner sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Restaurante);
        setTitle(title);

        setContentView(R.layout.activity_restaurantes);

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
                //Log.i(TAG, dir.getAlias());
                progressBar.setVisibility(View.VISIBLE);
                buscarRestaurnates();
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
                    Intent imenu = new Intent(RestaurantesActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_rest:
                    return true;
                case R.id.menu_pedido:
                    Intent ipedido = new Intent(RestaurantesActivity.this, VerPedidosActivity.class);
                    startActivity(ipedido);
                    return true;
                case R.id.menu_perfil:
                    Intent iusr = new Intent(RestaurantesActivity.this, PerfilActivity.class);
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
                Intent ipedido = new Intent(RestaurantesActivity.this, VerPedidoActivity.class);
                startActivity(ipedido);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addRestaurantes(List<DtRestaurante> restaurantes){
        recyclerView = findViewById(R.id.recyclerView);
        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(RestaurantesActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        if(restaurantes.size()!=0){
            adapter = new RestauranteAdapter(RestaurantesActivity.this, restaurantes);
            // Set CustomAdapter as the adapter for RecyclerView.
        } else {
            adapter = null;
        }
        recyclerView.setAdapter(adapter);

    }

    private void buscarRestaurnates() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETRESTAURANTES_URL;
        stringUrl = stringUrl.replace("{point}", dtPedido.getGeometry());
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new RestaurantesActivity.DownloadRestaurantesTask().execute(stringUrl);
        }
    }

    private class DownloadRestaurantesTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return RestauranteInfoGralUrl(urls[0]);
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
                    List<DtRestaurante> restaurantes = (List<DtRestaurante>) response.getCuerpo();
                    addRestaurantes(restaurantes);
                } else {
                    if (ContextCompat.checkSelfPermission(RestaurantesActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(RestaurantesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        onBackPressed();
                    }
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(RestaurantesActivity.this).create();
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

    private DtResponse RestauranteInfoGralUrl(String myurl) throws IOException {
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
        List<DtRestaurante> restaurantes = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                restaurantes = readOBJArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, restaurantes);

    }

    public List<DtRestaurante> readOBJArray(JsonReader reader) throws IOException {
        List<DtRestaurante> restaurantes = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            restaurantes.add(readRestaurante(reader));
        }
        reader.endArray();
        return restaurantes;
    }

    public DtRestaurante readRestaurante(JsonReader reader) throws IOException {
        Long id = null;
        String nombre = null;
        String telefono = null;
        byte[] imagen = null;
        DtRCalificacion calificacion = null;
        DtDireccion direccion = null;
        String horarioApertura = null;
        String horarioCierre = null;
        Boolean abierto = false;


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
                direccion = new DtDireccion();
                direccion.setAlias(reader.nextString());
            } else if (name.equals("abierto") && reader.peek() != JsonToken.NULL) {
                abierto = reader.nextBoolean();
            } else if (name.equals("horarioApertura") && reader.peek() != JsonToken.NULL) {
                horarioApertura = readerHorario(reader);
            } else if (name.equals("horarioCierre") && reader.peek() != JsonToken.NULL) {
                horarioCierre = readerHorario(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtRestaurante(id, nombre, null, telefono, null, direccion, imagen, calificacion, abierto, horarioApertura, horarioCierre);
    }

    public String readerHorario(JsonReader reader) throws IOException {
        String horario = null;
        Integer hour = null;
        Integer min = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("hour") && reader.peek() != JsonToken.NULL) {
                hour = reader.nextInt();
            } else if (name.equals("minute") && reader.peek() != JsonToken.NULL) {
                min = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(hour != null){
            horario= (hour<10?"0":"") + hour +":" +
                    (min<10?"0":"") + min;

        }

        return horario;
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
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new DtRCalificacion(rapidez, comida, servicio, general, comentario);
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

        if (simagen != null) {
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
                Intent adduseractivity = new Intent(RestaurantesActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(RestaurantesActivity.this).create();
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
    }
}