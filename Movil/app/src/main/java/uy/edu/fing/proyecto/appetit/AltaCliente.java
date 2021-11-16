package uy.edu.fing.proyecto.appetit;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class AltaCliente extends AppCompatActivity {
    private static final String TAG = "AltaCliente";
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;

    EditText textNombre;
    EditText textCorreo;
    EditText textTelefono;
    EditText textpass;
    EditText textpass2;
    TextView textAlias;
    TextView textDetalle;
    Button confirm;
    ProgressBar progressBar;


    private final DtUsuario dtUsuario = DtUsuario.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_addDatos);
        setTitle(title);
        setContentView(R.layout.activity_alta_cliente);

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();


        textNombre = findViewById(R.id.data_name);
        textCorreo = findViewById(R.id.data_correo);
        textTelefono = findViewById(R.id.data_tel);
        textpass = findViewById(R.id.data_pass);
        textpass2 = findViewById(R.id.data_pass2);
        confirm = findViewById(R.id.data_confirm);
        textAlias = findViewById(R.id.DirTAlias);
        textDetalle = findViewById(R.id.DirDetalle);
        progressBar = findViewById(R.id.pBarAddCliente);
        progressBar.setVisibility(View.INVISIBLE);

        if(dtUsuario.getCorreo()!= null && !dtUsuario.getCorreo().equals("")){
            textCorreo.setText(dtUsuario.getCorreo());
            textCorreo.setEnabled(false);
        }
        if (dtUsuario.getTelefono()!=null && !dtUsuario.getTelefono().equals("")){
            textTelefono.setText(dtUsuario.getTelefono());
            textTelefono.setEnabled(false);
        }
        if(dtUsuario.getNombre()!=null && !dtUsuario.getNombre().equals("")){
            textNombre.setText(dtUsuario.getNombre());
            textNombre.setEnabled(false);
        }

        if(dtUsuario.getDirecciones().size()==0){
            AltaCliente.super.onBackPressed();
        } else {
            DtDireccion dtd = dtUsuario.getDirecciones().get(0);
            textAlias.setText(dtd.getAlias());
            String detalle = dtd.getCalle() + " " +
                    dtd.getNumero() ;
            if(dtd.getApartamento().equals("")){
                detalle += ", ";
            }else {
                detalle += ", " + getString(R.string.map_text_apto) + dtd.getApartamento();

            }

            if(!dtd.getReferencias().equals(""))
                detalle += ", " + dtd.getReferencias();

            textDetalle.setText(detalle);
        }

        confirm.setOnClickListener(v -> {
            Pattern corr = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = corr.matcher(textCorreo.getText().toString());

            if (textNombre.getText().toString().equalsIgnoreCase("")){
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.datos_err_name));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            } else if(textCorreo.getText().toString().equalsIgnoreCase("") || !matcher.find()) {
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.datos_err_email));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            }else if(textTelefono.getText().toString().equalsIgnoreCase("")) {
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.datos_err_phone));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            }else if(textpass.getText().toString().equalsIgnoreCase("")) {
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.datos_err_pass));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            } else if(!textpass.getText().toString().equals(textpass2.getText().toString())) {
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.datos_err_pass2));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            } else{
                dtUsuario.setNombre(textNombre.getText().toString());
                dtUsuario.setCorreo(textCorreo.getText().toString());
                dtUsuario.setUsername(textCorreo.getText().toString());
                dtUsuario.setTelefono(textTelefono.getText().toString());
                dtUsuario.setPassword(textpass.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                addCliente();
            }
        });
    }

    private void addCliente() {

        String stringUrl = ConnConstants.API_ADDCLIENT_URL;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new AltaCliente.GetAddClienteTask().execute(stringUrl);
        }
    }

    //@SuppressLint("StaticFieldLeak")
    private class GetAddClienteTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return AddCliente(urls[0]);
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

                if (response.getOk()) {
                    AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                    dialog.setTitle(R.string.alert_t_info);
                    dialog.setIcon(android.R.drawable.ic_dialog_info);
                    dialog.setMessage(response.getMensaje());

                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                        Intent menuactivity = new Intent(AltaCliente.this, MenuActivity.class);
                        startActivity(menuactivity);
                    });
                    dialog.show();

                } else {
                    AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                    dialog.setTitle(R.string.alert_t_error);
                    dialog.setIcon(android.R.drawable.ic_dialog_alert);

                    dialog.setMessage(response.getMensaje());

                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {});
                    dialog.show();
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(AltaCliente.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);

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

    private DtResponse AddCliente(String myurl) throws IOException {
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


            String data = AddClienteToJSON();
            Log.i(TAG, data);

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
        Boolean body = false;
        DtResponse res = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            } else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                body = true;
                readUsuario(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (body) {
            return new DtResponse(ok, mensaje, dtUsuario);
        } else {
            return new DtResponse(ok, mensaje, null);
        }

    }

    public void readUsuario(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("correo") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setCorreo(reader.nextString());
            } else if (name.equals("telefono") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setTelefono(reader.nextString());
            } else if (name.equals("token") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setToken(reader.nextString());
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setNombre(reader.nextString());
            } else if (name.equals("direcciones") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setDirecciones(readDireccionesArray(reader));
            } else if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setId(reader.nextLong());
            } else if (name.equals("username") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setUsername(reader.nextString());
            } else if (name.equals("password") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setPassword(reader.nextString());
            } else if (name.equals("bloqueado") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setBloqueado(reader.nextBoolean());
            } else if (name.equals("calificacion") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setCalificacion(readCalifiacion(reader));
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

    public DtCalificacion readCalifiacion(JsonReader reader) throws IOException {
        Integer clasificacion = 0;
        Integer restaurantes = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("clasificacion") && reader.peek() != JsonToken.NULL) {
                clasificacion = reader.nextInt();
            } else if (name.equals("restaurantes") && reader.peek() != JsonToken.NULL) {
                restaurantes = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtCalificacion(clasificacion, restaurantes);

    }


    private String AddClienteToJSON() {
        String res = "";

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("nombre", dtUsuario.getNombre());
            jsonObject.put("username", dtUsuario.getUsername());
            jsonObject.put("password", dtUsuario.getPassword());
            jsonObject.put("telefono", dtUsuario.getTelefono());
            jsonObject.put("correo", dtUsuario.getCorreo());
            jsonObject.put("tokenFireBase", dtUsuario.getTokenFirebase());

            DtDireccion dir = dtUsuario.getDirecciones().get(0);

            JSONObject jsonDir = new JSONObject();
            jsonDir.put("id_cliente", dtUsuario.getId());
            jsonDir.put("alias", dir.getAlias());
            jsonDir.put("calle", dir.getCalle());
            jsonDir.put("numero", dir.getNumero());
            jsonDir.put("apartamento", dir.getApartamento());
            jsonDir.put("referencias", dir.getReferencias());
            jsonDir.put("geometry", dir.getGeometry());

            jsonObject.put("direccion", jsonDir);

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }
}