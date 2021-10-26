package uy.edu.fing.proyecto.appetit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtLogin;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loginUsuario();
            }
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

            if (result instanceof DtLogin) {
                DtLogin login = (DtLogin) result;
                //Toast.makeText(LoginActivity.this, login.getMensaje(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "ServerJWT:" + dtUsuario.getToken());
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
                dialog.setTitle(R.string.info_title);
                dialog.setMessage(login.getMensaje()+": "+ dtUsuario.getTelefono());
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();

            }else{
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if(result instanceof String){
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    private DtLogin LoginInfoGralUrl(String myurl) throws IOException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("PUT");
            //conn.setDoInput(true);
            conn.setDoOutput(true);

            String data = LoginToJSON();
            Log.i(TAG, data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.i(TAG, "ResponseCode:" + String.valueOf(response));
            is = conn.getInputStream();

            return readInfoGralJsonStream(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    public DtLogin readInfoGralJsonStream(InputStream in) throws IOException {
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

    public DtLogin readRESTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        DtLogin res = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ok")) {
                ok = reader.nextBoolean();
            }else if (name.equals("mensaje")) {
                mensaje = reader.nextString();
            } else if (name.equals("cuerpo") && reader.peek() != JsonToken.NULL) {
                readUsuario(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtLogin(ok, mensaje, dtUsuario);
    }

    public void readUsuario(JsonReader reader) throws IOException {
        String correo = null;
        String telefono = null;
        String token = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("correo") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setCorreo(reader.nextString());
            } else if (name.equals("telefono") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setTelefono(reader.nextString());
            } else if (name.equals("token") && reader.peek() != JsonToken.NULL) {
                dtUsuario.setToken(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

    }

    private String LoginToJSON(){
        String res = "";

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("correo", mail.getText());
            jsonObject.put("password", password.getText());

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }

}