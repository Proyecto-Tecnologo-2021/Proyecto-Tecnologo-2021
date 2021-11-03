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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.ProductAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtCategoria;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtExtraMenu;
import uy.edu.fing.proyecto.appetit.obj.DtMenu;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtRestaurante;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProductAdapter adapter;

    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_PedidosGral);
        setTitle(title);

        setContentView(R.layout.activity_menu);

        progressBar = findViewById(R.id.pBarMenus);
        progressBar.setVisibility(View.VISIBLE);

        buscarMenus();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.menu_menus:
                    return true;
                case R.id.menu_pedido:
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

    }

    private void addMenus(List<DtMenu> menus){

        if(menus.size()!=0){
            recyclerView = findViewById(R.id.recyclerView);

            // LinearLayoutManager is used here, this will layout the elements in a similar fashion
            // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
            // elements are laid out.

            // Nuestro RecyclerView usará un linear layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(MenuActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new ProductAdapter(MenuActivity.this, menus);
            // Set CustomAdapter as the adapter for RecyclerView.
            recyclerView.setAdapter(adapter);
            // END_INCLUDE(initializeRecyclerView)
        }


        //Log.i(TAG, "addDepartamentos List<DtDepartamento>: " + departamentos.size());


        /*
        if(departamentos.size()!=0){
            expandableListView = findViewById(R.id.expandableListView);
            expandableListAdapter = new CustomExpandableListAdapter(MapDeptoLoc.this, departamentos);
            expandableListView.setAdapter(expandableListAdapter);

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    result.putExtra("departamento", departamentos.get(groupPosition).getId().toString());
                    result.putExtra("localidad", departamentos.get(groupPosition).getLocalidades().get(childPosition).getId().toString());
                    MapDeptoLoc.this.setResult(RESULT_OK, result);
                    finish();
                    return false;
                }
            });

        }else {
            result.putExtra("departamento", -1);
            result.putExtra("localidad", -1);
            MapDeptoLoc.this.setResult(RESULT_CANCELED, result);
            finish();
        }
        */
    }

    private void buscarMenus() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_GETMENUS_URL;

        if (networkInfo != null && networkInfo.isConnected()) {
            new MenuActivity.DownloadMenusTask().execute(stringUrl);
        } else {
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
                //Log.i(TAG, "ServerLoginFirebase:" + response.getOk());
                if (response.getOk()) {
                    List<DtMenu> menus = (List<DtMenu>) response.getCuerpo();
                    addMenus(menus);
                } else {
                    if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        onBackPressed();
                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this).create();
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


            //String data = LoginToJSON();
            //Log.i(TAG, data);

            //byte[] out = data.getBytes(StandardCharsets.UTF_8);
            //OutputStream stream = conn.getOutputStream();
            //stream.write(out);

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
        List<DtMenu> menus = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                menus = readMenusArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, menus);

    }

    public List<DtMenu> readMenusArray(JsonReader reader) throws IOException {
        List<DtMenu> menus = new ArrayList<DtMenu>();
        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readMenu(reader));
        }
        reader.endArray();
        return menus;
    }

    public DtMenu readMenu(JsonReader reader) throws IOException {
        Long id = null;
        Long id_restaurante = null;
        String nom_restaurante = null;
        Double descuento = null;
        String nombre = null;
        String descripcion = null;
        Double precioSimple = null;
        Double precioTotal = null;
        List<DtExtraMenu> extras = null;
        List<DtProducto> productos = null;
        byte[] imagen = null;

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
            } else if (name.equals("extras") && reader.peek() != JsonToken.NULL) {
                extras = readExtrasMenusArray(reader);
            } else if (name.equals("productos") && reader.peek() != JsonToken.NULL) {
                productos = readProductosArray(reader);
            } else if (name.equals("imagen") && reader.peek() != JsonToken.NULL) {
                imagen = readImagenObj(reader);;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new DtMenu(id, id_restaurante, nom_restaurante,
                descuento, nombre, descripcion, precioSimple,
                precioTotal, extras, productos, imagen);
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
        Log.i(TAG, String.valueOf(requestCode));
        if (requestCode == PERMISOS_REQUERIDOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent adduseractivity = new Intent(MenuActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this).create();
                dialog.setTitle(R.string.access_title_err);
                dialog.setMessage(getString(R.string.access_msg_err));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> requestPermissions(permissions, PERMISOS_REQUERIDOS));
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> finish());
                dialog.show();


            }
        }
    }

}