package uy.edu.fing.proyecto.appetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtLogin;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    Button confirmButton;
    EditText mail;
    EditText password;
    ProgressBar progressBar;
    DtUsuario dtUsuario = DtUsuario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        confirmButton = findViewById(R.id.login_input);
        mail = findViewById(R.id.login_mail);
        password = findViewById(R.id.login_pwd);
        progressBar = findViewById(R.id.progressBarLogin);

        progressBar.setVisibility(View.INVISIBLE);

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        confirmButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            loginUsuario();
        });
    }

    private void loginUsuario() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_USRLOGIN_URL;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new LoginActivity.GetLoginUsuarioTask().execute(stringUrl);
        }
    }

    private class GetLoginUsuarioTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return LoginInfoGralUrl(urls[0]);
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
                    Intent menuactivity = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(menuactivity);
                } else {
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
                        dialog.setTitle(R.string.info_title);

                        if (response.getMensaje().equalsIgnoreCase("Usuario inexistente.")){
                            String mensaje = response.getMensaje() + "\n" +
                            getString(R.string.nuevo_usuario_q);

                            dialog.setMessage(mensaje);
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> {
                                Intent adduseractivity = new Intent(LoginActivity.this, AltaDireccionActivity.class);
                                startActivity(adduseractivity);
                            });
                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> finish());


                        } else if (response.getMensaje().equalsIgnoreCase("Password incorrecto.")) {
                            dialog.setMessage(response.getMensaje());
                            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {});

                        } else {
                            dialog.setMessage(response.getMensaje());
                            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {});
                        }
                        dialog.show();



                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
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

    private DtResponse LoginInfoGralUrl(String myurl) throws IOException {
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

            String data = LoginToJSON();
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
                return readInfoGralJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
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

    public DtResponse readInfoGralJsonStream(InputStream in) throws IOException {
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
            dtUsuario.setEsFirebase(false);
            return new DtResponse(ok, mensaje, dtUsuario);
        }else {
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
            } else if (name.equals("jwt") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setToken(reader.nextString());
            } else if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setId(reader.nextLong());
            } else if (name.equals("nombre") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setNombre(reader.nextString());
            } else if (name.equals("direcciones") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setDirecciones(readDireccionesArray(reader));
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

    private String LoginToJSON(){
        String res = "";

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("usuario", mail.getText());
            jsonObject.put("password", password.getText());
            jsonObject.put("notificationFirebase", dtUsuario.getNotFirebase());

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
        Log.i(TAG, String.valueOf(requestCode));
        if (requestCode == PERMISOS_REQUERIDOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent adduseractivity = new Intent(LoginActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
                dialog.setTitle(R.string.access_title_err);
                dialog.setMessage(getString(R.string.access_msg_err));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> requestPermissions(permissions, PERMISOS_REQUERIDOS));
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> finish());
                dialog.show();


            }
        }
    }

}