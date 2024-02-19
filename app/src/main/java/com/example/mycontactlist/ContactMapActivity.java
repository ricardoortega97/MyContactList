package com.example.mycontactlist;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity  implements
        OnMapReadyCallback {
    //variable declarations
    final int PERMISSION_REQUEST_LOCATION = 101;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    ArrayList<Contact> contacts = new ArrayList<>();
    Contact currentContact = null;
    GoogleMap gmap;

    Sensor accelerometer;
    SensorManager sensorManager;
    Sensor magnetometer;
    TextView textDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapTypeButtons();
        setContentView(R.layout.activity_contact_map);

        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            if (extras != null) {
                currentContact = ds.getSpecificContact(extras.getInt("contactid"));
            }
            else {
                contacts = ds.getContacts("contactname", "ASC");
            }
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Contact(s) could not be retrieved", Toast.LENGTH_LONG).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        createLocationRequest();
        createLocationCallback();
        intiListButton();
        intiMapButton();
        intiSettingsButton();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(mySensorEventListener, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(mySensorEventListener, magnetometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Sensors not found ", Toast.LENGTH_LONG).show();
        }
        textDirection = (TextView) findViewById(R.id.textHeading);
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

    //Sensor method
    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy){
            float[] accelerometerValues;
            float[] magneticValues;
        }
        public void onSensorChanged(SensorEvent event){
            float[] accelerometerValues = new float[0];
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = event.values;
            float[] magneticValues = new float[0];
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticValues = event.values;
            if (accelerometerValues != null && magneticValues != null){
                float R[] = new float[9];
                float I[] = new float[9];

                boolean success = SensorManager.getRotationMatrix(R, I,
                        accelerometerValues,magneticValues);

                if (success) {
                    float orientational[] = new float[3];
                    SensorManager.getOrientation(R, orientational);

                    float azimut = (float) Math.toDegrees(orientational[0]);
                    if (azimut < 0.0f) {
                        azimut+=360.0f;
                    }
                    String direction;
                        if (azimut >= 315 || azimut < 45 ) {
                            direction = "N";}
                        else if (azimut >= 225 && azimut < 315) {
                            direction = "W";}
                        else if (azimut >= 135 && azimut < 225) {
                            direction = "S";}
                        else {
                            direction = "E";
                        }
                    textDirection.setText(direction);

                }
            }
        }
    };

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
    public void onMapReady(GoogleMap googleMap) {
        RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
        rbNormal.setChecked(true);
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;

        if (contacts.size()>0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < contacts.size(); i++) {
                currentContact = contacts.get(i);

                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + ", " +
                        currentContact.getZipcode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude());
                builder.include(point);
                gmap.addMarker(new MarkerOptions().position(point).
                        title(currentContact.getContactName()).snippet(address));
            }
            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    measuredWidth, measuredHeight, 450));
        }
        else {
            if (currentContact != null) {
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + ", " +
                        currentContact.getZipcode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng point = new
                        LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());

                gmap.addMarker(new MarkerOptions().position(point).
                        title(currentContact.getContactName()).snippet(address));
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        ContactMapActivity.this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data available for the mapping function.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.show();
            }
        }
        ActivityCompat.requestPermissions(
                ContactMapActivity.this,
                new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_LOCATION);
    }

    private void initMapTypeButtons() {
        RadioGroup rgMapType = findViewById(R.id.radioGroupMapType);

        if (rgMapType != null) {
            rgMapType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
                    if (rbNormal.isChecked()) {
                        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                    else {
                        gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    }

                }
            });
        }
        else {

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
}