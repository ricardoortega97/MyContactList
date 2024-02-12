package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ContactMapActivity extends AppCompatActivity  implements
        OnMapReadyCallback {
    //variable declarations
    final int PERMISSION_REQUEST_LOCATION = 101;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    GoogleMap gmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        createLocationRequest();
        createLocationCallback();
        intiListButton();
        intiMapButton();
        intiSettingsButton();


    }
    private void intiListButton(){
        ImageButton ibList = findViewById(R.id.contactsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this,ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    private void intiMapButton(){
        ImageButton ibList = findViewById(R.id.mapButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageButton ibSettings = findViewById(R.id.settingsButton);
                ibSettings.setEnabled(false);
            }
        });
    }
    private void intiSettingsButton(){
        ImageButton ibList = findViewById(R.id.settingsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this,ContactSettingsActivity.class);//change this to settings
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    //the declaration of of bestLocation variable and is called in onLocationChanged Method

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);

    }
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()){
                    Toast.makeText(getBaseContext(), "Lat:" + location.getLatitude() +
                            " Long: " + location.getLongitude() +
                            " Accurracy: " + location.getAccuracy(),Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            gmap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(ContactMapActivity.this, "MyContactList will not locate" +
                            "your contacts.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
        }
        try {
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ActivityCompat.requestPermissions(
                ContactMapActivity.this,
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_LOCATION);

    }
}