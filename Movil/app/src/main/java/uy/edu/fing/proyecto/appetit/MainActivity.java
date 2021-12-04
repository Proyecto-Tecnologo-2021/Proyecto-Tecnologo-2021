package uy.edu.fing.proyecto.appetit;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtCotizacion;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtProducto;
import uy.edu.fing.proyecto.appetit.obj.DtRCalificacion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtRestaurante;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private FirebaseAuth mAuth;
    String firebaseToken;

    SignInButton signInButton;
    Button loginButton;
    ProgressBar progressBar;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private final DtUsuario dtUsuario = DtUsuario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buscarCotizacion();

        signInButton = findViewById(R.id.sign_in_button);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.pBarLogin);

        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(getColor(R.color.second_color), PorterDuff.Mode.SRC_IN);


        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText(getString(R.string.google_login));

        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        loginButton.setOnClickListener(v -> {
            if (isConnected) {
                Intent ilogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(ilogin);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.err_conntitle);
                dialog.setMessage(getString(R.string.err_conectividad));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        });

        signInButton.setOnClickListener(v -> signIn());


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ConnConstants.WEB_CLIENT_ID)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "No se pudo obtener el token de registro de FCM", task.getException());
                        return;
                    }
                    firebaseToken = task.getResult();
                    //Log.i(TAG, firebaseToken);
                    dtUsuario.setNotFirebase(firebaseToken);
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.i(TAG, String.valueOf(requestCode));
        if (requestCode == PERMISOS_REQUERIDOS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent adduseractivity = new Intent(MainActivity.this, AltaDireccionActivity.class);
                startActivity(adduseractivity);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle(R.string.access_title_err);
                dialog.setMessage(getString(R.string.access_msg_err));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> requestPermissions(permissions, PERMISOS_REQUERIDOS));
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> finish());
                dialog.show();


            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isConnected) {
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.err_conntitle);
            dialog.setMessage(getString(R.string.err_conectividad));
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            dialog.show();
        }
    }

    private void signIn() {
        if (isConnected) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.err_conntitle);
            dialog.setMessage(getString(R.string.err_conectividad));
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            dialog.show();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser account) {
        if (account != null) {
            account.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Send token to your backend via HTTPS
                            dtUsuario.setTokenFirebase(task.getResult().getToken());
                            //Log.i(TAG, "Google Firebase Token: " + task.getResult().getToken());
                            dtUsuario.setNombre(account.getDisplayName());
                            dtUsuario.setCorreo(account.getEmail());
                            dtUsuario.setUsername(account.getEmail());
                            dtUsuario.setTelefono(account.getPhoneNumber());
                            dtUsuario.setEsFirebase(true);
                            progressBar.setVisibility(View.VISIBLE);
                            loginUsuario();
                        } else {
                            // Handle error -> task.getException();
                            // Google Sign In failed, update UI appropriately
                            //Log.w(TAG, "Google sign in failed", task.getException());

                            AlertDialog dialog = new AlertDialog.Builder(this).create();
                            dialog.setTitle(R.string.access_title_err);
                            dialog.setMessage(getString(R.string.firebase_error));
                            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                                //onBackPressed();
                            });
                            dialog.show();
                        }
                    });

        } else {
            dtUsuario.setCorreo(null);
            dtUsuario.setTokenFirebase(null);
            dtUsuario.setToken(null);
            dtUsuario.setUsername(null);
            dtUsuario.setTelefono(null);
            dtUsuario.setPassword(null);
            dtUsuario.setNombre(null);
            dtUsuario.setDirecciones(new ArrayList<DtDireccion>());
            signInButton.setEnabled(true);
            loginButton.setEnabled(true);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void loginUsuario() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_USRLOGINFIREBASE_URL;
        //Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new MainActivity.GetLoginUsuarioTask().execute(stringUrl);
        }
    }

    private class GetLoginUsuarioTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.

            try {
                return LoginInfoGralUrl(urls[0]);
            } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
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
                    Intent menuactivity = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(menuactivity);
                } else {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    } else {
                        Intent adduseractivity = new Intent(MainActivity.this, AltaDireccionActivity.class);
                        startActivity(adduseractivity);
                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
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

    private DtResponse LoginInfoGralUrl(String myurl) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        InputStream is = null;
        HttpURLConnection conn = null;
        //HttpsURLConnection conn = null;
        try {
            //String authorization ="Bearer  " + usuario.getToken();
            URL url = new URL(myurl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            //conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);


            String data = LoginToJSON();
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
                //readJWTUsuario(reader.nextString());
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

    public void readJWTUsuario(String token) {
        JWT jwt = new JWT(token);
        String subject = jwt.getSubject();
        //Log.i(TAG, subject);
        Claim claim = jwt.getClaim("correo");
        //Log.i(TAG, claim.asString());

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

    private String LoginToJSON() {
        String res = "";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("usuario", dtUsuario.getCorreo());
            jsonObject.put("password", dtUsuario.getTokenFirebase());
            jsonObject.put("notificationFirebase", dtUsuario.getNotFirebase());

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }

    private void buscarCotizacion() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        String stringUrl = ConnConstants.GET_COTIZACION;
        stringUrl = stringUrl.replace("{date}", fecha);
        //Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new MainActivity.GetCotizaTask().execute(stringUrl);
        }
    }

    private class GetCotizaTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return CotizaInfoGralUrl(urls[0]);
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
                    DtCotizacion dtCotizacion = (DtCotizacion) response.getCuerpo();
                    DtPedido dtPedido = DtPedido.getInstance();
                    dtPedido.setCotizacion(dtCotizacion);
                } else {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
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


    private DtResponse CotizaInfoGralUrl(String myurl) throws IOException {
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
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            //Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readCotizaJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readCotizaJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readCotizaJsonStream(is);
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

    public DtResponse readCotizaJsonStream(InputStream in) throws IOException {
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
            return readGETMessage(reader);
        } finally {
            reader.close();
        }
    }

    public DtResponse readGETMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        String base = null;
        DtCotizacion dtCotizacion = null;

        try {
            reader.beginObject();
            dtCotizacion = new DtCotizacion();
            ok = true;
            mensaje = "GET cotizacion ok";
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("base")) {
                    base = reader.nextString();
                } else if (name.equals("rates")) {
                    dtCotizacion = readCotizacionObj(reader);
                } else {
                    reader.skipValue();
                }
            }
            dtCotizacion.setBase(base);
        } catch (Exception e) {
            ok = false;
            mensaje = "GET cotizacion false";
            dtCotizacion = null;

        }

        reader.endObject();
        return new DtResponse(ok, mensaje, dtCotizacion);

    }

    public DtCotizacion readCotizacionObj(JsonReader reader) throws IOException {
        DtCotizacion dtCotizacion = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("USD") && reader.peek() != JsonToken.NULL) {
                dtCotizacion = readCotizacion(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return dtCotizacion;
    }

    public DtCotizacion readCotizacion(JsonReader reader) throws IOException {

        Double sell = null;
        Double buy = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("sell") && reader.peek() != JsonToken.NULL) {
                sell = reader.nextDouble();
            } else if (name.equals("buy")) {
                buy = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DtCotizacion(null, sell, buy);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
