package uy.edu.fing.proyecto.appetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.constant.MapConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class AddDireccionActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "AddDireccionActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    MapView map;
    ImageButton imlocation;
    Button buscar;
    private EditText textAlias;
    private EditText textCalle;
    private EditText textNro;
    private EditText textApto;
    private EditText textRef;
    private MapController mc;
    private LocationManager locationManager;
    private Location location;
    private GeoPoint center;
    private GeoPoint findCenter = null;
    Marker marker;
    ProgressBar progressBar;
    private Bundle bDireccion;

    private final DtUsuario dtUsuario = DtUsuario.getInstance();
    DtDireccion dtDireccion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        String title = getString(R.string.datos_adireccion);
        setTitle(title);

        setContentView(R.layout.activity_altadireccion);

        bDireccion = new Bundle();
        imlocation = findViewById(R.id.imageButtonLocation);
        progressBar = findViewById(R.id.progressBarMapView);
        buscar = findViewById(R.id.buttonB);
        textAlias = findViewById(R.id.dir_alias);
        textCalle = findViewById(R.id.dir_calle);
        textNro = findViewById(R.id.dir_nro);
        textApto = findViewById(R.id.dir_apto);
        textRef = findViewById(R.id.dir_referencia);
        map = findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(MapConstants.MAP_ZOOM_MAX);
        map.setMinZoomLevel(MapConstants.MAP_ZOOM_MIN);

        mc = (MapController) map.getController();
        mc.setZoom(MapConstants.MAP_ZOOM);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MapConstants.MAP_TIME_MS, MapConstants.MAP_DISTANCE_M, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location == null) {
                    center = new GeoPoint(MapConstants.LAT, MapConstants.LONG);
                } else {
                    center = new GeoPoint(location.getLatitude(), location.getLongitude());
                }

                mc.setCenter(center);
                mc.animateTo(center);

                //Marker marker = new Marker(map);
                marker = new Marker(map);
                marker.setPosition(center);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(getString(R.string.map_text_loc));
                addMarker(marker);

                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.map_text_loc_err);
            dialog.setMessage(getString(R.string.map_text_loc_err_msg));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settingsIntent);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AddDireccionActivity.super.onBackPressed();
                }
            });
            dialog.show();

        }

        imlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mc.setZoom(MapConstants.MAP_ZOOM);
                mc.animateTo(center);
            }
        });

        buscar.setOnClickListener(v -> {
            dtDireccion = new DtDireccion();

            if (textAlias.getText().toString().equalsIgnoreCase("")){
                AlertDialog dialog = new AlertDialog.Builder(AddDireccionActivity.this).create();
                dialog.setTitle(R.string.alert_t_error);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(getString(R.string.map_err_alias));
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                });
                dialog.show();
            } else {
                dtDireccion.setAlias(textAlias.getText().toString());
                dtDireccion.setCalle(bDireccion.getString("calle"));
                dtDireccion.setNumero(bDireccion.getString("numero"));
                dtDireccion.setApartamento(textApto.getText().toString());
                dtDireccion.setReferencias(textRef.getText().toString());

                String geometry = "POINT(" + bDireccion.getDouble("x") + " " + bDireccion.getDouble("y") + ")";
                dtDireccion.setGeometry(geometry);
                //dtUsuario.getDirecciones().add(dtDireccion);
                progressBar.setVisibility(View.VISIBLE);
                addDireccion();
            }
        });

        map.addMapListener(new DelayedMapListener(new MapListener() {
                    public boolean onZoom(final ZoomEvent e) {
                        GeoPoint point = new GeoPoint(map.getMapCenter().getLatitude(), map.getMapCenter().getLongitude());
                        marker.setPosition(point);
                        new AddDireccionActivity.GetDireccionTask().execute(point);
                        return true;
                    }

                    public boolean onScroll(final ScrollEvent e) {
                        GeoPoint point = new GeoPoint(map.getMapCenter().getLatitude(), map.getMapCenter().getLongitude());
                        marker.setPosition(point);
                        new AddDireccionActivity.GetDireccionTask().execute(point);
                        return true;
                    }
                }, 100)
        );
    }

    public void addMarker(Marker marker) {
        map.getOverlays().add(marker);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        center = new GeoPoint(location.getLatitude(), location.getLongitude());
        mc.animateTo(center);

        marker.setPosition(center);
    }

    private class GetDireccionTask extends AsyncTask<GeoPoint, Void, Object> {

        @Override
        protected Object doInBackground(GeoPoint... points) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return DireccionPorPunto(points[0]);
            } catch (IOException e) {
                //return getString(R.string.err_recuperarpag);
                return e.getMessage();
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Address) {
                Address response = (Address) result;
                String display_name = response.getExtras().getString("display_name");
                String[] nros = display_name.split(",");

                Log.i(TAG, response.toString());

                textCalle.setText(response.getThoroughfare());
                bDireccion.putString("calle", response.getThoroughfare());

                Pattern pattern = Pattern.compile("^\\d.*");
                Matcher matcher = pattern.matcher(nros[0]);

                if (matcher.find()) {
                    textNro.setText(nros[0]);
                    bDireccion.putString("numero", nros[0]);
                } else {
                    textNro.setText("");
                    bDireccion.putString("numero", "");
                }

                CRSFactory crsFactory = new CRSFactory();
                CoordinateReferenceSystem proj4326 = crsFactory.createFromName("EPSG:4326");
                CoordinateReferenceSystem proj32721 = crsFactory.createFromName("EPSG:32721");
                CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
                CoordinateTransform toProj32721 = ctFactory.createTransform(proj4326, proj32721);

                ProjCoordinate point32721 = new ProjCoordinate();
                toProj32721.transform(new ProjCoordinate(response.getLongitude(), response.getLatitude()), point32721);

                bDireccion.putDouble("lon", response.getLongitude());
                bDireccion.putDouble("lat", response.getLatitude());
                bDireccion.putDouble("x", point32721.x);
                bDireccion.putDouble("y", point32721.y);
                //Log.i(TAG, "GetDireccionTask:" + point32721.toString());

            }
        }
    }

    private Address DireccionPorPunto(GeoPoint point) throws IOException {
        Address direccion = null;
        GeocoderNominatim geocoder = new GeocoderNominatim(ConnConstants.USER_AGENT);
        try {
            @SuppressLint("StaticFieldLeak")
            List<Address> addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            if (addresses.size() > 0) {
                direccion = addresses.get(0);
            }
        } catch (IOException e) {
            direccion = null;
        }

        return direccion;
    }

    private void addDireccion() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.API_ADDCLIENTADDRERSS_URL;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new AddDireccionActivity.PostDireccionTask().execute(stringUrl);
        }
    }

    private class PostDireccionTask extends AsyncTask<String, Void, Object> {
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

                AlertDialog dialog = new AlertDialog.Builder(AddDireccionActivity.this).create();
                dialog.setTitle(R.string.info_title);
                dialog.setMessage(response.getMensaje());
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), (dialog1, which) -> {
                    if(response.getOk())
                        onBackPressed();
    
                    dialog.cancel();
                });

                dialog.show();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(AddDireccionActivity.this).create();
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
            conn.setRequestMethod("POST");
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
            jsonObject.put("alias", dtDireccion.getAlias());
            jsonObject.put("calle", dtDireccion.getCalle());
            jsonObject.put("numero", dtDireccion.getNumero());
            jsonObject.put("apartamento", dtDireccion.getApartamento());
            jsonObject.put("referencias", dtDireccion.getReferencias());
            jsonObject.put("geometry", dtDireccion.getGeometry());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}