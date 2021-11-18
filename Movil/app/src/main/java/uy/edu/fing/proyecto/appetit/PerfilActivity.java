package uy.edu.fing.proyecto.appetit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.proyecto.appetit.adapter.DireccionPerfilAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class PerfilActivity extends AppCompatActivity {
    private static final String TAG = "PerfilActivity";
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    
    EditText textNombre;
    EditText textCorreo;
    EditText textTelefono;
    ListView listView;
    ListAdapter listAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;



    private final DtUsuario dtUsuario = DtUsuario.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Perfil);
        setTitle(title);

        setContentView(R.layout.activity_perfil);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        textNombre = findViewById(R.id.data_name);
        textCorreo = findViewById(R.id.data_correo);
        textTelefono = findViewById(R.id.data_tel);

        progressBar = findViewById(R.id.pBarAddCliente);
        progressBar.setVisibility(View.INVISIBLE);

        textNombre.setText(dtUsuario.getNombre());
        textCorreo.setText(dtUsuario.getCorreo());
        textTelefono.setText(dtUsuario.getTelefono());
        textCorreo.setEnabled(false);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menu_perfil);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.menu_menus:
                    Intent imenu = new Intent(PerfilActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_rest:
                    Intent irest = new Intent(PerfilActivity.this, RestaurantesActivity.class);
                    startActivity(irest);
                    return true;
                case R.id.menu_pedido:
                    Intent ipedido = new Intent(PerfilActivity.this, VerPedidosActivity.class);
                    startActivity(ipedido);
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

        cargarListaDirecciones();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ConnConstants.WEB_CLIENT_ID)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void cargarListaDirecciones() {
        listView = findViewById(R.id.dirListView);
        listAdapter = new DireccionPerfilAdapter(this, dtUsuario.getDirecciones());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {

            AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
            dialog.setTitle(R.string.info_title);
            dialog.setMessage(getString(R.string.datos_ddireccion));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> {
                eliminarDireccion(dtUsuario.getDirecciones().get(position));
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> {
                dialog.cancel();
            });
            dialog.show();
        });

    }

    private void eliminarDireccion(DtDireccion direccion) {
        if(dtUsuario.getDirecciones().size() == 1){
            AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
            dialog.setTitle(R.string.info_title);
            dialog.setMessage(getString(R.string.datos_err_ddireccion));
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog12, which) -> {
                dialog.cancel();
            });
            dialog.show();
        } else {

            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();

            String stringUrl = ConnConstants.API_DELCLIENTADDRERSS_URL;
            stringUrl = stringUrl.replace("{id}", direccion.getId().toString());

            Log.i(TAG, stringUrl);

            if (networkInfo != null && networkInfo.isConnected()) {
                new PerfilActivity.PutEliminarDirTask().execute(stringUrl);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.perfil_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.perfil_cerrar:
                // Check if user is signed in (non-null) and update UI accordingly.
                //FirebaseUser currentUser = mAuth.getCurrentUser();
                signOut();
                return true;
            case R.id.perfil_modificar:
                AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
                dialog.setTitle(R.string.info_title);
                dialog.setMessage(getString(R.string.datos_qmodificar));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> {
                    modificarDatos();
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> {
                    dialog.cancel();
                });
                dialog.show();

                return true;
            case R.id.perfil_crear_dir:
                Intent inewdir = new Intent(PerfilActivity.this, AddDireccionActivity.class);
                startActivity(inewdir);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemCall = menu.findItem(R.id.perfil_modificar);

        if (dtUsuario.getEsFirebase()) {
            menuItemCall.setEnabled(false).setVisible(false);
        } else {
            menuItemCall.setEnabled(true).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    private void modificarDatos() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_UPDCLIENTE_URL;
        stringUrl = stringUrl.replace("{id}", dtUsuario.getId().toString());

        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new PerfilActivity.PutClienteTask().execute(stringUrl);
        }
    }

    private class PutClienteTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return ClienteInfoGralUrl(urls[0]);
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

                AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
                dialog.setTitle(R.string.info_title);
                dialog.setMessage(response.getMensaje());
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                    dialog.cancel();
                });

                dialog.show();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> onBackPressed());
                dialog.show();
            }
        }
    }

    private DtResponse ClienteInfoGralUrl(String myurl) throws IOException {
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
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = ClienteToJSON();
            Log.i(TAG, "data: " + data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
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

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setToken(reader.nextString());
                dtUsuario.setNombre(textNombre.getText().toString());
                dtUsuario.setTelefono(textTelefono.getText().toString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, dtUsuario);
    }

    private String ClienteToJSON() {
        String res = "";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("nombre", textNombre.getText().toString());
            jsonObject.put("username", dtUsuario.getUsername());
            jsonObject.put("telefono", textTelefono.getText().toString());

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }

    private void signOut() {
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        dtUsuario.setCorreo(null);
        dtUsuario.setTokenFirebase(null);
        dtUsuario.setToken(null);
        dtUsuario.setUsername(null);
        dtUsuario.setTelefono(null);
        dtUsuario.setPassword(null);
        dtUsuario.setNombre(null);
        dtUsuario.setDirecciones(new ArrayList<DtDireccion>());

        Intent imain = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(imain);

    }

    private class PutEliminarDirTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return DireccionInfoGralUrl(urls[0]);
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
                //Log.i(TAG, "ServerLoginFirebase:" + response.getOk());

                AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
                dialog.setTitle(R.string.info_title);
                dialog.setMessage(response.getMensaje());
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {dialog.cancel();});

                dialog.show();
                if(response.getOk()){
                    cargarListaDirecciones();
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> onBackPressed());
                dialog.show();
            }
        }
    }

    private DtResponse DireccionInfoGralUrl(String myurl) throws IOException {
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
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = DireccionToJSON();
            Log.i(TAG, "data: " + data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            Log.i(TAG, "conn.getResponseCode: " + response +" - " + conn.getResponseMessage());
            if (response == 200){
                is = conn.getInputStream();
                return readPUTInfoGralJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readPUTInfoGralJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readPUTInfoGralJsonStream(is);
            } else{
                return new DtResponse(false, response +" - " + conn.getResponseMessage(), null);
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

    public DtResponse readPUTInfoGralJsonStream(InputStream in) throws IOException {
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(in);
        //Creating a BufferedReader object
        BufferedReader breader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = breader.readLine())!= null){
            sb.append(str);
        }
        JsonReader reader = new JsonReader(new StringReader(sb.toString()));
        try {
            return readPUTMessage(reader);
        } finally {
            reader.close();
        }
    }

    public DtResponse readPUTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        Boolean body = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            }else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                body = true;
                readUsuario(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (body){
            return new DtResponse(ok, mensaje, dtUsuario);
        }else {
            return new DtResponse(ok, mensaje, null);
        }

    }

    private String DireccionToJSON(){
        String res = "";

        JSONObject jsonObject= new JSONObject();
        try {

            jsonObject.put("id_cliente", dtUsuario.getId().toString());

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }
    public void readUsuario(JsonReader reader) throws IOException {

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("correo") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setCorreo(reader.nextString());
            } else if (name.equals("telefono") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setTelefono(reader.nextString());
            } else if (name.equals("jwt") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setToken(reader.nextString());
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setNombre(reader.nextString());
            } else if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setId(reader.nextLong());
            } else if (name.equals("direcciones") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setDirecciones(readDireccionesArray(reader));
            } else if (name.equals("tokenFireBase") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setTokenFirebase(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

    }

    public List<DtDireccion> readDireccionesArray(JsonReader reader) throws IOException {
        List<DtDireccion> dosis = new ArrayList<DtDireccion>();
        reader.beginArray();
        while (reader.hasNext()) {
            dosis.add(readDireccion(reader));
        }
        reader.endArray();
        return dosis;
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

}