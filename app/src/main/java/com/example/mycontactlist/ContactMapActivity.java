package com.example.mycontactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ContactMapActivity extends AppCompatActivity {
    //variable declarations
    LocationManager locationManager;
    LocationManager gpsListenser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        intiListButton();
        intiMapButton();
        intiSettingsButton();
        initGetLocationButton();

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
    //Get location button method
    private void initGetLocationButton() {
        Button locationButton = findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    locationManager = (LocationManager)getBaseContext().
                            getSystemService(Context.LOCATION_SERVICE);
                    gpsListenser = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                            TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                            TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                            txtLatitude.setText(String.valueOf(location.getLatitude()));
                            txtLongitude.setText(String.valueOf(location.getLongitude()));
                            txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                        }
                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                        public void onProviderEnabled(String provider) {}
                        public void onProviderDisabled(String provider) {}
                    };
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,0, 0,gpsListenser);
                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error, Location not available",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(gpsListenser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}