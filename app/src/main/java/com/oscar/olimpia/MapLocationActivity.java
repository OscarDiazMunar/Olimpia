package com.oscar.olimpia;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oscar.olimpia.Modelo.Usuario;
import com.oscar.olimpia.Utils.Constants;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 777;
    @Bind(R.id.btnSiguiente)
    Button btnSiguiente;
    @Bind(R.id.txtAddress)
    TextView txtAddress;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        usuario = (Usuario) getIntent().getSerializableExtra(Constants.extraActivity.pojoUsuario);
        starLocation();
    }

    public void starLocation() {
        if (checkPermission()) {

        } else {
            requestPermission();
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(this, "GPS permission needed in order to capture your location", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String msg = "Tus coordenadas son: longitud:%s latitud:%s";

            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(here).title("Aqui estas"));
            msg = String.format(msg, location.getLongitude(), location.getLatitude());
            try {
                msg += "\nStreet: " + getAddressFromLocation(location.getLatitude(), location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            usuario.setCoordenadas(Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude()));
            txtAddress.setText(msg);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(MapLocationActivity.this, " GPS Provider Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(MapLocationActivity.this, "GPS Provider Disabled", Toast.LENGTH_SHORT).show();
            alertaGPSoff();
        }
    };

    private void alertaGPSoff() {

        final AlertDialog.Builder alertaGPS = new AlertDialog.Builder(this);
        alertaGPS.setTitle("GPS Apagado");
        alertaGPS.setMessage("El GPS esta desactivado ¿Desea activarlo?");
        alertaGPS.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent settingGps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingGps);
            }
        });
        alertaGPS.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialogGPS = alertaGPS.create();
        dialogGPS.show();
    }




    private String getAddressFromLocation(double latitude, double longitude) throws IOException {
        Geocoder geoCoder = new Geocoder(this);
        List<Address> matches = geoCoder.getFromLocation(latitude, longitude, 1);
        if (matches.size() < 1) return "";
        return (matches.isEmpty() ? null : matches.get(0)).getAddressLine(0).toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(49.894634,
                        -98.22876
                ));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.addMarker()
        //googleMap.moveCamera(center);
        //googleMap.animateCamera(zoom);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.btnSiguiente)
    public void onViewClicked() {
        Intent intent = new Intent(this, StateWiFiBlueActivity.class);
        intent.putExtra(Constants.extraActivity.pojoUsuario, usuario);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(txtAddress,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(txtAddress,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}
