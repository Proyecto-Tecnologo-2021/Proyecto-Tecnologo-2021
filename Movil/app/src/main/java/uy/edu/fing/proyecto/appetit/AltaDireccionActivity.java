package uy.edu.fing.proyecto.appetit;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.constant.MapConstants;
import uy.edu.fing.proyecto.appetit.obj.DtDireccion;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class AltaDireccionActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "AltaDireccionActivity";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        String title = getString(R.string.title_addDireccion);
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
                    AltaDireccionActivity.super.onBackPressed();
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
            Intent adduseractivity = new Intent(AltaDireccionActivity.this, AltaCliente.class);
            DtDireccion dtDireccion = new DtDireccion();

            if (textAlias.getText().toString().equalsIgnoreCase("")){
                AlertDialog dialog = new AlertDialog.Builder(AltaDireccionActivity.this).create();
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
                dtUsuario.getDirecciones().add(dtDireccion);
                startActivity(adduseractivity);
            }
        });

        map.addMapListener(new DelayedMapListener(new MapListener() {
                    public boolean onZoom(final ZoomEvent e) {
                        GeoPoint point = new GeoPoint(map.getMapCenter().getLatitude(), map.getMapCenter().getLongitude());
                        marker.setPosition(point);
                        new AltaDireccionActivity.GetDireccionTask().execute(point);
                        return true;
                    }

                    public boolean onScroll(final ScrollEvent e) {
                        GeoPoint point = new GeoPoint(map.getMapCenter().getLatitude(), map.getMapCenter().getLongitude());
                        marker.setPosition(point);
                        new AltaDireccionActivity.GetDireccionTask().execute(point);
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

                textCalle.setText(response.getThoroughfare());
                bDireccion.putString("calle", response.getThoroughfare());

                if (android.text.TextUtils.isDigitsOnly(nros[0])) {
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
}