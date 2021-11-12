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
import java.util.Date;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.DireccionAdapter;
import uy.edu.fing.proyecto.appetit.adapter.PedidoAdapter;
import uy.edu.fing.proyecto.appetit.adapter.ProductAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtCotizacion;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtExtraMenu;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtPromocion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;
import uy.edu.fing.proyecto.appetit.obj.DtVPedido;
import uy.edu.fing.proyecto.appetit.obj.ETipoPago;

public class VerPedidosActivity extends AppCompatActivity {
    private static final String TAG = "VerPedidosActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected PedidoAdapter adapter;

    DtPedido dtPedido = DtPedido.getInstance();
    DtUsuario dtUsuario = DtUsuario.getInstance();

    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Pedidos);
        setTitle(title);

        setContentView(R.layout.activity_ver_pedidos);


        progressBar = findViewById(R.id.pBarMenus);
        progressBar.setVisibility(View.VISIBLE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menu_pedido);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.menu_menus:
                    Intent imenu = new Intent(VerPedidosActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_rest:
                    return true;
                case R.id.menu_pedido:
                    return true;
                case R.id.menu_perfil:
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
                Intent ipedido = new Intent(VerPedidosActivity.this, VerPedidoActivity.class);
                startActivity(ipedido);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void addPedidos(List<DtVPedido> pedidos){

        if(pedidos.size()!=0){
            recyclerView = findViewById(R.id.recyclerView);


            // Nuestro RecyclerView usará un linear layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(VerPedidosActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new PedidoAdapter(VerPedidosActivity.this, pedidos);
            // Set CustomAdapter as the adapter for RecyclerView.
            recyclerView.setAdapter(adapter);
        }
    }


    private void buscarPedidos() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETPEDIDOSPORCLIENTEID_URL;
        stringUrl = stringUrl.replace("{id}", dtUsuario.getId().toString());

        if (networkInfo != null && networkInfo.isConnected()) {
            new VerPedidosActivity.DownloadPedidosTask().execute(stringUrl);
        } else {
        }
    }

    private class DownloadPedidosTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return PedidoInfoGralUrl(urls[0]);
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
                    List<DtVPedido> pedidos = (List<DtVPedido>) response.getCuerpo();
                    addPedidos(pedidos);
                } else {
                    if (ContextCompat.checkSelfPermission(VerPedidosActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VerPedidosActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(VerPedidosActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(VerPedidosActivity.this).create();
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

    private DtResponse PedidoInfoGralUrl(String myurl) throws IOException {
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
        List<DtVPedido> pedidos = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                pedidos = readOBJArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, pedidos);

    }

    public List<DtVPedido> readOBJArray(JsonReader reader) throws IOException {
        List<DtVPedido> menus = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readPedido(reader));
        }
        reader.endArray();
        return menus;
    }

    /*
    private Long id;
    private Long idcli;
    private Long iddir;
    private Long idrest;
    private Boolean pago;
    private ETipoPago tipo;
    private Double total;
    private Date fecha;
    private String geometry;
    private DtCotizacion cotizacion;
    private String id_paypal;
    private String res_nombre;
    private String estado;

    private List<Object> menus = new ArrayList<Object>();

     */
    public DtVPedido readPedido(JsonReader reader) throws IOException {
        Long id = null;
        Long idcli = null;
        Long iddir = null;
        Long idrest = null;
        Boolean pago = true;
        ETipoPago tipo = null;
        Double total = null;
        Date fecha = null;
        String geometry = null;
        DtCotizacion cotizacion = null;
        String id_paypal = null;
        String res_nombre = null;
        String estado = null;
        List<Object> menus = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                id = reader.nextLong();
            } else if (name.equals("idcli") && reader.peek() != JsonToken.NULL) {
                idcli = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return null;
    }

}