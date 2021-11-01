package uy.edu.fing.proyecto.appetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import uy.edu.fing.proyecto.appetit.constant.MapConstants;

public class AltaDireccionActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "VacunMapActivity";
    final static Integer CODE = 2021;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    MapView map;
    ImageButton imlocation;
    private MapController mc;
    private LocationManager locationManager;
    private Location location;
    private GeoPoint center;
    private GeoPoint findCenter = null;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        String title = getString(R.string.app_name) + " - " + getString(R.string.title_addDireccion);
        setTitle(title);

        setContentView(R.layout.activity_altadireccion);

        imlocation = findViewById(R.id.imageButtonLocation);
        progressBar = findViewById(R.id.progressBarMapView);

        map = findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(MapConstants.MAP_ZOOM_MAX);
        map.setMinZoomLevel(MapConstants.MAP_ZOOM_MIN);

        mc = (MapController) map.getController();
        mc.setZoom(MapConstants.MAP_ZOOM);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(gpsEnabled){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MapConstants.MAP_TIME_MS, MapConstants.MAP_DISTANCE_M, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location==null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location==null){
                    center = new GeoPoint(-34.917831, -56.166322);
                }else{
                    center = new GeoPoint(location.getLatitude(), location.getLongitude());
                }

                mc.setCenter(center);
                mc.animateTo(center);

                Marker marker = new Marker(map);
                marker.setPosition(center);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(getString(R.string.map_text_loc));
                addMarker(marker);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }else{
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.map_text_loc_err);
            dialog.setMessage(getString(R.string.map_text_loc_err_msg));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settingsIntent);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), new DialogInterface.OnClickListener()
            {
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

    }

    public void addMarker (Marker marker){
        map.getOverlays().add(marker);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        center = new GeoPoint(location.getLatitude(), location.getLongitude());
        mc.animateTo(center);

        Marker marker = new Marker(map);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(getString(R.string.map_text_loc));
        addMarker(marker);
    }
}